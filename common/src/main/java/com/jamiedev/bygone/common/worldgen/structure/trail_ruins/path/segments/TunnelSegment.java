package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TunnelSegment extends SegmentState.PlacementMode {
    private static final int PADDING = 8;

    private final BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos();
    private final Set<BlockPos> changedPositions = new HashSet<>();

    @Override public boolean canPlaceBuildings() { return true; }

    @Override
    public BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference) {
        BoundingBox centerLineBounds = this.fromSamples(samples);
        int radius = this.radius(width);
        return this.boundingBox(
            centerLineBounds, radius,
            centerLineBounds.minY(),
            centerLineBounds.maxY() + radius
        );
    }

    @Override protected void innerPlace(
        WorldGenLevel level, BlockPos center,
        int sampleIndex, int x, int z,
        int distanceSquared, int radiusSquared, int bridgingDifference
    ) {
        int height = (int) Math.sqrt(radiusSquared - distanceSquared);
        for (int offsetY = 1; offsetY <= height; offsetY++) {
            this.target.set(x, center.getY() + offsetY, z);
            if (this.changedPositions.add(this.target.immutable()))
                level.setBlock(this.target, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    @Override protected int radius(int width) { return width + PADDING; }
}
