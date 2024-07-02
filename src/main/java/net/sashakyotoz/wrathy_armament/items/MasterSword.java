package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.Comparator;
import java.util.List;

public class MasterSword extends SwordLikeItem {
    private int timerToRerecord = 200;

    public MasterSword(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        if (player.getRandom().nextBoolean()) {
            if (player.getRandom() instanceof ServerLevel serverLevel) {
                LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(serverLevel);
                entityToSpawn.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(player.getX(), player.getY(), player.getZ())));
                serverLevel.addFreshEntity(entityToSpawn);
                ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), serverLevel, 0.2f, true, false, 2,
                        ParticleTypes.ELECTRIC_SPARK, "semicycle");
                particleEntity.moveTo(new Vec3(player.getX(), player.getY() + 1, player.getZ()));
                serverLevel.addFreshEntity(particleEntity);
            }
        } else {
            if (!player.level().isClientSide()) {
                player.setSecondsOnFire(15);
            }
            if (player.level() instanceof ServerLevel serverLevel) {
                int tmp = RandomSource.create().nextInt(5, 11) + 3;
                for (int i = 0; i < tmp; i++) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX() + OnActionsTrigger.getXVector(i, player.getYRot()), player.getY() + 1, player.getZ() + OnActionsTrigger.getZVector(i, player.getYRot()), 27, 1 + OnActionsTrigger.getXVector(i, player.getYRot()), 1, 1 + OnActionsTrigger.getZVector(i, player.getYRot()), 1);
                }
            }
        }
    }
    @Override
    public void rightClick(Player player, ItemStack stack) {
        WrathyArmament.LOGGER.debug("Coordinate: {}", stack.getOrCreateTag().getDouble("playerX"));
        this.timerToRerecord = 240;
        OnActionsTrigger.addParticles(ParticleTypes.END_ROD, player.level(), player.getX(), player.getY(), player.getZ(), 4);
        player.teleportTo(stack.getOrCreateTag().getDouble("playerX"), stack.getOrCreateTag().getDouble("playerY"), stack.getOrCreateTag().getDouble("playerZ"));
        stack.getOrCreateTag().putDouble("playerX", 0);
        player.getCooldowns().addCooldown(stack.getItem(),20);
        stack.onStopUsing(player,20);
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 16, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.game.master_sword").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.PURPLE_TITLE_FORMAT));
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
            if (level instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.GLOWSTONE.defaultBlockState()),
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
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean p_41408_) {
        if (entity instanceof Player player) {
            if (this.timerToRerecord > 0)
                this.timerToRerecord--;
            else {
                stack.getOrCreateTag().putDouble("playerX", player.getX());
                stack.getOrCreateTag().putDouble("playerY", player.getY());
                stack.getOrCreateTag().putDouble("playerZ", player.getZ());
                this.timerToRerecord = 400;
            }
        }
    }
}
