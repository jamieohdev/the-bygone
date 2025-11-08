package com.jamiedev.bygone.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import org.jetbrains.annotations.NotNull;

public class BycoralMushroomFeature extends BycoralFeature {
    public BycoralMushroomFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeFeature(@NotNull LevelAccessor level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        int xSize = random.nextInt(3) + 3;
        int ySize = random.nextInt(3) + 3;
        int zSize = random.nextInt(3) + 3;
        int yOffset = random.nextInt(3) + 1;
        int posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();
        BlockPos.MutableBlockPos mutable = pos.mutable();

        for (int i = 0; i <= xSize; ++i) {
            for (int j = 0; j <= ySize; ++j) {
                for (int k = 0; k <= zSize; ++k) {
                    mutable.set(i + posX, j + posY, k + posZ);
                    mutable.move(Direction.DOWN, yOffset);
                    if ((i != 0 && i != xSize || j != 0 && j != ySize) && (k != 0 && k != zSize || j != 0 && j != ySize) && (i != 0 && i != xSize || k != 0 && k != zSize) && (i == 0 || i == xSize || j == 0 || j == ySize || k == 0 || k == zSize) && !(random.nextFloat() < 0.1F)) {
                        this.placeBycoralBlock(level, random, mutable, state);
                    }
                }
            }
        }

        return true;
    }
}
