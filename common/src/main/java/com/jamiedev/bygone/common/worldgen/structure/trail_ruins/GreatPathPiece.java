package com.jamiedev.bygone.common.worldgen.structure.trail_ruins;

import com.jamiedev.bygone.core.registry.BGStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.List;
import java.util.Optional;

public final class GreatPathPiece extends StructurePiece {

    private final GreatPathGenerator.PathSegment segment;

    private final int width;
    private final int startY;

    GreatPathPiece(
        GreatPathGenerator.PathSegment segment,
        int width, int startY, int minBuildHeight
    ) {
        super(BGStructures.TRAIL_PATH, 0, fromSegment(segment, width, startY, minBuildHeight));
        this.segment = segment;
        this.width = width;
        this.startY = startY;
    }

    static BoundingBox fromSegment(
        GreatPathGenerator.PathSegment segment,
        int width, int startY, int minBuildHeight
    ) {
        BlockPos from = segment.from().groundPos();
        BlockPos to = segment.to().groundPos();
        return BoundingBox.fromCorners(
            new BlockPos(from.getX(), minBuildHeight, from.getZ()),
            new BlockPos(to.getX(), Math.max(startY, Math.max(from.getY(), to.getY())), to.getZ())
        ).inflatedBy(width, 0, width);
    }

    public GreatPathPiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super(BGStructures.TRAIL_PATH, compoundTag);
        this.segment = GreatPathGenerator.PathSegment.CODEC
            .parse(NbtOps.INSTANCE, compoundTag.get("SegmentState"))
            .getOrThrow();
        this.width = compoundTag.getInt("Width");
        this.startY = compoundTag.getInt("StartY");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        compoundTag.put("SegmentState", GreatPathGenerator.PathSegment
            .CODEC.encodeStart(NbtOps.INSTANCE, segment).getOrThrow());
        compoundTag.putInt("Width", this.width);
        compoundTag.putInt("StartY", this.startY);
    }

    @Override
    public void postProcess(
        WorldGenLevel worldGenLevel, StructureManager structureManager,
        ChunkGenerator chunkGenerator, RandomSource randomSource,
        BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos
    ) {
        BlockPos from = segment.from().groundPos();
        BlockPos to = segment.to().groundPos();
        List<BlockPos> centerLine = GreatPathGenerator.supercoverLine(from, to);
        int last = Math.max(1, centerLine.size() - 1);

        for (int index = 0; index < centerLine.size(); index++) {
            BlockPos horizontal = centerLine.get(index);
            double progress = (double) index / (double) last;
            int interpolatedSurfaceY = Mth.floor(Mth.lerp(progress, from.getY(), to.getY()) + 0.5D);

            for (int offsetX = -this.width; offsetX <= this.width; offsetX++) {
                for (int offsetZ = -this.width; offsetZ <= this.width; offsetZ++) {
                    int distanceSquared = offsetX * offsetX + offsetZ * offsetZ;
                    if (distanceSquared > (this.width * this.width)) continue;

                    int x = horizontal.getX() + offsetX;
                    int z = horizontal.getZ() + offsetZ;
                    if (!boundingBox.intersects(x, z, x, z)) continue;

//                    boolean edge = this.width > 1 && distanceSquared > (this.width - 1) * (this.width - 1);
                    Optional<BlockPos> optionalTarget = GreatPathGenerator.findGroundPosition(worldGenLevel, x, z, this.startY);
                    BlockPos targetPosition = optionalTarget.orElse(new BlockPos(x, interpolatedSurfaceY, z));
                    if (boundingBox.isInside(targetPosition)) {
                        BlockState block = segment.state().blockGetter.get();
                        worldGenLevel.setBlock(targetPosition, block, 2);
                    }
                }
            }
        }

        // debuuuug
        worldGenLevel.setBlock(from, Blocks.BAMBOO_SLAB.defaultBlockState(), 2);
        worldGenLevel.setBlock(to, Blocks.BAMBOO_SLAB.defaultBlockState(), 2);
    }
}
