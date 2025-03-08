package com.jamiedev.bygone.common.block;

import org.jetbrains.annotations.NotNull;
import com.jamiedev.bygone.common.block.entity.BlemishSpreadManager;
import com.jamiedev.bygone.common.block.entity.BlemishSpreadable;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import java.util.Collection;
import java.util.Iterator;

public class BlemishVeinBlock extends MultifaceBlock implements BlemishSpreadable, SimpleWaterloggedBlock {
    public static final MapCodec<BlemishVeinBlock> CODEC = simpleCodec(BlemishVeinBlock::new);
    private static final BooleanProperty WATERLOGGED;
    private final MultifaceSpreader allGrowTypeGrower;
    private final MultifaceSpreader samePositionOnlyGrower;

    @Override
    public MapCodec<BlemishVeinBlock> codec() {
        return CODEC;
    }

    public BlemishVeinBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.allGrowTypeGrower = new MultifaceSpreader(new BlemishVeinBlock.BlemishVeinGrowChecker(this, MultifaceSpreader.DEFAULT_SPREAD_ORDER));
        this.samePositionOnlyGrower = new MultifaceSpreader(new BlemishVeinBlock.BlemishVeinGrowChecker(this, MultifaceSpreader.SpreadType.SAME_POSITION));
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return this.allGrowTypeGrower;
    }

    public MultifaceSpreader getSamePositionOnlyGrower() {
        return this.samePositionOnlyGrower;
    }

    public static boolean place(LevelAccessor world, BlockPos pos, BlockState state, Collection<Direction> directions) {
        boolean bl = false;
        BlockState blockState = BGBlocks.BLEMISH_VEIN.defaultBlockState();
        Iterator<Direction> var6 = directions.iterator();

        while(var6.hasNext()) {
            Direction direction = (Direction)var6.next();
            BlockPos blockPos = pos.relative(direction);
            if (canAttachTo(world, direction, blockPos, world.getBlockState(blockPos))) {
                blockState = blockState.setValue(getFaceProperty(direction), true);
                bl = true;
            }
        }

        if (!bl) {
            return false;
        } else {
            if (!state.getFluidState().isEmpty()) {
                blockState = blockState.setValue(WATERLOGGED, true);
            }

            world.setBlock(pos, blockState, 3);
            return true;
        }
    }

    @Override
    public void spreadAtSamePosition(LevelAccessor world, BlockState state, BlockPos pos, @NotNull RandomSource random) {
        if (state.is(this)) {
            Direction[] var5 = DIRECTIONS;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Direction direction = var5[var7];
                BooleanProperty booleanProperty = getFaceProperty(direction);
                if (state.getValue(booleanProperty) && world.getBlockState(pos.relative(direction)).is(BGBlocks.BLEMISH)) {
                    state = state.setValue(booleanProperty, false);
                }
            }

            if (!hasAnyFace(state)) {
                FluidState fluidState = world.getFluidState(pos);
                state = (fluidState.isEmpty() ? Blocks.AIR : Blocks.WATER).defaultBlockState();
            }

            world.setBlock(pos, state, 3);
            BlemishSpreadable.super.spreadAtSamePosition(world, state, pos, random);
        }
    }

    @Override
    public int spread(BlemishSpreadManager.Cursor cursor, LevelAccessor world, BlockPos catalystPos, @NotNull RandomSource random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
        if (shouldConvertToBlock && this.convertToBlock(spreadManager, world, cursor.getPos(), random)) {
            return cursor.getCharge() - 1;
        } else {
            return random.nextInt(spreadManager.getSpreadChance()) == 0 ? Mth.floor((float)cursor.getCharge() * 0.5F) : cursor.getCharge();
        }
    }

    private boolean convertToBlock(BlemishSpreadManager spreadManager, LevelAccessor world, BlockPos pos, @NotNull RandomSource random) {
        BlockState blockState = world.getBlockState(pos);
        TagKey<Block> tagKey = spreadManager.getReplaceableTag();
        Iterator<Direction> var7 = Direction.allShuffled(random).iterator();

        while(var7.hasNext()) {
            Direction direction = (Direction)var7.next();
            if (hasFace(blockState, direction)) {
                BlockPos blockPos = pos.relative(direction);
                BlockState blockState2 = world.getBlockState(blockPos);
                if (blockState2.is(tagKey)) {
                    BlockState blockState3 = BGBlocks.BLEMISH.defaultBlockState();
                    world.setBlock(blockPos, blockState3, 3);
                    Block.pushEntitiesUp(blockState2, blockState3, world, blockPos);
                    world.playSound(null, blockPos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.allGrowTypeGrower.spreadAll(blockState3, world, blockPos, spreadManager.isWorldGen());
                    Direction direction2 = direction.getOpposite();
                    Direction[] var13 = DIRECTIONS;
                    int var14 = var13.length;

                    for(int var15 = 0; var15 < var14; ++var15) {
                        Direction direction3 = var13[var15];
                        if (direction3 != direction2) {
                            BlockPos blockPos2 = blockPos.relative(direction3);
                            BlockState blockState4 = world.getBlockState(blockPos2);
                            if (blockState4.is(this)) {
                                this.spreadAtSamePosition(world, blockState4, blockPos2, random);
                            }
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean veinCoversBlemishReplaceable(LevelAccessor world, BlockState state, BlockPos pos) {
        if (!state.is(BGBlocks.BLEMISH_VEIN)) {
            return false;
        } else {
            Direction[] var3 = DIRECTIONS;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Direction direction = var3[var5];
                if (hasFace(state, direction) && world.getBlockState(pos.relative(direction)).is(JamiesModTag.BLEMISH_REPLACEABLE)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.getItemInHand().is(Item.byBlock(BGBlocks.BLEMISH_VEIN)) || super.canBeReplaced(state, context);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }

    private class BlemishVeinGrowChecker extends MultifaceSpreader.DefaultSpreaderConfig {
        private final MultifaceSpreader.SpreadType[] growTypes;

        public BlemishVeinGrowChecker(final BlemishVeinBlock BlemishVeinBlock, final MultifaceSpreader.SpreadType... growTypes) {
            super(BlemishVeinBlock);
            this.growTypes = growTypes;
        }

        @Override
        public boolean stateCanBeReplaced(BlockGetter world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state) {
            BlockState blockState = world.getBlockState(growPos.relative(direction));
            if (!blockState.is(BGBlocks.BLEMISH) && !blockState.is(BGBlocks.BLEMISH_CATALYST) && !blockState.is(Blocks.MOVING_PISTON)) {
                if (pos.distManhattan(growPos) == 2) {
                    BlockPos blockPos = pos.relative(direction.getOpposite());
                    if (world.getBlockState(blockPos).isFaceSturdy(world, blockPos, direction)) {
                        return false;
                    }
                }

                FluidState fluidState = state.getFluidState();
                if (!fluidState.isEmpty() && !fluidState.is(Fluids.WATER)) {
                    return false;
                } else if (state.is(BlockTags.FIRE)) {
                    return false;
                } else {
                    return state.canBeReplaced() || super.stateCanBeReplaced(world, pos, growPos, direction, state);
                }
            } else {
                return false;
            }
        }

        @Override
        public MultifaceSpreader.SpreadType[] getSpreadTypes() {
            return this.growTypes;
        }

        @Override
        public boolean isOtherBlockValidAsSource(BlockState state) {
            return !state.is(BGBlocks.BLEMISH_VEIN);
        }
    }
}
