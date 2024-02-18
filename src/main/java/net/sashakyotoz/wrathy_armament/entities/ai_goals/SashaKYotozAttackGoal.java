package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.EnumSet;

public class SashaKYotozAttackGoal extends Goal {
    protected final SashaKYotoz mob;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;

    public SashaKYotozAttackGoal(SashaKYotoz sashaKYotoz, double speedModifier, boolean followIfInvisible) {
        this.mob = sashaKYotoz;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followIfInvisible;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        long i = this.mob.level().getGameTime();
        if (i - this.lastCanUseCheck < 15 || this.mob.isPhantomRayAttack() || this.mob.isPhantomCycleAttack()) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (canPenalize) {
                    if (--this.ticksUntilNextPathRecalculation <= 0) {
                        this.path = this.mob.getNavigation().createPath(livingentity, 0);
                        this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.mob.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.mob.isWithinMeleeAttackRange(livingentity);
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.getNavigation().isDone();
        } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative() || !this.mob.getMeleeAttackType().equals("cycleAttack");
        }
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    public void stop() {
        LivingEntity livingentity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.mob.setTarget(null);
        }
        this.mob.setAggressive(false);
        this.mob.setAttacking(false);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && !this.mob.getMeleeAttackType().equals("cycleAttack")) {
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 0.5D || this.mob.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingentity.getX();
                this.pathedTargetY = livingentity.getY();
                this.pathedTargetZ = livingentity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                double d0 = this.mob.distanceToSqr(livingentity);
                if (this.canPenalize) {
                    this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                    if (this.mob.getNavigation().getPath() != null) {
                        net.minecraft.world.level.pathfinder.Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }
                if (d0 > 1024.0D) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256.0D) {
                    this.ticksUntilNextPathRecalculation += 5;
                }
                if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity);
        }
    }

    protected void checkAndPerformAttack(LivingEntity entity) {
        if (this.canPerformAttack(entity)) {
            this.mob.setAttacking(true);
            setRandomMeleeAttackType();
            int tmpDuration = 12;
            this.resetAttackCooldown();
            if (this.mob.getMeleeAttackType().equals("blade"))
                tmpDuration = 15;
            OnActionsTrigger.queueServerWork(tmpDuration, () -> {
                this.mob.doHurtTarget(entity);
                this.mob.setAttacking(false);
            });
        }
    }
    private void setRandomMeleeAttackType(){
        RandomSource source = RandomSource.create();
        switch (this.mob.getMeleeAttackType()){
            case "scythe" -> {
                if (this.mob.getCycleAttackCooldown() <= 0 && source.nextBoolean()){
                    this.mob.setMeleeAttackType("cycleAttack");
                    this.mob.setCycleAttackCooldown(this.mob.getRandom().nextIntBetweenInclusive(200,400));
                }
                else
                    this.mob.setMeleeAttackType("blade");
            }
            case "blade" ->{
                if (this.mob.getCycleAttackCooldown() <= 0 && source.nextBoolean()){
                    this.mob.setMeleeAttackType("cycleAttack");
                    this.mob.setCycleAttackCooldown(this.mob.getRandom().nextIntBetweenInclusive(200,400));
                }
                else
                    this.mob.setMeleeAttackType("scythe");
            }
            case "cycleAttack" -> this.mob.setMeleeAttackType(source.nextBoolean() ? "scythe" : "blade");
        }
    }
    protected void resetAttackCooldown() {
        int tmp = this.mob.getRandom().nextIntBetweenInclusive(30,80);
        this.ticksUntilNextAttack = this.adjustedTickDelay(tmp);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean canPerformAttack(LivingEntity entity) {
        return this.isTimeToAttack() && this.mob.isWithinMeleeAttackRange(entity) && this.mob.getSensing().hasLineOfSight(entity);
    }
}