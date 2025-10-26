package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.FallingBlock.isFree;

public class SablenutBlock extends Block implements BonemealableBlock, Fallable
{
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE;
    public static final DirectionProperty FACING;

    protected static final VoxelShape HANGING_SHAPE;
    protected static final VoxelShape[] SHAPE = new VoxelShape[]{
            Shapes.or(Block.box(0.8, 0, 8, 15.2, 16, 8),
                    Block.box(8, 0, 0.8, 8, 16, 15.2),
                    Block.box(6.5, 9, 6.5, 9.5, 12, 9.5)),

            Shapes.or(Block.box(0.8, 0, 8, 15.2, 16, 8),
                    Block.box(8, 0, 0.8, 8, 16, 15.2),
                    Block.box(6, 8, 6, 10, 12, 10)),

            Shapes.or(Block.box(0.8, 0, 8, 15.2, 16, 8),
                    Block.box(8, 0, 0.8, 8, 16, 15.2),
                    Block.box(5.5, 7, 5.5, 10.5, 12, 10.5)),

            Shapes.or(Block.box(0.8, 0, 8, 15.2, 16, 8),
                    Block.box(8, 0, 0.8, 8, 16, 15.2),
                    Block.box(4.5, 5, 4.5, 11.5, 12, 11.5)),
    };

    public static final MapCodec<SablenutBlock> CODEC = simpleCodec(SablenutBlock::new);

    public SablenutBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)
                ((BlockState)this.stateDefinition.any())
                        .setValue(FACING, Direction.UP)).setValue(AGE, 0));
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return (Integer)state.getValue(AGE) < 3;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.random.nextInt(5) == 0) {
            int i = (Integer)state.getValue(AGE);
            if (i < 3) {
                level.setBlock(pos, (BlockState)state.setValue(AGE, i + 1), 2);
            }
        }
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int i = (Integer)state.getValue(AGE);
        return SHAPE[i];
    }

    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    protected void falling(FallingBlockEntity entity) {
    }

    private static void spawnFallingSablenut(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
        BlockState blockstate = state;

        FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, blockpos$mutableblockpos, blockstate);
        fallingblockentity.dropItem = false;
        blockpos$mutableblockpos.move(Direction.DOWN);
        blockstate = level.getBlockState(blockpos$mutableblockpos);
    }


    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, @NotNull RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            spawnFallingSablenut(state, world, pos);
        }
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == state.getValue(FACING) && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return (Integer)state.getValue(AGE) < 3;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, (BlockState)state.setValue(AGE, (Integer)state.getValue(AGE) + 1), 2);
    }

    public boolean isFullyGrown(BlockState state)
    {
        int i = (Integer)state.getValue(AGE);
        return i == MAX_AGE | i > MAX_AGE;
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockpos = hit.getBlockPos();
            if (projectile.mayInteract(level, blockpos)
                    && projectile.mayBreak(level)
                    && projectile instanceof Projectile
                    && projectile.getDeltaMovement().length() > 0.6) {
                spawnFallingSablenut(state, (ServerLevel) level, blockpos);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(BGBlocks.SABLE_WOOD.get()) || level.getBlockState(pos.above()).is(BGBlocks.SABLE_LEAVES.get())
               ;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, AGE});
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected MapCodec<SablenutBlock> codec() {
        return CODEC;
    }

    public static BlockState createNewHangingNut() {
        return createNewHangingNut(0);
    }

    public static BlockState createNewHangingNut(int age) {
        return (BlockState)((BlockState)BGBlocks.SABLENUT.get().defaultBlockState().setValue(FACING, Direction.UP)).setValue(AGE, age);
    }

    static {
        AGE = BlockStateProperties.AGE_3;
        FACING = BlockStateProperties.FACING;
        HANGING_SHAPE = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0),
                Block.box(6.0, 7.0, 6.0, 10.0, 9.0, 10.0));
    }
}

