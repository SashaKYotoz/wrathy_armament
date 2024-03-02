package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.enchants.NightmareJumping;
import net.sashakyotoz.wrathy_armament.enchants.PhantomFury;
import net.sashakyotoz.wrathy_armament.enchants.Phantoquake;

public class WrathyArmamentEnchants {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, WrathyArmament.MODID);
    public static final RegistryObject<Enchantment> PHANTOM_FURY = ENCHANTMENTS.register("phantom_fury",()-> new PhantomFury(Enchantment.Rarity.RARE,EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> NIGHTMARE_JUMPING = ENCHANTMENTS.register("nightmare_jumping",()-> new NightmareJumping(Enchantment.Rarity.RARE,EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> PHANTOQUAKE = ENCHANTMENTS.register("phantoquake",()-> new Phantoquake(Enchantment.Rarity.RARE,EnchantmentCategory.WEAPON, EquipmentSlot.values()));
}
