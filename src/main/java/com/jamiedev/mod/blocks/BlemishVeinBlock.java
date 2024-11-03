package com.jamiedev.mod.blocks;

import com.jamiedev.mod.blocks.entity.BlemishSpreadManager;
import com.jamiedev.mod.blocks.entity.BlemishSpreadable;
import com.jamiedev.mod.init.JamiesModBlocks;
import com.jamiedev.mod.init.JamiesModTag;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Collection;
import java.util.Iterator;

public class BlemishVeinBlock extends MultifaceGrowthBlock implements BlemishSpreadable, Waterloggable {
    public static final MapCodec<BlemishVeinBlock> CODEC = createCodec(BlemishVeinBlock::new);
    private static final BooleanProperty WATERLOGGED;
    private final LichenGrower allGrowTypeGrower;
    private final LichenGrower samePositionOnlyGrower;

    public MapCodec<BlemishVeinBlock> getCodec() {
        return CODEC;
    }

    public BlemishVeinBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.allGrowTypeGrower = new LichenGrower(new BlemishVeinBlock.BlemishVeinGrowChecker(this, LichenGrower.GROW_TYPES));
        this.samePositionOnlyGrower = new LichenGrower(new BlemishVeinBlock.BlemishVeinGrowChecker(this, new LichenGrower.GrowType[]{LichenGrower.GrowType.SAME_POSITION}));
        this.setDefaultState((BlockState)this.getDefaultState().with(WATERLOGGED, false));
    }

    public LichenGrower getGrower() {
        return this.allGrowTypeGrower;
    }

    public LichenGrower getSamePositionOnlyGrower() {
        return this.samePositionOnlyGrower;
    }

    public static boolean place(WorldAccess world, BlockPos pos, BlockState state, Collection<Direction> directions) {
        boolean bl = false;
        BlockState blockState = JamiesModBlocks.BLEMISH_VEIN.getDefaultState();
        Iterator var6 = directions.iterator();

        while(var6.hasNext()) {
            Direction direction = (Direction)var6.next();
            BlockPos blockPos = pos.offset(direction);
            if (canGrowOn(world, direction, blockPos, world.getBlockState(blockPos))) {
                blockState = (BlockState)blockState.with(getProperty(direction), true);
                bl = true;
            }
        }

        if (!bl) {
            return false;
        } else {
            if (!state.getFluidState().isEmpty()) {
                blockState = (BlockState)blockState.with(WATERLOGGED, true);
            }

            world.setBlockState(pos, blockState, 3);
            return true;
        }
    }

    public void spreadAtSamePosition(WorldAccess world, BlockState state, BlockPos pos, Random random) {
        if (state.isOf(this)) {
            Direction[] var5 = DIRECTIONS;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Direction direction = var5[var7];
                BooleanProperty booleanProperty = getProperty(direction);
                if ((Boolean)state.get(booleanProperty) && world.getBlockState(pos.offset(direction)).isOf(JamiesModBlocks.BLEMISH)) {
                    state = (BlockState)state.with(booleanProperty, false);
                }
            }

            if (!hasAnyDirection(state)) {
                FluidState fluidState = world.getFluidState(pos);
                state = (fluidState.isEmpty() ? Blocks.AIR : Blocks.WATER).getDefaultState();
            }

            world.setBlockState(pos, state, 3);
            BlemishSpreadable.super.spreadAtSamePosition(world, state, pos, random);
        }
    }

    public int spread(BlemishSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
        if (shouldConvertToBlock && this.convertToBlock(spreadManager, world, cursor.getPos(), random)) {
            return cursor.getCharge() - 1;
        } else {
            return random.nextInt(spreadManager.getSpreadChance()) == 0 ? MathHelper.floor((float)cursor.getCharge() * 0.5F) : cursor.getCharge();
        }
    }

    private boolean convertToBlock(BlemishSpreadManager spreadManager, WorldAccess world, BlockPos pos, Random random) {
        BlockState blockState = world.getBlockState(pos);
        TagKey<Block> tagKey = spreadManager.getReplaceableTag();
        Iterator var7 = Direction.shuffle(random).iterator();

        while(var7.hasNext()) {
            Direction direction = (Direction)var7.next();
            if (hasDirection(blockState, direction)) {
                BlockPos blockPos = pos.offset(direction);
                BlockState blockState2 = world.getBlockState(blockPos);
                if (blockState2.isIn(tagKey)) {
                    BlockState blockState3 = JamiesModBlocks.BLEMISH.getDefaultState();
                    world.setBlockState(blockPos, blockState3, 3);
                    Block.pushEntitiesUpBeforeBlockChange(blockState2, blockState3, world, blockPos);
                    world.playSound((PlayerEntity)null, blockPos, SoundEvents.BLOCK_SCULK_SPREAD, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.allGrowTypeGrower.grow(blockState3, world, blockPos, spreadManager.isWorldGen());
                    Direction direction2 = direction.getOpposite();
                    Direction[] var13 = DIRECTIONS;
                    int var14 = var13.length;

                    for(int var15 = 0; var15 < var14; ++var15) {
                        Direction direction3 = var13[var15];
                        if (direction3 != direction2) {
                            BlockPos blockPos2 = blockPos.offset(direction3);
                            BlockState blockState4 = world.getBlockState(blockPos2);
                            if (blockState4.isOf(this)) {
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

    public static boolean veinCoversBlemishReplaceable(WorldAccess world, BlockState state, BlockPos pos) {
        if (!state.isOf(JamiesModBlocks.BLEMISH_VEIN)) {
            return false;
        } else {
            Direction[] var3 = DIRECTIONS;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Direction direction = var3[var5];
                if (hasDirection(state, direction) && world.getBlockState(pos.offset(direction)).isIn(JamiesModTag.BLEMISH_REPLACEABLE)) {
                    return true;
                }
            }

            return false;
        }
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(new Property[]{WATERLOGGED});
    }

    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        return !context.getStack().isOf(Item.fromBlock(JamiesModBlocks.BLEMISH_VEIN)) || super.canReplace(state, context);
    }

    protected FluidState getFluidState(BlockState state) {
        return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
    }

    private class BlemishVeinGrowChecker extends LichenGrower.LichenGrowChecker {
        private final LichenGrower.GrowType[] growTypes;

        public BlemishVeinGrowChecker(final BlemishVeinBlock BlemishVeinBlock, final LichenGrower.GrowType... growTypes) {
            super(BlemishVeinBlock);
            this.growTypes = growTypes;
        }

        public boolean canGrow(BlockView world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state) {
            BlockState blockState = world.getBlockState(growPos.offset(direction));
            if (!blockState.isOf(JamiesModBlocks.BLEMISH) && !blockState.isOf(JamiesModBlocks.BLEMISH_CATALYST) && !blockState.isOf(Blocks.MOVING_PISTON)) {
                if (pos.getManhattanDistance(growPos) == 2) {
                    BlockPos blockPos = pos.offset(direction.getOpposite());
                    if (world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction)) {
                        return false;
                    }
                }

                FluidState fluidState = state.getFluidState();
                if (!fluidState.isEmpty() && !fluidState.isOf(Fluids.WATER)) {
                    return false;
                } else if (state.isIn(BlockTags.FIRE)) {
                    return false;
                } else {
                    return state.isReplaceable() || super.canGrow(world, pos, growPos, direction, state);
                }
            } else {
                return false;
            }
        }

        public LichenGrower.GrowType[] getGrowTypes() {
            return this.growTypes;
        }

        public boolean canGrow(BlockState state) {
            return !state.isOf(JamiesModBlocks.BLEMISH_VEIN);
        }
    }
}
