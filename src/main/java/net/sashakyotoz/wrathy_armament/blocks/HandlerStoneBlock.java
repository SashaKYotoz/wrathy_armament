package net.sashakyotoz.wrathy_armament.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.bosses.JohannesKnight;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class HandlerStoneBlock extends Block {
    public static final IntegerProperty SWORD_INDEX =IntegerProperty.create("sword_index",0,3);
    public static final BooleanProperty CONTAINS_KEEPER = BooleanProperty.create("contains_keeper");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public HandlerStoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SWORD_INDEX, 0).setValue(CONTAINS_KEEPER,false));
    }
    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return state.getValue(SWORD_INDEX) > 0 ? 8 : 0;
    }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return box(0,0,0,16,state.getValue(SWORD_INDEX) > 0 ? 16 : 8,16);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(SWORD_INDEX);
        builder.add(CONTAINS_KEEPER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        if (state.getValue(CONTAINS_KEEPER) && source.nextBoolean())
            OnActionsTrigger.addParticles(new BlockParticleOption(ParticleTypes.BLOCK,level.getBlockState(pos.below())),level,pos.getX(),pos.getY(),pos.getZ(),2);
        super.randomTick(state, level, pos, source);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (state.getValue(CONTAINS_KEEPER) && level.getBlockState(pos.above()).isAir()){
            switch (state.getValue(SWORD_INDEX)){
                case 1 -> {
                    player.spawnAtLocation(WrathyArmamentItems.MASTER_SWORD.get());
                    provokeCollapse(level,pos,7,2);
                    OnActionsTrigger.shakingTime += 100;
                    OnActionsTrigger.vec3 = pos.getCenter();
                    OnActionsTrigger.queueServerWork(20,()-> waveFlaming(level,pos));
                    OnActionsTrigger.queueServerWork(100,()-> OnActionsTrigger.vec3 = new Vec3(0,-256,0));
                }
                case 2 ->{
                    waveFlaming(level,pos);
                    OnActionsTrigger.queueServerWork(30,()->{
                        if (level instanceof ServerLevel serverLevel){
                            LichKing king = new LichKing(WrathyArmamentEntities.LICH_KING.get(),serverLevel);
                            king.moveTo(pos.getCenter().offsetRandom(RandomSource.create(),3));
                            serverLevel.addFreshEntity(king);
                        }
                    });
                }
                case 3 ->{
                    waveFlaming(level,pos);
                    OnActionsTrigger.queueServerWork(30,()->{
                        if (level instanceof ServerLevel serverLevel){
                            JohannesKnight knight = new JohannesKnight(WrathyArmamentEntities.JOHANNES_KNIGHT.get(),serverLevel);
                            knight.moveTo(pos.getCenter().offsetRandom(RandomSource.create(),3));
                            serverLevel.addFreshEntity(knight);
                        }
                    });
                }
            }
            level.setBlock(pos,this.defaultBlockState().setValue(SWORD_INDEX,0),3);
            level.setBlock(pos,this.defaultBlockState().setValue(CONTAINS_KEEPER,false),3);
        }
        return super.use(state, level, pos, player, hand, result);
    }
    private void provokeCollapse(Level level,BlockPos pos,int radius, int yOffset){
        int x,y,z;
        y = -radius + yOffset;
        for (int i = 0; i < radius; i++) {
            x = -radius;
            for (int j = 0; j < radius; j++) {
                z = -radius;
                for (int k = 0; k < radius; k++) {
                    if (level.getBlockState(BlockPos.containing(pos.getX() + x,pos.getY()+y,pos.getZ()+z)).canOcclude() && level.getBlockState(pos.offset(x,y-1,z)).isAir() && level instanceof ServerLevel serverLevel){
                        BlockPos pos1 = BlockPos.containing(pos.getX() + x,pos.getY()+y,pos.getZ()+z);
                        WrathyArmament.LOGGER.debug(level.getBlockState(pos1).toString());
                        if (RandomSource.create().nextBoolean()){
                            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST,level.getBlockState(pos1)),pos1.getX(),pos1.getY() -1,pos1.getZ(),0,-1,0);
                        }else
                            FallingBlockEntity.fall(serverLevel,pos1,level.getBlockState(pos1));
                    }
                    z = z+1;
                }
                x = x+1;
            }
            y = y+1;
        }
    }
    private void waveFlaming(Level level, BlockPos pos){
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            OnActionsTrigger.queueServerWork(20 + 10 * i,()-> OnActionsTrigger.addParticles(ParticleTypes.END_ROD,level,pos.getX(),pos.getY(),pos.getZ(),2 * finalI));
        }
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(SWORD_INDEX) > 0 ? 8 : 0;
    }
}
