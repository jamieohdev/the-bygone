package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum SegmentState implements StringRepresentable {
    TUNNEL("tunnel", TunnelSegment::new) {
        @Override
        public void forEachPlacementState(Consumer<SegmentState> consumer) {
            super.forEachPlacementState(consumer);
            consumer.accept(NORMAL);
        }
    },
    NORMAL("normal", () -> new GroundSegment(BGBlocks.ALPHA_MOSSY_CLAYSTONE_PATH)),
    BRIDGE("bridge", BridgeSegment::new);

    public static final Codec<SegmentState> CODEC = StringRepresentable.fromEnum(SegmentState::values);

    private final String identifier;
    private final PlacementMode placement;
    private final Supplier<? extends PlacementMode> placementFactory;

    SegmentState(String identifier, Supplier<? extends PlacementMode> placementFactory) {
        this.identifier = identifier;
        this.placementFactory = placementFactory;
        this.placement = placementFactory.get();
    }

    public BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference) {
        return this.placement.createBoundingBox(samples, width, bridgingDifference);
    }

    public void place(WorldGenLevel level, BoundingBox chunkBounds, List<BlockPos> samples, int width, int bridgingDifference) {
        this.placementFactory.get().place(level, chunkBounds, samples, width, bridgingDifference);
    }

    public boolean inlineTerrain() { return this.placement.inlineTerrain(); }
    public boolean rejectsSteepSlope() { return this.placement.rejectsSteepSlope(); }
    public boolean canPlaceBuildings() { return this.placement.canPlaceBuildings(); }

    public void forEachPlacementState(Consumer<SegmentState> consumer) {
        consumer.accept(this);
    }

    @Override public @NotNull String getSerializedName() {
        return this.identifier;
    }

    public abstract static class PlacementMode {
        public abstract BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference);

        protected abstract void innerPlace(
            WorldGenLevel level, BlockPos center,
            int sampleIndex, int x, int z,
            int distanceSquared, int radiusSquared, int bridgingDifference
        );

        public abstract boolean canPlaceBuildings();

        public void place(
            WorldGenLevel level, BoundingBox chunkBounds,
            List<BlockPos> samples, int width, int bridgingDifference
        ) {
            int radius = this.radius(width);
            int radiusSquared = radius * radius;
            for (int sampleIndex = 0; sampleIndex < samples.size(); sampleIndex++) {
                BlockPos center = samples.get(sampleIndex);
                for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                    for (int offsetZ = -radius; offsetZ <= radius; offsetZ++) {
                        int distanceSquared = offsetX * offsetX + offsetZ * offsetZ;
                        if (distanceSquared > radiusSquared) continue;

                        int x = center.getX() + offsetX;
                        int z = center.getZ() + offsetZ;
                        if (!chunkBounds.intersects(x, z, x, z)) continue;

                        this.innerPlace(
                            level, center, sampleIndex,
                            x, z, distanceSquared,
                            radiusSquared, bridgingDifference
                        );
                    }
                }
            }
        }

        protected int radius(int width) { return width; }
        public boolean inlineTerrain() { return false; }
        public boolean rejectsSteepSlope() { return false; }

        protected BoundingBox fromSamples(List<BlockPos> samples) {
            return BoundingBox.encapsulatingPositions(samples).orElseThrow();
        }

        protected BoundingBox boundingBox(
            BoundingBox centerLineBounds, int radius, int minY, int maxY
        ) {
            return BoundingBox.fromCorners(
                new BlockPos(centerLineBounds.minX(), minY, centerLineBounds.minZ()),
                new BlockPos(centerLineBounds.maxX(), maxY, centerLineBounds.maxZ())
            ).inflatedBy(radius, 0, radius);
        }
    }
}
