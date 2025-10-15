package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.sashakyotoz.SashaKYotozMovementGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.sashakyotoz.SashaKYotozRandomStrollGoal;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class SashaKYotoz extends BossLikePathfinderMob implements PowerableMob {
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.sashakyotoz"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
    private static final EntityDataAccessor<SashaKYotozPhase> DATA_SASHAKYOTOZ_PHASE = SynchedEntityData.defineId(SashaKYotoz.class, WrathyArmamentMiscRegistries.SASHAKYOTOZ_PHASE.get());
    public int timeOfAbility = 0;

    public final AnimationState idle = new AnimationState();
    public final AnimationState takeOff = new AnimationState();
    public final AnimationState landing = new AnimationState();
    public final AnimationState flying = new AnimationState();
    public final AnimationState attackByScythe = new AnimationState();
    public final AnimationState attackByBlade = new AnimationState();
    public final AnimationState attackPhantomRay = new AnimationState();
    public final AnimationState attackCircleOfPhantoms = new AnimationState();
    public final AnimationState death = new AnimationState();

    private final GroundPathNavigation groundPathNavigation = new GroundPathNavigation(this, this.level());
    private final FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, this.level());

    public SashaKYotoz(EntityType<? extends SashaKYotoz> type, Level level) {
        super(WrathyArmamentEntities.SASHAKYOTOZ.get(), level);
        this.xpReward = XP_REWARD_BOSS;
        this.moveControl = new FlyingMoveControl(this, 15, false);
        this.setMaxUpStep(1.25f);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_SASHAKYOTOZ_PHASE.equals(pKey)) {
            if (!isInPhase(SashaKYotozPhase.FLYING)) {
                this.navigation = groundPathNavigation;
                this.flying.stop();
            }
            if (!isInPhase(SashaKYotozPhase.IDLING))
                this.idle.stop();
            switch (this.getSashaKYotozPhase()) {
                case IDLING -> this.idle.start(this.tickCount);
                case FLYING -> {
                    this.flying.start(this.tickCount);
                    this.navigation = flyingPathNavigation;
                }
//                case TAKING_OFF -> this.takeOff.start(this.tickCount);
                case LANDING -> this.landing.start(this.tickCount);
                case RANGED_ATTACKING -> {
                    this.attackPhantomRay.start(this.tickCount);
                    queueServerWork(10, () -> {
                        this.setDeltaMovement(
                                this.getXVector(-1, this.getYRot()),
                                0.35,
                                this.getZVector(-1, this.getYRot()));
                        this.rayAttack();
                    });
                }
                case MELEE_ATTACKING -> {
                    this.attackByBlade.start(this.tickCount);
                    this.playSound(SoundEvents.PHANTOM_SWOOP, 1.5f, 2f);
                    this.queueServerWork(20, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 12)
                            this.hurtTarget(this, this.getTarget());
                    });
                }
                case MELEE_SCYTHE_ATTACKING -> {
                    this.attackByScythe.start(this.tickCount);
                    this.playSound(SoundEvents.PHANTOM_BITE, 1.5f, 2f);
                    this.queueServerWork(20, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 16)
                            this.hurtTarget(this, this.getTarget());
                    });
                }
                case MELEE_CIRCLE_ATTACKING -> {
                    this.attackCircleOfPhantoms.start(this.tickCount);
                    this.setDeltaMovement(new Vec3(0, -1, 0));
                    this.queueServerWork(30, () -> {
                        this.spawnParticle(WrathyArmamentMiscRegistries.PHANTOM_RAY.get(), this.level(), this.getX(), this.getY() + 1, this.getZ(), 2);
                        this.playSound(SoundEvents.PHANTOM_SWOOP, 1.5f, 1.6f);
                        this.hitNearbyMobs(Component.translatable("death.attack.wrathy_armament.phantom_shock_message"), 12, 7);
                    });
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public boolean doHurtTarget(Entity entity) {
        if (!(entity instanceof LivingEntity))
            return false;
        else {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.playSound(SoundEvents.PHANTOM_SWOOP, 1.0F, this.getVoicePitch());
            return hurtTarget(this, (LivingEntity) entity);
        }
    }

    private boolean hurtTarget(LivingEntity warrior, LivingEntity target) {
        float f = (float) warrior.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = target.hurt(warrior.damageSources().mobAttack(warrior), f);
        if (flag)
            warrior.doEnchantDamageEffects(warrior, target);
        return flag;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 2 == 0) {
            if (this.isInPhase(SashaKYotozPhase.FLYING))
                this.animatePhantomFlight();
        }
        if (this.timeOfAbility > 0)
            this.timeOfAbility--;
        if (this.getTarget() != null && this.timeOfAbility <= 0)
            setPhase();
    }

    public void setPhase() {
        switch (this.getSashaKYotozPhase()) {
            case IDLING, LANDING ->
                    this.setSashaKYotozPhase(this.getTarget() == null ? SashaKYotozPhase.IDLING : SashaKYotozPhase.MELEE_ATTACKING);
            case MELEE_ATTACKING -> this.setSashaKYotozPhase(SashaKYotozPhase.MELEE_CIRCLE_ATTACKING);
            case MELEE_CIRCLE_ATTACKING -> this.setSashaKYotozPhase(SashaKYotozPhase.MELEE_SCYTHE_ATTACKING);
            case MELEE_SCYTHE_ATTACKING -> this.setSashaKYotozPhase(SashaKYotozPhase.RANGED_ATTACKING);
            case RANGED_ATTACKING -> this.setSashaKYotozPhase(SashaKYotozPhase.TAKING_OFF);
            case TAKING_OFF -> this.setSashaKYotozPhase(SashaKYotozPhase.FLYING);
            case FLYING -> {
                if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 9)
                    this.setSashaKYotozPhase(SashaKYotozPhase.LANDING);
            }
        }
    }

    private void animatePhantomFlight() {
        float f = Mth.cos((this.getId() * 3 + this.tickCount) * 7.45F * ((float) Math.PI / 180F) + (float) Math.PI);
        int i = 1;
        float f2 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * (1.1F + 0.21F * (float) i);
        float f3 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * (1.1F + 0.21F * (float) i);
        float f4 = (0.3F + f * 0.45F) * ((float) i * 0.2F + 1.0F);
        this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double) f2, this.getY() + (double) f4 + 0.5f, this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
        this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double) f2, this.getY() + (double) f4 + 0.5f, this.getZ() - (double) f3, 0.0D, 0.0D, 0.0D);
    }

    // ray attack
    public void rayAttack() {
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

    @Override
    public void setNoGravity(boolean ignored) {
        super.setNoGravity(this.isInPhase(SashaKYotozPhase.FLYING));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SashaKYotozMovementGoal(this));
        this.goalSelector.addGoal(2, new SashaKYotozRandomStrollGoal(this, 0.8, 160));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_SASHAKYOTOZ_PHASE, SashaKYotozPhase.IDLING);
        super.defineSynchedData();
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource source) {
        return !this.isInPhase(SashaKYotozPhase.FLYING) && super.causeFallDamage(pFallDistance, pMultiplier, source);
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
        return this.isInPhase(SashaKYotozPhase.LANDING) || this.isInPhase(SashaKYotozPhase.TAKING_OFF) || this.isInPhase(SashaKYotozPhase.FLYING);
    }

    public void setSashaKYotozPhase(SashaKYotozPhase phase) {
        this.entityData.set(DATA_SASHAKYOTOZ_PHASE, phase);
        this.timeOfAbility = phase.getAbilityTime;
    }

    public SashaKYotozPhase getSashaKYotozPhase() {
        return this.entityData.get(DATA_SASHAKYOTOZ_PHASE);
    }

    public boolean isInPhase(SashaKYotozPhase phase) {
        return this.getSashaKYotozPhase() == phase;
    }

    public enum SashaKYotozPhase {
        IDLING(100),
        FLYING(80),
        TAKING_OFF(20),
        LANDING(20),
        RANGED_ATTACKING(80),
        MELEE_ATTACKING(50),
        MELEE_CIRCLE_ATTACKING(60),
        MELEE_SCYTHE_ATTACKING(50);
        public final int getAbilityTime;

        SashaKYotozPhase(int abilityTime) {
            this.getAbilityTime = abilityTime;
        }
    }
}