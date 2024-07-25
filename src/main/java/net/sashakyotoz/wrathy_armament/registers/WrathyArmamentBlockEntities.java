package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.WorldshardWorkbenchBlockEntity;

public class WrathyArmamentBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<BlockEntityType<WorldshardWorkbenchBlockEntity>> WORLDSHARD_WORKBENCH =
            BLOCK_ENTITIES.register("worldshard_workbench", () ->
                    BlockEntityType.Builder.of(WorldshardWorkbenchBlockEntity::new,
                            WrathyArmamentBlocks.WORLDSHARD_WORKBENCH.get()).build(null));
}
