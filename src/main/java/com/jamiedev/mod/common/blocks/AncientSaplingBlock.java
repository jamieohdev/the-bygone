package com.jamiedev.mod.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class AncientSaplingBlock  extends PlantBlock implements Fertilizable {
    public static final MapCodec<AncientSaplingBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(SaplingGenerator.CODEC.fieldOf("tree").forGetter((block) -> {
            return block.generator;
        }), createSettingsCodec()).apply(instance, AncientSaplingBlock::new);
    });
    public static final IntProperty STAGE;
    protected static final float field_31236 = 6.0F;
    protected static final VoxelShape SHAPE;
    protected final SaplingGenerator generator;

    public MapCodec<? extends AncientSaplingBlock> getCodec() {
        return CODEC;
    }

    public AncientSaplingBlock(SaplingGenerator generator, AbstractBlock.Settings settings) {
        super(settings);
        this.generator = generator;
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(STAGE, 0));
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getLightLevel(pos.up()) >= 9 && random.nextInt(7) == 0) {
            this.generate(world, pos, state, random);
        }

    }

    public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        if ((Integer)state.get(STAGE) == 0) {
            world.setBlockState(pos, (BlockState)state.cycle(STAGE), 4);
        } else {
            this.generator.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
        }

    }

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return (double)world.random.nextFloat() < 0.45;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.generate(world, pos, state, random);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{STAGE});
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(BlockTags.DIRT) || floor.isOf(Blocks.FARMLAND);
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return this.canPlantOnTop(world.getBlockState(blockPos), world, blockPos);
    }

    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return type == NavigationType.AIR && !this.collidable || super.canPathfindThrough(state, type);
    }

    static {
        STAGE = Properties.STAGE;
        SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
    }
}
