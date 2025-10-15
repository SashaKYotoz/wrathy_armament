package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
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
        MoveControl movecontrol = this.moonLord.getMoveControl();
        if (!movecontrol.hasWanted()) {
            return true;
        } else {
            double d0 = movecontrol.getWantedX() - this.moonLord.getX();
            double d1 = movecontrol.getWantedY() - this.moonLord.getY();
            double d2 = movecontrol.getWantedZ() - this.moonLord.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 1.0 || d3 > 3600.0;
        }
    }
    private void resetWantedPos(){
        RandomSource randomsource = this.moonLord.getRandom();
        double d0 = this.moonLord.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double d1 = this.moonLord.getY() + (double) ((randomsource.nextFloat() * 2.25F - 1.25F) * 8.0F);
        double d2 = this.moonLord.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        this.moonLord.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
    }

    @Override
    public boolean canContinueToUse() {
        return moonLord.getTarget() != null && moonLord.getTarget().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.moonLord.getTarget();
        float oscillatingValue = RenderUtils.getOscillatingWithNegativeValue(this.moonLord.tickCount, 4);
        if (target != null) {
            double distanceSqr = this.moonLord.distanceToSqr(target);
            if (!this.moonLord.isInPose(MoonLord.LordPose.DYING) && !this.moonLord.isInPose(MoonLord.LordPose.LASERING))
                this.moonLord.lookAt(target, 25, 25);
            switch (this.moonLord.getLordPose()) {
                case IDLING -> this.moonLord.getMoveControl().strafe(0, oscillatingValue);
                case DYING, TELEPORTING -> this.moonLord.getNavigation().stop();
                case LASERING -> this.moonLord.getMoveControl().strafe(distanceSqr > 324 ? 0.5f : -0.2f, oscillatingValue);
                case SHOOTING,EYE_BLADES,ATTACKING -> resetWantedPos();
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}