package net.sashakyotoz.wrathy_armament.registers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.client.models.mobs.*;
import net.sashakyotoz.wrathy_armament.client.models.technical.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class WrathyArmamentModels {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LancerBackModel.LAYER_LOCATION, LancerBackModel::createBodyLayer);
        event.registerLayerDefinition(CopperSwordModel.LAYER_LOCATION, CopperSwordModel::createBodyLayer);
        event.registerLayerDefinition(ZenithModel.LAYER_LOCATION, ZenithModel::createBodyLayer);
        event.registerLayerDefinition(TerrabladeModel.LAYER_LOCATION, TerrabladeModel::createBodyLayer);
        event.registerLayerDefinition(MeowmereModel.LAYER_LOCATION, MeowmereModel::createBodyLayer);
        event.registerLayerDefinition(JohannesSpearsModel.LAYER_LOCATION, JohannesSpearsModel::createBodyLayer);
        event.registerLayerDefinition(JohannesFountainModel.LAYER_LOCATION, JohannesFountainModel::createBodyLayer);
        event.registerLayerDefinition(JohannesKnightModel.LAYER_LOCATION, JohannesKnightModel::createBodyLayer);
        event.registerLayerDefinition(SashaKYotozModel.LAYER_LOCATION, SashaKYotozModel::createBodyLayer);
        event.registerLayerDefinition(LichKingModel.LAYER_LOCATION, LichKingModel::createBodyLayer);
        event.registerLayerDefinition(LichMyrmidonModel.LAYER_LOCATION, LichMyrmidonModel::createBodyLayer);
        event.registerLayerDefinition(ParticleLikeEntityModel.LAYER_LOCATION, ParticleLikeEntityModel::createBodyLayer);
        event.registerLayerDefinition(AxeProjectileModel.LAYER_LOCATION, AxeProjectileModel::createBodyLayer);
        event.registerLayerDefinition(DaggerProjectileModel.LAYER_LOCATION, DaggerProjectileModel::createBodyLayer);
        event.registerLayerDefinition(HugeSwordModel.LAYER_LOCATION, HugeSwordModel::createBodyLayer);
    }
}
