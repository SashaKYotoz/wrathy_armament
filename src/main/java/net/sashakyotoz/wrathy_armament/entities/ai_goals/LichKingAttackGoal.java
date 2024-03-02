package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class LichKingAttackGoal extends Goal {
    private final LichKing lichKing;
    private final LivingEntity target;
    public LichKingAttackGoal(LichKing lichKing, LivingEntity target) {
        this.lichKing = lichKing;
        this.target = target;
    }
    @Override
    public boolean canUse() {
        return this.target != null && this.target.isAlive() && !this.lichKing.isInHealingState();
    }

    @Override
    public void start() {
        if (this.lichKing.getAIPhase().equals("combo_attack"))
            this.lichKing.getNavigation().moveTo(this.target,1);
        this.lichKing.setAggressive(true);
        super.start();
    }
    @Override
    public void stop(){
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.target)) {
            this.lichKing.setTarget(null);
        }
        this.lichKing.setAggressive(false);
        super.stop();
    }
    public boolean requiresUpdateEveryTick() {
        return true;
    }
    @Override
    public void tick() {
        if (this.lichKing.getAIPhase().equals("combo_attack")){
            if (canPerformAttack(this.target)){
                comboAttacks();
            }
        }
        super.tick();
    }
    private void comboAttacks(){
        this.lichKing.setAttacking(true);
        for (int i = 0; i < 3; i++) {
            this.lichKing.setIdOfCombo();
            OnActionsTrigger.queueServerWork(20*i,()->{
                if (this.lichKing.distanceToSqr(this.target) <= 8)
                    this.lichKing.doHurtTarget(this.target);
            });
        }

    }
    protected void resetAttackCooldown() {
        int tmp = this.lichKing.getRandom().nextIntBetweenInclusive(30,80);
        this.adjustedTickDelay(tmp);
    }

    private boolean canPerformAttack(LivingEntity entity) {
        return this.lichKing.isWithinMeleeAttackRange(entity) && this.lichKing.getSensing().hasLineOfSight(entity);
    }
}
