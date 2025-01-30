package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class VanishableLikeEntity extends Entity {
    public int timeToVanish = 0;
    @Nullable
    public LivingEntity owner;

    public VanishableLikeEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {

    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public float rotationRelativelyToY() {
        return this.owner != null ? this.owner.getYRot() : 0;
    }

    public Player getNearestPlayer() {
        final Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(24), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
        for (Entity entityIterator : entityList) {
            if (entityIterator instanceof Player player)
                return player;
        }
        return null;
    }

    public double getXVector(double yaw) {
        return Math.cos((yaw + 90) * (Math.PI / 180));
    }

    public double getYVector(double xRot) {
        return (xRot * (-0.025)) + 0.25;
    }

    public double getZVector(double yaw) {
        return Math.sin((yaw + 90) * (Math.PI / 180));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        timeToVanish = tag.getInt("timeToVanish");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (timeToVanish > 0)
            tag.putInt("timeToVanish", this.timeToVanish);
    }
}
