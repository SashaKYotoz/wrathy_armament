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
import net.sashakyotoz.wrathy_armament.client.models.technical.ParticleLikeEntityModel;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;

public class ParticleLikeEntityRenderer extends EntityRenderer<ParticleLikeEntity> {
    private static final ResourceLocation FIRE_CIRCLE = WrathyArmament.createWALocation("textures/entity/particle_like/fire_circle.png");
    private static final ResourceLocation FIRE_SEMICIRCLE = WrathyArmament.createWALocation("textures/entity/particle_like/fire_semicircle.png");
    public static final ResourceLocation LIGHT_CIRCLE = WrathyArmament.createWALocation("textures/entity/particle_like/light_circle.png");
    private static final ResourceLocation PURPLE_CIRCLE = WrathyArmament.createWALocation("textures/entity/particle_like/purple_circle.png");
    private static final ResourceLocation SOUL_CIRCLE = WrathyArmament.createWALocation("textures/entity/particle_like/soul_circle.png");
    private final ParticleLikeEntityModel<ParticleLikeEntity> model;

    public ParticleLikeEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ParticleLikeEntityModel<>(context.bakeLayer(ParticleLikeEntityModel.LAYER_LOCATION));
    }
    @Override
    public void render(ParticleLikeEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource source, int packedLightIn) {
        stack.pushPose();
        if (entity.toTurnParticle() && entity.rotationRelativelyToY() != 0){
            stack.mulPose(Axis.ZP.rotationDegrees(entity.rotationRelativelyToY()));
            stack.mulPose(Axis.YP.rotationDegrees(180 - entity.rotationRelativelyToY()));
        }
        float f7 = this.getBob(entity, partialTicks);
        this.scale(entity, stack);
        if (entity.getParticleType().equals("lich_rain") || entity.getParticleType().equals("rain"))
            stack.mulPose(Axis.YP.rotationDegrees((entity.tickCount % 360)*2.5f));
        this.model.setupAnim(entity, 0, 0.0F, f7, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexconsumer = source.getBuffer(this.model.renderType(getTextureLocation(entity)));
        if (entity.timeToVanish > 10)
            this.model.renderToBuffer(stack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,1.0F - entity.timeToVanish/20f);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, source, packedLightIn);
    }
    public void scale(ParticleLikeEntity entity, PoseStack stack) {
        stack.scale(entity.getSize(),0.1f,entity.getSize());
    }
    private float getBob(ParticleLikeEntity entity, float partialTicks) {
        return (float)entity.tickCount + partialTicks;
    }
    @Override
    public ResourceLocation getTextureLocation(ParticleLikeEntity entity) {
        switch (entity.getIdOfColor()){
            case 1 ->{
                return FIRE_SEMICIRCLE;
            }
            case 2 ->{
                return LIGHT_CIRCLE;
            }
            case 3->{
                return PURPLE_CIRCLE;
            }
            case 4->{
                return SOUL_CIRCLE;
            }
            default -> {
                return FIRE_CIRCLE;
            }
        }
    }
}