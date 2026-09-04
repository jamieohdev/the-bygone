package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.*;
import java.util.function.Supplier;

record GroundSegment(Supplier<BlockState> blockGetter) implements SegmentState.PlacementMode {
    @Override public BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference) {
        BoundingBox centerLineBounds = this.fromSamples(samples);
        return this.boundingBox(
            centerLineBounds, width,
            centerLineBounds.minY() - bridgingDifference,
            centerLineBounds.maxY() + bridgingDifference
        );
    }

    private record CachedGroundKey(int x, int z, int startY) {}

    @Override public void place(
        WorldGenLevel level, BoundingBox chunkBounds,
        List<BlockPos> samples, int width, int bridgingDifference
    ) {
        Map<CachedGroundKey, Optional<BlockPos>> groundPositions = new HashMap<>();
        BlockState block = this.blockGetter.get();
        BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos();
        Set<Long> changedPositions = new HashSet<>();

        for (BlockPos center : samples) {
            for (int offsetX = -width; offsetX <= width; offsetX++) {
                for (int offsetZ = -width; offsetZ <= width; offsetZ++) {
                    int distanceSquared = offsetX * offsetX + offsetZ * offsetZ;
                    if (distanceSquared > width * width) continue;

                    int x = center.getX() + offsetX;
                    int z = center.getZ() + offsetZ;
                    if (!chunkBounds.intersects(x, z, x, z)) continue;

                    CachedGroundKey query = new CachedGroundKey(x, z, center.getY());
                    BlockPos targetPosition = groundPositions.computeIfAbsent(query, ignored
                        -> GreatPathGenerator.findGroundPosition(level, x, z, center.getY(), bridgingDifference
                    )).orElse(null);

                    if (targetPosition == null) continue;
                    target.set(targetPosition);
                    if (chunkBounds.isInside(target) && changedPositions.add(target.asLong()))
                        level.setBlock(target, block, 2);
                }
            }
        }
    }

    @Override
    public boolean inlineTerrain() { return true; }
}
