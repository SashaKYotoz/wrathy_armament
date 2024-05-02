package net.sashakyotoz.wrathy_armament.utils.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class WrathyArmamentRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public WrathyArmamentRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {

    }
}
