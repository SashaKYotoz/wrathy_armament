package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.habciak;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.bosses.Habciak;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class HabciakMirrorCastingGoal extends Goal {
    private final Habciak habciak;
    private int attackTime = 0;

    public HabciakMirrorCastingGoal(Habciak habciak) {
        this.habciak = habciak;
    }
    
    @Override
    public boolean canUse() {
        return this.habciak.getTarget() != null && !this.habciak.isDeadOrDying();
    }

    public void tick() {
        LivingEntity target = this.habciak.getTarget();
        if (target != null && this.habciak.hasLineOfSight(target)) {
            if (attackTime == 100) {
                attackTime = 0;
                this.habciak.mirrorCasting.stop();
                this.habciak.backFlip.start(this.habciak.tickCount);
                this.habciak.setDeltaMovement(
                        OnActionsTrigger.getXVector(-2.5, habciak.getYRot()),
                        0.75,
                        OnActionsTrigger.getZVector(-2.5, habciak.getYRot()));
                this.habciak.reassessAttackGoal(this.habciak.getRandom().nextInt(3));
            } else if (attackTime < 100) {
                attackTime++;
                this.habciak.mirrorCasting.startIfStopped( this.habciak.tickCount);
                if (attackTime % 2 == 0) {
                    Vec3 eyePos = this.habciak.getEyePosition();
                    Vec3 lookVec = this.habciak.getLookAngle();
                    Vec3 endPos = eyePos.add(lookVec.scale(4));
                    int particleCount = 20;
                    Vec3 step = endPos.subtract(eyePos).scale(1.0 / particleCount);
                    double radius = 0.5;
                    for (int j = 0; j < particleCount; j++) {
                        Vec3 basePos = eyePos.add(step.scale(j));
                        double angle = (attackTime) * Math.PI / 20 + (j * Math.PI / 10);
                        double offsetX = radius * Math.cos(angle);
                        double offsetY = radius * Math.sin(angle);
                        Vec3 perpendicular = lookVec.cross(new Vec3(0, 1, 0)).normalize();
                        Vec3 particlePos = basePos.add(perpendicular.scale(offsetX)).add(0, offsetY, 0);
                        this.habciak.level().addParticle(
                                ParticleTypes.CLOUD,
                                particlePos.x(),
                                particlePos.y(),
                                particlePos.z(),
                                0, 0, 0
                        );
                    }
                }
                if (attackTime % 5 == 0 && !target.hasEffect(MobEffects.BLINDNESS)){
                    target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,80,1));
                    target.setSecondsOnFire(4);
                }
                if (this.habciak.distanceToSqr(target) > 9)
                    this.habciak.getNavigation().stop();
                else
                    this.habciak.getMoveControl().strafe(-1, 0);
            }
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
