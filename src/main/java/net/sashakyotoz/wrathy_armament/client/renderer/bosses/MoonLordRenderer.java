package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.MoonLordModel;
import net.sashakyotoz.wrathy_armament.client.renderer.layers.MoonLordEmissiveLayer;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

public class MoonLordRenderer extends FixedDeathAnimationMobRenderer<MoonLord, MoonLordModel<MoonLord>>{
    private final ResourceLocation TEXTURE = WrathyArmament.createWALocation("textures/entity/bosses/moon_lord.png");
    private static final ResourceLocation HEART_TEXTURE = WrathyArmament.createWALocation("textures/entity/bosses/moon_lord/heart.png");
    public MoonLordRenderer(EntityRendererProvider.Context context) {
        super(context, new MoonLordModel<>(context.bakeLayer(MoonLordModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new MoonLordEmissiveLayer<>(this, HEART_TEXTURE, (t, v, v1) -> Math.max(0.0F, Mth.cos(v1 * 0.05F) * 0.25F), MoonLordModel::getHeartLayerModelParts));
    }

    @Override
    public void scale(MoonLord pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        pPoseStack.scale(2.5f,2.5f,2.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(MoonLord pEntity) {
        return TEXTURE;
    }
}