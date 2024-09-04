package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.kosmx.playerAnim.core.util.Vec3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.anitexlib.client.particles.parents.options.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentSounds;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class MirrorSword extends SwordLikeItem {
    public MirrorSword(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        Triple<Float, Float, Float> colorSet = new Triple<>(1f, 1f, 1f);
        WaveParticleOption option = new WaveParticleOption(player.getYRot(), 3f, colorSet.a, colorSet.b, colorSet.c);
        player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
                OnActionsTrigger.getXVector(1.25f, player.getYRot()),
                OnActionsTrigger.getYVector(1f, player.getXRot()),
                OnActionsTrigger.getZVector(1.25f, player.getYRot()));
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {

    }

    public void spawnArcParticles(Player player, int particleCount, boolean toLeftSide) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        double arcAngle = Math.PI / 4;
        double halfArc = arcAngle / 2;
        if (toLeftSide) {
            for (int i = 0; i < particleCount; i++) {
                double angle = -halfArc + (arcAngle * i / (particleCount - 1));
                Vec3 offset = rightVec.scale(Math.cos(angle)).add(lookVec.scale(Math.sin(angle))).normalize().scale(1.25);
                Vec3 particlePos = eyePos.add(offset);
                player.level().addParticle(ParticleTypes.END_ROD, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
            }
        } else {
            for (int i = 0; i < particleCount; i++) {
                double angle = halfArc - (arcAngle * i / (particleCount + 1));
                Vec3 offset = rightVec.scale(-Math.cos(angle)).add(lookVec.scale(-Math.sin(angle))).normalize().scale(1.25);
                Vec3 particlePos = eyePos.add(offset);
                player.level().addParticle(ParticleTypes.END_ROD, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
            }
        }
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {
        if (player.getMainHandItem().getItem() instanceof MirrorSword) {
            spawnArcParticles(player, 21, true);
            OnActionsTrigger.playPlayerAnimation(player.level(), player, "right_arm_mirror_swing");
        } else {
            spawnArcParticles(player, 21, false);
            OnActionsTrigger.playPlayerAnimation(player.level(), player, "left_arm_mirror_swing");
        }
        List<Entity> entities = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(7));
        for (Entity entity : entities) {
            if (entity != player) {
                entity.setDeltaMovement(new Vec3(
                        OnActionsTrigger.getXVector(-2, entity.getYRot()),
                        OnActionsTrigger.getYVector(-1f, player.getXRot()),
                        OnActionsTrigger.getZVector(-2, entity.getYRot())
                ));
                for (int i = 0; i < 7; i++) {
                    Vec3 vec3 = entity.getOnPos().getCenter().add(new Vec3(
                            OnActionsTrigger.getXVector(-2, entity.getYRot()),
                            OnActionsTrigger.getYVector(-1f, player.getXRot()),
                            OnActionsTrigger.getZVector(-2, entity.getYRot())
                    ));
                    entity.level().addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(),
                            vec3.x, vec3.y, vec3.z);
                }
            }
        }
        player.getCooldowns().addCooldown(stack.getItem(), 30);
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 0)
            return;
        var f = OnActionsTrigger.getPowerForTime(i);
        if (f > 0.5D) {
            if (level.isClientSide()) {
                Vec3 eyePos = entity.getEyePosition();
                Vec3 lookVec = entity.getViewVector(1.0F);
                Vec3 endPos = eyePos.add(lookVec.scale(3 * f));
                int particleCount = 20;
                Vec3 step = endPos.subtract(eyePos).scale(1.0 / particleCount);
                double radius = 0.5;
                for (int v = 0; v < 180; v++) {
                    for (int j = 0; j < particleCount; j++) {
                        Vec3 basePos = eyePos.add(step.scale(j));
                        double angle = (v) * Math.PI / 20 + (j * Math.PI / 10);
                        double offsetX = radius * Math.cos(angle);
                        double offsetY = radius * Math.sin(angle);
                        Vec3 perpendicular = lookVec.cross(new Vec3(0, 1, 0)).normalize();
                        Vec3 particlePos = basePos.add(perpendicular.scale(offsetX)).add(0, offsetY, 0);
                        level.addParticle(
                                ParticleTypes.CLOUD,
                                particlePos.x,
                                particlePos.y,
                                particlePos.z,
                                0, 0, 0
                        );
                    }
                }
            }
            for (int u = 0; u < 7; u++) {
                BlockPos pos1 = level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(2 * f + u)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
                final Vec3 center = pos1.getCenter();
                List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(2), e -> true).stream().sorted(Comparator.comparingDouble(entity1 -> entity1.distanceToSqr(center))).toList();
                for (LivingEntity entityIterator : entityList) {
                    if (!(entityIterator == entity)) {
                        float damage = stack.getOrCreateTag().getFloat("damageKeep") / 1.5f;
                        entityIterator.hurt(entity.damageSources().generic(), damage);
                        if (entity instanceof Player player)
                            player.getCooldowns().addCooldown(stack.getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 50, 100));
                    }
                    if (stack.hurt(1, RandomSource.create(), null)) {
                        stack.shrink(1);
                        stack.setDamageValue(0);
                    }
                }
            }
            if (entity instanceof Player player && player.getCooldowns().isOnCooldown(stack.getItem()))
                clearDamageThatKeeps(stack);
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity.tickCount % 12 == 0 && pLivingEntity instanceof Player player)
            OnActionsTrigger.playPlayerAnimation(pLevel, player, "mirror_sword_wind_swinging");
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        MutableComponent charge = Component.translatable("item.wrathy_armament.mirror_sword_charge");
        charge.withStyle(WrathyArmamentItems.TITLE_FORMAT);
        charge.append(CommonComponents.SPACE).append(Component.translatable("" + itemstack.getOrCreateTag().getFloat("damageKeep")));
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.left_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.mirror_sword_hint").withStyle(WrathyArmamentItems.GOLD_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.mirror_sword_mirroring").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(charge);
    }

    public void addDamageToKeep(float damage, ItemStack stack) {
        if (stack.getOrCreateTag().getFloat("damageKeep") < 20 * ((getCurrentSparkles(stack) / 2f) + 1))
            stack.getOrCreateTag().putFloat("damageKeep", stack.getOrCreateTag().getFloat("damageKeep") + damage);
    }

    public void clearDamageThatKeeps(ItemStack stack) {
        stack.getOrCreateTag().putFloat("damageKeep", 0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand hand) {
        ItemStack stack = pPlayer.getItemInHand(hand);
        if (stack.getOrCreateTag().getFloat("damageKeep") > 0)
            pPlayer.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, pPlayer.getItemInHand(hand));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 9, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}