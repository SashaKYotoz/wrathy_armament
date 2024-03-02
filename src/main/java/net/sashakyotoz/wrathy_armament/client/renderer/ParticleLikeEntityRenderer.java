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
    private static final ResourceLocation FIRE_CYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/fire_cycle.png");
    private static final ResourceLocation FIRE_SEMICYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/fire_semicycle.png");
    private static final ResourceLocation LIGHT_CYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/light_cycle.png");
    private static final ResourceLocation PURPLE_CYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/purple_cycle.png");
    private static final ResourceLocation SOUL_CYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/soul_cycle.png");
    private static final ResourceLocation ZENITH_SEMICYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/zenith_semicycle.png");
    private static final ResourceLocation LIGHT_SEMICYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/light_semicycle.png");
    private static final ResourceLocation RED_SEMICYCLE = new ResourceLocation(WrathyArmament.MODID,"textures/entity/particle_like/red_semicycle.png");
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
        this.scale(entity, stack, partialTicks);
        this.model.setupAnim(entity, 0, 0.0F, f7, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexconsumer = source.getBuffer(this.model.renderType(getTextureLocation(entity)));
        if (entity.timeToVanish > 10)
            this.model.renderToBuffer(stack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,1.0F - entity.timeToVanish/20f);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, source, packedLightIn);
    }
    public void scale(ParticleLikeEntity entity, PoseStack stack, float partialTicks) {
        stack.scale(entity.getSize(),0.1f,entity.getSize());
    }
    protected float getBob(ParticleLikeEntity entity, float partialTicks) {
        return (float)entity.tickCount + partialTicks;
    }
    @Override
    public ResourceLocation getTextureLocation(ParticleLikeEntity entity) {
        switch (entity.getIdOfColor()){
            default -> {
                return FIRE_CYCLE;
            }
            case 1 ->{
                return FIRE_SEMICYCLE;
            }
            case 2 ->{
                return LIGHT_CYCLE;
            }
            case 3->{
                return LIGHT_SEMICYCLE;
            }
            case 4->{
                return RED_SEMICYCLE;
            }
            case 5->{
                return PURPLE_CYCLE;
            }
            case 6->{
                return SOUL_CYCLE;
            }
            case 7->{
                return ZENITH_SEMICYCLE;
            }
        }
    }
}
