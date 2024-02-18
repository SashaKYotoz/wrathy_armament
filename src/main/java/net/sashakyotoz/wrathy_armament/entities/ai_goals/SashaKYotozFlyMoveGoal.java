package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

import java.util.EnumSet;

public class SashaKYotozFlyMoveGoal extends Goal {
    private final SashaKYotoz sashaKYotoz;
    public SashaKYotozFlyMoveGoal(SashaKYotoz sashaKYotoz) {
        this.sashaKYotoz = sashaKYotoz;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return sashaKYotoz.getTarget() != null && !sashaKYotoz.getMoveControl().hasWanted() && sashaKYotoz.getFlyPhase() == 1;
    }

    @Override
    public boolean canContinueToUse() {
        return sashaKYotoz.getMoveControl().hasWanted() && sashaKYotoz.getTarget() != null && sashaKYotoz.getTarget().isAlive() && sashaKYotoz.getFlyPhase() == 1;
    }

    @Override
    public void start() {
        LivingEntity livingentity = sashaKYotoz.getTarget();
        Vec3 vec3d = livingentity.getEyePosition(1);
        sashaKYotoz.getMoveControl().setWantedPosition(vec3d.x, vec3d.y + 0.5, vec3d.z, 2.5);
    }

    @Override
    public void tick() {
        LivingEntity livingentity = sashaKYotoz.getTarget();
        if (livingentity != null && sashaKYotoz.getBoundingBox().intersects(livingentity.getBoundingBox())) {
            sashaKYotoz.doHurtTarget(livingentity);
            stop();
        } else if (livingentity != null) {
            double d0 = sashaKYotoz.distanceToSqr(livingentity);
            if (d0 < 32) {
                Vec3 vec3d = livingentity.getEyePosition(1);
                sashaKYotoz.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 2);
            }
        }
    }
}