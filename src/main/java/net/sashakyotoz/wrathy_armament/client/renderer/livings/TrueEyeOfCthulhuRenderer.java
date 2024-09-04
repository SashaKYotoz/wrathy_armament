package net.sashakyotoz.wrathy_armament.client.renderer.livings;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.TrueEyeOfCthulhuModel;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.FixedDeathAnimationMobRenderer;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;

public class TrueEyeOfCthulhuRenderer extends FixedDeathAnimationMobRenderer<TrueEyeOfCthulhu, TrueEyeOfCthulhuModel<TrueEyeOfCthulhu>> {
    public TrueEyeOfCthulhuRenderer(EntityRendererProvider.Context context) {
        super(context, new TrueEyeOfCthulhuModel<>(context.bakeLayer(TrueEyeOfCthulhuModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public void render(TrueEyeOfCthulhu pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.translate(0,-0.25f,0);
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TrueEyeOfCthulhu pEntity) {
        return pEntity.isInAngryMode() ? WrathyArmament.createWALocation("textures/entity/evil_true_eye_of_cthulhu.png") : WrathyArmament.createWALocation("textures/entity/true_eye_of_cthulhu.png");
    }
}