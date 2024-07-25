package net.sashakyotoz.wrathy_armament.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.*;
import net.sashakyotoz.wrathy_armament.entities.technical.BladeOfChaosEntity;
import org.joml.Matrix4f;

public class BladeOfChaosRenderer extends AdvancedEntityRenderer<BladeOfChaosEntity, BladeOfChaosModel<BladeOfChaosEntity>> {
    private final BladeOfChaosModel<BladeOfChaosEntity> blade;
    private static final ResourceLocation bladeTexture = WrathyArmament.createWALocation("textures/item/blade_of_chaos.png");

    public BladeOfChaosRenderer(EntityRendererProvider.Context context) {
        super(context,new BladeOfChaosModel<>(context.bakeLayer(BladeOfChaosModel.LAYER_LOCATION)),0.5f);
        this.blade = new BladeOfChaosModel<>(context.bakeLayer(BladeOfChaosModel.LAYER_LOCATION));
    }

    @Override
    public void render(BladeOfChaosEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
        VertexConsumer vb = bufferSource.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180 + Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        if(this.model != null)
            this.model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.75f);
        else
            this.blade.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1f);
        poseStack.popPose();
        Entity entity1 = entity.getOwner();
        if (entity1 != null)
            this.renderLeash(entity, partialTicks, poseStack, bufferSource, entity1);
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
    }
    private void renderLeash(BladeOfChaosEntity entity, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, Entity pChainHolder) {
        pPoseStack.pushPose();
        Vec3 vec3 = pChainHolder.getRopeHoldPosition(pPartialTicks);
        double d0 = (double)(Mth.lerp(pPartialTicks, entity.yRotO, entity.getYRot()) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = entity.getLeashOffset(pPartialTicks);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(pPartialTicks, entity.xo, entity.getX()) + d1;
        double d4 = Mth.lerp(pPartialTicks, entity.yo, entity.getY()) + vec31.y;
        double d5 = Mth.lerp(pPartialTicks, entity.zo, entity.getZ()) + d2;
        pPoseStack.translate(d1, vec31.y, d2);
        float f = (float)(vec3.x - d3);
        float f1 = (float)(vec3.y - d4);
        float f2 = (float)(vec3.z - d5);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pPoseStack.last().pose();
        float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = BlockPos.containing(entity.getEyePosition(pPartialTicks));
        BlockPos blockpos1 = BlockPos.containing(pChainHolder.getEyePosition(pPartialTicks));
        int i = this.getBlockLightLevel(entity, blockpos);
        int j = pChainHolder.level().getBrightness(LightLayer.BLOCK, pChainHolder.getOnPos());
        int k = entity.level().getBrightness(LightLayer.SKY, blockpos);
        int l = entity.level().getBrightness(LightLayer.SKY, blockpos1);

        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, (int)i/2, (int)j/2, (int)k/2, l/2, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, (int)i/2, (int)j/2, (int)k/2, l/2, 0.025F, 0.0F, f5, f6, j1, true);
        }

        pPoseStack.popPose();
    }

    private static void addVertexPair(VertexConsumer pConsumer, Matrix4f pMatrix, float p_174310_, float p_174311_, float p_174312_, int pEntityBlockLightLevel, int pLeashHolderBlockLightLevel, int pEntitySkyLightLevel, int pLeashHolderSkyLightLevel, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int pIndex, boolean p_174322_) {
        float f = (float)pIndex / 24.0F;
        int i = (int)Mth.lerp(f, (float)pEntityBlockLightLevel, (float)pLeashHolderBlockLightLevel);
        int j = (int)Mth.lerp(f, (float)pEntitySkyLightLevel, (float)pLeashHolderSkyLightLevel);
        int k = LightTexture.pack(i, j);
        float f1 = pIndex % 2 == (p_174322_ ? 1 : 0) ? 0.7F : 1.0F;
        float f2 = 0.5F * f1;
        float f3 = 0.4F * f1;
        float f4 = 0.3F * f1;
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        pConsumer.vertex(pMatrix, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        pConsumer.vertex(pMatrix, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }
    @Override
    public ResourceLocation getTextureLocation(BladeOfChaosEntity entity) {
        return bladeTexture;
    }
}