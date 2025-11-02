package com.jamiedev.bygone.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AncientSaplingBlock extends SaplingBlock {
    public static final IntegerProperty STAGE;
    public static final MapCodec<AncientSaplingBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(TreeGrower.CODEC.fieldOf("tree").forGetter((block) -> {
            return block.generator;
        }), propertiesCodec()).apply(instance, AncientSaplingBlock::new);
    });
    protected static final float field_31236 = 6.0F;
    protected static final VoxelShape SHAPE;

    static {
        STAGE = BlockStateProperties.STAGE;
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
    }

    protected final TreeGrower generator;

    public AncientSaplingBlock(TreeGrower generator, BlockBehaviour.Properties settings) {
        super(generator, settings);
        this.generator = generator;
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    public MapCodec<? extends AncientSaplingBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(BlockTags.DIRT) || floor.is(Blocks.FARMLAND);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return type == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, type);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        return this.mayPlaceOn(world.getBlockState(blockPos), world, blockPos);
    }
}
