package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SunkenCityStructure extends Structure
{
    public final HeightProvider height;
    public static final MapCodec<SunkenCityStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(settingsCodec(instance), HeightProvider.CODEC.fieldOf("height").forGetter((structure) -> {
            return structure.height;
        })).apply(instance, SunkenCityStructure::new);
    });

    private static final ResourceLocation[] SUNKEN_NBT = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_city/big_shellbrick_1"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_city/big_shellbrick_2"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_city/big_shellbrick_3"),
    };

    public SunkenCityStructure(Structure.StructureSettings settings, HeightProvider height) {
        super(settings);
        this.height = height;
    }

   /** public @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        Rotation rotation = Rotation.getRandom(context.random());
        LevelHeightAccessor levelHeight = context.heightAccessor();
        int y = context.chunkGenerator().getBaseHeight(context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeight, context.randomState());
        if (y > context.chunkGenerator().getSeaLevel()) {
            return Optional.empty();
        }
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), context.chunkGenerator().getMinY() + 15, context.chunkPos().getMinBlockZ());
        ResourceLocation res = Util.getRandom(SUNKEN_NBT, context.random());
        return Optional.of(new GenerationStub(blockpos, (piecesBuilder ->
                piecesBuilder.addPiece(new SunkenCityPiece.Piece(context.structureTemplateManager(), res, blockpos, rotation)))));
    }**/

   @Override
   public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
       WorldgenRandom chunkRandom = context.random();
       int i = context.chunkPos().getMinBlockX() + chunkRandom.nextInt(16);
       int j = context.chunkPos().getMinBlockZ() + chunkRandom.nextInt(16);
       int k = context.chunkGenerator().getSeaLevel();
       WorldGenerationContext heightContext = new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor());
       int l = this.height.sample(chunkRandom, heightContext);
       NoiseColumn verticalBlockSample = context.chunkGenerator().getBaseColumn(i, j, context.heightAccessor(), context.randomState());
       BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(i, l, j);

       while(l > k) {
           BlockState blockState = verticalBlockSample.getBlock(l);
           --l;
           BlockState blockState2 = verticalBlockSample.getBlock(l);
           if (blockState.is(Blocks.WATER)
                   && (blockState2.is(BGBlocks.SHELLSAND.get())
                   || (blockState2.is(BGBlocks.SHELLSTONE.get())
                   || blockState2.isFaceSturdy(EmptyBlockGetter.INSTANCE, mutable.setY(l), Direction.UP)))) {
               break;
           }
       }

       if (l <= k) {
           return Optional.empty();
       } else {
           BlockPos blockPos = new BlockPos(i, l, j);
           return Optional.of(new Structure.GenerationStub(blockPos, (holder) -> {
               SunkenCityPiece.addPieces(context.structureTemplateManager(), holder, chunkRandom, blockPos);
           }));
       }
   }

    @Override
    public StructureType<?> type() {
        return BGStructures.SUNKEN_CITY;
    }

}
