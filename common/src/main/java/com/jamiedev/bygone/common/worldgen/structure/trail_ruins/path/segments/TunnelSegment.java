package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

record TunnelSegment() implements SegmentState.PlacementMode {
    private static final int PADDING = 8;

    @Override
    public BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference) {
        BoundingBox centerLineBounds = this.fromSamples(samples);
        int radius = width + PADDING;
        return this.boundingBox(
            centerLineBounds, radius,
            centerLineBounds.minY(),
            centerLineBounds.maxY() + radius
        );
    }

    @Override
    public void place(WorldGenLevel level, BoundingBox chunkBounds, List<BlockPos> samples, int width, int bridgingDifference) {
        int radius = width + PADDING;
        int radiusSquared = radius * radius;
        BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos();
        Set<Long> changedPositions = new HashSet<>();
        for (BlockPos center : samples) {
            for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                for (int offsetZ = -radius; offsetZ <= radius; offsetZ++) {
                    int horizontalDistanceSquared = offsetX * offsetX + offsetZ * offsetZ;
                    if (horizontalDistanceSquared >= radiusSquared) continue;

                    int x = center.getX() + offsetX;
                    int z = center.getZ() + offsetZ;
                    if (!chunkBounds.intersects(x, z, x, z)) continue;

                    int height = (int) Math.sqrt(radiusSquared - horizontalDistanceSquared);
                    for (int offsetY = 1; offsetY <= height; offsetY++) {
                        target.set(x, center.getY() + offsetY, z);
                        if (chunkBounds.isInside(target) && changedPositions.add(target.asLong()))
                            level.setBlock(target, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }
    }
}
