package net.sashakyotoz.wrathy_armament.utils;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BossBarOverlayHooks {
    private static final Set<BossEvent.BossBarColor> COLORED_INFO_FRAMES;
    private static final ResourceLocation BOSSBAR_SASHAKYOTOZ = new ResourceLocation(WrathyArmament.MODID,"textures/gui/bossbars/sashakyotoz_bossbar.png");

    public BossBarOverlayHooks() {
    }
    public static void render(Minecraft minecraft, Map<UUID, LerpingBossEvent> events, GuiGraphics graphics) {
        if (!events.isEmpty()) {
            int i = minecraft.getWindow().getGuiScaledWidth();
            int j = 12;
            for (LerpingBossEvent clientbossinfo : events.values()) {
                int k = i / 2 - 91;
                if (shouldDisplayFrame(clientbossinfo)) {
                    graphics.blit(BOSSBAR_SASHAKYOTOZ, k, j - 2, 0, 0, 183, 9, 183, 9);
                }
                j += 10 + 9;
                if (j >= minecraft.getWindow().getGuiScaledHeight() / 3) {
                    break;
                }
            }
        }

    }
    private static boolean shouldDisplayFrame(LerpingBossEvent info) {
        return info.getName().getString().contains("SashaKYotoz") && COLORED_INFO_FRAMES.contains(info.getColor());
    }

    static {
        COLORED_INFO_FRAMES = Sets.newHashSet(BossEvent.BossBarColor.BLUE, BossEvent.BossBarColor.GREEN, BossEvent.BossBarColor.RED, BossEvent.BossBarColor.PINK);
    }
}
