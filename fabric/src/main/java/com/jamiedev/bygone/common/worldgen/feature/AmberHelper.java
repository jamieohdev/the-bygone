package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.blocks.PointedAmberBlock;
import com.jamiedev.bygone.fabric.init.JamiesModBlocks;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;

public class AmberHelper 
{
    public AmberHelper() {
    }

    public static double scaleHeightFromRadius(double radius, double scale, double heightScale, double bluntness) {
        if (radius < bluntness) {
            radius = bluntness;
        }

        double d = 0.384;
        double e = radius / scale * 0.384;
        double f = 0.75 * Math.pow(e, 1.3333333333333333);
        double g = Math.pow(e, 0.6666666666666666);
        double h = 0.3333333333333333 * Math.log(e);
        double i = heightScale * (f - g - h);
        i = Math.max(i, 0.0);
        return i / 0.384 * scale;
    }

    public static boolean canGenerateBase(WorldGenLevel world, BlockPos pos, int height) {
        if (canGenerateOrLava(world, pos)) {
            return false;
        } else {
            float f = 6.0F;
            float g = 6.0F / (float)height;

            for(float h = 0.0F; h < 6.2831855F; h += g) {
                int i = (int)(Mth.cos(h) * (float)height);
                int j = (int)(Mth.sin(h) * (float)height);
                if (canGenerateOrLava(world, pos.offset(i, 0, j))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean canGenerate(LevelAccessor world, BlockPos pos) {
        return world.isStateAtPosition(pos, AmberHelper::canGenerate);
    }

    public static boolean canGenerateOrLava(LevelAccessor world, BlockPos pos) {
        return world.isStateAtPosition(pos, AmberHelper::canGenerateOrLava);
    }

    protected static void getAmberThickness(Direction direction, int height, boolean merge, Consumer<BlockState> callback) {
        if (height >= 3) {
            callback.accept(getState(direction, DripstoneThickness.BASE));

            for(int i = 0; i < height - 3; ++i) {
                callback.accept(getState(direction, DripstoneThickness.MIDDLE));
            }
        }

        if (height >= 2) {
            callback.accept(getState(direction, DripstoneThickness.FRUSTUM));
        }

        if (height >= 1) {
            callback.accept(getState(direction, merge ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
        }

    }

    protected static void generatePointedAmber(LevelAccessor world, BlockPos pos, Direction direction, int height, boolean merge) {
        if (canReplace(world.getBlockState(pos.relative(direction.getOpposite())))) {
            BlockPos.MutableBlockPos mutable = pos.mutable();
            getAmberThickness(direction, height, merge, (state) -> {
                if (state.is(JamiesModBlocks.POINTED_AMBER)) {
                    state = state.setValue(PointedAmberBlock.WATERLOGGED, world.isWaterAt(mutable));
                }

                world.setBlock(mutable, state, 2);
                mutable.move(direction);
            });
        }
    }

    protected static boolean generateAmberBlock(LevelAccessor world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.is(BlockTags.DRIPSTONE_REPLACEABLE)) {
            world.setBlock(pos, JamiesModBlocks.COBBLED_AMBER.defaultBlockState(), 2);
            return true;
        } else {
            return false;
        }
    }

    private static BlockState getState(Direction direction, DripstoneThickness thickness) {
        return JamiesModBlocks.POINTED_AMBER.defaultBlockState().setValue(PointedAmberBlock.VERTICAL_DIRECTION, direction).setValue(PointedAmberBlock.THICKNESS, thickness);
    }

    public static boolean canReplaceOrLava(BlockState state) {
        return canReplace(state) || state.is(Blocks.LAVA);
    }

    public static boolean canReplace(BlockState state) {
        return state.is(JamiesModBlocks.COBBLED_AMBER) || state.is(BlockTags.DRIPSTONE_REPLACEABLE);
    }

    public static boolean canGenerate(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    public static boolean cannotGenerate(BlockState state) {
        return !state.isAir() && !state.is(Blocks.WATER);
    }

    public static boolean canGenerateOrLava(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA);
    }
}
