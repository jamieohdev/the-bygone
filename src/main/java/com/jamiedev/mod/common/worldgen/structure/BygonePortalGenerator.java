package com.jamiedev.mod.common.worldgen.structure;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.fabric.init.JamiesModStructures;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class BygonePortalGenerator 
{
    private static final Identifier[] PORTAL = new Identifier[]{Identifier.of(JamiesModFabric.MOD_ID, "bygone_portal/bygone_portal")};
    

    public static void addPieces(StructureTemplateManager manager, StructurePiecesHolder holder, Random random, BlockPos pos) {
        BlockRotation blockRotation = BlockRotation.random(random);
        holder.addPiece(new BygonePortalGenerator.Piece(manager, (Identifier) Util.getRandom(PORTAL, random), pos, blockRotation));
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureTemplateManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(JamiesModStructures.PORTAL_PIECE, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureTemplateManager manager, NbtCompound nbt) {
            super(JamiesModStructures.PORTAL_PIECE, nbt, manager, (id) -> {
                return createPlacementData(BlockRotation.valueOf(nbt.getString("Rot")));
            });
        }

        public Piece(StructureContext structureContext, NbtCompound nbt) {
            super(JamiesModStructures.PORTAL_PIECE, nbt, structureContext.structureTemplateManager(), (id) -> {
                return createPlacementData(BlockRotation.valueOf(nbt.getString("Rot")));
            });
        }


        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        }

        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
        }

        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            chunkBox.encompass(this.template.calculateBoundingBox(this.placementData, this.pos));
            super.generate(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, pivot);
        }
    }
}
