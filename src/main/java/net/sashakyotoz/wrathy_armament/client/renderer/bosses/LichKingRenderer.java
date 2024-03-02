package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.LichKingModel;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;

public class LichKingRenderer extends FixedDeathAnimationMobRenderer<LichKing, LichKingModel<LichKing>>{

    public LichKingRenderer(EntityRendererProvider.Context context) {
        super(context, new LichKingModel<>(context.bakeLayer(LichKingModel.LAYER_LOCATION)), 1.0f);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation(WrathyArmament.MODID,"textures/entity/bosses/lich_king_glowing_eyes.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(LichKing king) {
        return new ResourceLocation(WrathyArmament.MODID,"textures/entity/bosses/lich_king.png");
    }
}
