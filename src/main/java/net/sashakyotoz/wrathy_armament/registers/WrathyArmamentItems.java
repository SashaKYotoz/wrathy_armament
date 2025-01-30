package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.items.*;
import net.sashakyotoz.wrathy_armament.items.swords.*;
import net.sashakyotoz.wrathy_armament.utils.ModRarity;

public class WrathyArmamentItems {
    public static final ChatFormatting RED_DESCRIPTION_FORMAT = ChatFormatting.DARK_RED;
    public static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    public static final ChatFormatting AQUA_TITLE_FORMAT = ChatFormatting.DARK_AQUA;
    public static final ChatFormatting DARK_GREY_TITLE_FORMAT = ChatFormatting.DARK_GRAY;
    public static final ChatFormatting PURPLE_TITLE_FORMAT = ChatFormatting.DARK_PURPLE;
    public static final ChatFormatting GOLD_TITLE_FORMAT = ChatFormatting.GOLD;
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WrathyArmament.MODID);
    public static final RegistryObject<Item> PHANTOM_LANCER = ITEMS.register("phantom_lancer", () -> new PhantomLancer(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(2400).fireResistant()));
    public static final RegistryObject<Item> ZENITH = ITEMS.register("zenith",()-> new Zenith(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> JOHANNES_SWORD = ITEMS.register("johannes_sword",()-> new JohannesSword(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> MASTER_SWORD = ITEMS.register("master_sword",()-> new MasterSword(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> BLADE_OF_CHAOS = ITEMS.register("blade_of_chaos",()-> new BladeOfChaos(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> FROSTMOURNE = ITEMS.register("frostmourne",()-> new Frostmourne(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> MURASAMA = ITEMS.register("murasama",()-> new Murasama(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> MISTSPLITTER_REFORGED = ITEMS.register("mistsplitter_reforged",()-> new MistsplitterReforged(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> HALF_ZATOICHI = ITEMS.register("half_zatoichi",()-> new HalfZatoichi(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> MIRROR_SWORD = ITEMS.register("mirror_sword",()-> new MirrorSword(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    public static final RegistryObject<Item> BLACKRAZOR = ITEMS.register("blackrazor",()-> new Blackrazor(new Item.Properties().rarity(ModRarity.LEGENDARY_RARITY).stacksTo(1).durability(3200).fireResistant()));
    //materials
    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register("copper_sword",()->new ItemWithDescription(new Item.Properties().stacksTo(1),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> MEOWMERE = ITEMS.register("meowmere",()->new ItemWithDescription(new Item.Properties().stacksTo(1),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> MYTHRIL_INGOT = ITEMS.register("mythril_ingot",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> SHARD_OF_CLEAR_MYTHRIL = ITEMS.register("shard_of_clear_mythril",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> SHARD_OF_ORICHALCUM = ITEMS.register("shard_of_orichalcum",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> SHARD_OF_MECHANVIL = ITEMS.register("shard_of_mechanvil",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
    public static final RegistryObject<Item> SHARD_OF_NETHERNESS = ITEMS.register("shard_of_netherness",()->new ItemWithDescription(new Item.Properties(),"item.wrathy_armament.material"));
}