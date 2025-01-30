package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.LichKingMovementGoal;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentSounds;

import java.util.Comparator;
import java.util.List;

public class LichKing extends BossLikePathfinderMob {
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.lich_king"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
    private final TargetingConditions targetCountTargeting = TargetingConditions.forNonCombat().range(48.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
    private static final EntityDataAccessor<KingPose> DATA_KING_POSE = SynchedEntityData.defineId(LichKing.class, WrathyArmamentMiscRegistries.LICH_KING_POSE.get());
    public final AnimationState spawn = new AnimationState();
    public final AnimationState death = new AnimationState();
    public final AnimationState combo_attack = new AnimationState();
    public final AnimationState combo_attack1 = new AnimationState();
    public final AnimationState combo_attack2 = new AnimationState();
    public final AnimationState spin_attack = new AnimationState();
    public final AnimationState rain_cast = new AnimationState();
    public final AnimationState summon = new AnimationState();
    public final AnimationState transition_to_heal = new AnimationState();
    public final AnimationState heal_pose = new AnimationState();
    private int timeOfAbility = 0;

    public LichKing(EntityType<? extends BossLikePathfinderMob> type, Level level) {
        super(type, level);
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(WrathyArmamentItems.FROSTMOURNE.get()));
        this.setMaxUpStep(1.25f);
        this.xpReward = 50;
    }

    @Override
    protected void tickDeath() {
        if (deathTime == 19)
            this.spawnParticle(this.level(), this.getX(), this.getY(), this.getZ(), 2);
        super.tickDeath();
    }

