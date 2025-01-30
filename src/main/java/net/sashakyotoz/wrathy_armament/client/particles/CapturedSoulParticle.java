package net.sashakyotoz.wrathy_armament.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.client.particles.options.CapturedSoulParticleOption;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Consumer;

public class CapturedSoulParticle extends TextureSheetParticle {
    private final PositionSource target;
    private float rot;
    private float rotO;
    private float pitch;
    private float pitchO;

    public CapturedSoulParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, PositionSource pTarget, int pLifetime) {
        super(level, x, y, z, vx, vy, vz);
        this.quadSize = 0.75f;
        this.gravity = 0;
        this.target = pTarget;
        this.lifetime = pLifetime;
        Optional<Vec3> optional = pTarget.getPosition(level);
        if (optional.isPresent()) {
            Vec3 vec3 = optional.get();
            double d0 = x - vec3.x();
            double d1 = y - vec3.y();
            double d2 = z - vec3.z();
            this.rotO = this.rot = (float) Mth.atan2(d0, d2);
            this.pitchO = this.pitch = (float) Mth.atan2(d1, Math.sqrt(d0 * d0 + d2 * d2));
        }
    }

    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        float f = Mth.sin(((float) this.age + pPartialTicks - ((float) Math.PI * 2F)) * 0.05F) * 2.0F;
        float f1 = Mth.lerp(pPartialTicks, this.rotO, this.rot);
        float f2 = Mth.lerp(pPartialTicks, this.pitchO, this.pitch) + ((float) Math.PI / 2F);
        this.renderSignal(pBuffer, pRenderInfo, pPartialTicks, (quaternionf) -> quaternionf.rotateY(f1).rotateX(-f2).rotateY(f));
        this.renderSignal(pBuffer, pRenderInfo, pPartialTicks, (quaternionf) -> quaternionf.rotateY(-(float) Math.PI + f1).rotateX(f2).rotateY(f));
    }

    private void renderSignal(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks, Consumer<Quaternionf> pQuaternionConsumer) {
        Vec3 vec3 = pRenderInfo.getPosition();
        float f = (float) (Mth.lerp(pPartialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(pPartialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(pPartialTicks, this.zo, this.z) - vec3.z());
        Vector3f vector3f = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0.0F, vector3f.x(), vector3f.y(), vector3f.z());
        pQuaternionConsumer.accept(quaternionf);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(pPartialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f1 = avector3f[i];
            vector3f1.rotate(quaternionf);
            vector3f1.mul(f3);
            vector3f1.add(f, f1, f2);
        }

        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(pPartialTicks);
        pBuffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        pBuffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    public int getLightColor(float pPartialTick) {
        return 240;
    }

    public static @NotNull CapturedSoulParticle.CapturedSoulParticleProvider provider(SpriteSet spriteSet) {
        return new CapturedSoulParticleProvider(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class CapturedSoulParticleProvider implements ParticleProvider<CapturedSoulParticleOption> {
        private final SpriteSet spriteSet;

        public CapturedSoulParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(CapturedSoulParticleOption typeIn, @NotNull ClientLevel pLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CapturedSoulParticle particle = new CapturedSoulParticle(pLevel, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.source(), typeIn.arrivalInTicks());
            particle.pickSprite(spriteSet);
            particle.setColor(typeIn.red(), typeIn.green(), typeIn.blue());
            return particle;
        }
    }
}