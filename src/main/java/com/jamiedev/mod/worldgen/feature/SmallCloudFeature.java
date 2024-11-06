package com.jamiedev.mod.worldgen.feature;

import com.jamiedev.mod.worldgen.feature.config.SmallCloudConfig;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SmallCloudFeature extends Feature<SmallCloudConfig>
{

    public SmallCloudFeature(Codec<SmallCloudConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<SmallCloudConfig> context) {
        StructureWorldAccess level = context.getWorld();
        Random random = context.getRandom();
        SmallCloudConfig config = context.getConfig();
        boolean posZ = random.nextBoolean();
        BlockPos blockPos = context.getOrigin().add(-random.nextInt(8), 0, (posZ ? 0 : 8) - random.nextInt(8));
        BlockState blockState = config.block().get(random, blockPos);

        int baseWidth = 3;
        int baseHeight = 1;

        for (int lengthCount = 0; lengthCount < config.bounds(); ++lengthCount) {
            boolean changeYChance = random.nextInt(7) > 5;
            blockPos = blockPos.add(random.nextInt(2), (changeYChance ? random.nextInt(3) - 1 : 0), random.nextInt(2) * (posZ ? 1 : -1));

            for (int x = 0; x < baseWidth + random.nextInt(3); ++x) {
                for (int y = 0; y < baseHeight + random.nextInt(2); ++y) {
                    for (int z = 0; z < baseWidth + random.nextInt(3); ++z) {
                        BlockPos newPosition = blockPos.add(x, y, z);
                        BlockState blockState2 = level.getBlockState(blockPos.add(x, y, z));
                        if (level.isAir(newPosition))
                        {
                            if (x + y + z < 9 + random.nextInt(16)) {
                                this.setBlockState(level, newPosition, blockState);
                            }
                        }

                    }
                }
            }
        }

        return true;
    }
}