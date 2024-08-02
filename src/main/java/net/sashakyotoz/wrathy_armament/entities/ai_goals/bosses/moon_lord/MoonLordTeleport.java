package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

public class MoonLordTeleport extends Goal {
    private final MoonLord lord;
    private int attackTime = 0;

    public MoonLordTeleport(MoonLord lord) {
        this.lord = lord;
    }
    @Override
    public boolean canUse() {
        return this.lord.getTarget() != null && this.lord.getTarget().isAlive() && this.lord.getDataLordPose().equals(MoonLord.LordPose.TELEPORTING);
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
                if (this.lord.level() instanceof ServerLevel level)
                    this.lord.applyBrightnessAround(level,this.lord.getOnPos().getCenter(),this.lord,24);
                this.lord.setPos(new Vec3(target.getRandomX(2), target.getY(), target.getRandomZ(2)));
                this.lord.level().playSound(this.lord.getTarget(),this.lord.getOnPos(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.HOSTILE,2.0f,2.0f);
                this.lord.setRandomLordPose();
            } else if (attackTime < 60) {
                attackTime++;
            }
            this.lord.getNavigation().stop();
        } else {
            this.lord.getNavigation().moveTo(target, 0.5);
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
