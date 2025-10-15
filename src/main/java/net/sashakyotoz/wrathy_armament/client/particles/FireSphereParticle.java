package net.sashakyotoz.wrathy_armament.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.SphereLikeEntityModel;
import net.sashakyotoz.wrathy_armament.client.particles.options.FireSphereParticleOption;
import org.jetbrains.annotations.NotNull;

public class FireSphereParticle extends TextureSheetParticle {
    private final float maxScale;
    private final boolean isReversed;
    private final boolean toScale;

    public FireSphereParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz,float maxScale,boolean isReversed,boolean toScale) {
        super(level, x, y, z, vx, vy, vz);
        this.quadSize = 1;
        this.hasPhysics = false;
        this.lifetime = 60;
        this.isReversed = isReversed;
        this.toScale = toScale;
        this.gravity = 0;
        this.maxScale = maxScale;
        this.setParticleSpeed(0,0,0);
        this.setAlpha(0.5f);
        new SphereRenderSequence(this).start();
    }

    public static @NotNull FireTrailParticleProvider provider(SpriteSet spriteSet) {
        return new FireTrailParticleProvider(spriteSet);
    }
    private static class SphereRenderSequence {
        private final FireSphereParticle particle;

        private class CircleRenderer {
            public final EntityModel<Entity> model = new SphereLikeEntityModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(SphereLikeEntityModel.LAYER_LOCATION));

            public CircleRenderer() {
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void render(RenderLevelStageEvent event) {
                if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                    VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(WrathyArmament.createWALocation("textures/entity/particle_like/spheres/fire_circle.png")));
                    Vec3 camPos = event.getCamera().getPosition();
                    double x = Mth.lerp(event.getPartialTick(), particle.xo, particle.x) - camPos.x();
                    double y = Mth.lerp(event.getPartialTick(), particle.yo, particle.y) - camPos.y()-3;
                    double z = Mth.lerp(event.getPartialTick(), particle.zo, particle.z) - camPos.z();
                    event.getPoseStack().pushPose();
                    event.getPoseStack().translate(x, y, z);
                    event.getPoseStack().mulPose(Axis.YP.rotationDegrees(particle.age*5 % 360));
                    event.getPoseStack().scale(particle.quadSize,particle.quadSize,particle.quadSize);
                    model.renderToBuffer(event.getPoseStack(), consumer, particle.getLightColor(event.getPartialTick()), OverlayTexture.NO_OVERLAY, particle.rCol, particle.gCol, particle.bCol, particle.alpha);
                    event.getPoseStack().popPose();
                }
            }
        }

        private final CircleRenderer renderer;

        public SphereRenderSequence(FireSphereParticle particle) {
            this.particle = particle;
            this.renderer = new CircleRenderer();
        }

        public void start() {
            MinecraftForge.EVENT_BUS.register(renderer);
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void tick(TickEvent.ClientTickEvent event) {
            if (!particle.isAlive())
                end();
        }

        private void end() {
            MinecraftForge.EVENT_BUS.unregister(renderer);
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.toScale){
            if (this.isReversed)
                this.quadSize+=this.quadSize > maxScale/10f ? -0.05f:0;
            else
                this.quadSize+=this.quadSize < maxScale ? 0.05f:0;
        }else
            this.quadSize = this.maxScale;
        this.rCol +=this.rCol > 0.025f ? -0.025f : 0;
        this.gCol +=this.gCol > 0.025f ? -0.025f : 0;
        this.bCol +=this.bCol > 0.025f ? -0.025f : 0;
        this.alpha +=this.alpha > 0.05f ? -0.025f : 0;
    }

    public static class FireTrailParticleProvider implements ParticleProvider<FireSphereParticleOption> {
        private final SpriteSet spriteSet;

        public FireTrailParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(FireSphereParticleOption typeIn, @NotNull ClientLevel pLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FireSphereParticle particle = new FireSphereParticle(pLevel, x, y, z,xSpeed,ySpeed,zSpeed,typeIn.maxScale(),typeIn.isReversed(),typeIn.toScale());
            particle.setColor(typeIn.redColor(),typeIn.greenColor(), typeIn.blueColor());
            return particle;
        }
    }
}