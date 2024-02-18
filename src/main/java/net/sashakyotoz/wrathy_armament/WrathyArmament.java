package net.sashakyotoz.wrathy_armament;

import com.mojang.logging.LogUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.sashakyotoz.wrathy_armament.layers.PhantomLancerOnBackLayer;
import net.sashakyotoz.wrathy_armament.particles.FireTrailParticle;
import net.sashakyotoz.wrathy_armament.particles.FrostSoulParticle;
import net.sashakyotoz.wrathy_armament.particles.PhantomRayParticle;
import net.sashakyotoz.wrathy_armament.particles.ZenithWayParticle;
import net.sashakyotoz.wrathy_armament.utils.*;
import org.slf4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(WrathyArmament.MODID)
public class WrathyArmament {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "wrathy_armament";

    public WrathyArmament() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        WrathyArmamentItems.ITEMS.register(modEventBus);
        WrathyArmamentTab.CREATIVE_MODE_TABS.register(modEventBus);
        WrathyArmamentParticleTypes.PARTICLE_TYPES.register(modEventBus);
        WrathyArmamentEnchants.ENCHANTMENTS.register(modEventBus);
        WrathyArmamentEntities.REGISTRY.register(modEventBus);
        WrathyArmamentSounds.init();
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::registerLayer);
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    @OnlyIn(Dist.CLIENT)
    private void registerLayer(EntityRenderersEvent event) {
        if (event instanceof EntityRenderersEvent.AddLayers addLayersEvent) {
            EntityModelSet entityModels = addLayersEvent.getEntityModels();
            addLayersEvent.getSkins().forEach((s) -> {
                LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = addLayersEvent.getSkin(s);
                if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                    playerRenderer.addLayer(new PhantomLancerOnBackLayer<>(playerRenderer, entityModels));
                }
            });
        }
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onParticleSetup(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(WrathyArmamentParticleTypes.PHANTOM_RAY.get(), PhantomRayParticle::provider);
            event.registerSpriteSet(WrathyArmamentParticleTypes.ZENITH_WAY.get(), ZenithWayParticle::provider);
            event.registerSpriteSet(WrathyArmamentParticleTypes.FIRE_TRAIL.get(), FireTrailParticle::provider);
            event.registerSpriteSet(WrathyArmamentParticleTypes.FROST_SOUL_RAY.get(), FrostSoulParticle::provider);
        }
    }
}
