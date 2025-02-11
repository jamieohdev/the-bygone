package com.jamiedev.mod.common.worldgen.structure;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.jamiedev.mod.fabric.init.JamiesModStructures;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class BygoneMineshaftStructure extends Structure {
    public static final MapCodec<BygoneMineshaftStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(settingsCodec(instance), BygoneMineshaftStructure.Type.CODEC.fieldOf("mineshaft_type").forGetter((mineshaftStructure) -> {
            return mineshaftStructure.type;
        })).apply(instance, BygoneMineshaftStructure::new);
    });
    private final BygoneMineshaftStructure.Type type;

    public BygoneMineshaftStructure(Structure.StructureSettings config, BygoneMineshaftStructure.Type type) {
        super(config);
        this.type = type;
    }

    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        context.random().nextDouble();
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getMiddleBlockX(), 50, chunkPos.getMinBlockZ());
        StructurePiecesBuilder structurePiecesCollector = new StructurePiecesBuilder();
        int i = this.addPieces(structurePiecesCollector, context);
        return Optional.of(new Structure.GenerationStub(blockPos.offset(0, i, 0), Either.right(structurePiecesCollector)));
    }

    private int addPieces(StructurePiecesBuilder collector, Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom chunkRandom = context.random();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        BygoneMineshaftGenerator.BygoneMineshaftRoom mineshaftRoom = new BygoneMineshaftGenerator.BygoneMineshaftRoom(0, chunkRandom, chunkPos.getBlockX(2), chunkPos.getBlockZ(2), this.type);
        collector.addPiece(mineshaftRoom);
        mineshaftRoom.addChildren(mineshaftRoom, collector, chunkRandom);
        int i = chunkGenerator.getSeaLevel();

       return collector.moveBelowSeaLevel(i, chunkGenerator.getMinY() + 150, chunkRandom, 10);

    }

    public StructureType<?> type() {
        return JamiesModStructures.BYGONE_MINESHAFT;
    }

    public static enum Type implements StringRepresentable {
        NORMAL("ancient", JamiesModBlocks.ANCIENT_LOG, JamiesModBlocks.ANCIENT_PLANKS, JamiesModBlocks.ANCIENT_FENCE);

        public static final Codec<BygoneMineshaftStructure.Type> CODEC = StringRepresentable.fromEnum(BygoneMineshaftStructure.Type::values);
        private static final IntFunction<BygoneMineshaftStructure.Type> BY_ID = ByIdMap.continuous((ToIntFunction<Type>) Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        private final String name;
        private final BlockState log;
        private final BlockState planks;
        private final BlockState fence;

        private Type(final String name, final Block log, final Block planks, final Block fence) {
            this.name = name;
            this.log = log.defaultBlockState();
            this.planks = planks.defaultBlockState();
            this.fence = fence.defaultBlockState();
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
            return JamiesModBlocks.ANCIENT_PLANKS.defaultBlockState();
        }

        public BlockState getFence() {
            return this.fence;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
