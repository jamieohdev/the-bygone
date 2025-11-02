package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

import java.util.Optional;
import java.util.function.IntFunction;

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

    @Override
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

    @Override
    public StructureType<?> type() {
        return BGStructures.BYGONE_MINESHAFT;
    }

    public enum Type implements StringRepresentable {
        NORMAL("ancient", BGBlocks.ANCIENT_LOG.get(), BGBlocks.ANCIENT_PLANKS.get(), BGBlocks.ANCIENT_FENCE.get());

        public static final Codec<BygoneMineshaftStructure.Type> CODEC = StringRepresentable.fromEnum(BygoneMineshaftStructure.Type::values);
        private static final IntFunction<BygoneMineshaftStructure.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        private final String name;
        private final BlockState log;
        private final BlockState planks;
        private final BlockState fence;

        Type(final String name, final Block log, final Block planks, final Block fence) {
            this.name = name;
            this.log = log.defaultBlockState();
            this.planks = planks.defaultBlockState();
            this.fence = fence.defaultBlockState();
        }

        public static BygoneMineshaftStructure.Type byId(int id) {
            return BY_ID.apply(id);
        }

        public String getName() {
            return this.name;
        }

        public BlockState getLog() {
            return this.log;
        }

        public BlockState getPlanks() {
            return BGBlocks.ANCIENT_PLANKS.get().defaultBlockState();
        }

        public BlockState getFence() {
            return this.fence;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
