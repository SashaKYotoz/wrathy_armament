package net.sashakyotoz.wrathy_armament.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ZenithWayParticle extends WaveParticle {

    public ZenithWayParticle(ClientLevel world, double x, double y, double z, List<Float> movementVector, List<Float> colorList, SpriteSet spriteset) {
        super(world, x, y, z, movementVector, colorList, spriteset);
        this.xRot = 12.5f;
    }

    public static @NotNull ZenithWayParticleProvider provider(SpriteSet spriteSet) {
        return new ZenithWayParticleProvider(spriteSet);
    }
    public static class ZenithWayParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;
        public ZenithWayParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            float randomColor = RandomSource.create().nextFloat();
            return new ZenithWayParticle(worldIn, x, y, z,List.of((float)xSpeed,(float)ySpeed,(float)zSpeed),List.of(randomColor,1f,randomColor), this.spriteSet);
        }
    }
}
