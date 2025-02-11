package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.blocks.shelf.ShelfFungiWallFanBlock;
import com.jamiedev.bygone.fabric.init.JamiesModTag;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import java.util.Iterator;

public class ShelfFungiFeature extends Feature<NoneFeatureConfiguration> {
    public ShelfFungiFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    CoralFeature ref;

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        context.config();
        if (!structureWorldAccess.isEmptyBlock(blockPos)) {
            return false;
        } else {
            Direction[] var4 = Direction.values();
            int var5 = var4.length;

            for (Direction direction : var4) {
                if (direction != Direction.DOWN && ShelfFungiWallFanBlock.shouldConnectTo(structureWorldAccess, blockPos.relative(direction), direction)) {
                    Iterator var7 = Direction.Plane.HORIZONTAL.iterator();

                    while(var7.hasNext()) {
                        Direction direction1 = (Direction) var7.next();
                        //structureWorldAccess.setBlockState(blockPos, (BlockState) JamiesModBlocks.ORANGE_FUNGI_WALL_FAN.getDefaultState().with(ShelfFungiWallFanBlock2.getFacingProperty(direction), true), 2);
                        return true;
                    }

                }
            }

            return false;
        }
    }
    protected boolean generateCoralPiece(LevelAccessor world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.above();
        BlockState blockState = world.getBlockState(pos);
        if ((blockState.is(BlockTags.MINEABLE_WITH_PICKAXE))) {
            world.setBlock(pos, state, 3);


            Iterator var7 = Direction.Plane.HORIZONTAL.iterator();

            while(var7.hasNext()) {
                Direction direction = (Direction)var7.next();
                if (random.nextFloat() < 0.2F) {
                    BlockPos blockPos2 = pos.relative(direction);
                    if (world.getBlockState(blockPos2).is(Blocks.AIR)) {
                        BuiltInRegistries.BLOCK.getRandomElementOf(JamiesModTag.SHELF_FUNGI, random).map(Holder::value).ifPresent((block) -> {
                            BlockState blockState1 = block.defaultBlockState();
                            if (blockState1.hasProperty(ShelfFungiWallFanBlock.FACING)) {
                                blockState1 = (BlockState)blockState1.setValue(ShelfFungiWallFanBlock.FACING, direction);
                            }

                            world.setBlock(blockPos2, blockState1, 2);
                        });
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
