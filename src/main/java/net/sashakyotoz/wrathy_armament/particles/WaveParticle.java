package net.sashakyotoz.wrathy_armament.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.particles.shaders.ParticleShaders;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class WaveParticle extends TextureSheetParticle {
    public SpriteSet spriteset;
    public float xRot,yRot,zRot;

    public WaveParticle(ClientLevel world, double x, double y, double z, List<Float> movementVector, List<Float> colorList, SpriteSet spriteset) {
        super(world, x, y, z, movementVector.get(0), movementVector.get(1), movementVector.get(2));
        this.setColor(colorList.get(0), colorList.get(1), colorList.get(2));
        this.setPos(x, y, z);
        this.quadSize = 0.9f;
        this.gravity = 0;
        this.friction = 0f;
        this.lifetime = RandomSource.create().nextInt(10, 31) + 10;
        this.spriteset = spriteset;
        this.pickSprite(spriteset);
    }

    @Override
    protected int getLightColor(float color) {
        return 0x92B4A7;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleShaders.PARTICLE_SHEET_ADDITIVE_MULTIPLY;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float quadSize) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp(quadSize, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(quadSize, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(quadSize, this.zo, this.z) - vec3.z());
        Quaternionf quaternionf = new Quaternionf(camera.rotation());
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(quadSize);
        for (int i = 0; i < 4; ++i) {
            quaternionf.rotateX(xRot*i);
            quaternionf.rotateX(yRot*i);
            quaternionf.rotateX(zRot*i);
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternionf);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }
        float f6 = this.getU0();
        float f7 = this.getU1();
        float f4 = this.getV0();
        float f5 = this.getV1();
        int j = this.getLightColor(quadSize);
        consumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public void tick() {
        float ratio = (float) (this.getLifetime() - this.age) / this.getLifetime();
        this.setAlpha(ratio);
        this.setSize(this.bbWidth * ratio, this.bbHeight * ratio);
        this.setSpriteFromAge(this.spriteset);
        this.move(xd,yd,zd);
        super.tick();
    }
}
