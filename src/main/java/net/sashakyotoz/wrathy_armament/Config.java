package net.sashakyotoz.wrathy_armament;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WrathyArmament.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue SHOW_LANCER_ON_PLAYER_BACK = BUILDER
            .comment("Determine if phantom lancer will be shown on player back")
            .define("Show lancer", true);
    public static final ForgeConfigSpec.BooleanValue SHOW_IF_ARMOR_EQUIP = BUILDER
            .comment("Determine if phantom lancer will be shown if armor is equipped")
            .define("Show lancer if armor is equipped", true);
    static final ForgeConfigSpec SPEC = BUILDER.build();
}
