package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
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
    private final BlockState boardwalkBlock;

    GroundSegment(Supplier<Block> block) {
        this.block = block.get().defaultBlockState();
        this.boardwalkBlock = BGBlocks.ANCIENT_PLANKS
            .get().defaultBlockState();
    }

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

        boolean submergedPath = level.getFluidState(targetPosition).is(FluidTags.WATER)
            || level.getFluidState(targetPosition.above()).is(FluidTags.WATER);
        BlockPos placementPosition = submergedPath ? this.highestWaterPosition(level, targetPosition) : targetPosition;

        // hacky lol. todo pass changed positions to the structure and use it in every segment
        // would probably fix some other issues but idr wanna do that rn
        if (level.getBlockState(placementPosition.above()).is(boardwalkBlock.getBlock())
        || level.getBlockState(placementPosition).is(boardwalkBlock.getBlock())) return;

        BlockState placement = submergedPath ? boardwalkBlock : this.block;
        if (this.changedPositions.add(placementPosition)) level.setBlock(placementPosition, placement, 2);
    }

    private BlockPos highestWaterPosition(WorldGenLevel level, BlockPos groundPosition) {
        BlockPos.MutableBlockPos waterPosition = groundPosition.mutable();
        if (!level.getFluidState(waterPosition).is(FluidTags.WATER))
            waterPosition.setY(waterPosition.getY() + 1);
        while (waterPosition.getY() < level.getMaxBuildHeight() - 1 &&
            level.getFluidState(waterPosition.above()).is(FluidTags.WATER)
        ) waterPosition.setY(waterPosition.getY() + 1);
        return waterPosition.immutable();
    }

    @Override
    public boolean inlineTerrain() { return true; }
}
