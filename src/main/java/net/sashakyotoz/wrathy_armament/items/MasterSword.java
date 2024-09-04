package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentTags;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.Comparator;
import java.util.List;

public class MasterSword extends SwordLikeItem {

    public MasterSword(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        float scaling = 0;
        for (int i = 0; i < 8; i++) {
            BlockPos pos = player.level().clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
            if (!player.level().getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                scaling++;
            BlockPos pos1 = player.level().clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
            player.level().addParticle(new ColorableParticleOption("wisp", 0.5f, 0.5f, 1f), pos1.getX(), pos1.getY() + 0.5f, pos1.getZ(),
                    OnActionsTrigger.getXVector(2, player.getYRot()),
                    OnActionsTrigger.getYVector(1, player.getXRot()),
                    OnActionsTrigger.getZVector(2, player.getYRot()));
        }
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        OnActionsTrigger.addParticles(ParticleTypes.END_ROD, player.level(), player.getX(), player.getY(), player.getZ(), 4);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getDouble("playerX") != 0 && tag.getDouble("playerZ") != 0)
            player.teleportTo(tag.getDouble("playerX"), tag.getDouble("playerY"), tag.getDouble("playerZ"));
        player.getCooldowns().addCooldown(stack.getItem(), 20);
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {
        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            if (player.level() instanceof ServerLevel level) {
                HarmfulProjectileEntity entity = new HarmfulProjectileEntity(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(), level, 7 + getCurrentSparkles(stack), "shield_dash");
                entity.moveTo(player.getX() + OnActionsTrigger.getXVector(0.5f, player.getYRot()), player.getY() + 0.25f, player.getZ() + OnActionsTrigger.getZVector(0.5f, player.getYRot()));
                entity.setOwner(player);
                level.addFreshEntity(entity);
            }
            player.setDeltaMovement(-OnActionsTrigger.getXVector(1.25f, player.getYRot()), -OnActionsTrigger.getYVector(0.675f, player.getXRot()) + 0.25f, -OnActionsTrigger.getZVector(1.25f, player.getYRot()));
            player.getCooldowns().addCooldown(stack.getItem(), 50);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 13.5 + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.game.master_sword").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.master_sword_hint").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.master_sword_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.master_sword_hint1").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.master_sword_alter_time").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);
        return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int i) {
        if (i > 20 && entity.isCrouching() && entity instanceof Player player) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new ColorableParticleOption("wisp", 1f, 1f, 1f),
                        entity.getX() + OnActionsTrigger.getXVector(2, entity.getYRot()),
                        entity.getY() + 1,
                        entity.getZ() + OnActionsTrigger.getZVector(2, entity.getYRot()),
                        9,
                        OnActionsTrigger.getXVector(2, entity.getYRot()),
                        0.25f,
                        OnActionsTrigger.getZVector(2, entity.getYRot()),
                        0.5f
                );
            }
            final Vec3 center = new Vec3(entity.getX() + OnActionsTrigger.getXVector(2, entity.getYRot()), entity.getY(), entity.getZ() + OnActionsTrigger.getZVector(2, entity.getYRot()));
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(3), e -> true).stream().sorted(Comparator.comparingDouble(entity1 -> entity1.distanceToSqr(center))).toList();
            for (Entity entityIterator : entities) {
                if (entityIterator instanceof LivingEntity target && target != entity && target.getMobType().equals(MobType.UNDEAD)) {
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(stack.getItem()) && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                        entityIterator.hurt(entityIterator.damageSources().magic(), 8);
                        if (stack.hurt(1, RandomSource.create(), null)) {
                            stack.shrink(1);
                            stack.setDamageValue(0);
                        }
                    }
                }
            }
        }
        super.onUseTick(level, entity, stack, i);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean isSelected) {
        if (entity instanceof Player player) {
            if (player.tickCount % 400 == 0 && player.onGround() && level.loadedAndEntityCanStandOn(player.getOnPos(), player)) {
                stack.getOrCreateTag().putDouble("playerX", player.getX());
                stack.getOrCreateTag().putDouble("playerY", player.getY());
                stack.getOrCreateTag().putDouble("playerZ", player.getZ());
            }
            if (level instanceof ServerLevel serverLevel && player.tickCount % 10 == 0 && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                BlockPos pos = serverLevel.findNearestMapStructure(WrathyArmamentTags.Structures.VISIBLE_FOR_MASTER_SWORD, player.getOnPos(), 96, false);
                if (pos != null) {
                    double distanceSquared = player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
                    double normalizedDistance = Math.min(distanceSquared / 4613, 10.0);
                    double lightsValue = 10.0 - normalizedDistance;
                    stack.getOrCreateTag().putInt("Lights", (int) Math.round(lightsValue));
                }
            }
        }
    }
}