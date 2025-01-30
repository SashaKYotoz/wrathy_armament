package net.sashakyotoz.wrathy_armament.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.WorldshardWorkbenchBlockEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlockEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class WorldshardWorkbench extends BaseEntityBlock {
    public WorldshardWorkbench(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WorldshardWorkbenchBlockEntity(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, 12, 16);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof WorldshardWorkbenchBlockEntity block) {
                block.drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof WorldshardWorkbenchBlockEntity block)
                NetworkHooks.openScreen(((ServerPlayer) pPlayer), block, pPos);
            else
                throw new IllegalStateException("Container provider is missing!");
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, WrathyArmamentBlockEntities.WORLDSHARD_WORKBENCH.get(), (level1, pos, state1, entity) -> entity.tick(level1, pos, state1, entity));
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        return Collections.singletonList(new ItemStack(WrathyArmamentBlocks.WORLDSHARD_WORKBENCH.get()));
    }
}