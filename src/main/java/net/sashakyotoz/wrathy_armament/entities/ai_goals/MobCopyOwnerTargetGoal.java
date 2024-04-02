package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.sashakyotoz.wrathy_armament.entities.technical.OwnerableMob;

public class MobCopyOwnerTargetGoal extends TargetGoal {
    private final OwnerableMob mob;
    private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    public MobCopyOwnerTargetGoal(OwnerableMob mob) {
        super(mob, false);
        this.mob = mob;
    }
    public boolean canUse() {
        return this.mob.getOwner() != null && this.mob.getOwner().getLastHurtByMob() != null && this.canAttack(this.mob.getOwner().getLastHurtByMob(), this.copyOwnerTargeting);
    }

    public void start() {
        this.mob.setTarget(this.mob.getOwner().getLastHurtByMob());
        super.start();
    }
}