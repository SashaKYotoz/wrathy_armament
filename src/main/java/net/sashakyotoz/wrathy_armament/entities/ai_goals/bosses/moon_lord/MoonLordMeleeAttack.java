package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

import java.util.EnumSet;

public class MoonLordMeleeAttack extends Goal {
    private final MoonLord moonLord;
    private int tickTimer;
    public MoonLordMeleeAttack(MoonLord lord) {
        this.moonLord = lord;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }
    public boolean canUse() {
        return moonLord.getTarget() != null && !moonLord.getMoveControl().hasWanted() && moonLord.getDataLordPose().equals(MoonLord.LordPose.ATTACKING);
    }
    @Override
    public boolean canContinueToUse() {
        return moonLord.getMoveControl().hasWanted() && moonLord.getTarget() != null && moonLord.getTarget().isAlive() && moonLord.getDataLordPose().equals(MoonLord.LordPose.ATTACKING);
    }

    @Override
    public void start() {
        LivingEntity livingentity = moonLord.getTarget();
        Vec3 vec3d = livingentity.getEyePosition(1);
        moonLord.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 3);
    }
    @Override
    public void tick() {
        LivingEntity livingentity = moonLord.getTarget();
        tickTimer++;
        if (moonLord.getBoundingBox().intersects(livingentity.getBoundingBox())) {
            moonLord.doHurtTarget(livingentity);
            tickTimer-=10;
        } else {
            double d0 = moonLord.distanceToSqr(livingentity);
            if (d0 < 32) {
                Vec3 vec3d = livingentity.getEyePosition(1);
                moonLord.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 3);
            }
        }
        if (tickTimer > 120)
            this.moonLord.setRandomLordPose();
    }
}