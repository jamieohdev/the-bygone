package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AlphaMossBlock extends Block implements BonemealableBlock {

    public AlphaMossBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean b) {
        return world.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level world, @NotNull RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, @NotNull RandomSource random, BlockPos pos, BlockState state) {
        world.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap((key) -> {
            return key.getHolder(BGConfiguredFeatures.ALPHA_MOSS_PATCH_BONEMEAL);
        }).ifPresent((entry) -> {
            entry.value().place(world, world.getChunkSource().getGenerator(), random, pos.above());
        });
    }
}
