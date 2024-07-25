package net.sashakyotoz.wrathy_armament.entities.bosses.parts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.entity.PartEntity;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

import javax.annotation.Nullable;

public class MoonLordPart extends PartEntity<MoonLord> {
    public final MoonLord parentMob;
    public final String name;
    private final EntityDimensions size;
    public MoonLordPart(MoonLord parent,String name, float x, float z) {
        super(parent);
        this.size = EntityDimensions.scalable(x, z);
        this.refreshDimensions();
        this.parentMob = parent;
        this.name = name;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
    public boolean isPickable() {
        return true;
    }

    @Nullable
    public ItemStack getPickResult() {
        return this.parentMob.getPickResult();
    }
    public boolean hurt(DamageSource damageSource, float amount) {
        return !this.isInvulnerableTo(damageSource) && this.parentMob.hurt(damageSource, amount);
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
