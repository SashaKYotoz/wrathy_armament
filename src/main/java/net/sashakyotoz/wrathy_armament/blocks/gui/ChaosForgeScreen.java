package net.sashakyotoz.wrathy_armament.blocks.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.ChaosForge;

public class ChaosForgeScreen extends ItemCombinerScreen<ChaosForgeMenu> {
    private static final ResourceLocation CHAOS_FORGE = new ResourceLocation(WrathyArmament.MODID, "textures/gui/blocks/chaos_forge_gui.png");
    private static final ResourceLocation ONE_OVER_THREE_FIRED = new ResourceLocation(WrathyArmament.MODID, "textures/gui/blocks/icons/one_over_three_fire.png");
    private static final ResourceLocation TWO_OVER_THREE_FIRED = new ResourceLocation(WrathyArmament.MODID, "textures/gui/blocks/icons/two_over_three_fire.png");
    private static final ResourceLocation FULLY_FIRED = new ResourceLocation(WrathyArmament.MODID, "textures/gui/blocks/icons/full_fired.png");
    private static final Component TOO_EXPENSIVE_TEXT = Component.translatable("container.repair.expensive");
    private final Level level;
    private final BlockPos pos;
    private EditBox name;
    private final Player player;

    public ChaosForgeScreen(ChaosForgeMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component, CHAOS_FORGE);
        this.player = inventory.player;
        this.level = menu.level;
        this.pos = menu.pos;
        this.titleLabelX = 60;
    }

    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 62, j + 24, 103, 12, Component.translatable("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        this.name.setEditable(false);
    }

    public void resize(Minecraft minecraft, int p_97887_, int p_97888_) {
        String s = this.name.getValue();
        this.init(minecraft, p_97887_, p_97888_);
        this.name.setValue(s);
    }

    public boolean keyPressed(int p_97878_, int p_97879_, int p_97880_) {
        if (p_97878_ == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.name.keyPressed(p_97878_, p_97879_, p_97880_) || this.name.canConsumeInput() || super.keyPressed(p_97878_, p_97879_, p_97880_);
    }

    private void onNameChanged(String name) {
        Slot slot = this.menu.getSlot(0);
        if (slot.hasItem()) {
            String s = name;
            if (!slot.getItem().hasCustomHoverName() && name.equals(slot.getItem().getHoverName().getString())) {
                s = "";
            }

            if (this.menu.setItemName(s)) {
                this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
            }

        }
    }

    protected void renderLabels(GuiGraphics graphics, int p_282417_, int p_283022_) {
        super.renderLabels(graphics, p_282417_, p_283022_);
        int i = this.menu.getCost();
        if (i > 0) {
            int j = 8453920;
            Component component;
            if (i >= 40 && !this.minecraft.player.getAbilities().instabuild) {
                component = TOO_EXPENSIVE_TEXT;
                j = 16736352;
            } else if (!this.menu.getSlot(2).hasItem()) {
                component = null;
            } else {
                component = Component.translatable("container.repair.cost", i);
                if (!this.menu.getSlot(2).mayPickup(this.player)) {
                    j = 16736352;
                }
            }

            if (component != null) {
                int k = this.imageWidth - 8 - this.font.width(component) - 2;
                graphics.fill(k - 2, 67, this.imageWidth - 8, 79, 1325400064);
                graphics.drawString(this.font, component, k, 69, j);
            }
        }

    }

    @Override
    public void renderBg(GuiGraphics graphics, float p_283412_, int p_282871_, int p_281306_) {
        super.renderBg(graphics, p_283412_, p_282871_, p_281306_);
        graphics.blit(CHAOS_FORGE, this.leftPos + 59, this.topPos + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
        switch (this.level.getBlockState(pos).getValue(ChaosForge.FIRING)){
            case 1 -> graphics.blit(ONE_OVER_THREE_FIRED, this.leftPos + 107, this.topPos + 60, 0, 0, 20, 18,20,18);
            case 2 -> graphics.blit(TWO_OVER_THREE_FIRED, this.leftPos + 107, this.topPos + 60, 0, 0, 20, 18,20,18);
            case 3 -> graphics.blit(FULLY_FIRED, this.leftPos + 107, this.topPos + 60, 0, 0, 20, 18,20,18);
        }
    }

    @Override
    public void renderFg(GuiGraphics graphics, int p_283263_, int p_281526_, float p_282957_) {
        this.name.render(graphics, p_283263_, p_281526_, p_282957_);
    }

    @Override
    public void renderErrorIcon(GuiGraphics graphics, int p_283237_, int p_282237_) {
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            graphics.blit(CHAOS_FORGE, p_283237_ + 99, p_282237_ + 45, this.imageWidth, 0, 28, 21);
        }

    }

    public void slotChanged(AbstractContainerMenu menu, int p_97883_, ItemStack stack) {
        if (p_97883_ == 0) {
            this.name.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
            this.name.setEditable(!stack.isEmpty());
            this.setFocused(this.name);
        }
    }
}
