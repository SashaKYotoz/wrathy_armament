package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.ChaosForgeBlockEntity;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.MythrilAnvilBlockEntity;

public class WrathyArmamentBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WrathyArmament.MODID);
    public static final RegistryObject<BlockEntityType<MythrilAnvilBlockEntity>> MYTHRIL_ANVIL =
            BLOCK_ENTITIES.register("mythril_anvil", () ->
                    BlockEntityType.Builder.of(MythrilAnvilBlockEntity::new,
                            WrathyArmamentBlocks.MYTHRIL_ANVIL.get()).build(null));
    public static final RegistryObject<BlockEntityType<ChaosForgeBlockEntity>> CHAOS_FORGE =
            BLOCK_ENTITIES.register("chaos_forge", () ->
                    BlockEntityType.Builder.of(ChaosForgeBlockEntity::new,
                            WrathyArmamentBlocks.CHAOS_FORGE.get()).build(null));
}
