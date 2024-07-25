package net.sashakyotoz.wrathy_armament.blocks.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.AniTexLib;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

import java.util.ArrayList;

public class WorldshardWorkbenchScreen extends AbstractContainerScreen<WorldshardWorkbenchMenu> {
    private final Level level;
    private final Player player;
    private final ArrayList<WorkbenchSparkle> sparkles = new ArrayList<>();

    public WorldshardWorkbenchScreen(WorldshardWorkbenchMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.level = inventory.player.level();
        this.player = inventory.player;
        this.imageWidth = 176;
        this.imageHeight = 197;
    }
    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(WrathyArmament.MODID,"textures/gui/blocks/worldshard_workbench_bg.png");

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos + 16, 0, 0, this.imageWidth, this.imageHeight);
        renderProgressArrow(guiGraphics);
        if (this.getMenu().blockEntity.getRecipe() != null){
            RandomSource random = this.level.random;
            if (random.nextInt(5) == 0) {
                sparkles.add(new WorkbenchSparkle(this.player,this.leftPos, this.topPos, 0.5 + random.nextDouble(), 0.5 + random.nextDouble()));
            }
        }
        RenderSystem.disableBlend();
        for (WorkbenchSparkle sparkle : this.sparkles) {
            if (sparkle.tickCount < 1000) {
                sparkle.render(guiGraphics);
            }
        }
    }
    private void renderProgressArrow(GuiGraphics graphics) {
        if(getMenu().isCrafting()) {
            graphics.blit(BACKGROUND_LOCATION, this.leftPos+108, this.topPos + 57, 175, 41, getMenu().getScaledProgress(), 16);
        }
    }
    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("block.wrathy_armament.worldshard_workbench"), 62, 124, 0xff0a0, false);
    }
    public static class WorkbenchSparkle {
        private final int centerX;
        private final int centerY;
        private final double speedX;
        private final double speedY;

        public float scale;
        public int tickCount;
        public boolean isReverse;

        public WorkbenchSparkle(Player player,int centerX, int centerY, double speedX, double speedY) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.speedX = speedX;
            this.speedY = speedY;
            this.scale = 1F + player.getRandom().nextFloat() * 0.5F;
            this.isReverse = player.getRandom().nextBoolean();
        }

        public void render(GuiGraphics pGuiGraphics) {
            tickCount++;
            RenderSystem.enableBlend();
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.5F + Mth.sin(tickCount * 0.02F) + 0.5F);
            pGuiGraphics.pose().pushPose();
            double xOff = centerX + (tickCount * speedX);
            double yOff = centerY + (tickCount * speedY);
            pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(tickCount * (isReverse ? -0.2F : 0.2F)));
            pGuiGraphics.pose().translate(xOff, yOff, 0);
            pGuiGraphics.pose().scale(scale, scale, scale);
            pGuiGraphics.blit(new ResourceLocation(AniTexLib.MODID,"textures/particle/sparkle.png"), (int) xOff, (int) yOff, 0, 0, 16, 16);
            pGuiGraphics.pose().popPose();
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    }
}
