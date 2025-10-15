package net.sashakyotoz.wrathy_armament.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlockEntities;

public class ParadiseBlockEntity extends BlockEntity {
    public ParadiseBlockEntity(BlockPos pos, BlockState state) {
        super(WrathyArmamentBlockEntities.PARADISE_BLOCK.get(), pos, state);
    }
}