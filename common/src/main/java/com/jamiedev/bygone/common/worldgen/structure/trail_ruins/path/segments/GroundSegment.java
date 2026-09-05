package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.*;
import java.util.function.Supplier;

class GroundSegment extends SegmentState.PlacementMode {

    private final Map<CachedGroundKey, Optional<BlockPos>> groundPositions = new HashMap<>();
    private final Set<BlockPos> changedPositions = new HashSet<>();
    private final BlockState block;

    GroundSegment(Supplier<Block> block) { this.block = block.get().defaultBlockState(); }

    @Override public boolean canPlaceBuildings() { return true; }

    @Override public BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference) {
        BoundingBox centerLineBounds = this.fromSamples(samples);
        return this.boundingBox(
            centerLineBounds, this.radius(width),
            centerLineBounds.minY() - bridgingDifference,
            centerLineBounds.maxY() + bridgingDifference
        );
    }

    private record CachedGroundKey(int x, int z, int startY) {}

    @Override public void place(
        WorldGenLevel level, BoundingBox chunkBounds,
        List<BlockPos> samples, int width, int bridgingDifference
    ) {
        super.place(level, chunkBounds, samples, width, bridgingDifference);
    }

    @Override protected void innerPlace(
        WorldGenLevel level, BlockPos center,
        int sampleIndex, int x, int z,
        int distanceSquared, int radiusSquared, int bridgingDifference
    ) {
        CachedGroundKey query = new CachedGroundKey(x, z, center.getY());
        BlockPos targetPosition = this.groundPositions.computeIfAbsent(query, ignored
            -> GreatPathGenerator.findGroundPosition(level, x, z, center.getY(), bridgingDifference)
        ).orElse(null);

        if (targetPosition == null) return;

        if (this.changedPositions.add(targetPosition))
            level.setBlock(targetPosition, this.block, 2);
    }

    @Override
    public boolean inlineTerrain() { return true; }
}
