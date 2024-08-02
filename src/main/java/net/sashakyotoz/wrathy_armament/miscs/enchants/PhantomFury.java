package net.sashakyotoz.wrathy_armament.miscs.enchants;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

import java.util.Set;

public class PhantomFury extends Enchantment {
    public PhantomFury(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(WrathyArmamentItems.PHANTOM_LANCER.get()) || stack.is(WrathyArmamentItems.ZENITH.get()) || stack.is(WrathyArmamentItems.FROSTMOURNE.get());
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public Component getFullname(int i) {
        MutableComponent mutablecomponent = Component.translatable(this.getDescriptionId());
        mutablecomponent.withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT);
        if (i != 1 || this.getMaxLevel() != 1) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + i));
        }
        return mutablecomponent;
    }

    @Override
    public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories) {
        return true;
    }
}
