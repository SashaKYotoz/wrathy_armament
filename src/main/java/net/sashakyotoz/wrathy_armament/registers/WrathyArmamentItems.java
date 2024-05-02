package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.items.*;
import net.sashakyotoz.wrathy_armament.utils.ModRarity;

public class WrathyArmamentItems {
    public static final ChatFormatting RED_DESCRIPTION_FORMAT = ChatFormatting.DARK_RED;
    public static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    public static final ChatFormatting AQUA_TITLE_FORMAT = ChatFormatting.DARK_AQUA;
    public static final ChatFormatting DARK_GREY_TITLE_FORMAT = ChatFormatting.DARK_GRAY;
    public static final ChatFormatting PURPLE_TITLE_FORMAT = ChatFormatting.DARK_PURPLE;
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WrathyArmament.MODID);
    public static final RegistryObject<Item> PHANTOM_LANCER = ITEMS.register("phantom_lancer", () -> new PhantomLancer(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(2400).fireResistant()));
    public static final RegistryObject<Item> ZENITH = ITEMS.register("zenith",()-> new Zenith(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> JOHANNES_SWORD = ITEMS.register("johannes_sword",()-> new JohannesSword(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> MASTER_SWORD = ITEMS.register("master_sword",()-> new MasterSword(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> BLADE_OF_CHAOS = ITEMS.register("blade_of_chaos",()-> new BladeOfChaos(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> FROSTMOURNE = ITEMS.register("frostmourne",()-> new Frostmourne(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> MURASAMA = ITEMS.register("murasama",()-> new Murasama(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> MISTSPLITTER_REFORGED = ITEMS.register("mistsplitter_reforged",()-> new MistsplitterReforged(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> HALF_ZATOICHI = ITEMS.register("half_zatoichi",()-> new HalfZatoichi(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).fireResistant().stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register("copper_sword",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> MEOWMERE = ITEMS.register("meowmere",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> MYTHRIL_INGOT = ITEMS.register("mythril_ingot",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
}