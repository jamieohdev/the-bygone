package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.Optional;

public class AncientRootStructure extends Structure {

    public static final MapCodec<AncientRootStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(AncientRootStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter),
                    DimensionPadding.CODEC.optionalFieldOf("dimension_padding", JigsawStructure.DEFAULT_DIMENSION_PADDING).forGetter(structure -> structure.dimensionPadding),
                    LiquidSettings.CODEC.optionalFieldOf("liquid_settings", JigsawStructure.DEFAULT_LIQUID_SETTINGS).forGetter(structure -> structure.liquidSettings)
            ).apply(instance, AncientRootStructure::new)
    );

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final DimensionPadding dimensionPadding;
    private final LiquidSettings liquidSettings;

    public AncientRootStructure(Structure.StructureSettings config,
                                Holder<StructureTemplatePool> startPool,
                                Optional<ResourceLocation> startJigsawName,
                                int size,
                                HeightProvider startHeight,
                                Optional<Heightmap.Types> projectStartToHeightmap,
                                int maxDistanceFromCenter,
                                DimensionPadding dimensionPadding,
                                LiquidSettings liquidSettings) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.dimensionPadding = dimensionPadding;
        this.liquidSettings = liquidSettings;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom chunkRandom = context.random();
        ChunkGenerator chunkGenerator = context.chunkGenerator();

        int x = chunkPos.getMinBlockX() + chunkRandom.nextInt(16);
        int z = chunkPos.getMinBlockZ() + chunkRandom.nextInt(16);
        int y = this.startHeight.sample(context.random(), new WorldGenerationContext(chunkGenerator, context.heightAccessor()));

        int yMin = chunkGenerator.getMinY();

        NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(x, z, context.heightAccessor(), context.randomState());

        boolean currBlockAir = columnOfBlocks.getBlock(y).isAir();
        y--;

        while (y > yMin) {
            BlockState belowState = columnOfBlocks.getBlock(y);
            boolean belowBlockAir = belowState.isAir();
            boolean belowBlockSolid = !belowBlockAir && !belowState.is(Blocks.WATER);

            if (currBlockAir && belowBlockSolid) {
                return JigsawPlacement.addPieces(
                        context,
                        startPool,
                        startJigsawName,
                        size,
                        new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ()),
                        false,
                        projectStartToHeightmap,
                        maxDistanceFromCenter,
                        PoolAliasLookup.EMPTY, // Optional thing that allows swapping a template pool with another per structure json instance. We don't need this but see vanilla JigsawStructure class for how to wire it up if you want it.
                        dimensionPadding,
                        liquidSettings
                );
            }
            y--;
            currBlockAir = belowBlockAir;
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return BGStructures.ANCIENT_ROOTS;
    }
}
