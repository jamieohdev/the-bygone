package com.jamiedev.mod.worldgen.feature;

import com.jamiedev.mod.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AncientVinesFeature extends Feature<DefaultFeatureConfig> {
    public AncientVinesFeature(Codec<DefaultFeatureConfig> codec) {
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
                    structureWorldAccess.setBlockState(blockPos, (BlockState) JamiesModBlocks.ANCIENT_VINE.getDefaultState().with(VineBlock.getFacingProperty(direction), true), 2);
                    return true;
                }
            }

            return false;
        }
    }
}
