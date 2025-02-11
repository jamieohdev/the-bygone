package com.jamiedev.bygone.common.worldgen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class AncientForestVegetationFeature extends Feature<AncientForestVegetationFeatureConfig> {
    public AncientForestVegetationFeature(Codec<AncientForestVegetationFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<AncientForestVegetationFeatureConfig> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        BlockState blockState = structureWorldAccess.getBlockState(blockPos.below());
        AncientForestVegetationFeatureConfig AncientForestVegetationFeatureConfig = (AncientForestVegetationFeatureConfig)context.config();
        RandomSource random = context.random();

            int i = blockPos.getY();
            if (i >= structureWorldAccess.getMinBuildHeight() + 1 && i + 1 < structureWorldAccess.getMaxBuildHeight()) {
                int j = 0;

                for(int k = 0; k < AncientForestVegetationFeatureConfig.spreadWidth * AncientForestVegetationFeatureConfig.spreadWidth; ++k) {
                    BlockPos blockPos2 = blockPos.offset(random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth) - random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth), random.nextInt(AncientForestVegetationFeatureConfig.spreadHeight) - random.nextInt(AncientForestVegetationFeatureConfig.spreadHeight), random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth) - random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth));
                    BlockState blockState2 = AncientForestVegetationFeatureConfig.stateProvider.getState(random, blockPos2);
                    if (structureWorldAccess.isEmptyBlock(blockPos2) && blockPos2.getY() > structureWorldAccess.getMinBuildHeight() && blockState2.canSurvive(structureWorldAccess, blockPos2)) {
                        structureWorldAccess.setBlock(blockPos2, blockState2, 2);
                        ++j;
                    }
                }

                return j > 0;
            } else {
                return false;
            }

    }
}
