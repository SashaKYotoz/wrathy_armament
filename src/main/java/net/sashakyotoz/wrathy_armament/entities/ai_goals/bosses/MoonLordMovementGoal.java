package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.wrathy_armament.entities.bosses.Habciak;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;
import net.sashakyotoz.wrathy_armament.utils.RenderUtils;

import java.util.EnumSet;

public class MoonLordMovementGoal extends Goal {
    private final MoonLord moonLord;

    public MoonLordMovementGoal(MoonLord moonLord) {
        this.moonLord = moonLord;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return moonLord.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return moonLord.getTarget() != null && moonLord.getTarget().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.moonLord.getTarget();
        if (target != null) {
            double distanceSqr = this.moonLord.distanceToSqr(target);
            if (!this.moonLord.isInPose(MoonLord.LordPose.DYING) && !this.moonLord.isInPose(MoonLord.LordPose.LASERING))
                this.moonLord.lookAt(target, 25, 25);
            switch (this.moonLord.getLordPose()) {
                case IDLING -> this.moonLord.getMoveControl().strafe(0,
                        RenderUtils.getOscillatingWithNegativeValue(this.moonLord.tickCount, 6));
                case DYING, TELEPORTING -> this.moonLord.getNavigation().stop();
                case LASERING -> this.moonLord.getMoveControl().strafe(distanceSqr > 400 ? 0.5f : -0.1f,
                        RenderUtils.getOscillatingWithNegativeValue(this.moonLord.tickCount, 4));
                case ATTACKING -> {
                    if (distanceSqr > 40)
                        this.moonLord.getNavigation().moveTo(target, 0.9f);
                    else
                        this.moonLord.getMoveControl().strafe(-0.35f, 0);
                }
                case SHOOTING -> {
                    if (distanceSqr > 64)
                        this.moonLord.getNavigation().moveTo(target, 1.15f);
                    else
                        this.moonLord.getMoveControl().strafe(-0.35f, 0);
                }
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}