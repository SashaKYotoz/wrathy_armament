package net.sashakyotoz.wrathy_armament.client.renderer.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.SashaKYotozModel;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

public class SashaKYotozArmorLayer extends EnergySwirlLayer<SashaKYotoz, SashaKYotozModel<SashaKYotoz>> {
    private static final ResourceLocation SASHAKYOTOZ_ARMOR_LAYER = WrathyArmament.createWALocation("textures/entity/bosses/sashakyotoz_layer.png");
    private final SashaKYotozModel<SashaKYotoz> model;

    public SashaKYotozArmorLayer(RenderLayerParent<SashaKYotoz, SashaKYotozModel<SashaKYotoz>> layerParent, EntityModelSet modelSet) {
        super(layerParent);
        this.model = new SashaKYotozModel<>(modelSet.bakeLayer(SashaKYotozModel.LAYER_LOCATION));
    }

    protected float xOffset(float offset) {
        return Mth.cos(offset * 0.02F) * 3.0F;
    }

    protected ResourceLocation getTextureLocation() {
        return SASHAKYOTOZ_ARMOR_LAYER;
    }

    protected EntityModel<SashaKYotoz> model() {
        return this.model;
    }
}
