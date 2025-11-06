package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class SunkenCityStructure extends Structure {
    public final HeightProvider height;
    public static final MapCodec<SunkenCityStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(settingsCodec(instance),
                    HeightProvider.CODEC.fieldOf("height").forGetter(structure -> structure.height)
            ).apply(instance, SunkenCityStructure::new)
    );

    private static final ResourceLocation[] SUNKEN_NBT = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_city/big_shellbrick_1"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_city/big_shellbrick_2"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_city/big_shellbrick_3"),
    };

    public SunkenCityStructure(Structure.StructureSettings settings, HeightProvider height) {
        super(settings);
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

        boolean currBlockWater = columnOfBlocks.getBlock(y).is(Blocks.WATER);
        y--;

        while (y > yMin) {
            BlockState belowState = columnOfBlocks.getBlock(y);
            boolean belowBlockWater = belowState.is(Blocks.WATER);
            boolean belowBlockSolid = !belowBlockWater && !belowState.isAir();

            if (currBlockWater && belowBlockSolid) {
                BlockPos blockPos = new BlockPos(x, y, z);
                return Optional.of(
                        new Structure.GenerationStub(blockPos, holder ->
                                SunkenCityPiece.addPieces(context.structureTemplateManager(),
                                        holder,
                                        chunkRandom,
                                        blockPos
                                )
                        )
                );
            }
            y--;
            currBlockWater = belowBlockWater;
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return BGStructures.SUNKEN_CITY;
    }

}
