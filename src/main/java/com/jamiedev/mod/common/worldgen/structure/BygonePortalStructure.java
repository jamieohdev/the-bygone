package com.jamiedev.mod.common.worldgen.structure;

import com.jamiedev.mod.fabric.init.JamiesModStructures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class BygonePortalStructure extends Structure
{
    public static final MapCodec<BygonePortalStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(configCodecBuilder(instance), HeightProvider.CODEC.fieldOf("height").forGetter((structure) -> {
            return structure.height;
        })).apply(instance, BygonePortalStructure::new);
    });
    public final HeightProvider height;

    public BygonePortalStructure(Config config, HeightProvider height) {
        super(config);
        this.height = height;
    }

    public Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkRandom chunkRandom = context.random();
        int i = context.chunkPos().getStartX() + chunkRandom.nextInt(16);
        int j = context.chunkPos().getStartZ() + chunkRandom.nextInt(16);
        int k = context.chunkGenerator().getSeaLevel();
        HeightContext heightContext = new HeightContext(context.chunkGenerator(), context.world());
        int l = this.height.get(chunkRandom, heightContext);
        VerticalBlockSample verticalBlockSample = context.chunkGenerator().getColumnSample(i, j, context.world(), context.noiseConfig());
        BlockPos.Mutable mutable = new BlockPos.Mutable(i, l, j);

        while(l > k) {
            BlockState blockState = verticalBlockSample.getState(l);
            --l;
            BlockState blockState2 = verticalBlockSample.getState(l);
            if (blockState.isAir() && (blockState2.isOf(Blocks.COBBLED_DEEPSLATE) || (blockState2.isOf(Blocks.DEEPSLATE) || blockState2.isSideSolidFullSquare(EmptyBlockView.INSTANCE, mutable.setY(l), Direction.UP)))) {
                break;
            }
        }

        if (l <= k) {
            return Optional.empty();
        } else {
            BlockPos blockPos = new BlockPos(i, l, j);
            return Optional.of(new StructurePosition(blockPos, (holder) -> {
                BygonePortalGenerator.addPieces(context.structureTemplateManager(), holder, chunkRandom, blockPos);
            }));
        }
    }

    public StructureType<?> getType() {
        return JamiesModStructures.BYGONE_PORTAL;
    }
}
