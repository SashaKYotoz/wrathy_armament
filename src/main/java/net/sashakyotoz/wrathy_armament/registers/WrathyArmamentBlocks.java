package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.HandlerStoneBlock;

public class WrathyArmamentBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WrathyArmament.MODID);
    public static final RegistryObject<Block> HANDLER_STONE = BLOCKS.register("handler_stone", () -> new HandlerStoneBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).emissiveRendering((bs, br, bp) -> bs.getValue(HandlerStoneBlock.SWORD_INDEX) > 0).randomTicks().strength(10f,10f).noLootTable().noOcclusion()));
}
