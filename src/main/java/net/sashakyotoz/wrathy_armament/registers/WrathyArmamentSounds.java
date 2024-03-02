package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class WrathyArmamentSounds {
    public static final SoundEvent ITEM_LANCER_SHOT = create("item.lancer_shot");
    private static SoundEvent create(String name) {
        ResourceLocation location = new ResourceLocation(WrathyArmament.MODID,name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(location);
        ForgeRegistries.SOUND_EVENTS.register(location, sound);
        return sound;
    }
    public static void init(){}
}
