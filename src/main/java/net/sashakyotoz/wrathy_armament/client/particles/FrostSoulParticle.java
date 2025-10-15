package net.sashakyotoz.wrathy_armament.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.sashakyotoz.anitexlib.client.particles.parents.rendertypes.FluidParticleRenderType;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FrostSoulParticle extends TextureSheetParticle {

    public FrostSoulParticle(ClientLevel level, double x, double y, double z, List<Double> movementVector) {
        super(level, x, y, z);
        this.quadSize = Mth.randomBetween(level.getRandom(), 0.3f, 0.8f) + 0.1f;
        this.hasPhysics = true;
        this.gravity = 0.225F;
        this.friction = 1.0F;
        this.xd = movementVector.get(0) + (Math.random() * 2.0D - 1.0D) * (double) 0.05F;
        this.yd = movementVector.get(1) + (Math.random() * 2.0D - 1.0D) * (double) 0.05F;
        this.zd = movementVector.get(2) + (Math.random() * 2.0D - 1.0D) * (double) 0.05F;
        this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
        this.lifetime = (int) (24.0D / (this.random.nextFloat() * 0.8f + 0.2f)) + 2;
    }

    public static @NotNull FrostSoulParticleProvider provider(SpriteSet spriteSet) {
        return new FrostSoulParticleProvider(spriteSet);
    }

    public void tick() {
        super.tick();
        this.xd *= 0.95F;
        this.yd *= 0.9F;
        this.zd *= 0.95F;
        float progress = (float) this.age / this.lifetime;
        this.alpha = Mth.lerp(progress, 1F, 0);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return OnActionsTrigger.isOculusIn() ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT : FluidParticleRenderType.INSTANCE;
    }

    public static class FrostSoulParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public FrostSoulParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FrostSoulParticle particle = new FrostSoulParticle(worldIn, x, y, z, List.of(xSpeed, ySpeed, zSpeed));
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}