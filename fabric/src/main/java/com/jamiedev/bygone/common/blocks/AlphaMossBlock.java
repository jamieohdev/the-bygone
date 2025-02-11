package com.jamiedev.bygone.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class AlphaMossBlock extends Block implements BonemealableBlock
{
    public static final MapCodec<AlphaMossBlock> CODEC = simpleCodec(AlphaMossBlock::new);

    @Override
    public MapCodec<AlphaMossBlock> codec() {
        return CODEC;
    }
    public AlphaMossBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        world.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap((key) -> {
            return key.getHolder(CaveFeatures.MOSS_PATCH_BONEMEAL);
        }).ifPresent((entry) -> {
            entry.value().place(world, world.getChunkSource().getGenerator(), random, pos.above());
        });
    }

    @Override
    public BonemealableBlock.Type getType() {
        return Type.NEIGHBOR_SPREADER;
    }
}
