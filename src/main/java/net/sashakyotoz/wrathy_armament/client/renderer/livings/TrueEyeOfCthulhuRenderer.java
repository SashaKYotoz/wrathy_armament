package net.sashakyotoz.wrathy_armament.client.renderer.livings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.TrueEyeOfCthulhuModel;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.FixedDeathAnimationMobRenderer;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;

public class TrueEyeOfCthulhuRenderer extends FixedDeathAnimationMobRenderer<TrueEyeOfCthulhu, TrueEyeOfCthulhuModel<TrueEyeOfCthulhu>> {
    public TrueEyeOfCthulhuRenderer(EntityRendererProvider.Context context) {
        super(context, new TrueEyeOfCthulhuModel<>(context.bakeLayer(TrueEyeOfCthulhuModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(TrueEyeOfCthulhu pEntity) {
        return pEntity.isInAngryMode() ? WrathyArmament.createWALocation("textures/entity/evil_true_eye_of_cthulhu.png") : WrathyArmament.createWALocation("textures/entity/true_eye_of_cthulhu.png");
    }
}