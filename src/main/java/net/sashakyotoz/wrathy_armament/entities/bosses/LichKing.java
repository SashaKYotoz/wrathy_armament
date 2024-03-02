package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.LichKingAttackGoal;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class LichKing extends BossLikePathfinderMob{
    public static final EntityDataAccessor<Boolean> IS_IN_HEALING_STATE = SynchedEntityData.defineId(LichKing.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> AI_PHASE = SynchedEntityData.defineId(LichKing.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> ID_OF_COMBO = SynchedEntityData.defineId(LichKing.class, EntityDataSerializers.INT);
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.lich_king"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
    public final AnimationState spawn = new AnimationState();
    public final AnimationState combo_attack = new AnimationState();
    public final AnimationState combo_attack1 = new AnimationState();
    public final AnimationState combo_attack2 = new AnimationState();
    public final AnimationState rain_cast = new AnimationState();
    public final AnimationState summon = new AnimationState();
    public final AnimationState transition_to_heal = new AnimationState();
    public int attackAnimationTimeout = 0;
    public LichKing(EntityType<? extends BossLikePathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    public void onAddedToWorld() {
        this.spawn.start(this.tickCount);
        super.onAddedToWorld();
    }
    //attack things
    @Override
    public void onLeaveCombat() {
        if (this.getHealth() < this.getMaxHealth()){
            this.transition_to_heal.start(this.tickCount);
            OnActionsTrigger.queueServerWork(30,()-> {
                this.setIsInHealingState(true);
                this.setAiPhase("idling");
            });
        }
        super.onLeaveCombat();
    }
    @Override
    public void onEnterCombat() {
        if (this.getHealth() == this.getMaxHealth())
            this.setIsInHealingState(false);
        if (getAIPhase().equals("idling"))
            setAiPhase();
        super.onEnterCombat();
    }
    public void setAiPhase(){
        boolean toMeleeAttack = this.random.nextBoolean();
        float chanceToSetInHeal = this.getHealth() < this.getMaxHealth()/2 ? 0.5f : 0.25f;
        if (toMeleeAttack)
            setAiPhase("combo_attack");
        else {
            if (this.random.nextFloat() < chanceToSetInHeal)
                this.setIsInHealingState(true);
            else {
                if (this.random.nextBoolean())
                    setAiPhase("summon");
                else
                    setAiPhase("rain_cast");
            }
        }
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInHealingState() && this.random.nextFloat() > 0.75f)
            this.setIsInHealingState(false);
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        if (this.isInHealingState() && this.tickCount % 20 == 0)
            this.heal(5);
        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 30;
            switch (this.getIdOfCombo()) {
                default -> this.combo_attack.start(this.tickCount);
                case 1 -> this.combo_attack1.start(this.tickCount);
                case 2 -> this.combo_attack2.start(this.tickCount);
            }
        } else
            attackAnimationTimeout--;
        if (!this.isAttacking() || this.getIdOfCombo() > 3) {
            this.combo_attack.stop();
            this.combo_attack1.stop();
            this.combo_attack2.stop();
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LichKingAttackGoal(this,this.getTarget()));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false, true));
        super.registerGoals();
    }
    //logic
    private void setIsInHealingState(boolean b){
        this.entityData.set(IS_IN_HEALING_STATE,b);
    }
    public boolean isInHealingState(){
        return this.entityData.get(IS_IN_HEALING_STATE);
    }
    private void setAiPhase(String s){
        this.entityData.set(AI_PHASE,s);
    }
    public String getAIPhase(){
        return this.entityData.get(AI_PHASE);
    }
    public void setIdOfCombo(){
        if (this.getIdOfCombo() < 3)
            this.entityData.set(ID_OF_COMBO,this.getIdOfCombo() + 1);
        else
            this.entityData.set(ID_OF_COMBO,0);
    }
    private int getIdOfCombo(){
        return this.entityData.get(ID_OF_COMBO);
    }
    @Override
    protected void defineSynchedData() {
        entityData.define(IS_IN_HEALING_STATE,false);
        entityData.define(AI_PHASE,"idling");
        super.defineSynchedData();
    }

    @Override
    public ServerBossEvent bossInfo() {
        return bossEvent;
    }
    //attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 600.0D)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2);
    }
}
