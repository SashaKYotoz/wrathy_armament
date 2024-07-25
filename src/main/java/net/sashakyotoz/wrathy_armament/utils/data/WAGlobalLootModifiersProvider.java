package net.sashakyotoz.wrathy_armament.utils.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.data.loot.AddItemModifier;
import net.sashakyotoz.wrathy_armament.utils.data.loot.AddSusSandItemModifier;

public class WAGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public WAGlobalLootModifiersProvider(PackOutput output) {
        super(output, WrathyArmament.MODID);
    }

    @Override
    protected void start() {
        add("shard_of_orichalcum",new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY).build() }, WrathyArmamentItems.SHARD_OF_ORICHALCUM.get()));
        add("shard_of_mechanvil",new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE).build() }, WrathyArmamentItems.SHARD_OF_MECHANVIL.get()));
        add("shard_of_clear_mythril",new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(BuiltInLootTables.JUNGLE_TEMPLE).build() }, WrathyArmamentItems.SHARD_OF_CLEAR_MYTHRIL.get()));
        add("shard_of_netherness",new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(BuiltInLootTables.NETHER_BRIDGE).build() }, WrathyArmamentItems.SHARD_OF_NETHERNESS.get()));
    }
}
