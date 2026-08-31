package com.jamiedev.bygone.common.worldgen.structure.trail_ruins;

import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;
import java.util.Optional;

public class GreatTrailRuinsStructure extends Structure {
    public static final MapCodec<GreatTrailRuinsStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        settingsCodec(instance),
        StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
        StructureTemplatePool.CODEC.fieldOf("top_pool").forGetter(structure -> structure.startPool),
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
        List<GreatPathGenerator.PathNode> generatedNodes = generator.generateNodes(startPosition);
        if (generatedNodes.isEmpty() || generatedNodes.size() < 2) return Optional.empty();

        GreatPathGenerator.ResolvedPath resolvedPath = generator.resolvePath(generatedNodes);
        return Optional.of(new GenerationStub(resolvedPath.nodes().getFirst().groundPos(), resolvedPath::rasterize));
    }

    @Override public StructureType<?> type() { return BGStructures.GREAT_TRAIL_RUINS; }
}
