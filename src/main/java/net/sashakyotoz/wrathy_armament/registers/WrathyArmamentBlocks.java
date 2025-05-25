package net.sashakyotoz.wrathy_armament.registers;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.HandlerStoneBlock;
import net.sashakyotoz.wrathy_armament.blocks.ParadiseBlock;
import net.sashakyotoz.wrathy_armament.blocks.WorldshardWorkbench;

import java.util.function.Supplier;

public class WrathyArmamentBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WrathyArmament.MODID);
    public static final RegistryObject<Block> HANDLER_STONE = registerBlock("handler_stone", () -> new HandlerStoneBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.IGNORE).emissiveRendering((bs, br, bp) -> bs.getValue(HandlerStoneBlock.SWORD_INDEX) > 0).randomTicks().strength(10f,10f).noLootTable().noOcclusion()));
    public static final RegistryObject<Block> WORLDSHARD_WORKBENCH = registerBlock("worldshard_workbench", () -> new WorldshardWorkbench(BlockBehaviour.Properties.copy(Blocks.ANVIL).noOcclusion().strength(15,15)));
    public static final RegistryObject<Block> PARADISE_BLOCK = registerBlock("paradise_block", () -> new ParadiseBlock(BlockBehaviour.Properties.of().noOcclusion().strength(-1).noCollission().noLootTable()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return WrathyArmamentItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}