    @Override
    public void die(DamageSource source) {
        this.deathTime = -40;
        this.setKingPose(KingPose.DYING);
        super.die(source);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (DATA_KING_POSE.equals(dataAccessor)) {
            if (!this.isInPose(KingPose.HEALING)){
                this.heal_pose.stop();
                this.transition_to_heal.stop();
            }
            if (!this.isInPose(KingPose.SUMMOING))
                this.summon.stop();
            switch (this.getKingPose()) {
                case SPAWNING -> this.spawn.start(this.tickCount);
                case DYING -> this.death.start(this.tickCount);
                case ATTACK -> {
                    if (this.getRandom().nextBoolean())
                        this.combo_attack.start(this.tickCount);
                    else
                        this.combo_attack1.start(this.tickCount);
                    this.playSound(WrathyArmamentSounds.SOUL_SWING, 1.5f, 2f);
                    this.queueServerWork(30, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 11)
                            this.doEnchantDamageEffects(this, this.getTarget());
                    });
                }
                case SECOND_ATTACK -> {
                    this.combo_attack2.start(this.tickCount);
                    this.playSound(WrathyArmamentSounds.SOUL_SWING, 1.25f, 2.2f);
                    this.queueServerWork(30, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 12)
                            this.doEnchantDamageEffects(this, this.getTarget());
                    });
                }
                case SPIN_ATTACK -> {
                    this.spin_attack.start(this.tickCount);
                    this.playSound(WrathyArmamentSounds.SOUL_SWING, 1.75f, 0.9f);
                    this.playSpinAnimation();
                }
                case HEALING -> {
                    this.transition_to_heal.start(this.tickCount);
                    this.navigation.stop();
                    queueServerWork(11, () -> this.heal_pose.startIfStopped(this.tickCount));
                }
                case SUMMOING -> {
                    this.summon.start(this.tickCount);
                    this.playSound(SoundEvents.SOUL_SOIL_BREAK,1.5f,0.9f);
                    if (this.isOrNotMyrmidonsAround())
                        this.queueServerWork(30, this::spawnMyrmidons);
                }
                case RAIN_CASTING -> {
                    this.rain_cast.start(this.tickCount);
                    this.rainCast();
                    this.queueServerWork(50, this::shootSoulRay);
                }
            }
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    private void spawnMyrmidons() {
        if (this.level() instanceof ServerLevel serverLevel) {
            int randomCountOfMyrmidons = this.random.nextIntBetweenInclusive(1, 4);
            for (int i = 0; i < randomCountOfMyrmidons; i++) {
                LichMyrmidon lichMyrmidon = new LichMyrmidon(WrathyArmamentEntities.LICH_MYRMIDON.get(), serverLevel);
                lichMyrmidon.moveTo(this.getX() + this.getZVector(2, this.getYRot()),
                        this.getY() + 1,
                        this.getZ() + this.getZVector(2, this.getYRot()));
                lichMyrmidon.setOwner(LichKing.this);
                serverLevel.addFreshEntity(lichMyrmidon);
            }
        }
    }

    private void clientDiggingParticles(AnimationState time) {
        if ((float) time.getAccumulatedTime() < 1000.0F) {
            RandomSource randomsource = this.getRandom();
            BlockState blockstate = this.getBlockStateOn();
            if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                for (int i = 0; i < 30; ++i) {
                    double d0 = this.getX() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    double d1 = this.getY() + 0.5f;
                    double d2 = this.getZ() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public void baseTick() {
        if (this.spawn.isStarted() || this.death.isStarted())
            this.clientDiggingParticles(this.spawn.isStarted() ? this.spawn : this.death);
        if (this.isInPose(KingPose.HEALING) && this.tickCount % 20 == 0) {
            if (this.getHealth() != this.getMaxHealth()) {
                this.spawnParticle(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), this.level(), this.getX(), this.getY() + 1, this.getZ(), 2.5f * this.random.nextInt(1, 4));
                this.heal(5);
            }
        }
        if (this.timeOfAbility > 0)
            this.timeOfAbility--;
        if (this.getTarget() != null && this.timeOfAbility <= 0)
            setPhase();
        super.baseTick();
    }

    private void setPhase() {
        switch (this.getKingPose()) {
            case SPAWNING, SUMMOING -> this.setKingPose(KingPose.ATTACK);
            case ATTACK ->
                    this.setKingPose(this.getRandom().nextBoolean() ? KingPose.SPIN_ATTACK : KingPose.SECOND_ATTACK);
            case SPIN_ATTACK, SECOND_ATTACK -> this.setKingPose(KingPose.RAIN_CASTING);
            case RAIN_CASTING -> this.setKingPose(KingPose.HEALING);
            case HEALING -> this.setKingPose(KingPose.SUMMOING);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LichKingMovementGoal(this));
        super.registerGoals();
    }

    private boolean isOrNotMyrmidonsAround() {
        int i = this.level().getNearbyEntities(LichMyrmidon.class, this.targetCountTargeting, this, this.getBoundingBox().inflate(48.0D)).size();
        return this.getRandom().nextInt(5) + 1 > i;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInPose(KingPose.HEALING) && this.random.nextFloat() > 0.75f)
            this.setPhase();
        return super.hurt(source, amount);
    }

    private void shootSoulRay() {
        this.playSound(SoundEvents.SOUL_ESCAPE);
        double d0 = -Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
        double d1 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
        float scaling = 0;
        for (int i1 = 0; i1 < 24; i1++) {
            scaling = scaling + 1;
            this.level().addParticle(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getX()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getY()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getZ()), d0,
                    0.1, d1);
            final Vec3 center = new Vec3(
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getX()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getY()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getZ()));
            List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(2D), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
            for (Entity entityIterator : entityList) {
                if (entityIterator != this) {
                    if (entityIterator instanceof LivingEntity livingEntity) {
                        livingEntity.hurt(this.damageSources().magic(), 8);
                        livingEntity.setDeltaMovement(0, 0.5, 0);
                        this.level().setBlock(livingEntity.blockPosition().above(2), this.level().getBlockState(livingEntity.blockPosition()), 2);
                        this.level().setBlock(livingEntity.blockPosition(), Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    private void playSpinAnimation() {
        BlockState blockstate = this.getBlockStateOn();
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate),
                        this.getX(), this.getY() + 0.25f, this.getZ(),
                        this.getXVector(3, this.getYRot()), 1, this.getZVector(3, this.getYRot()));
        }
        queueServerWork(15, () -> {
            if (this.getTarget() != null && this.spin_attack.isStarted())
                this.hitNearbyMobs(15, 12);
        });
    }

    private void rainCast() {
        if (this.level() instanceof ServerLevel level) {
            queueServerWork(20, () -> {
                ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), level, 1f, true, true, 4,
                        ParticleTypes.SOUL, "lich_rain");
                particleEntity.moveTo(new Vec3(this.getX(), this.getY() + 1f, this.getZ()));
                level.addFreshEntity(particleEntity);
            });
        }
    }

    //logic
    private void setKingPose(KingPose knightPose) {
        this.entityData.set(DATA_KING_POSE, knightPose);
        this.timeOfAbility = knightPose.getAbilityTime;
    }

    public KingPose getKingPose() {
        return this.entityData.get(DATA_KING_POSE);
    }

    public boolean isInPose(KingPose phase) {
        return this.getKingPose() == phase;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_KING_POSE, KingPose.SPAWNING);
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
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 5);
    }

    public enum KingPose {
        DYING(80),
        SPAWNING(60),
        ATTACK(40),
        SECOND_ATTACK(30),
        SPIN_ATTACK(30),
        RAIN_CASTING(50),
        HEALING(100),
        SUMMOING(30);
        public final int getAbilityTime;

        KingPose(int actionTime) {
            this.getAbilityTime = actionTime;
        }
    }
}