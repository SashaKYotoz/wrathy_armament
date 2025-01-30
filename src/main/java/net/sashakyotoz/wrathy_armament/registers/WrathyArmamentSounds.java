package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class WrathyArmamentSounds {
    public static final SoundEvent ITEM_LANCER_SHOT = create("item.lancer_shot");
    public static final SoundEvent KATANA_SWING = create("item.katana_swing");
    public static final SoundEvent SOUL_SWING = create("item.soul_swing");
    public static final SoundEvent LIGHT_SWING = create("item.light_swing");

    public static final SoundEvent BEAM_SHOOTING = create("entity.beam_shooting");
    public static final SoundEvent MOON_LORD_HURT = create("entity.moon_lord_hurt");

    private static SoundEvent create(String name) {
        ResourceLocation location = new ResourceLocation(WrathyArmament.MODID, name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(location);
        ForgeRegistries.SOUND_EVENTS.register(location, sound);
        return sound;
    }

    public static void init() {
    }
}
