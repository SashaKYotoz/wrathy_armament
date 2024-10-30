package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.SashaKYotozModel;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.SashaKYotozArmorLayer;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;
import org.jetbrains.annotations.NotNull;

public class SashaKYotozRenderer extends FixedDeathAnimationMobRenderer<SashaKYotoz, SashaKYotozModel<SashaKYotoz>> {
    public SashaKYotozRenderer(EntityRendererProvider.Context context) {
        super(context,new SashaKYotozModel<>(context.bakeLayer(SashaKYotozModel.LAYER_LOCATION)),0.5f);
        this.addLayer(new SashaKYotozArmorLayer(this, context.getModelSet()));
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public @NotNull RenderType renderType() {
                return RenderType.eyes(WrathyArmament.createWALocation("textures/entity/bosses/glowing_eyes/sashakyotoz_glowing_eyes.png"));
            }
        });
    }

    @Override
    public void render(SashaKYotoz pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        if (pEntity.isInPhase(SashaKYotoz.SashaKYotozPhase.FLYING))
            pPoseStack.mulPose(Axis.ZP.rotation(pEntity.takeOffRotDegrees));
    }

    @Override
    public ResourceLocation getTextureLocation(SashaKYotoz sashaKYotoz) {
        return WrathyArmament.createWALocation("textures/entity/bosses/sashakyotoz.png");
    }
}