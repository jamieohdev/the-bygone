package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import org.jetbrains.annotations.NotNull;

public class BycoralPillarsFeature extends BycoralFeature {
    public BycoralPillarsFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeFeature(@NotNull LevelAccessor level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        int xSize = random.nextInt(3) + random.nextInt(3) + 4;
        int zSize = random.nextInt(3) + random.nextInt(3) + 4;
        int yOffset = 0; // random.nextInt(3) + 1
        int posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();
        BlockPos.MutableBlockPos mutable = pos.mutable();

        // Iterate in a cornerless square.
        for (int i = 0; i <= xSize; ++i) {
            for (int k = 0; k <= zSize; ++k) {
                mutable.set(i + posX, posY, k + posZ);
                mutable.move(Direction.DOWN, yOffset);
                if (!((i == 0 && k == 0) || (i == 0 && k == zSize) || (i == xSize && k == 0) || (i == xSize && k == zSize))) {
                    // Place pillars upward.
                    // Find distance from edge in x and z
                    int xDistFromEdge = Math.min(i, xSize - i);
                    int zDistFromEdge = Math.min(k, zSize - k);
                    // Pillars are a minimum of one block high.
                    int pillarHeight = 1;
                    // They are increased by a random value up and including to the distance from each edge.
                    // This should make them taller in the center.
                    pillarHeight += random.nextInt(xDistFromEdge + 1);
                    pillarHeight += random.nextInt(zDistFromEdge + 1);
                    // One out of every five is set down to be one block tall.
                    if (random.nextFloat() < 0.2F) {
                        pillarHeight = 1;
                    }

                    // Seek for the floor.
                    BlockPos foundFloor = findFloorInColumn(
                            level,
                            mutable,
                            10,
                            JamiesModTag.CORAL_BLOCK_REPLACEABLE,
                            JamiesModTag.CORAL_BLOCKS
                    );
                    if (foundFloor != null) {
                        mutable.set(foundFloor);
                        // Build a pillar of this height.j=1 and <= to simplify checking for the top.
                        for (int j = 1; j <= pillarHeight; j++) {

                            this.placeBycoralBlock(
                                    level,
                                    random,
                                    mutable,
                                    state,
                                    j == pillarHeight && pillarHeight != 1
                            );
                            // Move upward.
                            mutable.move(Direction.UP);
                        }
                    }
                }
            }
        }

        return true;
    }
}
