package net.sashakyotoz.wrathy_armament.blocks.blockentities.recipes;

import net.minecraft.world.item.Items;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.WorldshardWorkbenchBlockEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

import java.util.List;

public class WorldshardWorkbenchRecipes {
  public static void addAllRecipes(){
      WorldshardWorkbenchBlockEntity.addRecipe("zenith_recipe", List.of(
              Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE.getDefaultInstance(),
              WrathyArmamentItems.MYTHRIL_INGOT.get().getDefaultInstance(),
              WrathyArmamentItems.COPPER_SWORD.get().getDefaultInstance(),
              Items.NETHERITE_SWORD.getDefaultInstance(),
              Items.DIAMOND_SWORD.getDefaultInstance(),
              WrathyArmamentItems.MEOWMERE.get().getDefaultInstance(),
              WrathyArmamentItems.PHANTOM_LANCER.get().getDefaultInstance(),
              Items.GOLDEN_SWORD.getDefaultInstance(),
              WrathyArmamentItems.MYTHRIL_INGOT.get().getDefaultInstance(),
              WrathyArmamentItems.ZENITH.get().getDefaultInstance()
      ));
      WorldshardWorkbenchBlockEntity.addRecipe("blade_of_chaos_recipe", List.of(
              Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE.getDefaultInstance(),
              Items.CHAIN.getDefaultInstance(),
              Items.NETHERITE_INGOT.getDefaultInstance(),
              Items.BLAZE_ROD.getDefaultInstance(),
              Items.NETHER_STAR.getDefaultInstance(),
              Items.DEEPSLATE.getDefaultInstance(),
              Items.BLAZE_ROD.getDefaultInstance(),
              Items.NETHERITE_INGOT.getDefaultInstance(),
              Items.CHAIN.getDefaultInstance(),
              WrathyArmamentItems.BLADE_OF_CHAOS.get().getDefaultInstance()
      ));
      WorldshardWorkbenchBlockEntity.addRecipe("mistsplitter_recipe",List.of(
              Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE.getDefaultInstance(),
              Items.END_ROD.getDefaultInstance(),
              Items.NETHER_STAR.getDefaultInstance(),
              Items.HEART_OF_THE_SEA.getDefaultInstance(),
              Items.PURPUR_BLOCK.getDefaultInstance(),
              Items.PURPUR_BLOCK.getDefaultInstance(),
              Items.END_CRYSTAL.getDefaultInstance(),
              Items.NETHER_STAR.getDefaultInstance(),
              Items.END_ROD.getDefaultInstance(),
              WrathyArmamentItems.MISTSPLITTER_REFORGED.get().getDefaultInstance()
      ));
      WorldshardWorkbenchBlockEntity.addRecipe("murasama_recipe",List.of(
              Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE.getDefaultInstance(),
              Items.BLAZE_ROD.getDefaultInstance(),
              Items.REDSTONE_BLOCK.getDefaultInstance(),
              Items.ENDER_EYE.getDefaultInstance(),
              Items.NETHERITE_INGOT.getDefaultInstance(),
              Items.NETHERITE_INGOT.getDefaultInstance(),
              Items.END_CRYSTAL.getDefaultInstance(),
              Items.REDSTONE_BLOCK.getDefaultInstance(),
              Items.STICK.getDefaultInstance(),
              WrathyArmamentItems.MURASAMA.get().getDefaultInstance()
      ));
      WorldshardWorkbenchBlockEntity.addRecipe("zatoichi_recipe",List.of(
              Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE.getDefaultInstance(),
              Items.STICK.getDefaultInstance(),
              Items.ANCIENT_DEBRIS.getDefaultInstance(),
              Items.NETHERITE_INGOT.getDefaultInstance(),
              Items.NETHERITE_INGOT.getDefaultInstance(),
              Items.NETHERITE_INGOT.getDefaultInstance(),
              Items.ECHO_SHARD.getDefaultInstance(),
              WrathyArmamentItems.SHARD_OF_MECHANVIL.get().getDefaultInstance(),
              Items.STICK.getDefaultInstance(),
              WrathyArmamentItems.HALF_ZATOICHI.get().getDefaultInstance()
      ));
  }
}