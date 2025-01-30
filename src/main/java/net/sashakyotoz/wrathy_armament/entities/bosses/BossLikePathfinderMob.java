package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.ResetAngerTargetGoal;
import net.sashakyotoz.wrathy_armament.entities.bosses.core.PersistentAngerMob;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BossLikePathfinderMob extends PathfinderMob implements Enemy, PersistentAngerMob {
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(BossLikePathfinderMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BossLikePathfinderMob.class, EntityDataSerializers.INT);
    public int attackAnimationTimeout = 30;
    public final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(36000, 1080000);
    @Nullable
    public UUID persistentAngerTarget;

    private final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    public BossLikePathfinderMob(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ATTACKING, false);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        super.defineSynchedData();
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int i) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, i);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    public void setAttacking(boolean tmp) {
        this.entityData.set(ATTACKING, tmp);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    public void spawnParticle(Level level, double x, double y, double z, float modifier) {
        spawnParticle(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), level, x, y, z, modifier);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new ResetAngerTargetGoal<>(this, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        super.registerGoals();
    }

    public void aiStep() {
        this.updateSwingTime();
        this.updateNoActionTime();
        super.aiStep();
    }

    @Override
    public void tick() {
        List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
        workQueue.forEach(work -> {
            work.setValue(work.getValue() - 1);
            if (work.getValue() == 0)
                actions.add(work);
        });
        actions.forEach(e -> e.getKey().run());
        workQueue.removeAll(actions);
        super.tick();
    }

    protected void updateNoActionTime() {
        float f = this.getLightLevelDependentMagicValue();
        if (f > 0.5F) {
            this.noActionTime += 2;
        }
    }

    protected boolean canRide(Entity pEntity) {
        return false;
    }

    public boolean shouldDropExperience() {
        return true;
    }

    public void spawnParticle(ParticleOptions type, Level level, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                level.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }

    public void hitNearbyMobs(Component customDeathName, float damage, int radius) {
        List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(centerOfMob(), centerOfMob()).inflate(radius), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(centerOfMob()))).toList();
        for (Entity entityIterator : entityList) {
            if (!(entityIterator == this)) {
                if (entityIterator instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(new DamageSource(livingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                        @Override
                        public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                            return customDeathName;
                        }
                    }, damage);
                    break;
                }
            }
        }
    }

    public void hitNearbyMobs(float damage, int radius) {
        List<LivingEntity> entityList = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(centerOfMob(), centerOfMob()).inflate(radius), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(centerOfMob()))).toList();
        for (LivingEntity entityIterator : entityList) {
            if (entityIterator != this) {
                entityIterator.hurt(this.damageSources().magic(), damage);
                break;
            }
        }
    }

    private Vec3 centerOfMob() {
        return new Vec3(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public void sendSystemMessage(Component component) {
        super.sendSystemMessage(component);
    }

    public abstract ServerBossEvent bossInfo();

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo().addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo().removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo().setProgress(this.getHealth() / this.getMaxHealth());
    }

    public double getXVector(double speed, double yaw) {
        return speed * Math.cos((yaw + 90) * (Math.PI / 180));
    }

    public double getZVector(double speed, double yaw) {
        return speed * Math.sin((yaw + 90) * (Math.PI / 180));
    }

    public boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }
}