package net.sashakyotoz.wrathy_armament;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue SHOW_LANCER_ON_PLAYER_BACK = BUILDER
            .comment("Determine if phantom lancer will be shown on player back")
            .define("Show lancer", true);
    public static final ForgeConfigSpec.BooleanValue SHOW_IF_ARMOR_EQUIP = BUILDER
            .comment("Determine if phantom lancer will be shown if armor is equipped")
            .define("Show lancer if armor is equipped", true);
    public static final ForgeConfigSpec.IntValue TIME_SINCE_REST_TO_SPAWN_SASHAKYOTOZ = BUILDER
            .comment("Determine needed time since rest to spawn SashaKYotoz")
            .defineInRange("time to spawn sashakyotoz", 96000,4000,9999999);
    public static final ForgeConfigSpec.IntValue TIME_TO_SPAWN_GUIDE = BUILDER
            .comment("Determine needed time needed to spawn Guide")
            .defineInRange("time to spawn guide", 30000,4000,9999999);
    static final ForgeConfigSpec SPEC = BUILDER.build();
}
