package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public interface PersistentAngerMob {
    String TAG_ANGER_TIME = "AngerTime";
    String TAG_ANGRY_AT = "AngryAt";

    int getRemainingPersistentAngerTime();

    void setRemainingPersistentAngerTime(int i);

    @Nullable
    UUID getPersistentAngerTarget();

    void setPersistentAngerTarget(@Nullable UUID uuid);

    void startPersistentAngerTimer();

    default void addPersistentAngerSaveData(CompoundTag tag) {
        tag.putInt(TAG_ANGER_TIME, this.getRemainingPersistentAngerTime());
        if (this.getPersistentAngerTarget() != null) {
            tag.putUUID(TAG_ANGRY_AT, this.getPersistentAngerTarget());
        }

    }

    default void readPersistentAngerSaveData(Level level, CompoundTag tag) {
        this.setRemainingPersistentAngerTime(tag.getInt(TAG_ANGER_TIME));
        if (level instanceof ServerLevel) {
            if (!tag.hasUUID(TAG_ANGRY_AT)) {
                this.setPersistentAngerTarget(null);
            } else {
                UUID uuid = tag.getUUID(TAG_ANGRY_AT);
                this.setPersistentAngerTarget(uuid);
                Entity entity = ((ServerLevel)level).getEntity(uuid);
                if (entity != null) {
                    if (entity instanceof Mob)
                        this.setLastHurtByMob((Mob)entity);

                    if (entity instanceof Player player)
                        this.setLastHurtByPlayer(player);
                }
            }
        }
    }

    default void updatePersistentAnger(ServerLevel level, boolean p_21668_) {
        LivingEntity livingentity = this.getTarget();
        UUID uuid = this.getPersistentAngerTarget();
        if ((livingentity == null || livingentity.isDeadOrDying()) && uuid != null && level.getEntity(uuid) instanceof Mob) {
            this.stopBeingAngry();
        } else {
            if (livingentity != null && !Objects.equals(uuid, livingentity.getUUID())) {
                this.setPersistentAngerTarget(livingentity.getUUID());
                this.startPersistentAngerTimer();
            }

            if (this.getRemainingPersistentAngerTime() > 0 && (livingentity == null || livingentity.getType() != EntityType.PLAYER || !p_21668_)) {
                this.setRemainingPersistentAngerTime(this.getRemainingPersistentAngerTime() - 1);
                if (this.getRemainingPersistentAngerTime() == 0) {
                    this.stopBeingAngry();
                }
            }

        }
    }

    default boolean isAngryAt(LivingEntity entity) {
        if (!this.canAttack(entity)) {
            return false;
        } else {
            return entity.getType() == EntityType.PLAYER && this.isAngryAtAllPlayers(entity.level()) || entity.getUUID().equals(this.getPersistentAngerTarget());
        }
    }

    default boolean isAngryAtAllPlayers(Level level) {
        return level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.isAngry() && this.getPersistentAngerTarget() == null;
    }

    default boolean isAngry() {
        return this.getRemainingPersistentAngerTime() > 0;
    }

    default void forgetCurrentTargetAndRefreshUniversalAnger() {
        this.stopBeingAngry();
        this.startPersistentAngerTimer();
    }

    default void stopBeingAngry() {
        this.setLastHurtByMob(null);
        this.setPersistentAngerTarget(null);
        this.setTarget(null);
        this.setRemainingPersistentAngerTime(0);
    }

    @Nullable
    LivingEntity getLastHurtByMob();

    void setLastHurtByMob(@Nullable LivingEntity entity);

    void setLastHurtByPlayer(@Nullable Player player);

    void setTarget(@Nullable LivingEntity entity);

    boolean canAttack(LivingEntity entity);
    @Nullable
    LivingEntity getTarget();
}