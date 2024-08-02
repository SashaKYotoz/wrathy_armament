package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;

public class MoonLordShooting extends Goal {
    private final MoonLord lord;
    private int attackTime = 0;

    public MoonLordShooting(MoonLord lord) {
        this.lord = lord;
    }
    @Override
    public boolean canUse() {
        return this.lord.getTarget() != null && this.lord.getTarget().isAlive() && this.lord.getDataLordPose().equals(MoonLord.LordPose.SHOOTING);
    }

    @Override
    public void start() {
        this.lord.interactive.stop();
    }
    public void tick() {
        LivingEntity target = this.lord.getTarget();
        if (target != null && target.distanceToSqr(this.lord) < 256.0D && this.lord.hasLineOfSight(target)) {
            if (attackTime == 60) {
                attackTime = 0;
                this.lord.eyeAttack.start(this.lord.tickCount);
                TrueEyeOfCthulhu eyeOfCthulhu = new TrueEyeOfCthulhu(WrathyArmamentEntities.TRUE_EYE_OF_CTHULHU.get(),this.lord.level());
                eyeOfCthulhu.setOwner(this.lord);
                eyeOfCthulhu.moveTo(this.lord.getOnPos().getCenter());
                this.lord.level().addFreshEntity(eyeOfCthulhu);
                this.lord.setRandomLordPose();
            } else if (attackTime < 60) {
                attackTime++;
            }
            this.lord.getNavigation().stop();
        } else {
            if (target != null)
                this.lord.getNavigation().moveTo(target, 0.5);
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
