package net.sashakyotoz.wrathy_armament.client.renderer.livings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.TrueEyeOfCthulhuModel;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.FixedDeathAnimationMobRenderer;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;

public class TrueEyeOfCthulhuRenderer extends FixedDeathAnimationMobRenderer<TrueEyeOfCthulhu, TrueEyeOfCthulhuModel<TrueEyeOfCthulhu>> {
    public TrueEyeOfCthulhuRenderer(EntityRendererProvider.Context context) {
        super(context, new TrueEyeOfCthulhuModel<>(context.bakeLayer(TrueEyeOfCthulhuModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public void render(TrueEyeOfCthulhu pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
       if (!pEntity.isInAngryMode()){
           float[][] offsets = {
                   {-1.0f, 0.0f},    // left
                   {-0.65f, 0.65f}, // left-up
                   {0.0f, 1.0f},     // up
                   {0.65f, 0.65f},  // up-right
                   {1.0f, 0.0f},     // right
                   {0.65f, -0.65f}, // down-right
                   {0.0f, -1.0f},    // down
                   {-0.65f, -0.65f} // down-left
           };
           for (int i = 0; i < offsets.length; i++) {
               pPoseStack.pushPose();
               pPoseStack.translate(offsets[i][0], offsets[i][1], 0);
               pPoseStack.scale(0.5f, 0.5f, 0.5f);
               pPoseStack.mulPose(Axis.YP.rotationDegrees((pEntity.tickCount % 180)));
               pPoseStack.mulPose(Axis.ZP.rotation((pEntity.tickCount % 180)*0.35f));
               model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 0.25f, 0.25f, 0.25f, 0.5f);
               pPoseStack.popPose();
           }
       }
        pPoseStack.translate(0,-0.25f,0);
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TrueEyeOfCthulhu pEntity) {
        return pEntity.isInAngryMode() ? WrathyArmament.createWALocation("textures/entity/evil_true_eye_of_cthulhu.png") : WrathyArmament.createWALocation("textures/entity/true_eye_of_cthulhu.png");
    }
}