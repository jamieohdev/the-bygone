package com.jamiedev.mod.common.worldgen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AncientForestVegetationFeature extends Feature<AncientForestVegetationFeatureConfig> {
    public AncientForestVegetationFeature(Codec<AncientForestVegetationFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<AncientForestVegetationFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        BlockState blockState = structureWorldAccess.getBlockState(blockPos.down());
        AncientForestVegetationFeatureConfig AncientForestVegetationFeatureConfig = (AncientForestVegetationFeatureConfig)context.getConfig();
        Random random = context.getRandom();

            int i = blockPos.getY();
            if (i >= structureWorldAccess.getBottomY() + 1 && i + 1 < structureWorldAccess.getTopY()) {
                int j = 0;

                for(int k = 0; k < AncientForestVegetationFeatureConfig.spreadWidth * AncientForestVegetationFeatureConfig.spreadWidth; ++k) {
                    BlockPos blockPos2 = blockPos.add(random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth) - random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth), random.nextInt(AncientForestVegetationFeatureConfig.spreadHeight) - random.nextInt(AncientForestVegetationFeatureConfig.spreadHeight), random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth) - random.nextInt(AncientForestVegetationFeatureConfig.spreadWidth));
                    BlockState blockState2 = AncientForestVegetationFeatureConfig.stateProvider.get(random, blockPos2);
                    if (structureWorldAccess.isAir(blockPos2) && blockPos2.getY() > structureWorldAccess.getBottomY() && blockState2.canPlaceAt(structureWorldAccess, blockPos2)) {
                        structureWorldAccess.setBlockState(blockPos2, blockState2, 2);
                        ++j;
                    }
                }

                return j > 0;
            } else {
                return false;
            }

    }
}
