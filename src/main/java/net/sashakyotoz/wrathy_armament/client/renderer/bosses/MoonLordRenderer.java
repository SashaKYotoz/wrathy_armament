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
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MoonLordRenderer extends FixedDeathAnimationMobRenderer<MoonLord, MoonLordModel<MoonLord>> {
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);
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
        this.addLayer(new MoonLordGlowingLayer<>(this,GLOWING_EYES_TEXTURE));
    }

    @Override
    public void scale(MoonLord lord, PoseStack pPoseStack, float pPartialTickTime) {
        pPoseStack.scale(2.5f, 2.5f, 2.5f);
    }

    @Override
    public void render(MoonLord lord, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(lord, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        if (lord.isLaserActivated())
            renderRay(lord, pPartialTicks, pPoseStack, pBuffer);
        if (lord.deathTicks > 0) {
            float f5 = ((float)lord.deathTicks + pPartialTicks) / 100.0F;
            float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
            RandomSource randomsource = RandomSource.create(432L);
            VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.lightning());
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, 1.0F, 0);

            for(int i = 0; (float)i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
                pPoseStack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F + f5 * 90.0F));
                float f3 = randomsource.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                float f4 = randomsource.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                Matrix4f matrix4f = pPoseStack.last().pose();
                int j = (int)(255.0F * (1.0F - f7));
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
    private BlockPos posOfViewing(LivingEntity entity, BlockPos pos) {
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

    private void renderRay(MoonLord pEntityLiving, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer) {
        pPoseStack.pushPose();
        BlockPos pos = posOfViewing(pEntityLiving, pEntityLiving.getOnPos().above(3));
        Vec3 vec3 = pos.getCenter();
        double d0 = (double) (Mth.lerp(pPartialTicks, pEntityLiving.yBodyRotO, pEntityLiving.yBodyRot) * ((float) Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = pEntityLiving.getLeashOffset(pPartialTicks);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(pPartialTicks, pEntityLiving.xo, pEntityLiving.getX()) + d1;
        double d4 = Mth.lerp(pPartialTicks, pEntityLiving.yo, pEntityLiving.getY()) + vec31.y;
        double d5 = Mth.lerp(pPartialTicks, pEntityLiving.zo, pEntityLiving.getZ()) + d2;
        pPoseStack.translate(d1, vec31.y, d2);
        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pPoseStack.last().pose();
        BlockPos blockpos = BlockPos.containing(pEntityLiving.getEyePosition(pPartialTicks));
        int i = this.getBlockLightLevel(pEntityLiving, blockpos);
        int j = getBlockLightLevel(pEntityLiving, pos);
        int k = pEntityLiving.level().getBrightness(LightLayer.SKY, blockpos);
        int l = pEntityLiving.level().getBrightness(LightLayer.SKY, pos);
        int segments = 32;
        float segmentSize = 0.25F;
        for (int i1 = 0; i1 <= segments; ++i1) {
            float t = (float) i1 / segments;
            float x = f * t;
            float y = f1 > 0.0F ? f1 * t * t : f1 - f1 * (1.0F - t) * (1.0F - t);
            float z = f2 * t;
            int blockLight = (int) Mth.lerp(t, (float) i, (float) j);
            int skyLight = (int) Mth.lerp(t, (float) k, (float) l);
            int light = LightTexture.pack(blockLight, skyLight);
            addCube(vertexconsumer, matrix4f, x, y, z, segmentSize, light);
        }
        pPoseStack.popPose();
    }

    protected int getBlockLightLevel(MoonLord pEntity, BlockPos pPos) {
        return pEntity.isOnFire() ? 15 : pEntity.level().getBrightness(LightLayer.BLOCK, pPos);
    }

    private void addCube(VertexConsumer vertexconsumer, Matrix4f matrix, float x, float y, float z, float size, int light) {
        float halfSize = size / 2.0F;
        addFace(vertexconsumer, matrix, x - halfSize, y - halfSize, z + halfSize, x + halfSize, y + halfSize, z + halfSize, light);
        addFace(vertexconsumer, matrix, x - halfSize, y - halfSize, z - halfSize, x + halfSize, y + halfSize, z - halfSize, light);
        addFace(vertexconsumer, matrix, x - halfSize, y - halfSize, z - halfSize, x - halfSize, y + halfSize, z + halfSize, light);
        addFace(vertexconsumer, matrix, x + halfSize, y - halfSize, z - halfSize, x + halfSize, y + halfSize, z + halfSize, light);
        addFace(vertexconsumer, matrix, x - halfSize, y + halfSize, z - halfSize, x + halfSize, y + halfSize, z + halfSize, light);
        addFace(vertexconsumer, matrix, x - halfSize, y - halfSize, z - halfSize, x + halfSize, y - halfSize, z + halfSize, light);
    }

    private void addFace(VertexConsumer vertexconsumer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2, int light) {
        vertexconsumer.vertex(matrix, x1, y1, z1).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix, x1, y2, z1).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix, x2, y2, z1).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix, x2, y1, z1).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();

        vertexconsumer.vertex(matrix, x1, y1, z2).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix, x1, y2, z2).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix, x2, y2, z2).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix, x2, y1, z2).color(0.25F, 0.75F, 1.0F, 0.5F).uv2(light).endVertex();
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