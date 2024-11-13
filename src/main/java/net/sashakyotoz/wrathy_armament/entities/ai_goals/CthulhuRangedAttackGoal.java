package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;

import java.util.EnumSet;

public class CthulhuRangedAttackGoal extends Goal {
    private final TrueEyeOfCthulhu eyeOfCthulhu;
    private int attackTime = 0;

    public CthulhuRangedAttackGoal(TrueEyeOfCthulhu pEyeOfCthulhu) {
        this.eyeOfCthulhu = pEyeOfCthulhu;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return this.eyeOfCthulhu.getTarget() != null && !this.eyeOfCthulhu.isInAngryMode();
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.eyeOfCthulhu.getNavigation().isDone()) && !this.eyeOfCthulhu.isInAngryMode();
    }

    public void start() {
        super.start();
        this.eyeOfCthulhu.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.eyeOfCthulhu.setAggressive(false);
        this.attackTime = 0;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity target = this.eyeOfCthulhu.getTarget();
        if (target != null && target.distanceToSqr(this.eyeOfCthulhu) > 16 && this.eyeOfCthulhu.hasLineOfSight(target)) {
            if (attackTime == 50) {
                attackTime = 0;
                this.eyeOfCthulhu.performRangedAttack(target,1);
            } else if (attackTime < 50) {
                attackTime++;
                this.eyeOfCthulhu.getNavigation().moveTo(target, 0.75);
                this.eyeOfCthulhu.lookAt(target,10,5);
            }
            this.eyeOfCthulhu.getNavigation().stop();
        } else {
            if (target != null)
                this.eyeOfCthulhu.getMoveControl().strafe(-1.5f,0);
        }
    }
}