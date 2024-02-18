package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.WitherArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.SashaKYotozModel;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.SashaKYotozArmorLayer;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

public class SashaKYotozRenderer extends FixedDeathAnimationMobRenderer<SashaKYotoz, SashaKYotozModel<SashaKYotoz>> {
    public SashaKYotozRenderer(EntityRendererProvider.Context context) {
        super(context,new SashaKYotozModel<>(context.bakeLayer(SashaKYotozModel.LAYER_LOCATION)),0.5f);
        this.addLayer(new SashaKYotozArmorLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(SashaKYotoz sashaKYotoz) {
        return new ResourceLocation(WrathyArmament.MODID,"textures/entity/bosses/sashakyotoz.png");
    }
}
