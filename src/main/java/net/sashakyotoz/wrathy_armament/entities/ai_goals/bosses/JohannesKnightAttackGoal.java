package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.sashakyotoz.wrathy_armament.entities.bosses.JohannesKnight;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.EnumSet;

public class JohannesKnightAttackGoal extends Goal {
    private final JohannesKnight knight;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;

    public JohannesKnightAttackGoal(JohannesKnight knight, double speedModifier, boolean followIfInvisible) {
        this.knight = knight;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followIfInvisible;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        long i = this.knight.level().getGameTime();
        if (i - this.lastCanUseCheck < 20) {
            return false;
        } else if (this.knight.getKnightPose() == JohannesKnight.KnightPose.DYING) {
            return false;
        } else if (!this.knight.onGround()) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.knight.getTarget();
            if (livingentity == null || !livingentity.isAlive()) {
                return false;
            } else {
                this.path = this.knight.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.knight.isWithinMeleeAttackRange(livingentity);
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.knight.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (this.knight.getKnightPose() == JohannesKnight.KnightPose.DYING)
            return false;
        else if (!this.followingTargetEvenIfNotSeen) {
            return this.knight.getNavigation().isInProgress();
        } else if (!this.knight.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    public void start() {
        this.knight.getNavigation().moveTo(this.path, this.speedModifier);
        this.knight.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.knight.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity))
            this.knight.setTarget(null);
        this.knight.setAggressive(false);
        this.knight.setAttacking(false);
        this.knight.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.knight.getTarget();
        if (livingentity != null) {
            double d0 = this.knight.distanceToSqr(livingentity);
            this.knight.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.followingTargetEvenIfNotSeen || this.knight.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 0.5D || this.knight.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.knight.getRandom().nextInt(7);
                if (d0 > 1024.0D)
                    this.ticksUntilNextPathRecalculation += 10;
                else if (d0 > 256.0D)
                    this.ticksUntilNextPathRecalculation += 5;
                if (!this.knight.getNavigation().moveTo(livingentity, this.speedModifier))
                    this.ticksUntilNextPathRecalculation += 15;
                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity);
        }
    }

    private void checkAndPerformAttack(LivingEntity entity) {
        if (this.canPerformAttack(entity)) {
            if (this.knight.getKnightPose() == JohannesKnight.KnightPose.IDLING)
                this.knight.setRandomPose();
            switch (this.knight.getKnightPose()) {
                case ATTACKING -> {
                    this.knight.setAttacking(true);
                    OnActionsTrigger.queueServerWork(20, () -> {
                        if (this.knight.distanceToSqr(entity) < 16)
                            this.knight.doHurtTarget(entity);
                        this.knight.setAttacking(false);
                        this.knight.setRandomPose();
                    });
                }
                case SHOOTING -> {
                    if (this.knight.isInSecondPhase())
                        this.knight.johannesSwordAbility();
                    else
                        this.knight.johannesSwordBackwardsDash();
                }
                case JUMPING -> this.knight.johannesSwordDash();
                case DASHING -> this.knight.dashing();
            }
            this.resetAttackCooldown();
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    private boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }
    public boolean isWithinExtendedMeleeAttackRange(LivingEntity entity) {
        double d0 = this.knight.getPerceivedTargetDistanceSquareForMeleeAttack(entity);
        return d0 <= this.knight.getMeleeAttackRangeSqr(entity) * 1.25f;
    }
    private boolean canPerformAttack(LivingEntity entity) {
        return this.isTimeToAttack() && this.isWithinExtendedMeleeAttackRange(entity) && this.knight.getSensing().hasLineOfSight(entity);
    }
}