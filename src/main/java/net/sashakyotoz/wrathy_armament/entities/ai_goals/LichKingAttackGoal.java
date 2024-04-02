package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.EnumSet;

public class LichKingAttackGoal extends Goal {
    private final LichKing lich;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;

    public LichKingAttackGoal(LichKing lich, double speedModifier, boolean followIfInvisible) {
        this.lich = lich;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followIfInvisible;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        long i = this.lich.level().getGameTime();
        if (i - this.lastCanUseCheck < 20) {
            return false;
        } else if (this.lich.isInHealingState()) {
            return false;
        } else if (!this.lich.onGround()) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.lich.getTarget();
            if (livingentity == null || !livingentity.isAlive()) {
                return false;
            } else {
                this.path = this.lich.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.lich.isWithinMeleeAttackRange(livingentity);
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.lich.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        }else if (this.lich.isInHealingState())
            return false;
        else if (!this.followingTargetEvenIfNotSeen) {
            return this.lich.getNavigation().isInProgress();
        } else if (!this.lich.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    public void start() {
        this.lich.getNavigation().moveTo(this.path, this.speedModifier);
        this.lich.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.lich.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity))
            this.lich.setTarget(null);
        this.lich.setAggressive(false);
        this.lich.setAttacking(false);
        this.lich.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.lich.getTarget();
        if (livingentity != null) {
            double d0 = this.lich.distanceToSqr(livingentity);
            this.lich.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.followingTargetEvenIfNotSeen || this.lich.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 0.5D || this.lich.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.lich.getRandom().nextInt(7);
                if (d0 > 1024.0D) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256.0D) {
                    this.ticksUntilNextPathRecalculation += 5;
                }
                if (!this.lich.getNavigation().moveTo(livingentity, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity);
        }
    }

    private void checkAndPerformAttack(LivingEntity entity) {
        if (this.canPerformAttack(entity)) {
            this.lich.setAttacking(true);
            this.lich.setIdOfCombo();
            this.resetAttackCooldown();
            OnActionsTrigger.queueServerWork(20,()->{
                if (this.lich.distanceToSqr(entity) < 16)
                    this.lich.doHurtTarget(entity);
                this.lich.setAttacking(false);
            });
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    private boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    private boolean canPerformAttack(LivingEntity entity) {
        return this.isTimeToAttack() && this.lich.isWithinMeleeAttackRange(entity) && this.lich.getSensing().hasLineOfSight(entity);
    }
}