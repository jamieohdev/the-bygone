package com.jamiedev.mod.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.MossBlock;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures;

public class AlphaMossBlock extends Block implements Fertilizable
{
    public static final MapCodec<AlphaMossBlock> CODEC = createCodec(AlphaMossBlock::new);

    @Override
    public MapCodec<AlphaMossBlock> getCodec() {
        return CODEC;
    }
    public AlphaMossBlock(Settings settings) {
        super(settings);
    }

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.up()).isAir();
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap((key) -> {
            return key.getEntry(UndergroundConfiguredFeatures.MOSS_PATCH_BONEMEAL);
        }).ifPresent((entry) -> {
            ((ConfiguredFeature)entry.value()).generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
        });
    }

    public Fertilizable.FertilizableType getFertilizableType() {
        return FertilizableType.NEIGHBOR_SPREADER;
    }
}
