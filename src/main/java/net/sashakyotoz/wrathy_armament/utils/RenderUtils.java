package net.sashakyotoz.wrathy_armament.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderUtils {

    public static void renderBeam(Mob entity, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, RenderType type, boolean toScale) {
        if (entity.getTarget() != null) {
            float f1 = entity.level().getGameTime() + pPartialTicks;
            float f2 = f1 * 0.5F % 1.0F;
            float f3 = entity.getEyeHeight() - 0.1f;
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, f3, 0.0F);
            Vec3 vec3 = getPosition(entity.getTarget(), (double) entity.getTarget().getBbHeight() * 0.5D, pPartialTicks);
            Vec3 vec31 = getPosition(entity, f3, pPartialTicks);
            Vec3 vec32 = vec3.subtract(vec31);
            float f4 = (float) (vec32.length() + 1.0D);
            vec32 = vec32.normalize();
            float f5 = (float) Math.acos(vec32.y);
            float f6 = (float) Math.atan2(vec32.z, vec32.x);
            float modifier = (float) getOscillatingValue(entity.tickCount, 3);
            modifier = Math.max(modifier, 0.25f);
            pPoseStack.scale(modifier, modifier, modifier);
            pPoseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
            float f7 = f1 * 0.05F * -1.5F;
            int j = 0;
            int k = 100;
            int l = 255;
            float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
            float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
            float f13 = Mth.cos(f7 + ((float) Math.PI / 4F)) * 0.282F;
            float f14 = Mth.sin(f7 + ((float) Math.PI / 4F)) * 0.282F;
            float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
            float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
            float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
            float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
            float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
            float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
            float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
            float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
            float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
            float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
            float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
            float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
            float f29 = -1.0F + f2;
            float f30 = f4 * 2.5F + f29;
            VertexConsumer vertexconsumer = pBuffer.getBuffer(type);
            PoseStack.Pose posestack$pose = pPoseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
            float f31 = 0.0F;
            if (entity.tickCount % 2 == 0)
                f31 = 0.5F;
            vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
            vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
            pPoseStack.popPose();
        } else {
            float f1 = entity.level().getGameTime() + pPartialTicks;
            float f2 = f1 * 0.5F % 1.0F;
            float f3 = entity.getEyeHeight() - 0.15f;
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, f3, 0.0F);
            Vec3 vec3 = getPosition(posOfViewing(entity, entity.getOnPos().above()), 1, pPartialTicks);
            Vec3 vec31 = getPosition(entity, f3, pPartialTicks);
            Vec3 vec32 = vec3.subtract(vec31);
            float f4 = (float) (vec32.length() + 1.0D);
            vec32 = vec32.normalize();
            float f5 = (float) Math.acos(vec32.y);
            float f6 = (float) Math.atan2(vec32.z, vec32.x);
            pPoseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
            float f7 = f1 * 0.05F * -1.5F;
            int j = 0;
            int k = 100;
            int l = 255;
            float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
            float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
            float f13 = Mth.cos(f7 + ((float) Math.PI / 4F)) * 0.282F;
            float f14 = Mth.sin(f7 + ((float) Math.PI / 4F)) * 0.282F;
            float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
            float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
            float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
            float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
            float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
            float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
            float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
            float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
            float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
            float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
            float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
            float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
            float f29 = -1.0F + f2;
            float f30 = f4 * 2.5F + f29;
            VertexConsumer vertexconsumer = pBuffer.getBuffer(type);
            PoseStack.Pose posestack$pose = pPoseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
            vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
            vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
            float f31 = 0.0F;
            if (entity.tickCount % 2 == 0)
                f31 = 0.5F;
            vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
            vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
            vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
            pPoseStack.popPose();
        }
    }

    private static BlockPos posOfViewing(LivingEntity entity, BlockPos pos) {
        BlockPos pos1 = pos;
        int scaling = 0;
        for (int i = 0; i < 32; i++) {
            BlockPos pos2 = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
            if (!entity.level().getBlockState(new BlockPos(pos2.getX(), pos2.getY(), pos2.getZ())).canOcclude())
                scaling = scaling + 1;
            pos1 = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
        }
        return pos1;
    }

    private static Vec3 getPosition(LivingEntity pLivingEntity, double pYOffset, float pPartialTick) {
        double d0 = Mth.lerp(pPartialTick, pLivingEntity.xOld, pLivingEntity.getX());
        double d1 = Mth.lerp(pPartialTick, pLivingEntity.yOld, pLivingEntity.getY()) + pYOffset;
        double d2 = Mth.lerp(pPartialTick, pLivingEntity.zOld, pLivingEntity.getZ());
        return new Vec3(d0, d1, d2);
    }

    private static Vec3 getPosition(BlockPos pos, double pYOffset, float pPartialTick) {
        double d0 = Mth.lerp(pPartialTick, pos.getX() + 0.5f, pos.getX());
        double d1 = Mth.lerp(pPartialTick, pos.getY(), pos.getY()) + pYOffset;
        double d2 = Mth.lerp(pPartialTick, pos.getZ() + 0.5f, pos.getZ());
        return new Vec3(d0, d1, d2);
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f pPose, Matrix3f pNormal, float pX, float pY, float pZ, int pRed, int pGreen, int pBlue, float pU, float pV) {
        pConsumer.vertex(pPose, pX, pY, pZ).color(pRed, pGreen, pBlue, 255).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static double getOscillatingValue(int tickCount, int periodInSeconds) {
        int periodInTicks = periodInSeconds * 20;
        double phase = (2 * Math.PI * (tickCount % periodInTicks)) / periodInTicks;
        return 0.5 * (1 + Math.sin(phase));
    }
}