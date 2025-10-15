package net.sashakyotoz.wrathy_armament.blocks.gui.api;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.entities.recipes.WorldshardWorkbenchRecipe;
import net.sashakyotoz.wrathy_armament.blocks.gui.WorldshardWorkbenchScreen;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlocks;
import org.jetbrains.annotations.Nullable;
@SuppressWarnings("removal")
public class WorldshardWorkbenchRecipeCategory implements IRecipeCategory<WorldshardWorkbenchRecipe> {
    public final static ResourceLocation UID = WrathyArmament.createWALocation("worldshard_workbench_crafting");
    public static RecipeType<WorldshardWorkbenchRecipe> WORLDSHARD_WORKBENCH_RECIPE = new RecipeType<>(WorldshardWorkbenchRecipeCategory.UID, WorldshardWorkbenchRecipe.class);

    private final IDrawable icon;
    private final IDrawable background;

    public WorldshardWorkbenchRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(WorldshardWorkbenchScreen.BACKGROUND_LOCATION, 0, 0, 176, 197);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(WrathyArmamentBlocks.WORLDSHARD_WORKBENCH.get()));
    }

    @Override
    public RecipeType<WorldshardWorkbenchRecipe> getRecipeType() {
        return WORLDSHARD_WORKBENCH_RECIPE;
    }
    @Override
    public Component getTitle() {
        return Component.translatable("block.wrathy_armament.worldshard_workbench");
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    @Nullable
    public IDrawable getBackground() {
        return background;
    }
    //    @Override
//    public int getWidth() {
//        return 176;
//    }
//
//    @Override
//    public int getHeight() {
//        return 197;
//    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WorldshardWorkbenchRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 53, 42).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 53, 13).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 77, 17).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 81, 42).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 77, 67).addIngredients(recipe.getIngredients().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 53, 71).addIngredients(recipe.getIngredients().get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 67).addIngredients(recipe.getIngredients().get(6));
        builder.addSlot(RecipeIngredientRole.INPUT, 25, 42).addIngredients(recipe.getIngredients().get(7));
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 17).addIngredients(recipe.getIngredients().get(8));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 146, 42).addItemStack(recipe.getResultItem(null));
    }
}