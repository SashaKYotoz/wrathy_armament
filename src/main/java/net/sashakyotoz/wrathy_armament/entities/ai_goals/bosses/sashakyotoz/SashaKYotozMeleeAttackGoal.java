package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.sashakyotoz;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentSounds;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.EnumSet;

public class SashaKYotozMeleeAttackGoal extends Goal {
    private final SashaKYotoz sashaKYotoz;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;

    public SashaKYotozMeleeAttackGoal(SashaKYotoz sashaKYotoz) {
        this.sashaKYotoz = sashaKYotoz;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        long i = this.sashaKYotoz.level().getGameTime();
        if (i - this.lastCanUseCheck < 20L || this.sashaKYotoz.isInPhase(SashaKYotoz.SashaKYotozPhase.RANGED_ATTACKING)) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.sashaKYotoz.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                this.path = this.sashaKYotoz.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.sashaKYotoz.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.sashaKYotoz.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.sashaKYotoz.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    public void start() {
        this.sashaKYotoz.getNavigation().moveTo(this.path, 1.5f);
        this.sashaKYotoz.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.sashaKYotoz.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity))
            this.sashaKYotoz.setTarget(null);

        this.sashaKYotoz.setAggressive(false);
        this.sashaKYotoz.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.sashaKYotoz.getTarget();
        if (livingentity != null) {
            this.sashaKYotoz.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.sashaKYotoz.getPerceivedTargetDistanceSquareForMeleeAttack(livingentity);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.sashaKYotoz.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.sashaKYotoz.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.sashaKYotoz.getRandom().nextInt(7);
                if (d0 > 1024.0)
                    this.ticksUntilNextPathRecalculation += 10;
                else if (d0 > 256.0)
                    this.ticksUntilNextPathRecalculation += 5;

                if (!this.sashaKYotoz.getNavigation().moveTo(livingentity, 1.5f))
                    this.ticksUntilNextPathRecalculation += 15;
                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity, d0);
        }

    }

    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            OnActionsTrigger.queueServerWork(10, () -> {
                this.sashaKYotoz.doHurtTarget(pEnemy);
                this.sashaKYotoz.setRandomPhase();
            });
        }

    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return this.sashaKYotoz.getBbWidth() * 2.5F * this.sashaKYotoz.getBbWidth() * 2.5F + pAttackTarget.getBbWidth();
    }
}