package net.sashakyotoz.wrathy_armament.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.TransparentHumanoidLayerModel;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;
import org.antlr.v4.runtime.misc.Triple;

public class TransparentFireLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final TransparentHumanoidLayerModel<T> humanoidLayerModel;
    private final TransparentHumanoidLayerModel<T> shadeHumanoidLayerModel;

    public TransparentFireLayer(RenderLayerParent<T, M> pRenderer, EntityModelSet modelSet) {
        super(pRenderer);
        humanoidLayerModel = new TransparentHumanoidLayerModel<>(modelSet.bakeLayer(TransparentHumanoidLayerModel.LAYER_LOCATION));
        shadeHumanoidLayerModel = new TransparentHumanoidLayerModel<>(modelSet.bakeLayer(TransparentHumanoidLayerModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity instanceof Player player && shouldRender(player)) {
            final boolean[] flags = {false, false};
            player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(mistsplitterDefenseCapability -> flags[0] = mistsplitterDefenseCapability.isDefenseModeOn());
            player.getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(halfZatoichiAbilityCapability -> flags[1] = halfZatoichiAbilityCapability.isInAdrenalinMode());
            final Triple<Float, Float, Float>[] colors = new Triple[]{new Triple<>(1, 1, 1)};
            if (flags[0] && !flags[1]) {
                player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(mistsplitterDefenseCapability -> {
                    switch (mistsplitterDefenseCapability.getDefenceType()) {
                        case "air" -> colors[0] = new Triple<>(1f, 1f, 1f);
                        case "earth" -> colors[0] = new Triple<>(0.2f, 0.9f, 0.15f);
                        case "elemental" -> colors[0] = new Triple<>(1f, 0f, 1f);
                        case "water" -> colors[0] = new Triple<>(0f, 0f, 1f);
                        default -> colors[0] = new Triple<>(1f, 0.25f, 0f);
                    }
                });
            } else if (!flags[0] && flags[1])
                player.getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(halfZatoichiAbilityCapability -> colors[0] = new Triple<>(1f, 1f, 0f));
            ResourceLocation resourcelocation = TextureAnimator.getAnimatedTextureByName(WrathyArmament.MODID, "textures/entity/layers/transparent_fire/", "transparent_fire_overlay");
            pPoseStack.pushPose();
            ModelFollowingRenderer.followBodyRotations(pLivingEntity, (HumanoidModel<LivingEntity>) this.humanoidLayerModel);
            this.getParentModel().copyPropertiesTo(this.humanoidLayerModel);
            this.humanoidLayerModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(resourcelocation, true));
            this.humanoidLayerModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, colors[0].a, colors[0].b, colors[0].c, 0.75F);
            pPoseStack.popPose();
            if (flags[1]) {
                pPoseStack.pushPose();
                pPoseStack.translate(0f, OnActionsTrigger.getYVector(0.125f, player.getXRot()), 0.125f);
                ModelFollowingRenderer.followBodyRotations(pLivingEntity, (HumanoidModel<LivingEntity>) this.shadeHumanoidLayerModel);
                this.getParentModel().copyPropertiesTo(this.shadeHumanoidLayerModel);
                this.shadeHumanoidLayerModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                this.shadeHumanoidLayerModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0, 0, 0, 0.675F);
                pPoseStack.popPose();
                pPoseStack.pushPose();
                pPoseStack.translate(0f, OnActionsTrigger.getYVector(0.15f, player.getXRot()), 0.25f);
                ModelFollowingRenderer.followBodyRotations(pLivingEntity, (HumanoidModel<LivingEntity>) this.shadeHumanoidLayerModel);
                this.getParentModel().copyPropertiesTo(this.shadeHumanoidLayerModel);
                this.shadeHumanoidLayerModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                this.shadeHumanoidLayerModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0, 0, 0, 0.5F);
                pPoseStack.popPose();
            }
        }
    }

    public boolean shouldRender(Player player) {
        final boolean[] flags = {false, false};
        player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(mistsplitterDefenseCapability -> flags[0] = mistsplitterDefenseCapability.isDefenseModeOn());
        player.getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(halfZatoichiAbilityCapability -> flags[1] = halfZatoichiAbilityCapability.isInAdrenalinMode());
        return flags[0] || flags[1];
    }
}