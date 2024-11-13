package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.MoonLordModel;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.MoonLordEmissiveLayer;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.MoonLordEyesLayer;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.MoonLordGlowingLayer;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;
import net.sashakyotoz.wrathy_armament.utils.RenderUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MoonLordRenderer extends FixedDeathAnimationMobRenderer<MoonLord, MoonLordModel<MoonLord>> {
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);
    private final ResourceLocation TEXTURE = WrathyArmament.createWALocation("textures/entity/bosses/moon_lord.png");
    private static final ResourceLocation HEART_TEXTURE = WrathyArmament.createWALocation("textures/entity/bosses/moon_lord/heart.png");
    private static final ResourceLocation HAND_EYE_TEXTURE = WrathyArmament.createWALocation("textures/entity/bosses/moon_lord/hand_eye.png");
    private static final ResourceLocation HEAD_EYE_TEXTURE = WrathyArmament.createWALocation("textures/entity/bosses/moon_lord/head_eye.png");
    private static final ResourceLocation GLOWING_EYES_TEXTURE = WrathyArmament.createWALocation("textures/entity/bosses/glowing_eyes/moon_lord_glowing_eyes.png");

    public MoonLordRenderer(EntityRendererProvider.Context context) {
        super(context, new MoonLordModel<>(context.bakeLayer(MoonLordModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new MoonLordEmissiveLayer<>(this, HEART_TEXTURE, (t, v, v1) -> Math.max(0.0F, Mth.cos(v1 * 0.1F) * 0.5F), MoonLordModel::getHeartLayerModelParts));
        this.addLayer(new MoonLordEyesLayer(this, HAND_EYE_TEXTURE));
        this.addLayer(new MoonLordEyesLayer(this, HEAD_EYE_TEXTURE));
        this.addLayer(new MoonLordGlowingLayer<>(this, GLOWING_EYES_TEXTURE));
    }

    @Override
    public void scale(MoonLord lord, PoseStack pPoseStack, float pPartialTickTime) {
        pPoseStack.scale(2.5f, 2.5f, 2.5f);
    }

    @Override
    public void render(MoonLord lord, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(lord, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        if (lord.isLaserActivated())
            RenderUtils.renderBeam(
                    lord,
                    pPartialTicks,
                    pPoseStack,
                    pBuffer,
                    RenderType.entityCutoutNoCull(WrathyArmament.createWALocation("textures/entity/bosses/moon_lord/beam.png")),
                    true);
        if (lord.deathTicks > -10) {
            float f5 = ((float) lord.deathTicks + pPartialTicks) / 100.0F;
            float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
            RandomSource randomsource = RandomSource.create(432L);
            VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.lightning());
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, 1.0F, 0);

            for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
                pPoseStack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F + f5 * 90.0F));
                float f3 = randomsource.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                float f4 = randomsource.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                Matrix4f matrix4f = pPoseStack.last().pose();
                int j = (int) (255.0F * (1.0F - f7));
                vertex01(vertexConsumer, matrix4f, j);
                vertex2(vertexConsumer, matrix4f, f3, f4);
                vertex3(vertexConsumer, matrix4f, f3, f4);
                vertex01(vertexConsumer, matrix4f, j);
                vertex3(vertexConsumer, matrix4f, f3, f4);
                vertex4(vertexConsumer, matrix4f, f3, f4);
                vertex01(vertexConsumer, matrix4f, j);
                vertex4(vertexConsumer, matrix4f, f3, f4);
                vertex2(vertexConsumer, matrix4f, f3, f4);
            }

            pPoseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(MoonLord pEntity) {
        return TEXTURE;
    }


    protected int getBlockLightLevel(MoonLord pEntity, BlockPos pPos) {
        return pEntity.isOnFire() ? 15 : pEntity.level().getBrightness(LightLayer.BLOCK, pPos);
    }

    @Override
    public boolean shouldRender(MoonLord pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    private static void vertex01(VertexConsumer pConsumer, Matrix4f pMatrix, int pAlpha) {
        pConsumer.vertex(pMatrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, pAlpha).endVertex();
    }

    private static void vertex2(VertexConsumer pConsumer, Matrix4f pMatrix, float p_253704_, float p_253701_) {
        pConsumer.vertex(pMatrix, -HALF_SQRT_3 * p_253701_, p_253704_, -0.5F * p_253701_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(VertexConsumer pConsumer, Matrix4f pMatrix, float p_253729_, float p_254030_) {
        pConsumer.vertex(pMatrix, HALF_SQRT_3 * p_254030_, p_253729_, -0.5F * p_254030_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(VertexConsumer pConsumer, Matrix4f pMatrix, float p_253649_, float p_253694_) {
        pConsumer.vertex(pMatrix, 0.0F, p_253649_, p_253694_).color(255, 0, 255, 0).endVertex();
    }
}