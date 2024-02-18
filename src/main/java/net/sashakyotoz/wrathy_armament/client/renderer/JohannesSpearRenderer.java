package net.sashakyotoz.wrathy_armament.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.JohannesSpearsModel;
import net.sashakyotoz.wrathy_armament.entities.technical.JohannesSpearEntity;

public class JohannesSpearRenderer extends EntityRenderer<JohannesSpearEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(WrathyArmament.MODID,"textures/entity/johannes_spear.png");
    private final JohannesSpearsModel<JohannesSpearEntity> model;

    public JohannesSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new JohannesSpearsModel<>(context.bakeLayer(JohannesSpearsModel.LAYER_LOCATION));
    }

    public void render(JohannesSpearEntity entity, float p_114529_, float p_114530_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_114533_) {
        float f = entity.getAnimationProgress(p_114530_);
        if (f != 0.0F) {
            float f1 = 2.0F;
            if (f > 0.9F) {
                f1 *= (1.0F - f) / 0.1F;
            }
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
            poseStack.scale(-f1, -f1, f1);
            poseStack.translate(0.0D, entity.lifeTicks < 11 ? -0.2D : 0.5, 0.0D);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(entity, f, 0.0F, 0.0F, entity.getYRot(), entity.getXRot());
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(poseStack, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            super.render(entity, p_114529_, p_114530_, poseStack, multiBufferSource, p_114533_);
        }
    }

    public ResourceLocation getTextureLocation(JohannesSpearEntity entity) {
        return TEXTURE_LOCATION;
    }
}
