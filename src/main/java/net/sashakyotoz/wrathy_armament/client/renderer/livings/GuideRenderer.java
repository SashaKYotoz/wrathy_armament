package net.sashakyotoz.wrathy_armament.client.renderer.livings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.alive.Guide;

public class GuideRenderer extends LivingEntityRenderer<Guide, PlayerModel<Guide>> {
    public GuideRenderer(EntityRendererProvider.Context pContext, boolean pUseSlimModel) {
        super(pContext, new PlayerModel<>(pContext.bakeLayer(pUseSlimModel ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), pUseSlimModel), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel<>(pContext.bakeLayer(pUseSlimModel ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel(pContext.bakeLayer(pUseSlimModel ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), pContext.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
        this.addLayer(new ArrowLayer<>(pContext, this));
        this.addLayer(new CustomHeadLayer<>(this, pContext.getModelSet(), pContext.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, pContext.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer<>(this, pContext.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Guide pEntity) {
        return WrathyArmament.createWALocation("textures/entity/the_guide.png");
    }

    @Override
    public void render(Guide pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        this.setModelProperties(pEntity);
    }

    private void setModelProperties(Guide guide) {
        PlayerModel<Guide> playermodel = this.getModel();
        playermodel.crouching = guide.isCrouching();
        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(guide, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(guide, InteractionHand.OFF_HAND);
        if (humanoidmodel$armpose.isTwoHanded())
            humanoidmodel$armpose1 = guide.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        if (guide.getMainArm() == HumanoidArm.RIGHT) {
            playermodel.rightArmPose = humanoidmodel$armpose;
            playermodel.leftArmPose = humanoidmodel$armpose1;
        } else {
            playermodel.rightArmPose = humanoidmodel$armpose1;
            playermodel.leftArmPose = humanoidmodel$armpose;
        }
    }

    @Override
    public void setupRotations(Guide pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        if (this.isShaking(pEntityLiving)) {
            pRotationYaw += (float) (Math.cos((double) pEntityLiving.tickCount * 3.25D) * Math.PI * (double) 0.4F);
        }
        if (!pEntityLiving.hasPose(Pose.SLEEPING)) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - pRotationYaw));
        }
        if (isEntityUpsideDown(pEntityLiving)) {
            pPoseStack.translate(0.0F, pEntityLiving.getBbHeight() + 0.1F, 0.0F);
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    private static HumanoidModel.ArmPose getArmPose(Guide pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (pPlayer.getUsedItemHand() == pHand && pPlayer.getUseItemRemainingTicks() > 0) {
                UseAnim useanim = itemstack.getUseAnimation();
                if (useanim == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useanim == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useanim == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (useanim == UseAnim.CROSSBOW && pHand == pPlayer.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (useanim == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (useanim == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            } else if (!pPlayer.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            HumanoidModel.ArmPose forgeArmPose = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(itemstack).getArmPose(pPlayer, pHand, itemstack);
            if (forgeArmPose != null) return forgeArmPose;

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    public void scale(Guide pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        float f = 0.9375F;
        pPoseStack.scale(f, f, f);
    }
}