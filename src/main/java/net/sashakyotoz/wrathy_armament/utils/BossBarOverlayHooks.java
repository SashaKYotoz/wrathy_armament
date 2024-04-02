package net.sashakyotoz.wrathy_armament.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

import java.util.Map;
import java.util.UUID;

public class BossBarOverlayHooks {
    private static final ResourceLocation BOSSBAR_SASHAKYOTOZ = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/sashakyotoz_bossbar.png");
    private static final ResourceLocation BOSSBAR_LICH_KING = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/lich_king_bossbar.png");
    private static final ResourceLocation BOSSBAR_JOHANNES = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/johannes_knight_bossbar.png");

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
        return info.getName().getString().contains("SashaKYotoz") || info.getName().getString().contains("Lich King") || info.getName().getString().contains("Johannes");
    }
    private static ResourceLocation getBossbarLocation(LerpingBossEvent info){
        if (info.getName().getString().contains("SashaKYotoz"))
            return BOSSBAR_SASHAKYOTOZ;
        if (info.getName().getString().contains("Lich King"))
            return BOSSBAR_LICH_KING;
        if (info.getName().getString().contains("Johannes"))
            return BOSSBAR_JOHANNES;
        return new ResourceLocation("");
    }

}
