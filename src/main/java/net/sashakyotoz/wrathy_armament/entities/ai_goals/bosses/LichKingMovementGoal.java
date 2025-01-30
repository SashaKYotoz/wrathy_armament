package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;

import java.util.EnumSet;

public class LichKingMovementGoal extends Goal {
    private final LichKing lichKing;

    public LichKingMovementGoal(LichKing lichKing) {
        this.lichKing = lichKing;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return lichKing.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return lichKing.getTarget() != null && lichKing.getTarget().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.lichKing.getTarget();
        if (target != null) {
            double distanceSqr = this.lichKing.distanceToSqr(target);
            if (this.lichKing.hasLineOfSight(target)) {
                if (!this.lichKing.isInPose(LichKing.KingPose.DYING))
                    this.lichKing.lookAt(target, 15, 15);
                switch (this.lichKing.getKingPose()) {
                    case ATTACK, SECOND_ATTACK, SPIN_ATTACK -> {
                        if (distanceSqr > 3)
                            this.lichKing.getNavigation().moveTo(target, 1.25f);
                    }
                    case RAIN_CASTING -> this.lichKing.getMoveControl().strafe(-0.35f, 0f);
                    case SUMMOING -> this.lichKing.getMoveControl().strafe(-0.4f, 0f);
                }
            } else
                this.lichKing.getNavigation().moveTo(target, 1f);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}