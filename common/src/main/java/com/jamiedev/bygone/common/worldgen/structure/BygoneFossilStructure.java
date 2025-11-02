package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class BygoneFossilStructure extends Structure {
    public static final MapCodec<BygoneFossilStructure> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            settingsCodec(instance),
                            HeightProvider.CODEC.fieldOf("height").forGetter(structure -> structure.height)
                    ).apply(instance, BygoneFossilStructure::new)
            );

    private final HeightProvider height;

    public BygoneFossilStructure(Structure.StructureSettings config, HeightProvider height) {
        super(config);
        this.height = height;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        WorldgenRandom chunkRandom = context.random();
        ChunkPos chunkPos = context.chunkPos();

        int x = chunkPos.getMinBlockX() + chunkRandom.nextInt(16);
        int z = chunkPos.getMinBlockZ() + chunkRandom.nextInt(16);
        int y = this.height.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));

        int yMin = context.chunkGenerator().getMinY();

        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());

        boolean currBlockAir = columnOfBlocks.getBlock(y).isAir();
        y--;

        while (y > yMin) {
            BlockState belowState = columnOfBlocks.getBlock(y);
            boolean belowBlockAir = belowState.isAir();
            boolean belowBlockSolid = !belowBlockAir && !belowState.is(Blocks.WATER);

            if (currBlockAir && belowBlockSolid) {
                BlockPos blockPos = new BlockPos(x, y, z);
                return Optional.of(
                        new Structure.GenerationStub(blockPos,
                                holder ->
                                        BygoneFossilGenerator.addPieces(context.structureTemplateManager(),
                                                holder,
                                                chunkRandom,
                                                blockPos
                                        )
                        )
                );
            }
            y--;
            currBlockAir = belowBlockAir;
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return BGStructures.BYGONE_FOSSIL;
    }
}
