package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class WrathyArmamentTags {
    public static class Structures {
        public static TagKey<Structure> VISIBLE_FOR_MASTER_SWORD = tag("visible_for_master_sword");

        private static TagKey<Structure> tag(String name) {
            return TagKey.create(Registries.STRUCTURE, new ResourceLocation(WrathyArmament.MODID, name));
        }
    }
}