package net.sashakyotoz.wrathy_armament.registers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.blocks.renderers.WorldshardWorkbenchEntityRenderer;
import net.sashakyotoz.wrathy_armament.client.models.mobs.*;
import net.sashakyotoz.wrathy_armament.client.models.technical.*;
import net.sashakyotoz.wrathy_armament.client.particles.FireSphereParticle;
import net.sashakyotoz.wrathy_armament.client.particles.FrostSoulParticle;
import net.sashakyotoz.wrathy_armament.client.particles.PhantomRayParticle;
import net.sashakyotoz.wrathy_armament.client.particles.ZenithWayParticle;
import net.sashakyotoz.wrathy_armament.client.renderer.*;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.JohannesKnightRenderer;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.LichKingRenderer;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.MoonLordRenderer;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.SashaKYotozRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WrathyArmamentClientEvents {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WrathyArmamentEntities.ZENITH.get(), ZenithRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.BLADE_OF_CHAOS.get(), BladeOfChaosRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.JOHANNES_SPEAR.get(), JohannesSpearRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.SASHAKYOTOZ.get(), SashaKYotozRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.LICH_KING.get(), LichKingRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.LICH_MYRMIDON.get(), LichMyrmidonRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.JOHANNES_KNIGHT.get(), JohannesKnightRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.MOON_LORD.get(), MoonLordRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), ParticleLikeEntityRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(), HarmfulProjectileRenderer::new);
        event.registerBlockEntityRenderer(WrathyArmamentBlockEntities.WORLDSHARD_WORKBENCH.get(), WorldshardWorkbenchEntityRenderer::new);
    }
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LancerBackModel.LAYER_LOCATION, LancerBackModel::createBodyLayer);
        event.registerLayerDefinition(CopperSwordModel.LAYER_LOCATION, CopperSwordModel::createBodyLayer);
        event.registerLayerDefinition(ZenithModel.LAYER_LOCATION, ZenithModel::createBodyLayer);
        event.registerLayerDefinition(TerrabladeModel.LAYER_LOCATION, TerrabladeModel::createBodyLayer);
        event.registerLayerDefinition(MeowmereModel.LAYER_LOCATION, MeowmereModel::createBodyLayer);
        event.registerLayerDefinition(BladeOfChaosModel.LAYER_LOCATION, BladeOfChaosModel::createBodyLayer);
        event.registerLayerDefinition(JohannesSpearsModel.LAYER_LOCATION, JohannesSpearsModel::createBodyLayer);
        event.registerLayerDefinition(JohannesFountainModel.LAYER_LOCATION, JohannesFountainModel::createBodyLayer);
        event.registerLayerDefinition(JohannesKnightModel.LAYER_LOCATION, JohannesKnightModel::createBodyLayer);
        event.registerLayerDefinition(SashaKYotozModel.LAYER_LOCATION, SashaKYotozModel::createBodyLayer);
        event.registerLayerDefinition(LichKingModel.LAYER_LOCATION, LichKingModel::createBodyLayer);
        event.registerLayerDefinition(LichMyrmidonModel.LAYER_LOCATION, LichMyrmidonModel::createBodyLayer);
        event.registerLayerDefinition(MoonLordModel.LAYER_LOCATION, MoonLordModel::createBodyLayer);
        event.registerLayerDefinition(ParticleLikeEntityModel.LAYER_LOCATION, ParticleLikeEntityModel::createBodyLayer);
        event.registerLayerDefinition(AxeProjectileModel.LAYER_LOCATION, AxeProjectileModel::createBodyLayer);
        event.registerLayerDefinition(DaggerProjectileModel.LAYER_LOCATION, DaggerProjectileModel::createBodyLayer);
        event.registerLayerDefinition(HugeSwordModel.LAYER_LOCATION, HugeSwordModel::createBodyLayer);
        event.registerLayerDefinition(CircleFlameModel.LAYER_LOCATION, CircleFlameModel::createBodyLayer);
        event.registerLayerDefinition(ShieldDashModel.LAYER_LOCATION, ShieldDashModel::createBodyLayer);
        event.registerLayerDefinition(SphereLikeEntityModel.LAYER_LOCATION, SphereLikeEntityModel::createBodyLayer);
        event.registerLayerDefinition(TransparentHumanoidLayerModel.LAYER_LOCATION,TransparentHumanoidLayerModel::createBodyLayer);
        event.registerLayerDefinition(ForgeModel.LAYER_LOCATION,ForgeModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void onParticleSetup(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(WrathyArmamentMiscRegistries.PHANTOM_RAY.get(), PhantomRayParticle::provider);
        event.registerSpriteSet(WrathyArmamentMiscRegistries.ZENITH_WAY.get(), ZenithWayParticle::provider);
        event.registerSpriteSet(WrathyArmamentMiscRegistries.FIRE_SPHERE.get(), FireSphereParticle::provider);
        event.registerSpriteSet(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), FrostSoulParticle::provider);
    }
}
