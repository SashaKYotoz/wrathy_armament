package net.sashakyotoz.wrathy_armament.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.*;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;

public class HarmfulProjectileRenderer extends EntityRenderer<HarmfulProjectileEntity> {
    private final ResourceLocation AXE = WrathyArmament.createWALocation("textures/entity/projectile_like/axe_projectile.png");
    private final ResourceLocation DAGGER = WrathyArmament.createWALocation("textures/entity/projectile_like/dagger_projectile.png");
    private final ResourceLocation SWORD = WrathyArmament.createWALocation("textures/entity/projectile_like/huge_sword.png");
    private EntityModel<HarmfulProjectileEntity> model;
    private final DaggerProjectileModel<HarmfulProjectileEntity> daggerModel;
    private final HugeSwordModel<HarmfulProjectileEntity> swordModel;
    private final ShieldDashModel<HarmfulProjectileEntity> shieldModel;
    private final AxeProjectileModel<HarmfulProjectileEntity> axeModel;
    private final ParticleLikeEntityModel<HarmfulProjectileEntity> circleModel;

    public HarmfulProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DaggerProjectileModel<>(context.bakeLayer(DaggerProjectileModel.LAYER_LOCATION));
        this.daggerModel = new DaggerProjectileModel<>(context.bakeLayer(DaggerProjectileModel.LAYER_LOCATION));
        this.axeModel = new AxeProjectileModel<>(context.bakeLayer(AxeProjectileModel.LAYER_LOCATION));
        this.swordModel = new HugeSwordModel<>(context.bakeLayer(HugeSwordModel.LAYER_LOCATION));
        this.shieldModel = new ShieldDashModel<>(context.bakeLayer(ShieldDashModel.LAYER_LOCATION));
        this.circleModel = new ParticleLikeEntityModel<>(context.bakeLayer(ParticleLikeEntityModel.LAYER_LOCATION));
    }

    @Override
    public void render(HarmfulProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
        poseStack.pushPose();
        if (entity.rotationRelativelyToY() != 0 && !entity.getProjectileType().equals("huge_sword")) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180 - entity.rotationRelativelyToY()));
        }
        if (entity.getProjectileType().equals("vertical_circle")) {
            poseStack.scale(0.75f, 0.75f, 0.75f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.ZP.rotationDegrees(entity.tickCount % 360));
        }
        if (entity.getProjectileType().equals("axe"))
            poseStack.mulPose(Axis.XP.rotation(Mth.TWO_PI * entity.tickCount * 0.125f));
        switch (entity.getProjectileType()) {
            case "axe", "knight_axe" -> this.model = axeModel;
            case "huge_sword" -> this.model = swordModel;
            case "shield_dash" -> this.model = shieldModel;
            case "vertical_circle" -> this.model = circleModel;
            default -> this.model = daggerModel;
        }
        float f7 = this.getBob(entity, partialTicks);
        if (entity.getProjectileType().equals("huge_sword")) {
            poseStack.scale(2.5f, 2.5f, 2.5f);
            poseStack.mulPose(Axis.XP.rotation(135));
            poseStack.translate(0, -3.5 + entity.timeToVanish / 40f, 0);
        } else if (entity.getProjectileType().equals("shield_dash")) {
            poseStack.scale(1.5f + entity.timeToVanish / 5f, 1.5f + entity.timeToVanish / 5f, 1.5f + entity.timeToVanish / 5f);
            poseStack.translate(0, -1.5f, 0);
        } else
            poseStack.translate(0, -0.5, 0);
        this.model.setupAnim(entity, 0, 0.0F, f7, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(getTextureLocation(entity)));
        if (entity.timeToVanish > 10)
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F - entity.timeToVanish / 20f);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
    }

    private float getBob(HarmfulProjectileEntity entity, float partialTicks) {
        return (float) entity.tickCount + partialTicks;
    }

    @Override
    public ResourceLocation getTextureLocation(HarmfulProjectileEntity entity) {
        return switch (entity.getProjectileType()) {
            case "axe", "knight_axe" -> AXE;
            case "huge_sword" -> SWORD;
            case "shield_dash" ->
                    TextureAnimator.getAnimatedTextureByName(WrathyArmament.MODID, "textures/entity/particle_like/shield_dash/", "shield_dash");
            case "vertical_circle" -> ParticleLikeEntityRenderer.LIGHT_CIRCLE;
            default -> DAGGER;
        };
    }
}
