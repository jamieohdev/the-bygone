package com.jamiedev.mod.worldgen.structure;

import com.jamiedev.mod.init.JamiesModBlocks;
import com.jamiedev.mod.init.JamiesModStructures;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class BygoneMineshaftStructure extends Structure {
    public static final MapCodec<BygoneMineshaftStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(configCodecBuilder(instance), BygoneMineshaftStructure.Type.CODEC.fieldOf("mineshaft_type").forGetter((mineshaftStructure) -> {
            return mineshaftStructure.type;
        })).apply(instance, BygoneMineshaftStructure::new);
    });
    private final BygoneMineshaftStructure.Type type;

    public BygoneMineshaftStructure(Structure.Config config, BygoneMineshaftStructure.Type type) {
        super(config);
        this.type = type;
    }

    public Optional<StructurePosition> getStructurePosition(Structure.Context context) {
        context.random().nextDouble();
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getCenterX(), 50, chunkPos.getStartZ());
        StructurePiecesCollector structurePiecesCollector = new StructurePiecesCollector();
        int i = this.addPieces(structurePiecesCollector, context);
        return Optional.of(new Structure.StructurePosition(blockPos.add(0, i, 0), Either.right(structurePiecesCollector)));
    }

    private int addPieces(StructurePiecesCollector collector, Structure.Context context) {
        ChunkPos chunkPos = context.chunkPos();
        ChunkRandom chunkRandom = context.random();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        BygoneMineshaftGenerator.BygoneMineshaftRoom mineshaftRoom = new BygoneMineshaftGenerator.BygoneMineshaftRoom(0, chunkRandom, chunkPos.getOffsetX(2), chunkPos.getOffsetZ(2), this.type);
        collector.addPiece(mineshaftRoom);
        mineshaftRoom.fillOpenings(mineshaftRoom, collector, chunkRandom);
        int i = chunkGenerator.getSeaLevel();

       return collector.shiftInto(i, chunkGenerator.getMinimumY() + 150, chunkRandom, 10);

    }

    public StructureType<?> getType() {
        return JamiesModStructures.BYGONE_MINESHAFT;
    }

    public static enum Type implements StringIdentifiable {
        NORMAL("ancient", JamiesModBlocks.ANCIENT_LOG, JamiesModBlocks.ANCIENT_PLANKS, JamiesModBlocks.ANCIENT_FENCE);

        public static final Codec<BygoneMineshaftStructure.Type> CODEC = StringIdentifiable.createCodec(BygoneMineshaftStructure.Type::values);
        private static final IntFunction<BygoneMineshaftStructure.Type> BY_ID = ValueLists.createIdToValueFunction((ToIntFunction<Type>) Enum::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO);
        private final String name;
        private final BlockState log;
        private final BlockState planks;
        private final BlockState fence;

        private Type(final String name, final Block log, final Block planks, final Block fence) {
            this.name = name;
            this.log = log.getDefaultState();
            this.planks = planks.getDefaultState();
            this.fence = fence.getDefaultState();
        }

        public String getName() {
            return this.name;
        }

        public static BygoneMineshaftStructure.Type byId(int id) {
            return BY_ID.apply(id);
        }

        public BlockState getLog() {
            return this.log;
        }

        public BlockState getPlanks() {
            return JamiesModBlocks.ANCIENT_PLANKS.getDefaultState();
        }

        public BlockState getFence() {
            return this.fence;
        }

        public String asString() {
            return this.name;
        }
    }
}
