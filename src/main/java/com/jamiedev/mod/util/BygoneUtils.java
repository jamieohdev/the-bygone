package com.jamiedev.mod.util;

import com.google.common.primitives.Doubles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;

public class BygoneUtils
{
    public static BlockPos getLowestLand(ChunkGenerator chunkGenerator, NoiseConfig randomState, BlockPos centerPos, HeightLimitView heightLimitView, boolean canBeOnLiquid, boolean canBeInLiquid) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(centerPos.getX(), 1, centerPos.getZ());
        VerticalBlockSample blockView = chunkGenerator.getColumnSample(mutable.getX(), mutable.getZ(), heightLimitView, randomState);
        BlockState currentBlockstate = blockView.getState(mutable.getY());
        BlockState pastBlockstate = currentBlockstate;
        while (mutable.getY() <= getMaxTerrainLimit(chunkGenerator)) {
            if(canBeInLiquid && !currentBlockstate.getFluidState().isEmpty())
            {
                mutable.move(Direction.UP);
                return mutable;
            }
            else if((canBeOnLiquid || !pastBlockstate.getFluidState().isEmpty()) && currentBlockstate.isAir())
            {
                mutable.move(Direction.UP);
                return mutable;
            }

            mutable.move(Direction.UP);
            pastBlockstate = currentBlockstate;
            currentBlockstate = blockView.getState(mutable.getY());
        }

        return mutable;
    }

    public static int getMaxTerrainLimit(ChunkGenerator chunkGenerator) {
        return chunkGenerator.getMinimumY() + chunkGenerator.getWorldHeight();
    }

    public static BlockPos getRandomBlockposWithinRange(LivingEntity entity, int maxRadius, int minRadius) {
        BlockPos newBeePos;
        newBeePos = BlockPos.ofFloored(
                entity.getX() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1),
                Doubles.constrainToRange(entity.getY() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1), 1, 254),
                entity.getZ() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1));
        return newBeePos;
    }

    public static void centerAllPieces(BlockPos targetPos, List<? extends StructurePiece> pieces) {
        if(pieces.isEmpty()) return;

        Vec3i structureCenter = pieces.get(0).getBoundingBox().getCenter();
        int xOffset = targetPos.getX() - structureCenter.getX();
        int zOffset = targetPos.getZ() - structureCenter.getZ();

        for(StructurePiece structurePiece : pieces) {
            structurePiece.translate(xOffset, 0, zOffset);
        }
    }

}
