package net.sashakyotoz.wrathy_armament.utils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.wrathy_armament.client.renderer.JohannesSpearRenderer;
import net.sashakyotoz.wrathy_armament.client.renderer.ZenithRenderer;
import net.sashakyotoz.wrathy_armament.client.renderer.bosses.SashaKYotozRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WrathyArmamentRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WrathyArmamentEntities.ZENITH.get(), ZenithRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.JOHANNES_SPEAR.get(), JohannesSpearRenderer::new);
        event.registerEntityRenderer(WrathyArmamentEntities.SASHAKYOTOZ.get(), SashaKYotozRenderer::new);
    }
}
