package net.sashakyotoz.wrathy_armament.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FrostSoulParticle extends WaveParticle {

    public FrostSoulParticle(ClientLevel world, double x, double y, double z, List<Float> movementVector, List<Float> colorList, SpriteSet spriteset) {
        super(world, x, y, z, movementVector, colorList, spriteset);
        this.quadSize = Mth.randomBetween(RandomSource.create(),0.35f, 0.9f) +0.1f;
        this.hasPhysics = true;
        this.gravity = 0.25f;
        this.zRot = 12.5f;
        this.xd = -movementVector.get(0) *1.25f;
        this.yd = -movementVector.get(1);
        this.zd = -movementVector.get(2) *1.25f;
    }

    public static @NotNull FrostSoulParticleProvider provider(SpriteSet spriteSet) {
        return new FrostSoulParticleProvider(spriteSet);
    }
    public static class FrostSoulParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public FrostSoulParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FrostSoulParticle(worldIn, x, y, z,List.of((float)xSpeed,(float)ySpeed,(float)zSpeed),List.of(1f,1f,1f), this.spriteSet);
        }
    }
}
