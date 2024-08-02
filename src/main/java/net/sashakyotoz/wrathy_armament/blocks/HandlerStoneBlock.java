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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
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
import org.antlr.v4.runtime.misc.Triple;

import java.util.Comparator;
import java.util.List;

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
                    ItemStack stack = WrathyArmamentItems.MASTER_SWORD.get().getDefaultInstance();
                    player.getCooldowns().addCooldown(stack.getItem(),200);
                    player.spawnAtLocation(stack);
                    provokeCollapse(level,pos,16,18);
                    List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(pos.getCenter(), pos.getCenter()).inflate(16), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(pos.getCenter()))).toList();
                    for (Entity entity : entityList) {
                        if (entity instanceof Player player1){
                            OnActionsTrigger.playerCameraData.computeIfAbsent(player1.getStringUUID(), k -> new Triple<>(0, 0, 0));
                            OnActionsTrigger.playerCameraData.put(player1.getStringUUID(),new Triple<>(
                                    OnActionsTrigger.playerCameraData.get(player1.getStringUUID()).a + 100,
                                    OnActionsTrigger.playerCameraData.get(player1.getStringUUID()).b,
                                    OnActionsTrigger.playerCameraData.get(player1.getStringUUID()).c
                            ));
                        }
                    }
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
    public static void provokeCollapse(Level level,BlockPos pos,int radius, int yOffset){
        for (int y = -radius+yOffset; y < radius-yOffset/3; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    int finalX = x;
                    int finalY = y;
                    int finalZ = z;
                    OnActionsTrigger.queueServerWork(10,()->{
                        if (level.getBlockState(pos.offset(finalX,finalY,finalZ)).canOcclude() && level instanceof ServerLevel serverLevel){
                            BlockPos pos1 = pos.offset(finalX,finalY,finalZ);
                            if (level.random.nextBoolean()){
                                level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST,level.getBlockState(pos1)),pos1.getX(),pos1.getY() -1,pos1.getZ(),0,-1,0);
                            }else
                                FallingBlockEntity.fall(serverLevel,pos1,level.getBlockState(pos1));
                        }
                    });
                }
            }
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