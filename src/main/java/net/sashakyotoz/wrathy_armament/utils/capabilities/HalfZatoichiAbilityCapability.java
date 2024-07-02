package net.sashakyotoz.wrathy_armament.utils.capabilities;

import net.minecraft.nbt.CompoundTag;

public class HalfZatoichiAbilityCapability {
  private int pointsHealed;
  private boolean isInAdrenalinMode;

    public boolean isInAdrenalinMode() {
        return isInAdrenalinMode;
    }

    public void setInAdrenalinMode(boolean inAdrenalinMode) {
        isInAdrenalinMode = inAdrenalinMode;
    }

    public int getPointsHealed() {
        return pointsHealed;
    }

    public void setPointsHealed(int pointsHealed) {
        this.pointsHealed = pointsHealed;
    }
    public void copyFrom(HalfZatoichiAbilityCapability source) {
        this.isInAdrenalinMode = source.isInAdrenalinMode;
        this.pointsHealed = source.pointsHealed;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("halfZanoichiAdrenalinFlag", isInAdrenalinMode);
        nbt.putInt("halfZanoichiPointsHealed", pointsHealed);
    }


    public void loadNBTData(CompoundTag nbt) {
        isInAdrenalinMode = nbt.getBoolean("halfZanoichiAdrenalinFlag");
        pointsHealed = nbt.getInt("halfZanoichiPointsHealed");
    }
}