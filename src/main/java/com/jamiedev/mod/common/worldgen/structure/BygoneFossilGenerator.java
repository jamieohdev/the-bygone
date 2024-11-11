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

public class BygoneFossilGenerator 
{
    private static final Identifier[] FOSSILS = new Identifier[]{Identifier.of(JamiesModFabric.MOD_ID, "bygone_fossil/bygone_fossil_1"), Identifier.of(JamiesModFabric.MOD_ID, "bygone_fossil/bygone_fossil_2"),
            Identifier.of(JamiesModFabric.MOD_ID, "bygone_fossil/bygone_fossil_3"), Identifier.of(JamiesModFabric.MOD_ID, "bygone_fossil/bygone_fossil_4"),
            Identifier.ofVanilla("nether_fossils/fossil_1"), Identifier.ofVanilla("nether_fossils/fossil_2"), Identifier.ofVanilla("nether_fossils/fossil_3"), Identifier.ofVanilla("nether_fossils/fossil_4"), Identifier.ofVanilla("nether_fossils/fossil_5"), Identifier.ofVanilla("nether_fossils/fossil_6"), Identifier.ofVanilla("nether_fossils/fossil_7"), Identifier.ofVanilla("nether_fossils/fossil_8"), Identifier.ofVanilla("nether_fossils/fossil_9"), Identifier.ofVanilla("nether_fossils/fossil_10"), Identifier.ofVanilla("nether_fossils/fossil_11"), Identifier.ofVanilla("nether_fossils/fossil_12"), Identifier.ofVanilla("nether_fossils/fossil_13"), Identifier.ofVanilla("nether_fossils/fossil_14")};
    public BygoneFossilGenerator() {
    }

    public static void addPieces(StructureTemplateManager manager, StructurePiecesHolder holder, Random random, BlockPos pos) {
        BlockRotation blockRotation = BlockRotation.random(random);
        holder.addPiece(new BygoneFossilGenerator.Piece(manager, (Identifier) Util.getRandom(FOSSILS, random), pos, blockRotation));
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureTemplateManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(JamiesModStructures.FOSSIL_PIECES, 0, manager, template, template.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureTemplateManager manager, NbtCompound nbt) {
            super(JamiesModStructures.FOSSIL_PIECES, nbt, manager, (id) -> {
                return createPlacementData(BlockRotation.valueOf(nbt.getString("Rot")));
            });
        }

        public Piece(StructureContext structureContext, NbtCompound nbt) {
            super(JamiesModStructures.FOSSIL_PIECES, nbt, structureContext.structureTemplateManager(), (id) -> {
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
