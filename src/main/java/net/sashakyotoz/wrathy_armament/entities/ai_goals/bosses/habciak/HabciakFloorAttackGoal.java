package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.habciak;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.wrathy_armament.entities.bosses.Habciak;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class HabciakFloorAttackGoal extends Goal {
    private final Habciak habciak;
    private int attackTime = 0;

    public HabciakFloorAttackGoal(Habciak habciak) {
        this.habciak = habciak;
    }

    @Override
    public void start() {
        if (this.habciak.level().isClientSide())
            this.habciak.floorAttack.start(this.habciak.tickCount);
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
                this.habciak.setDeltaMovement(
                        OnActionsTrigger.getXVector(2.5, habciak.getYRot()),
                        0.5,
                        OnActionsTrigger.getZVector(2.5, habciak.getYRot()));
                target.setDeltaMovement(
                        OnActionsTrigger.getXVector(-1, target.getYRot()),
                        0.35,
                        OnActionsTrigger.getZVector(-1, target.getYRot()));
                if (target instanceof Player player)
                    this.habciak.level().playSound(player,this.habciak.getOnPos(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.HOSTILE);
                this.habciak.reassessAttackGoal(this.habciak.getRandom().nextInt(4));
            } else if (attackTime < 100) {
                attackTime++;
                float sin = (float) Math.sin(attackTime * Math.PI / 10);
                float cos = (float) Math.cos(attackTime * Math.PI / 10);
                float tan = (float) Math.tan(attackTime * Math.PI / 10);
                if (this.habciak.level().isClientSide) {
                    this.habciak.level().addParticle(ParticleTypes.END_ROD,
                            this.habciak.getX() + sin, this.habciak.getY() + 0.25 + tan,
                            this.habciak.getZ() + cos, 0, 0, 0);
                }
                if (this.habciak.distanceToSqr(target) > 9)
                    this.habciak.getNavigation().moveTo(target, 0.5);
                else
                    this.habciak.getMoveControl().strafe(-2, 0);
            }
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
