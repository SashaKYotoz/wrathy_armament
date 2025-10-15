package net.sashakyotoz.wrathy_armament.utils.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModCapabilities implements ICapabilitySerializable<CompoundTag> {
    //misplitter defense capability
    public static Capability<MistsplitterDefenseCapability> MISTSPLITTER_DEFENCE = CapabilityManager.get(new CapabilityToken<>() {});
    private MistsplitterDefenseCapability mistsplitterDefenseCapability = null;
    private final LazyOptional<MistsplitterDefenseCapability> mistsplitterDefenseOptional = LazyOptional.of(this::createMistsplitterCapability);
    private MistsplitterDefenseCapability createMistsplitterCapability() {
        if(this.mistsplitterDefenseCapability == null) 
            this.mistsplitterDefenseCapability = new MistsplitterDefenseCapability();
        return this.mistsplitterDefenseCapability;
    }
    //half zatoichi abilities capability
    public static Capability<HalfZatoichiAbilityCapability> HALF_ZATOICHI_ABILITIES = CapabilityManager.get(new CapabilityToken<>() {});
    private HalfZatoichiAbilityCapability halfZatoichiAbilityCapability = null;
    private final LazyOptional<HalfZatoichiAbilityCapability> halfZatoichiAbilityOptional = LazyOptional.of(this::createHalfZatoichiCapability);
    private HalfZatoichiAbilityCapability createHalfZatoichiCapability() {
        if(this.halfZatoichiAbilityCapability == null)
            this.halfZatoichiAbilityCapability = new HalfZatoichiAbilityCapability();
        return this.halfZatoichiAbilityCapability;
    }

    //------//
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == MISTSPLITTER_DEFENCE)
            return mistsplitterDefenseOptional.cast();
        if (cap == HALF_ZATOICHI_ABILITIES)
            return halfZatoichiAbilityOptional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createMistsplitterCapability().saveNBTData(tag);
        createHalfZatoichiCapability().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createMistsplitterCapability().loadNBTData(nbt);
        createHalfZatoichiCapability().loadNBTData(nbt);
    }
}