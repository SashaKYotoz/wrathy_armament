package net.sashakyotoz.wrathy_armament.miscs;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
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
            float f = Mth.lerp(effectInstance.getFactorData().get().getFactor(entity, partialTicks), viewDistance, 20F);
            fogData.start = fogData.mode == FogRenderer.FogMode.FOG_SKY ? 0.25F : f * 0.95F;
            fogData.end = f;
        }
    }

    @Override
    public float getModifiedVoidDarkness(LivingEntity entity, MobEffectInstance effectInstance, float darknessFactor, float partialTicks) {
        return effectInstance.getFactorData().isEmpty() ? 0.0F : 1.0F - effectInstance.getFactorData().get().getFactor(entity, partialTicks);
    }
}