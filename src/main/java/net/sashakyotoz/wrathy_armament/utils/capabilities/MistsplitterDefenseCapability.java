package net.sashakyotoz.wrathy_armament.utils.capabilities;

import net.minecraft.nbt.CompoundTag;

public class MistsplitterDefenseCapability {
    private boolean isDefenseModeOn;
    private String defenceType = "";
    public boolean isDefenseModeOn(){
        return this.isDefenseModeOn;
    }
    public void setDefenseModeFlag(boolean flag){
        this.isDefenseModeOn = flag;
    }
    public String getDefenceType() {
        return defenceType;
    }

    public void setDefenceType(String defenceType) {
        this.defenceType = defenceType;
    }
    public void copyFrom(MistsplitterDefenseCapability source) {
        this.isDefenseModeOn = source.isDefenseModeOn;
        this.defenceType = source.defenceType;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("mistsplitterDefenceFlag", isDefenseModeOn);
        nbt.putString("mistsplitterDefenceType", defenceType);
    }


    public void loadNBTData(CompoundTag nbt) {
        isDefenseModeOn = nbt.getBoolean("mistsplitterDefenceFlag");
        defenceType = nbt.getString("mistsplitterDefenceType");
    }
}