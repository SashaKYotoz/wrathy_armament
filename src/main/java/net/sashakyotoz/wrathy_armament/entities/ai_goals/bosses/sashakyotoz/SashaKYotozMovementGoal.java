package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.sashakyotoz;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

import java.util.EnumSet;

public class SashaKYotozMovementGoal extends Goal {
    private final SashaKYotoz sashaKYotoz;

    public SashaKYotozMovementGoal(SashaKYotoz sashaKYotoz) {
        this.sashaKYotoz = sashaKYotoz;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return sashaKYotoz.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return sashaKYotoz.getTarget() != null && sashaKYotoz.getTarget().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.sashaKYotoz.getTarget();
        if (target != null) {
            double distanceSqr = this.sashaKYotoz.distanceToSqr(target);
            switch (this.sashaKYotoz.getSashaKYotozPhase()) {
                case FLYING -> {
                    if (this.sashaKYotoz.getBoundingBox().inflate(5).intersects(target.getBoundingBox()))
                        this.sashaKYotoz.setSashaKYotozPhase(SashaKYotoz.SashaKYotozPhase.LANDING);
                    if (distanceSqr > 4 && !this.sashaKYotoz.getMoveControl().hasWanted())
                        this.sashaKYotoz.getMoveControl().setWantedPosition(target.getX(), target.getY() + 1, target.getZ(), 1.25f);
                }
                case RANGED_ATTACKING -> {
                    if (distanceSqr > 24)
                        this.sashaKYotoz.getNavigation().moveTo(target, 1.25f);
                    if (distanceSqr < 9)
                        this.sashaKYotoz.getMoveControl().strafe(-1, 0);
                }
                case MELEE_SCYTHE_ATTACKING -> {
                    if (distanceSqr > 5)
                        this.sashaKYotoz.getNavigation().moveTo(target, 1.5f);
                }
                case MELEE_ATTACKING -> {
                    if (distanceSqr > 2)
                        this.sashaKYotoz.getNavigation().moveTo(target, 1.25f);
                }
            }
            if (!this.sashaKYotoz.isInPhase(SashaKYotoz.SashaKYotozPhase.RANGED_ATTACKING) && !this.sashaKYotoz.isInPhase(SashaKYotoz.SashaKYotozPhase.FLYING) && distanceSqr > 24 && this.sashaKYotoz.tickCount % 100 == 0) {
                this.sashaKYotoz.setSashaKYotozPhase(SashaKYotoz.SashaKYotozPhase.TAKING_OFF);
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}