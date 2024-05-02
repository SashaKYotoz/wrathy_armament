package net.sashakyotoz.wrathy_armament.blocks.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.MythrilAnvil;

import java.util.HashMap;

public class MythrilAnvilScreen extends AbstractContainerScreen<MythrilAnvilMenu> {
    private final static HashMap<String, Object> guistate = MythrilAnvilMenu.guiState;
    private final Level level;
    private final int x, y, z;
    private final Player player;

    public MythrilAnvilScreen(MythrilAnvilMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.level = container.level;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.player = container.player;
        this.imageWidth = 256;
        this.imageHeight = 256;
    }
    private static final ResourceLocation UNFIRED_STAR = new ResourceLocation(WrathyArmament.MODID, "textures/gui/blocks/icons/unfired_star.png");
    private static final ResourceLocation FIRED_STAR = new ResourceLocation(WrathyArmament.MODID, "textures/gui/blocks/icons/fired_star.png");
    private static final ResourceLocation MYTHRIL_ANVIL = new ResourceLocation(WrathyArmament.MODID,"textures/gui/blocks/mythril_anvil_gui.png");

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
        guiGraphics.blit(MYTHRIL_ANVIL, this.leftPos + 40, this.topPos + 32, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        if (this.level.getBlockState(new BlockPos(x,y,z)).getValue(MythrilAnvil.STARRED))
            guiGraphics.blit(FIRED_STAR, this.leftPos + 52, this.topPos + 75, 0, 0, 15, 15, 15, 15);
        else
            guiGraphics.blit(UNFIRED_STAR, this.leftPos + 52, this.topPos + 75, 0, 0, 15, 15, 15, 15);
        RenderSystem.disableBlend();
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
    public void containerTick() {
        super.containerTick();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("block.wrathy_armament.mythril_anvil"), 48, 40, 8453920, false);
    }


    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void init() {
        super.init();
    }
}
