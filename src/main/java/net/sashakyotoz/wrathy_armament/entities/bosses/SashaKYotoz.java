package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.SashaKYotozAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.SashaKYotozFlyMoveGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.SashaKYotozRandomStrollGoal;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentParticleTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class SashaKYotoz extends BossLikePathfinderMob implements PowerableMob {
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PHANTOM_RAY_ATTACK = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> MELEE_ATTACK_TYPE = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FLY_PHASE = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CYCLE_ATTACK_COOLDOWN = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.INT);
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.sashakyotoz"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
    private int timerToRayAttack = 0;
    private int timerToSetFly = 0;
    private int idleAnimationTimeout = 0;
    public int attackAnimationTimeout = 0;
    private final FlyingPathNavigation flyingPathNavigation;
    private final GroundPathNavigation groundNavigation;
    public final AnimationState idle = new AnimationState();
    public final AnimationState takeOff = new AnimationState();
    public final AnimationState landing = new AnimationState();
    public final AnimationState attackByScythe = new AnimationState();
    public final AnimationState attackByBlade = new AnimationState();
    public final AnimationState attackPhantomRay = new AnimationState();
    public final AnimationState attackCycleOfPhantoms = new AnimationState();
    public final AnimationState death = new AnimationState();

    public SashaKYotoz(EntityType<? extends PathfinderMob> type, Level level) {
        super(WrathyArmamentEntities.SASHAKYOTOZ.get(), level);
        this.xpReward = XP_REWARD_BOSS;
        this.setMaxUpStep(1.25f);
        this.groundNavigation = (GroundPathNavigation) this.createNavigation(this.level());
        this.flyingPathNavigation = new FlyingPathNavigation(this, level);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!isMoving()) {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idle.start(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
                this.idle.stop();
            }
        }
        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 60;
            switch (this.getMeleeAttackType()) {
                case "scythe" -> this.attackByScythe.start(this.tickCount);
                case "blade" -> this.attackByBlade.start(this.tickCount);
                case "cycleAttack" -> this.attackCycleOfPhantoms.start(this.tickCount);
            }
        } else
            attackAnimationTimeout--;
        if (!this.isAttacking()) {
            this.attackByScythe.stop();
            this.attackByBlade.stop();
            this.attackCycleOfPhantoms.stop();
        }
        if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) >= 12 && !this.isPhantomCycleAttack()) {
            if (this.timerToRayAttack > 0)
                this.timerToRayAttack--;
        }
        if (this.isPhantomRayAttack()) {
            this.navigation.stop();
            if ((!this.attackPhantomRay.isStarted()))
                this.attackPhantomRay.start(this.tickCount);
            OnActionsTrigger.queueServerWork(30, () -> {
                rayAttack();
                this.timerToRayAttack += this.getRandom().nextIntBetweenInclusive(160, 320);
            });
        }
        if (this.isPhantomCycleAttack()) {
            this.navigation.stop();
            if (!this.attackCycleOfPhantoms.isStarted())
                this.attackCycleOfPhantoms.start(this.tickCount);
            for (int i = 0; i < 2; i++) {
                int finalI = i;
                OnActionsTrigger.queueServerWork(30 * i, () -> {
                    spawnParticle(WrathyArmamentParticleTypes.PHANTOM_RAY.get(), this.level(), this.getX(), this.getY(), this.getZ(), 1 + finalI);
                    hitNearbyMobs(Component.translatable("death.attack.wrathy_armament.phantom_shock_message"), 12, 4 * finalI);
                    if (finalI == 1)
                        this.setMeleeAttackType("blade");
                });
            }
        }
    }

    private boolean playWalkAnimationCondition() {
        return !(this.isAttacking() && this.isPhantomRayAttack() && this.isPhantomCycleAttack() && idle.isStarted());
    }

    @Override
    protected void updateWalkAnimation(float updateTick) {
        float f;
        f = playWalkAnimationCondition() ? Math.min(updateTick * 6.0F, 1.0F) : 0;
        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.navigation = this.getFlyPhase() == 1 ? flyingPathNavigation : groundNavigation;
        this.setNoGravity(this.getFlyPhase() == 1);
        if (this.getCycleAttackCooldown() > 0)
            this.setCycleAttackCooldown(this.getCycleAttackCooldown() - 1);
        walkOrFlyAnimation();
        setPhantomRayAttack();
    }

    // ray attack
    private void rayAttack() {
        WrathyArmament.LOGGER.debug("sweep Attack");
        this.playSound(SoundEvents.PHANTOM_SWOOP);
        double d0 = -getZVector(1, this.getYRot());
        double d1 = getXVector(1, this.getYRot());
        float scaling = 0;
        for (int i1 = 0; i1 < 24; i1++) {
            if (!this.level().getBlockState(new BlockPos(
                            this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getX(),
                            this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getY(),
                            this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getZ()))
                    .canOcclude())
                scaling = scaling + 1;
            this.level().addParticle(WrathyArmamentParticleTypes.PHANTOM_RAY.get(),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getX()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getY()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getZ()), d0,
                    0.1, d1);
            final Vec3 center = new Vec3(
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getX()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getY()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getZ()));
            List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
            for (Entity entityIterator : entityList) {
                if (!(entityIterator == this)) {
                    if (entityIterator instanceof LivingEntity livingEntity) {
                        float damage = livingEntity.getMaxHealth() - livingEntity.getHealth() + 2;
                        if (damage > 100)
                            damage = damage / 4;
                        livingEntity.hurt(new DamageSource(livingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                            @Override
                            public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                                return Component.translatable("death.attack.wrathy_armament.phantom_shock_message");
                            }
                        }, damage);
                        break;
                    }
                }
            }
        }
    }

    //animations
    private void walkOrFlyAnimation() {
        if (this.getTarget() != null && this.getTarget().getY() > this.getY() + 3) {
            this.timerToSetFly++;
            if (timerToSetFly > 30) {
                if (!this.takeOff.isStarted())
                    this.takeOff.start(this.tickCount);
                OnActionsTrigger.queueServerWork(10, () -> {
                    this.setDeltaMovement(0, 0.5, 0);
                    this.setFlyPhase(1);
                    timerToSetFly = 0;
                });
            }
        }
        if (this.getFlyPhase() == 1 && this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 4) {
            timerToSetFly--;
            if (timerToSetFly < -30) {
                if (!this.landing.isStarted())
                    this.landing.start(this.tickCount);
                OnActionsTrigger.queueServerWork(10, () -> {
                    this.setFlyPhase(0);
                    timerToSetFly = 0;
                });
            }
        }
    }

    //logic
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SashaKYotozFlyMoveGoal(this));
        this.goalSelector.addGoal(1, new SashaKYotozRandomStrollGoal(this, 0.8, 160));
        this.targetSelector.addGoal(2, new SashaKYotozAttackGoal(this, 1.5, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false, true));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ATTACKING, false);
        this.entityData.define(PHANTOM_RAY_ATTACK, false);
        this.entityData.define(MELEE_ATTACK_TYPE, "scythe");
        this.entityData.define(FLY_PHASE, 0);
        this.entityData.define(CYCLE_ATTACK_COOLDOWN, 0);
        super.defineSynchedData();
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource source) {
        return this.getFlyPhase() != 1 && super.causeFallDamage(p_147187_, p_147188_, source);
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    public int getFlyPhase() {
        return this.entityData.get(FLY_PHASE);
    }

    public int getCycleAttackCooldown() {
        return this.entityData.get(CYCLE_ATTACK_COOLDOWN);
    }

    public void setCycleAttackCooldown(int tmp) {
        this.entityData.set(CYCLE_ATTACK_COOLDOWN, tmp);
    }

    private void setFlyPhase(int tmp) {
        this.entityData.set(FLY_PHASE, tmp);
    }

    public void setAttacking(boolean tmp) {
        this.entityData.set(ATTACKING, tmp);
    }

    public void setPhantomRayAttack() {
        this.entityData.set(PHANTOM_RAY_ATTACK, timerToRayAttack <= 0);
    }

    public void setMeleeAttackType(String tmp) {
        this.entityData.set(MELEE_ATTACK_TYPE, tmp);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public boolean isPhantomCycleAttack() {
        return this.getMeleeAttackType().equals("cycleAttack");
    }

    public boolean isPhantomRayAttack() {
        return this.entityData.get(PHANTOM_RAY_ATTACK);
    }

    public String getMeleeAttackType() {
        return this.entityData.get(MELEE_ATTACK_TYPE);
    }
    //hurt

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !isPowered() && super.hurt(source, amount);
    }

    //death ticks
    @Override
    protected void tickDeath() {
        if (!this.death.isStarted())
            this.death.start(this.tickCount);
        if (deathTime == 19) {
            this.spawnAtLocation(new ItemStack(WrathyArmamentItems.PHANTOM_LANCER.get()));
            this.setDeltaMovement(0, 0.25, 0);
            this.convertTo(EntityType.PHANTOM, false);
        }
        super.tickDeath();
    }

    @Override
    public void die(DamageSource source) {
        this.deathTime = -10;
        super.die(source);
    }

    //attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public ServerBossEvent bossInfo() {
        return bossEvent;
    }

    @Override
    public boolean isPowered() {
        return isPhantomCycleAttack();
    }
}