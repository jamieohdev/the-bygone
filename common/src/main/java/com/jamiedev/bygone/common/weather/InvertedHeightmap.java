package com.jamiedev.bygone.common.weather;

import net.minecraft.core.BlockPos;
import net.minecraft.util.BitStorage;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public class InvertedHeightmap {
    private final BitStorage data;
    private final ChunkAccess chunk;

    public boolean dirty = true;
    public InvertedHeightmap(ChunkAccess chunk) {
        this.chunk = chunk;
        int i = Mth.ceillog2(chunk.getHeight() + 1);
        this.data = new SimpleBitStorage(i, 256);
    }

    public void primeSelf() {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) encapsulatedPrime(mutableBlockPos, k, l);
        }
        dirty = false;
    }

    public void encapsulatedPrime(BlockPos.MutableBlockPos mutableBlockPos, int k, int l) {
        int startingBlock = chunk.getMinBuildHeight();
        for (int i1 = startingBlock + 1; i1 <= chunk.getMaxBuildHeight(); i1++) {
            mutableBlockPos.set(k, i1, l);
            BlockState blockstate = chunk.getBlockState(mutableBlockPos);
            if (!blockstate.is(Blocks.AIR)) {
                setHeight(k, l, i1 - 1);
                return;
            }
        }
    }

    public void setHeight(int x, int z, int value) {
        this.data.set(getIndex(x & 15, z & 15), value - this.chunk.getMinBuildHeight());
    }

    public int getHeight(int x, int z) {
        return this.data.get(getIndex(x & 15, z & 15)) + this.chunk.getMinBuildHeight();
    }

    private static int getIndex(int x, int z) {
        return x + z * 16;
    }
}
