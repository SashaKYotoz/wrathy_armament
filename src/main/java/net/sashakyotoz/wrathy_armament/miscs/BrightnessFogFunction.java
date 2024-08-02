package net.sashakyotoz.wrathy_armament.miscs;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;

@OnlyIn(Dist.CLIENT)
public class BrightnessFogFunction implements FogRenderer.MobEffectFogFunction {
    @Override
    public MobEffect getMobEffect() {
        return WrathyArmamentMiscRegistries.BRIGHTNESS.get();
    }

    @Override
    public void setupFog(FogRenderer.FogData fogData, LivingEntity entity, MobEffectInstance effectInstance, float viewDistance, float partialTicks) {
        if (effectInstance.getFactorData().isPresent()) {
            fogData.start = 1;
            fogData.end = 1;
        }
    }

    @Override
    public float getModifiedVoidDarkness(LivingEntity entity, MobEffectInstance effectInstance, float darknessFactor, float partialTicks) {
        return 1;
    }
}