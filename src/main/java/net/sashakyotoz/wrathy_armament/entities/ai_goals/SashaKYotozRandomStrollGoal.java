package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SashaKYotozRandomStrollGoal extends Goal {
    protected final SashaKYotoz sashaKYotoz;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;
    protected final double speedModifier;
    protected int interval;
    protected boolean forceTrigger;
    private final boolean checkNoActionTime;

    public SashaKYotozRandomStrollGoal(SashaKYotoz sashaKYotoz, double speedModifier, int interval) {
        this(sashaKYotoz, speedModifier, interval, true);
    }

    public SashaKYotozRandomStrollGoal(SashaKYotoz sashaKYotoz, double speedModifier, int interval, boolean checkNoActionTime) {
        this.sashaKYotoz = sashaKYotoz;
        this.speedModifier = speedModifier;
        this.interval = interval;
        this.checkNoActionTime = checkNoActionTime;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.sashaKYotoz.isVehicle() || this.sashaKYotoz.getTarget() != null) {
            return false;
        } else {
            if (!this.forceTrigger) {
                if (this.checkNoActionTime && this.sashaKYotoz.getNoActionTime() >= 120) {
                    return false;
                }
                if (this.sashaKYotoz.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
                    return false;
                }
            }
            Vec3 vec3 = this.getPosition();
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        return DefaultRandomPos.getPos(this.sashaKYotoz, 10, 8);
    }

    public boolean canContinueToUse() {
        return !this.sashaKYotoz.getNavigation().isDone() && !this.sashaKYotoz.isVehicle();
    }

    public void start() {
        this.sashaKYotoz.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    public void stop() {
        this.sashaKYotoz.getNavigation().stop();
        super.stop();
    }
}