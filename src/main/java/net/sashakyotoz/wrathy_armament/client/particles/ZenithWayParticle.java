package net.sashakyotoz.wrathy_armament.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ZenithWayParticle extends TextureSheetParticle {
    private final float startQuadSize;
    public final List<Triple<Float, Float, Float>> colors =
            List.of(
                    Triple.of(1f, 0f, 0f),
                    Triple.of(1f, 0.5f, 0f),
                    Triple.of(0f, 1f, 0f),
                    Triple.of(0.25f, 0.25f, 1f),
                    Triple.of(1f, 0.5f, 1f)
            );

    public ZenithWayParticle(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
        this.quadSize = 0.325F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 0.75F);
        this.startQuadSize = 0.35F + (float) this.random.nextGaussian() * 0.25F;
        this.lifetime = 25;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        float progress = (float) this.age / this.lifetime;
        this.quadSize = Mth.lerp(progress, startQuadSize, 0);
        this.alpha = Mth.lerp(progress, 1F, 0);
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
            ZenithWayParticle particle = new ZenithWayParticle(worldIn, x, y, z);
            int colorSet = worldIn.getRandom().nextInt(particle.colors.size());
            particle.rCol = particle.colors.get(colorSet).getLeft();
            particle.gCol = particle.colors.get(colorSet).getMiddle();
            particle.bCol = particle.colors.get(colorSet).getRight();
            particle.pickSprite(spriteSet);
            return particle;
        }
    }
}