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
    }
}
