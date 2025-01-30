package net.sashakyotoz.wrathy_armament.client.renderer.layers.moonlord;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.wrathy_armament.client.models.mobs.MoonLordModel;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;
import net.sashakyotoz.wrathy_armament.entities.bosses.core.MoonLordPart;

public class MoonLordEyesLayer extends RenderLayer<MoonLord, MoonLordModel<MoonLord>> {
    private final ResourceLocation texture;
    private final MoonLordModel<MoonLord> lordModel;

    public MoonLordEyesLayer(RenderLayerParent<MoonLord, MoonLordModel<MoonLord>> pRenderer, ResourceLocation texture) {
        super(pRenderer);
        this.texture = texture;
        this.lordModel = pRenderer.getModel();
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, MoonLord moonLord, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (moonLord.getMainParts().get(0).getHealthPoints() > 0 ||
                moonLord.getMainParts().get(2).getHealthPoints() > 0 ||
                moonLord.getMainParts().get(3).getHealthPoints() > 0) {
            pPoseStack.pushPose();
            final float[] redModifier = new float[1];
            for (MoonLordPart moonLordPart : moonLord.getMainParts()) {
                if (moonLord.damageTakenByPart.get(moonLordPart.name) != null && moonLord.damageTakenByPart.get(moonLordPart.name) > 0) {
                    redModifier[0] = 1;
                    break;
                } else
                    redModifier[0] = 0;
            }
            float alphaModifier = getAlphaModifier(moonLord);
            this.getParentModel().copyPropertiesTo(this.lordModel);
            this.lordModel.setupAnim(moonLord, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(texture));
            this.lordModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F - redModifier[0], 1.0F - redModifier[0], alphaModifier);
            pPoseStack.popPose();
        }
    }

    private float getAlphaModifier(MoonLord moonLord) {
        float alphaModifier = 1f;
        if (this.texture.getPath().contains("hand")) {
            if (moonLord.getMainParts().get(2).getHealthPoints() <= 0 ||
                    moonLord.getMainParts().get(3).getHealthPoints() <= 0)
                alphaModifier = 0.35f;
            else if (moonLord.getMainParts().get(2).getHealthPoints() <= 0 &&
                    moonLord.getMainParts().get(3).getHealthPoints() <= 0)
                alphaModifier = 0.25f;
            else if (moonLord.getLordPose().equals(MoonLord.LordPose.ATTACKING))
                alphaModifier = TextureAnimator.simpleAlphaFunction(0.5f, moonLord.tickCount);
        }
        return alphaModifier;
    }
}