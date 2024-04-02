package net.sashakyotoz.wrathy_armament.client.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.LichMyrmidonModel;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.FixedDeathAnimationMobRenderer;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;

public class LichMyrmidonRenderer extends FixedDeathAnimationMobRenderer<LichMyrmidon, LichMyrmidonModel<LichMyrmidon>> {
    public LichMyrmidonRenderer(EntityRendererProvider.Context context) {
        super(context, new LichMyrmidonModel<>(context.bakeLayer(LichMyrmidonModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation(WrathyArmament.MODID,"textures/entity/lich_myrmidon_glowing_eyes.png"));
            }
        });
    }
    @Override
    public ResourceLocation getTextureLocation(LichMyrmidon p_114482_) {
        return new ResourceLocation(WrathyArmament.MODID,"textures/entity/lich_myrmidon.png");
    }
}
