package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.HierarchicalPlayerModel;
import net.sashakyotoz.wrathy_armament.entities.bosses.Habciak;
import org.jetbrains.annotations.NotNull;

public class HabciakRenderer extends FixedDeathAnimationMobRenderer<Habciak, HierarchicalPlayerModel<Habciak>> {
    private final HierarchicalPlayerModel<Habciak> slimModel;
    private final HierarchicalPlayerModel<Habciak> normalModel;

    public HabciakRenderer(EntityRendererProvider.Context context) {
        super(context, new HierarchicalPlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), false), 0);
        slimModel = new HierarchicalPlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), false);
        normalModel = new HierarchicalPlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), true);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new ArrowLayer<>(context, this));
    }

    @Override
    public void render(Habciak habciak, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(habciak, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        if (habciak.playerToRender() instanceof AbstractClientPlayer player) {
            if (player.getModelName().equals("slim"))
                this.model = slimModel;
            else
                this.model = normalModel;
        }
        this.setModelProperties(habciak);
    }
    protected void scale(Habciak habciak, PoseStack pPoseStack, float pPartialTickTime) {
        float f = 0.9375F;
        pPoseStack.scale(f, f, f);
    }

    @Override
    protected void setupRotations(Habciak habciak, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(habciak, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        if (habciak.isFallFlying()) {
            super.setupRotations(habciak, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
            float f1 = (float)habciak.getFallFlyingTicks() + pPartialTicks;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!habciak.isAutoSpinAttack())
                pPoseStack.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - habciak.getXRot())));
            Vec3 vec3 = habciak.getViewVector(pPartialTicks);
            Vec3 vec31 = getDeltaMovementLerped(habciak,pPartialTicks);
            double d0 = vec31.horizontalDistanceSqr();
            double d1 = vec3.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
                double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
                pPoseStack.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        }
    }
    public Vec3 getDeltaMovementLerped(Habciak habciak,float pPatialTick) {
        return habciak.getDeltaMovement().lerp(habciak.getDeltaMovement(), pPatialTick);
    }
    private void setModelProperties(Habciak habciak) {
        HumanoidModel<Habciak> humanoidModel = this.getModel();
        humanoidModel.crouching = habciak.isCrouching();
        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(habciak, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(habciak, InteractionHand.OFF_HAND);
        if (humanoidmodel$armpose.isTwoHanded())
            humanoidmodel$armpose1 = habciak.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        if (habciak.getMainArm() == HumanoidArm.RIGHT) {
            humanoidModel.rightArmPose = humanoidmodel$armpose;
            humanoidModel.leftArmPose = humanoidmodel$armpose1;
        } else {
            humanoidModel.rightArmPose = humanoidmodel$armpose1;
            humanoidModel.leftArmPose = humanoidmodel$armpose;
        }
    }

    private static HumanoidModel.ArmPose getArmPose(Habciak habciak, InteractionHand pHand) {
        ItemStack itemstack = habciak.getItemInHand(pHand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (habciak.getUsedItemHand() == pHand && habciak.getUseItemRemainingTicks() > 0) {
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

                if (useanim == UseAnim.CROSSBOW && pHand == habciak.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }
            } else if (!habciak.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            HumanoidModel.ArmPose forgeArmPose = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(itemstack).getArmPose(habciak, pHand, itemstack);
            if (forgeArmPose != null) return forgeArmPose;

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Habciak habciak) {
        Player player = habciak.playerToRender();
        if (player instanceof AbstractClientPlayer clientPlayer) {
            return clientPlayer.getSkinTextureLocation();
        }
        return WrathyArmament.createWALocation("textures/entity/bosses/invisible_habciak.png");
    }
}
