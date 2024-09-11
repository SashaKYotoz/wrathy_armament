package net.sashakyotoz.wrathy_armament.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.ParadiseBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ParadiseBlock extends BaseEntityBlock {
    public ParadiseBlock(Properties properties) {
        super(properties);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ParadiseBlockEntity(pPos,pState);
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        super.attack(pState, pLevel, pPos, pPlayer);
        if (!pLevel.isClientSide())
            pLevel.setBlock(pPos,pLevel.getBlockState(pPos.below()),3);
    }
}