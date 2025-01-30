package net.sashakyotoz.wrathy_armament;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static class Common {
        public static final ForgeConfigSpec.IntValue TIME_SINCE_REST_TO_SPAWN_SASHAKYOTOZ;
        public static final ForgeConfigSpec.IntValue TIME_TO_SPAWN_GUIDE;
        public static final ForgeConfigSpec.BooleanValue CAN_SHARPNESS_BE_APPLIED;
        public static final ForgeConfigSpec.DoubleValue PHANTOM_LANCER_DAMAGE_MUL;
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;

        static {
            BUILDER.push("Spawns Config");
            TIME_SINCE_REST_TO_SPAWN_SASHAKYOTOZ = BUILDER
                    .comment("Determine needed time since rest to spawn SashaKYotoz")
                    .defineInRange("time to spawn sashakyotoz", 96000, 4000, Integer.MAX_VALUE);
            TIME_TO_SPAWN_GUIDE = BUILDER
                    .comment("Determine needed time needed to spawn Guide")
                    .defineInRange("time to spawn guide", 30000, 4000, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Weapons Config");
            CAN_SHARPNESS_BE_APPLIED = BUILDER
                    .comment("Determine if sharpness can be applied on mod's weapons")
                    .define("Can sharpness be applied", true);
            PHANTOM_LANCER_DAMAGE_MUL = BUILDER
                    .comment("Determine multiplication factor of phantom lancer ability")
                    .defineInRange("Damage multiplication factor", 1, 0.1f, 10f);
            BUILDER.pop();
            SPEC = BUILDER.build();
        }
    }

    public static class Client {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec.BooleanValue SHOW_WEAPON_ON_PLAYER_BACK;
        public static final ForgeConfigSpec.BooleanValue SHOW_WEAPON_IF_ARMOR_EQUIP;
        public static final ForgeConfigSpec.BooleanValue SHOW_ALL_WEAPONS_ON_PLAYER_BACK;
        public static final ForgeConfigSpec.BooleanValue CAN_EXTRA_CHARS_BE_SHOWN;
        public static final ForgeConfigSpec SPEC;

        static {
            BUILDER.push("Decorations Config");
            SHOW_WEAPON_ON_PLAYER_BACK = BUILDER
                    .comment("Determine if mod's weapon in player's inventory will be shown on player's back")
                    .define("Show weapon", true);
            SHOW_WEAPON_IF_ARMOR_EQUIP = BUILDER
                    .comment("Determine if mod's weapon will be shown if armor is equipped")
                    .define("Show mod's weapon if armor is equipped", true);
            SHOW_ALL_WEAPONS_ON_PLAYER_BACK = BUILDER
                    .comment("Determine if all mod's weapons in player's inventory will be shown on player's back")
                    .define("Show all weapons", false);
            BUILDER.pop();
            BUILDER.push("Weapons Config");
            CAN_EXTRA_CHARS_BE_SHOWN = BUILDER
                    .comment("Determine if show extra char in gui that shows soul count, ability chosen and etc")
                    .define("Can extra chars be shown", false);
            BUILDER.pop();
            SPEC = BUILDER.build();
        }
    }
}