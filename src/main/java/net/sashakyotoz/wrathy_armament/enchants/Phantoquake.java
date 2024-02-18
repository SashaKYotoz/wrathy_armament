package net.sashakyotoz.wrathy_armament.enchants;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentItems;

import java.util.Set;

public class Phantoquake extends Enchantment {
    public Phantoquake(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }    @Override
    public boolean canEnchant(ItemStack p_44689_) {
        return p_44689_.is(WrathyArmamentItems.PHANTOM_LANCER.get());
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public Component getFullname(int p_44701_) {
        return Component.translatable("enchantment.wrathy_armament.phantoquake").withStyle(ChatFormatting.GOLD);
    }

    @Override
    public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories) {
        return true;
    }
}
