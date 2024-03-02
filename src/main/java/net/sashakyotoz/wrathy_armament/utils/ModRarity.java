package net.sashakyotoz.wrathy_armament.utils;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;

import java.util.Optional;

public class ModRarity {
    public static Rarity LEGENDARY_RARITY = Rarity.create("legendary", style -> Styles.LEGENDARY);
    static class Styles{
        public static Style LEGENDARY = Style.create(
                Optional.of(TextColor.fromRgb(16761695)),
                Optional.of(Boolean.FALSE),
                Optional.of(Boolean.TRUE),
                Optional.of(Boolean.FALSE),
                Optional.of(Boolean.FALSE),
                Optional.of(Boolean.FALSE),
                Optional.of("legendary"),
                Optional.of(Style.DEFAULT_FONT));
    }
}
