package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentParticleTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public abstract class BossLikePathfinderMob extends PathfinderMob implements Enemy {
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(BossLikePathfinderMob.class, EntityDataSerializers.BOOLEAN);
    protected BossLikePathfinderMob(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ATTACKING, false);
        super.defineSynchedData();
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
    public void spawnParticle(Level world, double x, double y, double z, float modifier) {
        spawnParticle(WrathyArmamentParticleTypes.FROST_SOUL_RAY.get(), world, x, y, z, modifier);
    }
    public void spawnParticle(SimpleParticleType type, Level world, double x, double y, double z, float modifier) {
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                world.addParticle(type, x + 0.25, y, z + 0.25, Math.cos(i) * 0.25d * modifier, 0.2d, Math.sin(i) * 0.25d * modifier);
        }
    }
    public void hitNearbyMobs(Component customDeathName, float damage,int radius){
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
    public void hitNearbyMobs(float damage, int radius){
        List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(centerOfMob(), centerOfMob()).inflate(radius), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(centerOfMob()))).toList();
        for (Entity entityIterator : entityList) {
            if (!(entityIterator == this)) {
                if (entityIterator instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(this.damageSources().magic(), damage);
                    break;
                }
            }
        }
    }
    private Vec3 centerOfMob(){
        return new Vec3(this.getX(),this.getY(),this.getZ());
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
}