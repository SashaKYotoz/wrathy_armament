package net.sashakyotoz.wrathy_armament.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.sashakyotoz.anitexlib.client.particles.parents.rendertypes.FluidParticleRenderType;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BeamSparklesParticle extends TextureSheetParticle {

    public BeamSparklesParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z);
        this.quadSize = Mth.randomBetween(level.getRandom(), 0.15f, 0.5f) + 0.1f;
        this.hasPhysics = true;
        this.gravity = 0.2F;
        this.xd = vx + (Math.random() * 2.0D - 1.0D) * (double) 0.2F;
        this.yd = vy + (Math.random() * 2.0D - 1.0D) * (double) 0.2F;
        this.zd = vz + (Math.random() * 2.0D - 1.0D) * (double) 0.2F;
        this.lifetime = 30;
    }

    public static BeamSparklesParticleProvider provider(SpriteSet spriteSet) {
        return new BeamSparklesParticleProvider(spriteSet);
    }

    public void tick() {
        super.tick();
        this.xd *= 0.95F;
        this.yd *= 0.95F;
        this.zd *= 0.95F;
        float progress = (float) this.age / this.lifetime;
        this.alpha = Mth.lerp(progress, 1F, 0);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return OnActionsTrigger.isOculusIn() ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT : FluidParticleRenderType.INSTANCE;
    }

    public static class BeamSparklesParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BeamSparklesParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BeamSparklesParticle particle = new BeamSparklesParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}