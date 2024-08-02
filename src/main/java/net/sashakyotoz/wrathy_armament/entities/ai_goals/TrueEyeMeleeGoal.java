package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;

import java.util.EnumSet;

public class TrueEyeMeleeGoal extends Goal {
    private final TrueEyeOfCthulhu eyeOfCthulhu;

    public TrueEyeMeleeGoal(TrueEyeOfCthulhu eyeOfCthulhu) {
        this.eyeOfCthulhu = eyeOfCthulhu;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return eyeOfCthulhu.getTarget() != null && !eyeOfCthulhu.getMoveControl().hasWanted();
    }

    @Override
    public boolean canContinueToUse() {
        return eyeOfCthulhu.getMoveControl().hasWanted() && eyeOfCthulhu.getTarget() != null && eyeOfCthulhu.getTarget().isAlive();
    }

    @Override
    public void start() {
        LivingEntity livingentity = eyeOfCthulhu.getTarget();
        Vec3 vec3d = livingentity.getEyePosition(1);
        eyeOfCthulhu.getMoveControl().setWantedPosition(vec3d.x, vec3d.y + 0.5, vec3d.z, 3);
    }

    @Override
    public void tick() {
        LivingEntity livingentity = eyeOfCthulhu.getTarget();
        if (livingentity != null && eyeOfCthulhu.getBoundingBox().intersects(livingentity.getBoundingBox())) {
            eyeOfCthulhu.doHurtTarget(livingentity);
        } else if (livingentity != null) {
            double d0 = eyeOfCthulhu.distanceToSqr(livingentity);
            if (d0 < 24) {
                Vec3 vec3d = livingentity.getEyePosition(1);
                eyeOfCthulhu.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 3);
            }
        }
    }
}