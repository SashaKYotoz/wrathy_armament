package net.sashakyotoz.wrathy_armament.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PhantomRayParticle extends TextureSheetParticle  {
    private final SpriteSet sprites;
    public PhantomRayParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.lifetime = 20 + this.random.nextInt(20);
        this.quadSize = 0.75f;
        this.hasPhysics = true;
        this.gravity = -0.1f;
        this.sprites = spriteSet;
        this.xd = vx;
        this.zd = vz;
        this.setSpriteFromAge(spriteSet);
    }
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        this.move(this.xd, this.yd, this.zd);
        fadeOut();
        if (this.age++ >= this.lifetime && this.alpha < 0.1) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }
    private void fadeOut(){
        this.alpha = (-(1/(float)this.lifetime) * age + 1);
        if(quadSize > 0)
            this.quadSize = 0.75f -(float) age/40;
    }
    public static Provider provider(SpriteSet spriteSet) {
        return new Provider(spriteSet);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PhantomRayParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}