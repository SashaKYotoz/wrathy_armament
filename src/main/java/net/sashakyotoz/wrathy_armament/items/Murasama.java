package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.anitexlib.client.particles.parents.types.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class Murasama extends SwordLikeItem {
    private int timer = 0;
    private float speedBoost;
    public static final UUID SWIFTNESS = UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635");

    public Murasama(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        if (!player.isCrouching()) {
            if (speedBoost < 0.3) {
                speedBoost += 0.1F;
                timer += 100;
            }
            WaveParticleOption option = new WaveParticleOption(player.getYRot(), 2f, 0.75f, 0, 0);
            player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
                    OnActionsTrigger.getXVector(2, player.getYRot()),
                    OnActionsTrigger.getYVector(1, player.getXRot()),
                    OnActionsTrigger.getZVector(2, player.getYRot()));
        } else {
            Level level = player.level();
            float scaling = 0;
            double d0 = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
            double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
            for (int i = 0; i < 12 + stack.getOrCreateTag().getInt("Sparkle") * 2; i++) {
                BlockPos pos = level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                if (!level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                    scaling++;
                BlockPos pos1 = level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                OnActionsTrigger.queueServerWork(20, () -> level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()), pos1.getX(), pos1.getY(), pos1.getZ(), d0, 0.1, d1));
                final Vec3 center = new Vec3(pos1.getX(), pos1.getY(), pos1.getZ());
                List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(3d), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
                for (Entity entity : entityList) {
                    if (entity != player) {
                        if (entity instanceof LivingEntity livingEntity) {
                            livingEntity.hurt(player.damageSources().mobAttack(player), 5 + stack.getOrCreateTag().getInt("Sparkle"));
                            livingEntity.setDeltaMovement(new Vec3(
                                    OnActionsTrigger.getXVector(2, player.getYRot()),
                                    1,
                                    OnActionsTrigger.getZVector(2, player.getYRot())));
                            player.swing(player.getMainHandItem().is(stack.getItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
                            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 50, 4));
                            player.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        if (!player.getCooldowns().isOnCooldown(stack.getItem())){
            if (player.level().isClientSide())
                OnActionsTrigger.playPlayerAnimation(player,"zenith_turn");
            OnActionsTrigger.queueServerWork(20, () -> OnActionsTrigger.addParticlesWithDelay(ParticleTypes.FLAME, player.level(), player.getX(), player.getY() + 1, player.getZ(), 2, 5));
            final Vec3 center = new Vec3(player.getX(), player.getY(), player.getZ());
            for (int i = 0; i < 2; i++) {
                List<Entity> entityList = player.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(6d), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
                for (Entity entity : entityList) {
                    if (entity != player) {
                        if (entity instanceof LivingEntity livingEntity)
                            OnActionsTrigger.queueServerWork(20 * (i + 1), () -> livingEntity.hurt(player.damageSources().mobAttack(player), 8 + stack.getOrCreateTag().getInt("Sparkle")));
                    }
                }
            }
            player.getCooldowns().addCooldown(stack.getItem(),45);
        }
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 12.5, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -1.8, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SWIFTNESS, "Weapon modifier", speedBoost, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add((itemstack.getHoverName().getString().contains("Blue") ? Component.translatable("item.wrathy_armament.game.zenith") : Component.translatable("item.wrathy_armament.game.murasama")).withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.murasama_hint").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.murasama_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        if (timer > 0)
            timer--;
        else {
            if (speedBoost >= 0.1)
                speedBoost -= 0.1f;
            if (speedBoost < 0)
                speedBoost = 0;
            timer += 100;
            if (entity instanceof Player player)
                player.getInventory().setChanged();
        }
        itemStack.getOrCreateTag().putDouble("CustomModelData", itemStack.getHoverName().getString().contains("Muramasa") ? 1 : 0);
    }
}