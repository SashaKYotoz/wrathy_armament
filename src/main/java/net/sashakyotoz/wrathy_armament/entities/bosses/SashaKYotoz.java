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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.sashakyotoz.SashaKYotozMeleeAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.sashakyotoz.SashaKYotozMovementGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.sashakyotoz.SashaKYotozRandomStrollGoal;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class SashaKYotoz extends BossLikePathfinderMob implements PowerableMob {
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.sashakyotoz"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
    private static final EntityDataAccessor<SashaKYotozPhase> DATA_SASHAKYOTOZ_PHASE = SynchedEntityData.defineId(SashaKYotoz.class, WrathyArmamentMiscRegistries.SASHAKYOTOZ_PHASE.get());
    public int takeOffRotDegrees = 0;

    public final AnimationState idle = new AnimationState();
    public final AnimationState takeOff = new AnimationState();
    public final AnimationState landing = new AnimationState();
    public final AnimationState attackByScythe = new AnimationState();
    public final AnimationState attackByBlade = new AnimationState();
    public final AnimationState attackPhantomRay = new AnimationState();
    public final AnimationState attackCycleOfPhantoms = new AnimationState();
    public final AnimationState death = new AnimationState();

    public SashaKYotoz(EntityType<? extends SashaKYotoz> type, Level level) {
        super(WrathyArmamentEntities.SASHAKYOTOZ.get(), level);
        this.xpReward = XP_REWARD_BOSS;
        this.setMaxUpStep(1.25f);
        this.moveControl = new FlyingMoveControl(this, 15, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_SASHAKYOTOZ_PHASE.equals(pKey)) {
            switch (this.getSashaKYotozPhase()) {
                case IDLING -> this.idle.startIfStopped(this.tickCount);
                case TAKING_OFF -> {
                    this.takeOff.start(this.tickCount);
                    this.takeOffRotDegrees = 360;
                    this.setDeltaMovement(
                            this.getXVector(-0.25f, this.getYRot()),
                            0.5f,
                            this.getZVector(-0.25f, this.getYRot())
                    );
                    this.setSashaKYotozPhase(SashaKYotozPhase.FLYING);
                }
                case LANDING -> {
                    this.landing.start(this.tickCount);
                    this.setRandomPhase();
                }
                case RANGED_ATTACKING -> {
                    this.attackPhantomRay.start(this.tickCount);
                    OnActionsTrigger.queueServerWork(8, () -> this.setDeltaMovement(
                            this.getXVector(-2, this.getYRot()),
                            0.325,
                            this.getZVector(-2, this.getYRot())
                    ));
                    OnActionsTrigger.queueServerWork(15, () -> {
                        rayAttack();
                        this.setRandomPhase();
                    });
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public boolean doHurtTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        } else {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, this.getVoicePitch());
            return SashaKYotoz.hurtTarget(this, (LivingEntity) entity);
        }
    }

    public static boolean hurtTarget(LivingEntity warrior, LivingEntity target) {
        float f = (float) warrior.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = target.hurt(warrior.damageSources().mobAttack(warrior), f);
        if (flag)
            warrior.doEnchantDamageEffects(warrior, target);
        return flag;
    }

    public void handleEntityEvent(byte handleByte) {
        if (handleByte >= 4 && handleByte <= 20) {
            if (isInPhase(SashaKYotozPhase.MELEE_SCYTHE_ATTACKING))
                this.attackByScythe.start(this.tickCount);
            if (isInPhase(SashaKYotozPhase.MELEE_ATTACKING))
                this.attackByBlade.start(this.tickCount);
        } else
            super.handleEntityEvent(handleByte);
    }

    @Override
    public void baseTick() {
        super.baseTick();
//        WrathyArmament.LOGGER.info("SashaKYotoz's phase: {}", this.getSashaKYotozPhase());
        if (this.tickCount % 2 == 0) {
            if (this.isInPhase(SashaKYotozPhase.FLYING))
                this.animatePhantomFlight();
            if (this.getTarget() == null && this.isInPhase(SashaKYotozPhase.FLYING))
                this.setSashaKYotozPhase(SashaKYotozPhase.LANDING);
            if (this.getTarget() != null && this.isInPhase(SashaKYotozPhase.IDLING))
                this.setRandomPhase();
        }
        if (this.takeOffRotDegrees > 0)
            this.takeOffRotDegrees--;
    }

    public void setRandomPhase() {
        int random = this.random.nextIntBetweenInclusive(0, 3);
        if (!this.isInPhase(SashaKYotozPhase.FLYING) && !this.isInPhase(SashaKYotozPhase.TAKING_OFF)) {
            switch (random) {
                default -> this.setSashaKYotozPhase(SashaKYotozPhase.MELEE_SCYTHE_ATTACKING);
                case 1 -> this.setSashaKYotozPhase(SashaKYotozPhase.MELEE_ATTACKING);
                case 2 -> this.setSashaKYotozPhase(SashaKYotozPhase.RANGED_ATTACKING);
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

    //logic
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
        this.targetSelector.addGoal(3, new SashaKYotozMeleeAttackGoal(this));
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
        if (this.random.nextBoolean() && !this.isInPhase(SashaKYotozPhase.FLYING))
            this.setSashaKYotozPhase(SashaKYotozPhase.MELEE_SCYTHE_ATTACKING);
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
    }

    public SashaKYotozPhase getSashaKYotozPhase() {
        return this.entityData.get(DATA_SASHAKYOTOZ_PHASE);
    }

    public boolean isInPhase(SashaKYotozPhase phase) {
        return this.getSashaKYotozPhase() == phase;
    }

    public enum SashaKYotozPhase {
        IDLING,
        FLYING,
        TAKING_OFF,
        LANDING,
        RANGED_ATTACKING,
        MELEE_ATTACKING,
        MELEE_SCYTHE_ATTACKING
    }
}