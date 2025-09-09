package com.jamiedev.bygone.common.block.gourds;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GourdLanternBlock extends GrowingPlantBodyBlock implements BonemealableBlock {
    public static final MapCodec<GourdLanternBlock> CODEC = simpleCodec(GourdLanternBlock::new);
    public static final BooleanProperty HANGING;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape STANDING_SHAPE;
    protected static final VoxelShape HANGING_SHAPE;

    protected static final VoxelShape[] STANDING_AGING_SHAPE = new VoxelShape[]{
            Shapes.or(Block.box(5.5, 4.0, 5.5, 9.5, 7.0, 9.5),
                    Block.box(6.0, 7.0, 6.0, 9.0, 9.0, 9.0)),

            Shapes.or(Block.box(4.5, 4.0, 4.5, 10.5, 7.0, 10.5),
                    Block.box(6.0, 7.0, 6.0, 9.0, 9.0, 9.0)),

            Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0),
                    Block.box(6.0, 7.0, 6.0, 10.0, 9.0, 10.0))
    };
    protected static final VoxelShape[] HANGING_AGING_SHAPE = new VoxelShape[]{
            Shapes.or(Block.box(5.5, 4.0, 5.5, 9.5, 7.0, 9.5),
                    Block.box(6.0, 7.0, 6.0, 9.0, 9.0, 9.0)),

            Shapes.or(Block.box(4.5, 4.0, 4.5, 10.5, 7.0, 10.5),
                    Block.box(6.0, 7.0, 6.0, 9.0, 9.0, 9.0)),

            Shapes.or(Block.box(3.0, 1.0, 3.0, 13.0, 5.0, 13.0),
                    Block.box(6.0, 8.0, 6.0, 10.0, 10.0, 10.0))
    };


    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE;
    public static final BooleanProperty GROW_VINE;

    CocoaBlock ref1;

    @Override
    public MapCodec<GourdLanternBlock> codec() {
        return CODEC;
    }

    public GourdLanternBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.DOWN, HANGING_SHAPE, false);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, false).setValue(WATERLOGGED, false).setValue(AGE, 0).setValue(GROW_VINE, true));
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return (Integer)state.getValue(AGE) < 2;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.random.nextInt(5) == 0) {
            int i = (Integer)state.getValue(AGE);
            if (i < 2) {
                level.setBlock(pos, (BlockState)state.setValue(AGE, i + 1), 2);
            }

        }

        if (level.random.nextInt(2) == 0) {
            int i = (Integer)state.getValue(AGE);
            if (i < 2 && level.getBlockState(pos.below()).is(Blocks.AIR) && state.getValue(GROW_VINE)) {
                Set<Block> gourds = Set.of(
                        BGBlocks.GOURD_LANTERN_BEIGE.get(),
                        BGBlocks.GOURD_LANTERN_MUAVE.get(),
                        BGBlocks.GOURD_LANTERN_VERDANT.get()
                );
                List<Block> gourdsList = new ArrayList<>(gourds);
                level.setBlock(pos, BGBlocks.GOURD_VINE.get().defaultBlockState().setValue(GourdVineBlock.GOURD_TYPE, gourdsList.indexOf(this)), 2);
                level.setBlock(pos.below(), (BlockState)state.setValue(AGE, i), 2);
            }
        }

    }

    public boolean isFullyGrown(BlockState state)
    {
        int i = (Integer)state.getValue(AGE);
        return i == MAX_AGE | i > MAX_AGE;
    }

    public boolean getGrowVine(BlockState state)
    {
        return (Boolean)state.getValue(GROW_VINE);
    }

   @Override
   protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
       if (!level.isClientSide) {
           BlockPos blockpos = hit.getBlockPos();
           if (projectile.mayInteract(level, blockpos)
                   && projectile.mayBreak(level)
                   && projectile instanceof Projectile
                   && projectile.getDeltaMovement().length() > 0.6) {
               spawnFallingGourd(state, (ServerLevel) level, blockpos);
           }
       }
   }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, @NotNull RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            spawnFallingGourd(state, world, pos);
        }
    }

    private static void spawnFallingGourd(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
        BlockState blockstate = state;

            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, blockpos$mutableblockpos, blockstate);
            fallingblockentity.dropItem = false;
            blockpos$mutableblockpos.move(Direction.DOWN);
            blockstate = level.getBlockState(blockpos$mutableblockpos);

    }


    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN)
                || (level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP) && this.isFullyGrown(state));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int i = state.getValue(AGE);
        return state.getValue(HANGING) ? HANGING_AGING_SHAPE[i] : STANDING_AGING_SHAPE[i];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED, AGE, GROW_VINE);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return (Integer)state.getValue(AGE) < 2;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, (BlockState)state.setValue(AGE, (Integer)state.getValue(AGE) + 1), 2);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getBodyBlock());
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) BGBlocks.GOURD_VINE.get();
    }
    static {
        AGE = BlockStateProperties.AGE_2;
        GROW_VINE = BooleanProperty.create("grow_vine");
        HANGING = BlockStateProperties.HANGING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        STANDING_SHAPE = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0),
                Block.box(6.0, 7.0, 6.0, 10.0, 9.0, 10.0));
        HANGING_SHAPE = Shapes.or(Block.box(3.0, 1.0, 3.0, 13.0, 5.0, 13.0),
                Block.box(6.0, 8.0, 6.0, 10.0, 10.0, 10.0));

    }
}
