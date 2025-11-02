package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.worldgen.feature.config.MegalithConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MegalithFeature extends Feature<MegalithConfig> {
    public MegalithFeature(Codec<MegalithConfig> configCodec) {
        super(configCodec);
    }

    private static float[][] createErosionMask(int x, int z) {
        float[][] mask = new float[x][z];
        float centerX = x / 2.0f;
        float centerZ = z / 2.0f;
        float maxDistSquared = centerX * centerX + centerZ * centerZ;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < z; j++) {
                float dXSquared = (j - centerX) * (j - centerX);
                float dZSquared = (i - centerZ) * (i - centerZ);
                float distSquared = dXSquared + dZSquared;
                mask[i][j] = (float) StrictMath.sqrt(StrictMath.sqrt(distSquared / maxDistSquared));
            }
        }
        return mask;
    }

    @Override
    public boolean place(FeaturePlaceContext<MegalithConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        MegalithConfig megalithConfig = context.config();
        BlockPos blockPos = context.origin();
        int lengthX = megalithConfig.sizeX().sample(random);
        int lengthY = megalithConfig.sizeY().sample(random);
        int lengthZ = megalithConfig.sizeZ().sample(random);
        int fallenBlockCount = megalithConfig.fallenBlockCount().sample(random);
        int floatingBlockCount = megalithConfig.floatingBlockCount().sample(random);
        List<BlockPos> validFallenBlockCoords = new ArrayList<>();
        List<BlockPos> validFloatingBlockCoords = new ArrayList<>();

        float[][] erosionMask = createErosionMask(lengthX, lengthZ);

        // Notice: the booleans below only work if the loop order is x -> z -> y
        for (int x = 0; x < lengthX; x++) {
            for (int z = 0; z < lengthZ; z++) {
                boolean canErodeLayer = true;
                boolean lastBlockAir = true;
                for (int y = -megalithConfig.fallenBlockSearchRange(); y < lengthY; y++) {
                    BlockPos currPos = blockPos.offset(x, y, z);

                    // Initialize available fallen block positions
                    if (y < 0) {
                        boolean currBlockAir = level.isEmptyBlock(currPos);
                        if (currBlockAir) {
                            // Last block & current block air = floating position
                            if (lastBlockAir) {
                                // lastBlockAir stays true
                                validFloatingBlockCoords.add(currPos);
                            }
                            // Last block not air & current block air = floor position
                            else {
                                validFallenBlockCoords.add(currPos);
                            }
                            continue;
                        }
                        // Current block wasn't air
                        lastBlockAir = false;
                        continue;
                    }

                    // Main feature block placement

                    // Early exit, if it cannot replace blocks and the current block is not air
                    if (!megalithConfig.canReplaceBlocks()) {
                        if (!level.isEmptyBlock(currPos)) {
                            continue;
                        }
                    }

                    BlockState blockState = Blocks.AIR.defaultBlockState();

                    // Erosion layer placement
                    if (y < megalithConfig.maxErosionDepth()) {
                        float erosionDepthMask = 1 - ((float) y / megalithConfig.maxErosionDepth());
                        float combinedErosionChance = ((float) megalithConfig.erosionChance() * erosionMask[x][z] * erosionDepthMask);
                        if (canErodeLayer &&
                                random.nextFloat() < combinedErosionChance) {
                            this.setBlock(level, currPos, blockState);
                            continue;
                        }
                        canErodeLayer = false;
                    }

                    // Otherwise, use the default block provider
                    blockState = megalithConfig.megalithBlocks().getState(random, currPos);
                    this.setBlock(level, currPos, blockState);
                }
            }
        }
        placeFallenBlocks(validFallenBlockCoords, fallenBlockCount, megalithConfig.megalithBlocks(), random, level);
        placeFallenBlocks(validFloatingBlockCoords, floatingBlockCount, megalithConfig.megalithBlocks(), random, level);
        return true;
    }

    private void placeFallenBlocks(List<BlockPos> coords, int count, BlockStateProvider blockStateProvider, RandomSource random, WorldGenLevel level) {
        for (int i = coords.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Collections.swap(coords, i, j);
        }

        for (int i = 0; i < Math.min(count, coords.size()); i++) {
            BlockPos pos = coords.get(i);
            BlockState blockState = blockStateProvider.getState(random, pos);
            this.setBlock(level, pos, blockState);
        }
    }
}
