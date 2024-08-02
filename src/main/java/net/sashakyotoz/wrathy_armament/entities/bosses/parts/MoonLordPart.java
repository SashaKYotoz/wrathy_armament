package net.sashakyotoz.wrathy_armament.entities.bosses.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.entity.PartEntity;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

import javax.annotation.Nullable;

public class MoonLordPart extends PartEntity<MoonLord> {
    private static final EntityDataAccessor<Float> HEALTH_POINTS = SynchedEntityData.defineId(MoonLordPart.class, EntityDataSerializers.FLOAT);
    public final MoonLord parentMob;
    public final String name;
    private final EntityDimensions size;

    public MoonLordPart(MoonLord parent, String name, float x, float z) {
        super(parent);
        this.size = EntityDimensions.scalable(x, z);
        this.refreshDimensions();
        this.parentMob = parent;
        this.name = name;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEALTH_POINTS, 150f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.entityData.set(HEALTH_POINTS, pCompound.getFloat("healthPoints"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("healthPoints", getHealthPoints());
    }
    public boolean isPickable() {
        return true;
    }

    public float getHealthPoints() {
        return this.entityData.get(HEALTH_POINTS);
    }

    @Nullable
    public ItemStack getPickResult() {
        return this.parentMob.getPickResult();
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        if (getHealthPoints() > 0){
            this.entityData.set(HEALTH_POINTS, getHealthPoints() - amount);
            WrathyArmament.LOGGER.debug("Moon Lord's {} HPs: {}",this.name,getHealthPoints());
            return !this.isInvulnerableTo(damageSource);
        }
        else{
            this.entityData.set(HEALTH_POINTS,0f);
            return this.parentMob.hurt(damageSource,amount);
        }
    }

    public boolean is(Entity entity) {
        return this == entity || this.parentMob == entity;
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return this.size;
    }

    public boolean shouldBeSaved() {
        return false;
    }
}
