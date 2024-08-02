package net.sashakyotoz.wrathy_armament.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.client.models.mobs.MoonLordModel;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

public class MoonLordGlowingLayer<T extends MoonLord, M extends MoonLordModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation texture;

    public MoonLordGlowingLayer(RenderLayerParent<T, M> pRenderer, ResourceLocation location) {
        super(pRenderer);
        this.texture = location;
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, MoonLord pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.eyes(texture));
        this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,pLivingEntity.isEyesActive() ? 1.0F : 0.25F);
    }
}