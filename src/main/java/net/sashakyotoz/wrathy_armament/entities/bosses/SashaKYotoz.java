package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.SashaKYotozAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.SashaKYotozFlyMoveGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.SashaKYotozRandomStrollGoal;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class SashaKYotoz extends BossLikePathfinderMob implements PowerableMob {
    private static final EntityDataAccessor<Boolean> LONG_ATTACKING = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> MELEE_ATTACK_TYPE = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> LONG_ATTACK_TYPE = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FLY_PHASE = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LONG_ATTACK_COOLDOWN = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET_ABOVE_TIMER = SynchedEntityData.defineId(SashaKYotoz.class, EntityDataSerializers.INT);
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.sashakyotoz"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
    private int idleAnimationTimeout = 0;
    public final AnimationState idle = new AnimationState();
    public final AnimationState takeOff = new AnimationState();
    public final AnimationState landing = new AnimationState();
    public final AnimationState attackByScythe = new AnimationState();
    public final AnimationState attackByBlade = new AnimationState();
    public final AnimationState attackPhantomRay = new AnimationState();
    public final AnimationState attackCycleOfPhantoms = new AnimationState();
    public final AnimationState fly = new AnimationState();
    public final AnimationState walk = new AnimationState();
    public final AnimationState death = new AnimationState();

    public SashaKYotoz(EntityType<? extends SashaKYotoz> type, Level level) {
        super(WrathyArmamentEntities.SASHAKYOTOZ.get(), level);
        this.xpReward = XP_REWARD_BOSS;
        this.setMaxUpStep(1.25f);
        this.moveControl = new FlyingMoveControl(this, 15, false);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.onGround() || this.isAttacking() || this.isPowered() || this.isMoving())
            idle.stop();
        if (!this.isLongAttacking()) {
            walk.animateWhen(this.onGround() && this.isMoving(), this.tickCount);
            fly.animateWhen(this.getFlyPhase() == 1 && this.isMoving(), this.tickCount);
            if (!isMoving()) {
                walk.stop();
                if (this.idleAnimationTimeout <= 0 && this.getTarget() == null) {
                    this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                    this.idle.start(this.tickCount);
                } else {
                    --this.idleAnimationTimeout;
                }
            }
            if (this.isAttacking() && attackAnimationTimeout <= 0) {
                attackAnimationTimeout = 60;
                switch (this.getMeleeAttackType()) {
                    case "scythe" -> this.attackByScythe.start(this.tickCount);
                    case "blade" -> this.attackByBlade.start(this.tickCount);
                }
            } else
                attackAnimationTimeout--;
            if (!this.isAttacking()) {
                this.attackByScythe.stop();
                this.attackByBlade.stop();
            }
        }
        if (isPowered()) {
            if (this.onGround()) {
                if (this.getLongAttackType().equals("ray")) {
                    rayAttack();
                    if (!this.attackPhantomRay.isStarted())
                        this.attackPhantomRay.start(this.tickCount);
                } else {
                    for (int i = 0; i < 2; i++) {
                        int finalI = i;
                        OnActionsTrigger.queueServerWork(30 * i, () -> {
                            spawnParticle(WrathyArmamentMiscRegistries.PHANTOM_RAY.get(), this.level(), this.getX(), this.getY(), this.getZ(), 1 + finalI);
                            hitNearbyMobs(Component.translatable("death.attack.wrathy_armament.phantom_shock_message"), 10, 4 * finalI);
                        });
                    }
                    if (!this.attackCycleOfPhantoms.isStarted())
                        this.attackCycleOfPhantoms.start(this.tickCount);
                }
            }else
                this.animatePhantomFlight();
        }
        if (this.isLongAttacking() && !this.onGround())
            this.setLongAttacking(false);
        if (this.onGround() && this.getTarget() != null) {
            if (this.getLongAttackCooldown() > 0) {
                this.setLongAttackCooldown(this.getLongAttackCooldown() - 1);
            } else {
                this.setLongAttacking(true);
                this.navigation.stop();
                if (this.getLongAttackType().equals("ray")) {
                    OnActionsTrigger.queueServerWork(30, () -> {
                        this.setLongAttackType("cycle");
                        this.heal(10);
                        this.setLongAttacking(false);
                    });
                } else {
                    OnActionsTrigger.queueServerWork(40, () -> {
                        this.setLongAttackType("ray");
                        this.heal(10);
                        this.setLongAttacking(false);
                    });
                }
                this.setLongAttackCooldown(300);
            }
            LivingEntity target = this.getTarget();
            if (!isLongAttacking()) {
                if (this.getFlyPhase() == 0) {
                    if (target.getY() > this.getY() + 2 || this.distanceToSqr(target) > 24) {
                        this.setTargetAboveTimer(this.getTargetAboveTick() + 1);
                    }
                    if (this.getTargetAboveTick() > 60) {
                        animateTakeOff();
                        this.setDeltaMovement(0, 0.75, 0);
                        this.setTargetAboveTimer(0);
                    }
                } else {
                    this.setTargetAboveTimer(this.getTargetAboveTick() - 1);
                    if (this.getTargetAboveTick() < -60 && this.distanceToSqr(target) < 8) {
                        animateLanding();
                        this.setTargetAboveTimer(0);
                    }
                }
            }
        }
    }
    private void animatePhantomFlight(){
        float f = Mth.cos((this.getId() * 3 + this.tickCount) * 7.45F * ((float)Math.PI / 180F) + (float)Math.PI);
        int i = 1;
        float f2 = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * (1.1F + 0.21F * (float)i);
        float f3 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * (1.1F + 0.21F * (float)i);
        float f4 = (0.3F + f * 0.45F) * ((float)i * 0.2F + 1.0F);
        this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double)f2, this.getY() + (double)f4 +0.5f, this.getZ() + (double)f3, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double)f2, this.getY() + (double)f4 +0.5f, this.getZ() - (double)f3, 0.0D, 0.0D, 0.0D);
    }
    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.getFlyPhase() == 1)
            this.setFlyPhase(0);
        return super.doHurtTarget(entity);
    }

    // ray attack
    private void rayAttack() {
        this.playSound(SoundEvents.PHANTOM_SWOOP);
        double d0 = getZVector(1, this.getYRot());
        double d1 = getXVector(1, this.getYRot());
        float scaling = 0;
        for (int i1 = 0; i1 < 16; i1++) {
            BlockPos pos = this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
            if (!this.level().getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                scaling = scaling + 1;
            BlockPos pos1 = this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos();
            this.level().addParticle(WrathyArmamentMiscRegistries.PHANTOM_RAY.get(), pos1.getX(), pos1.getY(), pos1.getZ(), d0, 0.1, d1);
            final Vec3 center = new Vec3(pos1.getX(), pos1.getY(), pos1.getZ());
            List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(4), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
            for (Entity entityIterator : entityList) {
                if (!(entityIterator == this)) {
                    if (entityIterator instanceof LivingEntity livingEntity) {
                        float damage = livingEntity.getMaxHealth() - livingEntity.getHealth() + 2;
                        if (damage > 100)
                            damage = damage / 5;
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
    private void animateTakeOff() {
        walk.stop();
        WrathyArmament.LOGGER.debug("take off animation");
        if (!this.takeOff.isStarted())
            this.takeOff.start(this.tickCount);
        OnActionsTrigger.queueServerWork(20, () -> {
            this.setFlyPhase(1);
            this.setNoGravity(true);
        });
    }

    private void animateLanding() {
        if (!this.landing.isStarted())
            this.landing.start(this.tickCount);
        OnActionsTrigger.queueServerWork(20, () -> {
            this.setFlyPhase(0);
            this.setNoGravity(false);
        });
    }

    //logic
    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(getFlyPhase() == 1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SashaKYotozFlyMoveGoal(this));
        this.goalSelector.addGoal(1, new SashaKYotozRandomStrollGoal(this, 0.8, 160));
        this.targetSelector.addGoal(2, new SashaKYotozAttackGoal(this, 1.5, true));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LONG_ATTACKING, false);
        this.entityData.define(MELEE_ATTACK_TYPE, "scythe");
        this.entityData.define(LONG_ATTACK_TYPE, "ray");
        this.entityData.define(FLY_PHASE, 0);
        this.entityData.define(LONG_ATTACK_COOLDOWN, 0);
        this.entityData.define(TARGET_ABOVE_TIMER, 0);
        super.defineSynchedData();
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource source) {
        return this.getFlyPhase() != 1 && super.causeFallDamage(p_147187_, p_147188_, source);
    }

    public void setFlyPhase(int tmp) {
        this.entityData.set(FLY_PHASE, tmp);
    }


    public void setLongAttackCooldown(int tmp) {
        this.entityData.set(LONG_ATTACK_COOLDOWN, tmp);
    }

    private void setTargetAboveTimer(int tmp) {
        this.entityData.set(TARGET_ABOVE_TIMER, tmp);
    }

    public void setMeleeAttackType(String tmp) {
        this.entityData.set(MELEE_ATTACK_TYPE, tmp);
    }

    private void setLongAttackType(String tmp) {
        this.entityData.set(LONG_ATTACK_TYPE, tmp);
    }

    private void setLongAttacking(boolean b) {
        this.entityData.set(LONG_ATTACKING, b);
    }

    public boolean isLongAttacking() {
        return this.entityData.get(LONG_ATTACKING);
    }

    public int getFlyPhase() {
        return this.entityData.get(FLY_PHASE);
    }

    public int getLongAttackCooldown() {
        return this.entityData.get(LONG_ATTACK_COOLDOWN);
    }

    public int getTargetAboveTick() {
        return this.entityData.get(TARGET_ABOVE_TIMER);
    }

    public String getMeleeAttackType() {
        return this.entityData.get(MELEE_ATTACK_TYPE);
    }

    public String getLongAttackType() {
        return this.entityData.get(LONG_ATTACK_TYPE);
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
        return isLongAttacking() || this.getFlyPhase() == 1;
    }
}