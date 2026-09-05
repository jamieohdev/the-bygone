package com.jamiedev.bygone.common.worldgen.structure.trail_ruins;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.buildings.BuildingGenerator;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathPiece;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.PathResolver;
import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GreatTrailRuinsStructure extends Structure {
    public static final MapCodec<GreatTrailRuinsStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        settingsCodec(instance),
        StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
        StructureTemplatePool.CODEC.fieldOf("top_pool").forGetter(structure -> structure.topPool),
        GreatTrailSettings.CODEC.fieldOf("trail_settings").forGetter(structure -> structure.trailSettings),
        HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight)
    ).apply(instance, GreatTrailRuinsStructure::new));

    public GreatTrailRuinsStructure(
        StructureSettings settings,
        Holder<StructureTemplatePool> startPool,
        Holder<StructureTemplatePool> topPool,
        GreatTrailSettings trailSettings,
        HeightProvider startHeight
    ) {
        super(settings);
        this.topPool = topPool;
        this.startPool = startPool;
        this.trailSettings = trailSettings;
        this.startHeight = startHeight;
    }

    private final Holder<StructureTemplatePool> startPool;
    private final Holder<StructureTemplatePool> topPool;
    private final GreatTrailSettings trailSettings;
    private final HeightProvider startHeight;

    @Override protected Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        int startY = this.startHeight.sample(
            generationContext.random(), new WorldGenerationContext(
            generationContext.chunkGenerator(), generationContext.heightAccessor()
        ));
        BlockPos startPosition = generationContext.chunkPos().getMiddleBlockPosition(startY);
        GreatPathGenerator generator = new GreatPathGenerator(generationContext, this.trailSettings);
        List<BlockPos> generatedNodes = generator.generateNodes(startPosition);
        if (generatedNodes.size() < 2) return Optional.empty();

        PathResolver.ResolvedPath resolvedPath = new PathResolver(generator)
            .resolve(generatedNodes);
        return Optional.of(new GenerationStub(resolvedPath.startPosition(), (pieces) -> {
            resolvedPath.addTo(pieces, this.trailSettings);

            BuildingGenerator buildingGenerator = new BuildingGenerator(
                generationContext, generator, this.trailSettings,
                this.startPool, this.topPool
            );
            buildingGenerator.place(resolvedPath, pieces);
        }));
    }

    // debug white concrete placement
//    @Override public void afterPlace(
//        @NotNull WorldGenLevel level, @NotNull StructureManager structureManager,
//        @NotNull ChunkGenerator chunkGenerator, @NotNull RandomSource random,
//        @NotNull BoundingBox chunkBounds, @NotNull ChunkPos chunkPos, @NotNull PiecesContainer pieces
//    ) {
//        super.afterPlace(level, structureManager, chunkGenerator, random, chunkBounds, chunkPos, pieces);
//
//        Set<BlockPos> pathPositions = new LinkedHashSet<>();
//        for (StructurePiece piece : pieces.pieces()) {
//            if (!(piece instanceof GreatPathPiece pathPiece)) continue;
//
//            for (BlockPos sample : pathPiece.debugSamples())
//                pathPositions.add(sample.above(6));
//        }
//
//        for (BlockPos position : pathPositions) {
//            if (chunkBounds.isInside(position))
//                level.setBlock(position, Blocks.WHITE_CONCRETE.defaultBlockState(), 2);
//        }
//    }

    @Override public StructureType<?> type() { return BGStructures.GREAT_TRAIL_RUINS; }
}
