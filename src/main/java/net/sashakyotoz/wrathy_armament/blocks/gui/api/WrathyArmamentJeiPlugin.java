package net.sashakyotoz.wrathy_armament.blocks.gui.api;


import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.entities.recipes.WorldshardWorkbenchRecipe;
import net.sashakyotoz.wrathy_armament.blocks.gui.WorldshardWorkbenchScreen;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlocks;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

import java.util.List;

@JeiPlugin
public class WrathyArmamentJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return WrathyArmament.createWALocation("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WorldshardWorkbenchRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<WorldshardWorkbenchRecipe> recipes = recipeManager.getAllRecipesFor(WorldshardWorkbenchRecipe.Type.INSTANCE);
        registration.addRecipes(WorldshardWorkbenchRecipeCategory.WORLDSHARD_WORKBENCH_RECIPE, recipes);

        //Ingredients info

        registration.addIngredientInfo(WrathyArmamentItems.PHANTOM_LANCER.get(), Component.translatable("jei.wrathy_armament.phantom_lancer"));
        registration.addIngredientInfo(WrathyArmamentItems.MIRROR_SWORD.get(), Component.translatable("jei.wrathy_armament.mirrow_sword"));
        registration.addIngredientInfo(WrathyArmamentItems.MASTER_SWORD.get(), Component.translatable("jei.wrathy_armament.master_sword"));
        registration.addIngredientInfo(WrathyArmamentItems.JOHANNES_SWORD.get(), Component.translatable("jei.wrathy_armament.johannes_sword"));
        registration.addIngredientInfo(WrathyArmamentItems.FROSTMOURNE.get(), Component.translatable("jei.wrathy_armament.frostmourne"));
        registration.addIngredientInfo(WrathyArmamentItems.MURASAMA.get(), Component.translatable("jei.wrathy_armament.murasama"));
        registration.addIngredientInfo(WrathyArmamentItems.MISTSPLITTER_REFORGED.get(), Component.translatable("jei.wrathy_armament.mistsplitter_reforged"));
        registration.addIngredientInfo(WrathyArmamentItems.HALF_ZATOICHI.get(), Component.translatable("jei.wrathy_armament.half_zatoichi"));
        registration.addIngredientInfo(WrathyArmamentItems.BLADE_OF_CHAOS.get(), Component.translatable("jei.wrathy_armament.blade_of_chaos"));
        registration.addIngredientInfo(WrathyArmamentItems.BLACKRAZOR.get(), Component.translatable("jei.wrathy_armament.blackrazor"));
        registration.addIngredientInfo(WrathyArmamentItems.LUNAR_VOODOO_DOLL.get(), Component.translatable("jei.wrathy_armament.lunar_voodoo_doll"));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(WorldshardWorkbenchScreen.class, 108, 64, 32, 32,
                WorldshardWorkbenchRecipeCategory.WORLDSHARD_WORKBENCH_RECIPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(WrathyArmamentBlocks.WORLDSHARD_WORKBENCH.get()), WorldshardWorkbenchRecipeCategory.WORLDSHARD_WORKBENCH_RECIPE);
    }
}