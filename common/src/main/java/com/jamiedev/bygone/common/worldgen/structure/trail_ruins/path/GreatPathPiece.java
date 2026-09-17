package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator.PathSample;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments.SegmentState;
import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class GreatPathPiece extends StructurePiece {
    private final PlacementPass placementPass;

    private final int width;
    private final int bridgingDifference;

    GreatPathPiece(
        PlacementPass placementPass,
        int width, int bridgingDifference
    ) {
        super(BGStructures.TRAIL_PATH, 0,
            placementPass.state.createBoundingBox(placementPass.samples, width, bridgingDifference));
        this.placementPass = placementPass;
        this.width = width;
        this.bridgingDifference = bridgingDifference;
    }

    public GreatPathPiece(
        StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag
    ) {
        super(BGStructures.TRAIL_PATH, compoundTag);
        this.placementPass = PlacementPass.CODEC.parse(NbtOps.INSTANCE,
            compoundTag.get("PlacementPass")).getOrThrow();
        this.width = compoundTag.getInt("Width");
        this.bridgingDifference = compoundTag.getInt("BridgingDifference");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        compoundTag.put("PlacementPass", PlacementPass.CODEC
            .encodeStart(NbtOps.INSTANCE, this.placementPass).getOrThrow());
        compoundTag.putInt("Width", this.width);
        compoundTag.putInt("BridgingDifference", this.bridgingDifference);
    }

    @Override
    public void postProcess(
        @NotNull WorldGenLevel worldGenLevel, @NotNull StructureManager structureManager,
        @NotNull ChunkGenerator chunkGenerator, @NotNull RandomSource randomSource,
        @NotNull BoundingBox boundingBox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos
    ) {
        this.placementPass.state.place(
            worldGenLevel, boundingBox, this.placementPass.samples,
            this.width, this.bridgingDifference
        );
    }

    record PlacementPass(
        SegmentState state, List<BlockPos> samples
    ) {
        static final Codec<PlacementPass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SegmentState.CODEC.fieldOf("State").forGetter(PlacementPass::state),
            BlockPos.CODEC.listOf().fieldOf("Samples").forGetter(PlacementPass::samples)
        ).apply(instance, PlacementPass::new));

        static void addSegment(
            HashMap<SegmentState, List<PlacementPass>> placementPasses,
            List<PathSample> samples
        ) {
            HashMap<SegmentState, List<BlockPos>> samplesByState = new HashMap<>();
            for (PathSample sample : samples) {
                sample.state().forEachPlacementState(state ->
                    samplesByState.computeIfAbsent(state, ignored
                        -> new ArrayList<>()).add(sample.position())
                );
            }
            samplesByState.forEach((state, positions) ->
                placementPasses.computeIfAbsent(state, ignored -> new ArrayList<>())
                    .add(new PlacementPass(state, positions))
            );
        }
    }

    public List<BlockPos> debugSamples() { return this.placementPass.samples; }
}
