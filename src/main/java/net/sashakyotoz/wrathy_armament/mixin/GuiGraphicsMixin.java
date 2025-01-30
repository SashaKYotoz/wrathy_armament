package net.sashakyotoz.wrathy_armament.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.Config;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow
    public abstract int drawString(Font pFont, @Nullable String pText, int pX, int pY, int pColor);

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
    private void renderDecorations(Font pFont, ItemStack pStack, int pX, int pY, String pText, CallbackInfo ci) {
        if (pStack.getItem() instanceof SwordLikeItem && Config.Client.CAN_EXTRA_CHARS_BE_SHOWN.get()) {
            if (pStack.is(WrathyArmamentItems.BLADE_OF_CHAOS.get())) {
                switch (pStack.getOrCreateTag().getInt("StateIndex")) {
                    case 1 -> this.drawString(pFont, "C", pX + 19 - 2 - pFont.width("C"), pY + 6 + 3, 14188339);
                    case 2 -> this.drawString(pFont, "W", pX + 19 - 2 - pFont.width("W"), pY + 6 + 3, 14188339);
                    default -> this.drawString(pFont, "N", pX + 19 - 2 - pFont.width("N"), pY + 6 + 3, 14188339);
                }
            }
            if (pStack.is(WrathyArmamentItems.MISTSPLITTER_REFORGED.get())) {
                switch (pStack.getOrCreateTag().getInt("StateIndex")) {
                    case 1 -> this.drawString(pFont, "W", pX + 19 - 2 - pFont.width("W"), pY + 6 + 3, 15892389);
                    case 2 -> this.drawString(pFont, "E", pX + 19 - 2 - pFont.width("E"), pY + 6 + 3, 15892389);
                    case 3 -> this.drawString(pFont, "El", pX + 19 - 2 - pFont.width("El"), pY + 6 + 3, 15892389);
                    case 4 -> this.drawString(pFont, "A", pX + 19 - 2 - pFont.width("A"), pY + 6 + 3, 15892389);
                    default -> this.drawString(pFont, "F", pX + 19 - 2 - pFont.width("F"), pY + 6 + 3, 15892389);
                }
            }
            if (pStack.is(WrathyArmamentItems.FROSTMOURNE.get()))
                this.drawString(pFont, "" + pStack.getOrCreateTag().getInt("charge"), pX + 19 - 2 - pFont.width("S"), pY + 6 + 3, 3361970);
            if (pStack.is(WrathyArmamentItems.BLACKRAZOR.get()))
                this.drawString(pFont, "" + pStack.getOrCreateTag().getInt("charge"), pX + 19 - 2 - pFont.width("" + pStack.getOrCreateTag().getInt("charge")), pY + 6 + 3, 8339378);
            if (pStack.is(WrathyArmamentItems.HALF_ZATOICHI.get()))
                this.drawString(pFont, "" + pStack.getOrCreateTag().getInt("charge"), pX + 19 - 2 - pFont.width("S"), pY + 6 + 3, 15066419);
            if (pStack.is(WrathyArmamentItems.MURASAMA.get()))
                this.drawString(pFont, "" + (int) (pStack.getOrCreateTag().getFloat("RestSpeed") * 10), pX + 19 - 2 - pFont.width("R"), pY + 6 + 3, 10040115);
            if (pStack.is(WrathyArmamentItems.MIRROR_SWORD.get()))
                this.drawString(pFont, "" + (int) pStack.getOrCreateTag().getFloat("damageKeep"), pX + 19 - 2 - pFont.width("" + (int) pStack.getOrCreateTag().getFloat("damageKeep")), pY + 6 + 3, 5000268);
        }
    }
}