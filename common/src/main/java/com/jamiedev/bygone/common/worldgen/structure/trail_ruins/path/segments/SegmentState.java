package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public enum SegmentState implements StringRepresentable {
    TUNNEL("tunnel", new TunnelSegment()) {
        @Override
        public void forEachPlacementState(Consumer<SegmentState> consumer) {
            super.forEachPlacementState(consumer);
            consumer.accept(NORMAL);
        }
    },
    NORMAL("normal", new GroundSegment(() -> BGBlocks.ALPHA_MOSSY_CLAYSTONE_PATH.get().defaultBlockState())),
    BRIDGE("bridge", new BridgeSegment());

    public static final Codec<SegmentState> CODEC = StringRepresentable.fromEnum(SegmentState::values);

    private final String identifier;
    private final PlacementMode placement;

    SegmentState(String identifier, PlacementMode placement) {
        this.identifier = identifier;
        this.placement = placement;
    }

    public BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference) {
        return this.placement.createBoundingBox(samples, width, bridgingDifference);
    }

    public void place(WorldGenLevel level, BoundingBox chunkBounds, List<BlockPos> samples, int width, int bridgingDifference) {
        this.placement.place(level, chunkBounds, samples, width, bridgingDifference);
    }

    public boolean inlineTerrain() { return this.placement.inlineTerrain(); }
    public boolean rejectsSteepSlope() { return this.placement.rejectsSteepSlope(); }

    public void forEachPlacementState(Consumer<SegmentState> consumer) {
        consumer.accept(this);
    }

    @Override public @NotNull String getSerializedName() {
        return this.identifier;
    }

    public interface PlacementMode {
        BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference);
        void place(WorldGenLevel level, BoundingBox chunkBounds, List<BlockPos> samples, int width, int bridgingDifference);
        default boolean inlineTerrain() { return false; }
        default boolean rejectsSteepSlope() { return false; }

        default BoundingBox fromSamples(List<BlockPos> samples) {
            return BoundingBox.encapsulatingPositions(samples).orElseThrow();
        }

        default BoundingBox boundingBox(BoundingBox centerLineBounds, int radius, int minY, int maxY) {
            return BoundingBox.fromCorners(
                new BlockPos(centerLineBounds.minX(), minY, centerLineBounds.minZ()),
                new BlockPos(centerLineBounds.maxX(), maxY, centerLineBounds.maxZ())
            ).inflatedBy(radius, 0, radius);
        }
    }
}
