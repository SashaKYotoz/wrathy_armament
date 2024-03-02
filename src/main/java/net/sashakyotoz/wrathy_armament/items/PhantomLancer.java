package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEnchants;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentParticleTypes;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentSounds;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class PhantomLancer extends SwordLikeItem implements IClientItemExtensions {
    public PhantomLancer(Properties properties) {
        super(properties);
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
        var f = getPowerForTime(i);
        if (!((double) f < 0.75D)) {
            WrathyArmament.LOGGER.debug("sweep Attack");
            entity.playSound(SoundEvents.PHANTOM_SWOOP);
            double d0 = -Mth.sin(entity.getYRot() * ((float) Math.PI / 180F));
            double d1 = Mth.cos(entity.getYRot() * ((float) Math.PI / 180F));
            int tmp;
            int Int = stack.getEnchantmentLevel(WrathyArmamentEnchants.PHANTOM_FURY.get());
            if (Int > 0)
                tmp = Int;
            else
                tmp = 1;
            float scaling = 0;
            for (int i1 = 0; i1 < 15 + tmp * 3; i1++) {
                if (!level.getBlockState(new BlockPos(
                                level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX(),
                                level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY(),
                                level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()))
                        .canOcclude())
                    scaling = scaling + 1;
                level.addParticle(WrathyArmamentParticleTypes.PHANTOM_RAY.get(),
                        (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
                        (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
                        (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()), d0,
                        0.1, d1);
                final Vec3 _center = new Vec3(
                        (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
                        (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
                        (level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()));
                List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate((1.5 + tmp) / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : entityList) {
                    if (!(entityiterator == entity)) {
                        if (entityiterator instanceof LivingEntity livingEntity) {
                            float damage = livingEntity.getMaxHealth() - livingEntity.getHealth() + 2;
                            if (damage > 100)
                                damage = damage / 4;
                            livingEntity.hurt(new DamageSource(livingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                                @Override
                                public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                                    return Component.translatable("death.attack.wrathy_armament.phantom_shock_message");
                                }
                            }, damage);
                            WrathyArmament.LOGGER.debug("Damage:" + damage);
                            Player player = (Player) entity;
                            player.getCooldowns().addCooldown(stack.getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 60, 120));
                        }
                        if (stack.hurt(1, RandomSource.create(), null)) {
                            stack.shrink(1);
                            stack.setDamageValue(0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.left_hand").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_hint").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_circular_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_hint1").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_sweep_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        Player player = (Player) entity;
        final Vec3 center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        List<Entity> entities = player.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(entcnd -> entcnd.distanceToSqr(center))).toList();
        for (Entity entityIterator : entities) {
            if (entityIterator != null && entityIterator != entity) {
                if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(stack.getItem()) && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                    player.playSound(WrathyArmamentSounds.ITEM_LANCER_SHOT);
                    player.swing(InteractionHand.MAIN_HAND);
                    for (int i = 0; i < 360; i++) {
                        if (i % 20 == 0) {
                            entity.level().addParticle(ParticleTypes.SONIC_BOOM, entity.getX(), entity.getY() + 0.5, entity.getZ(),
                                    Math.cos(i) * 0.15d, 0.15d, Math.sin(i) * 0.15d);
                        }
                    }
                    float damage = player.getHealth() / 2;
                    if (damage <= 0.5)
                        damage = 4;
                    entityIterator.hurt(entityIterator.damageSources().generic(), damage);
                }
            }
        }
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return stack.is(WrathyArmamentItems.PHANTOM_LANCER.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);
        return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }

    private static float getPowerForTime(int i) {
        float f = (float) i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 9, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    //animations
    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private static final HumanoidModel.ArmPose STRIKE_POSE = HumanoidModel.ArmPose.create("STRIKE", false, (model, entity, arm) -> {
                if (arm == HumanoidArm.LEFT) {
                    model.leftArm.xRot = -0.75f;
                    model.leftArm.yRot = -0.3f;
                    model.leftArm.zRot = -0.15f;
                } else {
                    model.rightArm.xRot = -0.75f;
                    model.rightArm.yRot = 0.3f;
                    model.rightArm.zRot = 0.15f;
                }
            });
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!itemStack.isEmpty()) {
                    if (!(entityLiving.isUsingItem()) && !entityLiving.swinging)
                        return HumanoidModel.ArmPose.ITEM;
                    if (entityLiving.isUsingItem())
                        return STRIKE_POSE;
                    if (entityLiving.swinging)
                        return HumanoidModel.ArmPose.ITEM;
                }
                return HumanoidModel.ArmPose.EMPTY;
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                int k = arm == HumanoidArm.RIGHT ? 1 : -1;
                poseStack.translate(k * 0.56F, -0.52F, -0.72F);
                if (player.isUsingItem()) {
                    poseStack.translate((float) k * -0.28F, 0.15F, 0.158F);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-14F));
                    poseStack.mulPose(Axis.YP.rotationDegrees((float) k * 65F));
                    poseStack.mulPose(Axis.ZP.rotationDegrees((float) k * -10F));
                    float f8 = (float) itemInHand.getUseDuration() - ((float) player.getUseItemRemainingTicks() - partialTick + 1.0F);
                    float f12 = f8 / 20.0F;
                    f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                    if (f12 > 1.0F) {
                        f12 = 1.0F;
                    }
                    if (f12 > 0.1F) {
                        float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                        float f18 = f12 - 0.1F;
                        float f20 = f15 * f18;
                        poseStack.translate(0.0F, f20 * 0.004F, 0.0F);
                    }
                    poseStack.translate(0.0F, 0.0F, f12 * 0.04F);
                    poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                    poseStack.mulPose(Axis.YN.rotationDegrees((float) k * 75.0F));
                }
                return !player.swinging;
            }
        });
    }
}