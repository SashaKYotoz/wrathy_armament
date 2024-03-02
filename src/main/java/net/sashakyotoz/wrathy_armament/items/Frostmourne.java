package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEnchants;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentParticleTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class Frostmourne extends SwordLikeItem implements IClientItemExtensions {
    public Frostmourne(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 19, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.8, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.game.frostmourne").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.frostmourne_hint").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.frostmourne_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.frostmourne_hint1").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.frostmourne_attack1").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 0)
            return;
        var f = getPowerForTime(i);
        if (!((double) f < 0.75D)) {
            WrathyArmament.LOGGER.debug("Soul Attack");
            entity.playSound(SoundEvents.SOUL_ESCAPE);
            if (level instanceof ServerLevel serverLevel){
                ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), serverLevel, 0.6f, false, false, 6,
                        ParticleTypes.SOUL, "rain");
                particleEntity.setOwner(entity);
                particleEntity.moveTo(new Vec3(entity.getX(), entity.getY() + 1.5f, entity.getZ()));
                level.addFreshEntity(particleEntity);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 30;
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private static final HumanoidModel.ArmPose SHOOT_POSE = HumanoidModel.ArmPose.create("SHOOT", true, (model, entity, arm) -> {
                if (arm == HumanoidArm.RIGHT){
                    model.rightArm.xRot = -0.8f;
                    model.leftArm.xRot = -0.9f;
                    model.rightArm.yRot = 0.5f;
                    model.leftArm.yRot = 0.75f;
                    model.rightArm.zRot = 1.1f;
                }else{
                    model.leftArm.xRot = -0.8f;
                    model.rightArm.xRot = -0.9f;
                    model.leftArm.yRot = -0.5f;
                    model.rightArm.yRot = -0.75f;
                    model.leftArm.zRot = -1.1f;
                }
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (!(entityLiving.isUsingItem()) && !entityLiving.swinging)
                    return HumanoidModel.ArmPose.ITEM;
                if (entityLiving.isUsingItem())
                    return SHOOT_POSE;
                if (entityLiving.swinging)
                    return HumanoidModel.ArmPose.ITEM;
                return HumanoidModel.ArmPose.EMPTY;
            }
            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                int k = arm == HumanoidArm.RIGHT ? 1 : -1;
                poseStack.translate(k * 0.56F, -0.52F, -0.72F);
                if (player.isUsingItem()) {
                    poseStack.translate((float) k * -0.28F, 0.15F, 0.158F);
                    poseStack.mulPose(Axis.XP.rotationDegrees(35F));
                    poseStack.mulPose(Axis.YP.rotationDegrees((float) k * -45F));
                    poseStack.mulPose(Axis.ZP.rotationDegrees((float) k * 90F));
                    float f8 = (float) itemInHand.getUseDuration() - ((float) player.getUseItemRemainingTicks() - partialTick + 1.0F);
                    float f12 = f8 / 20.0F;
                    f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                    if (f12 > 1.0F) {
                        f12 = 0.75F;
                    }
                    if (f12 > 0.1F) {
                        float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                        float f18 = f12 - 0.1F;
                        float f20 = f15 * f18;
                        poseStack.translate(0.0F, f20 * 0.004F, 0.0F);
                    }
                    poseStack.translate(0.0F, 0.0F, f12 * 0.04F);
                    poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                }
                return !player.swinging;
            }
        });
    }
}
