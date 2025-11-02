package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class MinilithStructure extends Structure {
    public static final MapCodec<MinilithStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(settingsCodec(instance), HeightProvider.CODEC.fieldOf("height").forGetter((structure) -> {
            return structure.height;
        })).apply(instance, MinilithStructure::new);
    });
    public final HeightProvider height;

    public MinilithStructure(Structure.StructureSettings config, HeightProvider height) {
        super(config);
        this.height = height;
    }

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

        while (l > k) {
            BlockState blockState = verticalBlockSample.getBlock(l);
            --l;
            BlockState blockState2 = verticalBlockSample.getBlock(l);
            if (blockState.isAir() && (blockState2.is(BGBlocks.ALPHA_MOSS_BLOCK.get()) || (blockState2.is(BGBlocks.BYSLATE.get()) || blockState2.isFaceSturdy(EmptyBlockGetter.INSTANCE, mutable.setY(l), Direction.UP)))) {
                break;
            }
        }

        if (l <= k) {
            return Optional.empty();
        } else {
            BlockPos blockPos = new BlockPos(i, l, j);
            return Optional.of(new Structure.GenerationStub(blockPos, (holder) -> {
                MinilithGenerator.addPieces(context.structureTemplateManager(), holder, chunkRandom, blockPos);
            }));
        }
    }

    @Override
    public StructureType<?> type() {
        return BGStructures.MINILITHS;
    }
}
