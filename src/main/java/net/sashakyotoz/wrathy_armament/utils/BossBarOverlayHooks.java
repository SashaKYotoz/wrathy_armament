package net.sashakyotoz.wrathy_armament.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

import java.util.Map;
import java.util.UUID;

public class BossBarOverlayHooks {
    private static final ResourceLocation BOSSBAR_SASHAKYOTOZ = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/sashakyotoz_bossbar.png");
    private static final ResourceLocation BOSSBAR_LICH_KING = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/lich_king_bossbar.png");
    private static final ResourceLocation BOSSBAR_JOHANNES = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/johannes_knight_bossbar.png");
    private static final ResourceLocation BOSSBAR_MOON_LORD = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/moon_lord_bossbar.png");

    public BossBarOverlayHooks() {
    }
    public static void render(Minecraft minecraft, Map<UUID, LerpingBossEvent> events, GuiGraphics graphics) {
        if (!events.isEmpty()) {
            int i = minecraft.getWindow().getGuiScaledWidth();
            int j = 12;
            for (LerpingBossEvent clientbossinfo : events.values()) {
                int k = i / 2 - 91;
                if (shouldDisplayFrame(clientbossinfo)) {
                    graphics.blit(getBossbarLocation(clientbossinfo), k, j - 2, 0, 0, 183, 9, 183, 9);
                }
                j += 10 + 9;
                if (j >= minecraft.getWindow().getGuiScaledHeight() / 3) {
                    break;
                }
            }
        }
    }
    private static boolean shouldDisplayFrame(LerpingBossEvent info) {
        return info.getName().contains(Component.translatable("boss.wrathy_armament.lich_king")) || info.getName().contains(Component.translatable("boss.wrathy_armament.johannes_knight"))
                || info.getName().contains(Component.translatable("boss.wrathy_armament.johannes_fountain")) || info.getName().contains(Component.translatable("boss.wrathy_armament.sashakyotoz")) ||
                info.getName().contains(Component.translatable("entity.wrathy_armament.moon_lord"));
    }
    private static ResourceLocation getBossbarLocation(LerpingBossEvent info){
        if (info.getName().contains(Component.translatable("boss.wrathy_armament.sashakyotoz")))
            return BOSSBAR_SASHAKYOTOZ;
        if (info.getName().contains(Component.translatable("boss.wrathy_armament.lich_king")))
            return BOSSBAR_LICH_KING;
        if (info.getName().contains(Component.translatable("boss.wrathy_armament.johannes_fountain"))
                || info.getName().contains(Component.translatable("boss.wrathy_armament.johannes_knight")))
            return BOSSBAR_JOHANNES;
        if (info.getName().contains(Component.translatable("entity.wrathy_armament.moon_lord")))
            return BOSSBAR_MOON_LORD;
        return new ResourceLocation("");
    }
}