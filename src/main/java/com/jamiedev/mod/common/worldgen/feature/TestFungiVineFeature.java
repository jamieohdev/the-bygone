package com.jamiedev.mod.common.worldgen.feature;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class TestFungiVineFeature extends Feature<DefaultFeatureConfig> {
    public TestFungiVineFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

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
                if (direction != Direction.DOWN && VineBlock.shouldConnectTo(structureWorldAccess, blockPos.offset(direction), direction)) {
                    if (structureWorldAccess.getRandom().nextInt(5) == 1)
                    {
                        structureWorldAccess.setBlockState(blockPos, (BlockState) JamiesModBlocks.ORANGE_FUNGI_VINES.getDefaultState().with(VineBlock.getFacingProperty(direction), true), 2);

                    }
                    if (structureWorldAccess.getRandom().nextInt(4) == 1)
                    {
                        structureWorldAccess.setBlockState(blockPos, (BlockState) JamiesModBlocks.PINK_FUNGI_VINES.getDefaultState().with(VineBlock.getFacingProperty(direction), true), 2);

                    }
                    else
                    {
                        structureWorldAccess.setBlockState(blockPos, (BlockState) JamiesModBlocks.PURPLE_FUNGI_VINES.getDefaultState().with(VineBlock.getFacingProperty(direction), true), 2);

                    }
                    return true;
                }
            }

            return false;
        }
    }
}
