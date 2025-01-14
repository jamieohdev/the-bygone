package com.jamiedev.mod.common.worldgen.feature;

import com.jamiedev.mod.common.blocks.shelf.ShelfFungiWallFanBlock;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.CoralFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Iterator;

public class ShelfFungiFeature extends Feature<DefaultFeatureConfig> {
    public ShelfFungiFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    CoralFeature ref;

    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        context.getConfig();
        if (!structureWorldAccess.isAir(blockPos)) {
            return false;
        } else {
            Direction[] var4 = Direction.values();
            int var5 = var4.length;

            for (Direction direction : var4) {
                if (direction != Direction.DOWN && ShelfFungiWallFanBlock.shouldConnectTo(structureWorldAccess, blockPos.offset(direction), direction)) {
                    Iterator var7 = Direction.Type.HORIZONTAL.iterator();

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
    protected boolean generateCoralPiece(WorldAccess world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(pos);
        if ((blockState.isIn(BlockTags.PICKAXE_MINEABLE))) {
            world.setBlockState(pos, state, 3);


            Iterator var7 = Direction.Type.HORIZONTAL.iterator();

            while(var7.hasNext()) {
                Direction direction = (Direction)var7.next();
                if (random.nextFloat() < 0.2F) {
                    BlockPos blockPos2 = pos.offset(direction);
                    if (world.getBlockState(blockPos2).isOf(Blocks.AIR)) {
                        Registries.BLOCK.getRandomEntry(JamiesModTag.SHELF_FUNGI, random).map(RegistryEntry::value).ifPresent((block) -> {
                            BlockState blockState1 = block.getDefaultState();
                            if (blockState1.contains(ShelfFungiWallFanBlock.FACING)) {
                                blockState1 = (BlockState)blockState1.with(ShelfFungiWallFanBlock.FACING, direction);
                            }

                            world.setBlockState(blockPos2, blockState1, 2);
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
