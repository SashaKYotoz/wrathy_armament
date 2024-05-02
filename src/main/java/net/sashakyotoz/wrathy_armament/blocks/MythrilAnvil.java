package net.sashakyotoz.wrathy_armament.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.MythrilAnvilBlockEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlockEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlocks;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MythrilAnvil extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty STARRED = BooleanProperty.create("starred");
    private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape X_LEG1 = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
    private static final VoxelShape X_LEG2 = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
    private static final VoxelShape X_TOP = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
    private static final VoxelShape Z_LEG1 = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
    private static final VoxelShape Z_LEG2 = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
    private static final VoxelShape Z_TOP = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
    private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, X_LEG1, X_LEG2, X_TOP);
    private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, Z_LEG1, Z_LEG2, Z_TOP);

    public MythrilAnvil(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING,Direction.NORTH).setValue(STARRED,false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MythrilAnvilBlockEntity(pos, state);
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(STARRED);
    }

    public boolean isPathfindable(BlockState blockState, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide()) {
            if (player.getItemInHand(hand).is(Items.NETHER_STAR)) {
                if (!state.getValue(STARRED)) {
                    player.getInventory().clearOrCountMatchingItems(p -> player.getItemInHand(hand).getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                    level.setBlock(pos, state.setValue(STARRED, true), 3);
                }
            } else {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity instanceof MythrilAnvilBlockEntity)
                    NetworkHooks.openScreen(((ServerPlayer) player), (MythrilAnvilBlockEntity) entity, pos);
                else
                    throw new IllegalStateException("Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MythrilAnvilBlockEntity) {
                ((MythrilAnvilBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        ItemStack itemStack = new ItemStack(WrathyArmamentBlocks.MYTHRIL_ANVIL.get());
        return Collections.singletonList(itemStack);
    }
}
