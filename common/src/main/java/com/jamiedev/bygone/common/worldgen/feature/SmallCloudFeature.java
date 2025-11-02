package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.worldgen.feature.config.SmallCloudConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SmallCloudFeature extends Feature<SmallCloudConfig> {

    public SmallCloudFeature(Codec<SmallCloudConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SmallCloudConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        SmallCloudConfig config = context.config();
        boolean posZ = random.nextBoolean();
        BlockPos blockPos = context.origin().offset(-random.nextInt(8), 0, (posZ ? 0 : 8) - random.nextInt(8));
        BlockState blockState = config.block().getState(random, blockPos);

        int baseWidth = 3;
        int baseHeight = 1;

        for (int lengthCount = 0; lengthCount < config.bounds(); ++lengthCount) {
            boolean changeYChance = random.nextInt(7) > 5;
            blockPos = blockPos.offset(random.nextInt(2), (changeYChance ? random.nextInt(3) - 1 : 0), random.nextInt(2) * (posZ ? 1 : -1));

            for (int x = 0; x < baseWidth + random.nextInt(3); ++x) {
                for (int y = 0; y < baseHeight + random.nextInt(2); ++y) {
                    for (int z = 0; z < baseWidth + random.nextInt(3); ++z) {
                        BlockPos newPosition = blockPos.offset(x, y, z);
                        BlockState blockState2 = level.getBlockState(blockPos.offset(x, y, z));
                        if (level.isEmptyBlock(newPosition)) {
                            if (x + y + z < 9 + random.nextInt(16)) {
                                this.setBlock(level, newPosition, blockState);
                            }
                        }

                    }
                }
            }
        }

        return true;
    }
}