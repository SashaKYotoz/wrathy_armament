package net.sashakyotoz.wrathy_armament.entities.alive;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.CthulhuRangedAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.MobCopyOwnerTargetGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.TrueEyeMeleeGoal;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;
import net.sashakyotoz.wrathy_armament.entities.technical.EyeOfCthulhuProjectile;
import net.sashakyotoz.wrathy_armament.entities.technical.OwnerableMob;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;

public class TrueEyeOfCthulhu extends OwnerableMob implements RangedAttackMob {
    private final CthulhuRangedAttackGoal rangedAttackGoal = new CthulhuRangedAttackGoal(this);
    private final TrueEyeMeleeGoal meleeGoal = new TrueEyeMeleeGoal(this);
    private static final EntityDataAccessor<Boolean> IS_IN_ANGRY_MODE = SynchedEntityData.defineId(TrueEyeOfCthulhu.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState interactive = new AnimationState();

    public TrueEyeOfCthulhu(EntityType<? extends OwnerableMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = XP_REWARD_MEDIUM;
        this.navigation = new FlyingPathNavigation(this,this.level());
        this.moveControl = new FlyingMoveControl(this,30,true);
    }
    @Override
    public int getMaxHeadXRot() {
        return 70;
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2,new MobCopyOwnerTargetGoal(this));
        this.goalSelector.addGoal(3,new WaterAvoidingRandomFlyingGoal(this,1.5f));
        this.goalSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));
        super.registerGoals();
    }
    @Override
    public void tick() {
        super.tick();
        this.interactive.animateWhen(!this.isDeadOrDying(),this.tickCount);
        if (this.tickCount % 5 == 0){
            if (this.isInAngryMode()){
                this.goalSelector.removeGoal(rangedAttackGoal);
                this.goalSelector.addGoal(5,meleeGoal);
            }else{
                this.goalSelector.removeGoal(meleeGoal);
                this.goalSelector.addGoal(5,rangedAttackGoal);
            }
        }
        setIsInAngryMode(this.getHealth() <= this.getMaxHealth()/2);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_IN_ANGRY_MODE,false);
        super.defineSynchedData();
    }
    public boolean isInAngryMode(){
        return this.entityData.get(IS_IN_ANGRY_MODE);
    }
    public void setIsInAngryMode(boolean b){
        this.entityData.set(IS_IN_ANGRY_MODE,b);
    }
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ARMOR, 5)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.3);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return pEntity instanceof TrueEyeOfCthulhu || pEntity instanceof MoonLord;
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        EyeOfCthulhuProjectile projectile = new EyeOfCthulhuProjectile(WrathyArmamentEntities.EYE_OF_CTHULHU_PROJECTILE.get(),this,this.level());
        projectile.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 3.0F, 0.75F);
        this.level().addFreshEntity(projectile);
    }
}