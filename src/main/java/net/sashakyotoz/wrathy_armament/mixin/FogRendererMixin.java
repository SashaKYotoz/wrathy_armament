package net.sashakyotoz.wrathy_armament.mixin;

import net.minecraft.client.renderer.FogRenderer;
import net.sashakyotoz.wrathy_armament.miscs.BrightnessFogFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Shadow
    public static List<FogRenderer.MobEffectFogFunction> MOB_EFFECT_FOG;
    static {
        MOB_EFFECT_FOG.add(new BrightnessFogFunction());
    }
}