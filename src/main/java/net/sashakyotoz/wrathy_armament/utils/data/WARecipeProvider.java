package net.sashakyotoz.wrathy_armament.utils.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlocks;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.data.builders.WorkbenchRecipeBuilder;

import java.util.function.Consumer;

public class WARecipeProvider extends RecipeProvider implements IConditionBuilder {
    public WARecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, WrathyArmamentBlocks.WORLDSHARD_WORKBENCH.get())
                .define('A', WrathyArmamentItems.SHARD_OF_CLEAR_MYTHRIL.get())
                .define('B', WrathyArmamentItems.SHARD_OF_ORICHALCUM.get())
                .define('C', WrathyArmamentItems.SHARD_OF_MECHANVIL.get())
                .define('D', WrathyArmamentItems.SHARD_OF_NETHERNESS.get())
                .define('H', Items.HEART_OF_THE_SEA)
                .pattern("A B")
                .pattern(" H ")
                .pattern("C D")
                .unlockedBy("has_item", has(WrathyArmamentItems.MYTHRIL_INGOT.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, WrathyArmamentItems.COPPER_SWORD.get())
                .define('A', Items.STICK)
                .define('B', Items.COPPER_INGOT)
                .pattern(" B ")
                .pattern(" B ")
                .pattern(" A ")
                .unlockedBy("has_item", has(Items.COPPER_INGOT))
                .save(recipeConsumer);
        WorkbenchRecipeBuilder.shaped(WrathyArmamentItems.BLADE_OF_CHAOS.get())
                .define('S', Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)
                .define('C', Items.CHAIN)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.BLAZE_ROD)
                .define('E', Items.NETHER_STAR)
                .define('D', Items.DEEPSLATE)
                .pattern("SCN")
                .pattern("BED")
                .pattern("BNC")
                .unlockedBy("has_item", has(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE))
                .save(recipeConsumer);
        WorkbenchRecipeBuilder.shaped(WrathyArmamentItems.ZENITH.get())
                .define('W', Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)
                .define('m', WrathyArmamentItems.MYTHRIL_INGOT.get())
                .define('C', WrathyArmamentItems.COPPER_SWORD.get())
                .define('N', Items.NETHERITE_SWORD)
                .define('D', Items.DIAMOND_SWORD)
                .define('M', WrathyArmamentItems.MEOWMERE.get())
                .define('P', WrathyArmamentItems.PHANTOM_LANCER.get())
                .define('G', Items.GOLDEN_SWORD)
                .pattern("WmC")
                .pattern("NDM")
                .pattern("PGm")
                .unlockedBy("has_item", has(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE))
                .save(recipeConsumer);
        WorkbenchRecipeBuilder.shaped(WrathyArmamentItems.MISTSPLITTER_REFORGED.get())
                .define('E', Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)
                .define('e', Items.END_ROD)
                .define('N', Items.NETHER_STAR)
                .define('H', Items.HEART_OF_THE_SEA)
                .define('P', Items.PURPUR_BLOCK)
                .define('C', Items.END_CRYSTAL)
                .pattern("EeN")
                .pattern("HPP")
                .pattern("CNe")
                .unlockedBy("has_item", has(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE))
                .save(recipeConsumer);
        WorkbenchRecipeBuilder.shaped(WrathyArmamentItems.MURASAMA.get())
                .define('S', Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)
                .define('B', Items.BLAZE_ROD)
                .define('R', Items.REDSTONE_BLOCK)
                .define('E', Items.ENDER_EYE)
                .define('N', Items.NETHERITE_INGOT)
                .define('e', Items.END_CRYSTAL)
                .define('s', Items.STICK)
                .pattern("SBR")
                .pattern("ENN")
                .pattern("eRs")
                .unlockedBy("has_item", has(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE))
                .save(recipeConsumer);
        WorkbenchRecipeBuilder.shaped(WrathyArmamentItems.HALF_ZATOICHI.get())
                .define('R', Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)
                .define('A', Items.ANCIENT_DEBRIS)
                .define('E', Items.ECHO_SHARD)
                .define('N', Items.NETHERITE_INGOT)
                .define('S', WrathyArmamentItems.SHARD_OF_MECHANVIL.get())
                .define('s', Items.STICK)
                .pattern("RSA")
                .pattern("NNN")
                .pattern("ESs")
                .unlockedBy("has_item", has(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE))
                .save(recipeConsumer);
    }
}
