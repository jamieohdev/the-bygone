package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.buildings;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.GreatTrailRuinsStructure;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GreatTrailBuildingPiece extends PoolElementStructurePiece {
    public GreatTrailBuildingPiece(
        StructureTemplateManager templates,
        StructurePoolElement element,
        BlockPos position,
        Rotation rotation,
        BoundingBox boundingBox
    ) {
        super(
            templates, element, position,
            element.getGroundLevelDelta(),
            rotation, boundingBox,
            LiquidSettings.APPLY_WATERLOGGING
        );
    }

    public GreatTrailBuildingPiece(
        StructurePieceSerializationContext context,
        CompoundTag tag
    ) {
        super(context, tag);
    }

    private static final Supplier<Block> SUPPORT_SOLID = BGBlocks.CLAYSTONE_BRICKS;
    private static final Supplier<Block> SUPPORT_WALL = BGBlocks.CLAYSTONE_BRICKS_WALL;

    @Override public @NotNull StructurePieceType getType() { return BGStructures.TRAIL_BUILDING; }

    @Override public void postProcess(
        @NotNull WorldGenLevel level, @NotNull StructureManager structureManager,
        @NotNull ChunkGenerator chunkGenerator, @NotNull RandomSource random,
        @NotNull BoundingBox box, @NotNull ChunkPos chunkPos, @NotNull BlockPos pivot
    ) {
        super.postProcess(
            level, structureManager,
            chunkGenerator, random,
            box, chunkPos, pivot
        );

        // pillars
        this.getBoundingBox().forAllCorners((blockPos) -> {
            BlockPos belowPos = blockPos.below();
            if (belowPos.getY() >= boundingBox.minY()) return;

            if (belowPos.getX() < box.minX() || belowPos.getX() > box.maxX()
            || belowPos.getZ() < box.minZ() || belowPos.getZ() > box.maxZ()) return;

            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            mutableBlockPos.set(belowPos);
            while (mutableBlockPos.getY() >= level.getMinBuildHeight()) {
                BlockState currentBlockstate = level.getBlockState(mutableBlockPos);
                if (GreatPathGenerator.isValidGround(currentBlockstate, mutableBlockPos)) break;

                Supplier<Block> supplier = SUPPORT_WALL;
                if (belowPos.equals(mutableBlockPos)) supplier = SUPPORT_SOLID;
                else {
                    BlockPos belowCurrent = mutableBlockPos.below();
                    if (GreatPathGenerator.isValidGround(level.getBlockState(belowCurrent), belowCurrent))
                        supplier = SUPPORT_SOLID;
                }

                level.setBlock(
                    mutableBlockPos, supplier.get()
                    .defaultBlockState(), 2
                );

                mutableBlockPos.setY(mutableBlockPos.getY() - 1);
            }

        });
    }

}
