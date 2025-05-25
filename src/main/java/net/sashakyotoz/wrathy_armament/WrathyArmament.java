package net.sashakyotoz.wrathy_armament;

import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.wrathy_armament.blocks.gui.WorldshardWorkbenchScreen;
import net.sashakyotoz.wrathy_armament.networking.WANetworkingManager;
import net.sashakyotoz.wrathy_armament.registers.*;
import org.slf4j.Logger;

@Mod(WrathyArmament.MODID)
public class WrathyArmament {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "wrathy_armament";

    public WrathyArmament() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.Common.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.Client.SPEC);
        WrathyArmamentItems.ITEMS.register(modEventBus);
        WrathyArmamentBlocks.BLOCKS.register(modEventBus);
        WrathyArmamentMiscRegistries.register(modEventBus);
        WrathyArmamentBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        WrathyArmamentEntities.REGISTRY.register(modEventBus);
        WrathyArmamentSounds.init();
        modEventBus.addListener(WANetworkingManager::registerPackets);
        if (FMLEnvironment.dist.isClient())
            modEventBus.addListener(this::clientLoad);
        TextureAnimator.addEntityToAnimate(WrathyArmament.class, MODID, "entity/layers/transparent_fire", "transparent_fire_overlay");
        TextureAnimator.addEntityToAnimate(WrathyArmament.class, MODID, "entity/particle_like/shield_dash", "shield_dash");
    }

    @OnlyIn(Dist.CLIENT)
    public void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(WrathyArmamentMiscRegistries.WORLDSHARD_WORKBENCH.get(), WorldshardWorkbenchScreen::new));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                createWALocation("animation"), 40,
                WrathyArmament::registerPlayerAnimation);
    }

    public static ResourceLocation createWALocation(String path) {
        return new ResourceLocation(MODID, path);
    }

    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }
}