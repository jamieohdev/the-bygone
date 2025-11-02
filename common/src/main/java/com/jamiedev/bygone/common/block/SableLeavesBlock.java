package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SableLeavesBlock extends LeavesBlock implements BonemealableBlock {
    public static final MapCodec<SableLeavesBlock> CODEC = simpleCodec(SableLeavesBlock::new);

    public SableLeavesBlock(Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<SableLeavesBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (random.nextInt(10) == 0) {
            BlockPos blockPos = pos.below();
            BlockState blockState = world.getBlockState(blockPos);
            if (!isFaceFull(blockState.getCollisionShape(world, blockPos), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(world, pos, random, (ParticleOptions) BGParticleTypes.SABLE_LEAVES);
            }
        }
    }

    public boolean isValidBonemealTarget(LevelReader p_256534_, BlockPos p_256299_, BlockState p_255926_) {
        return p_256534_.getBlockState(p_256299_.below()).isAir();
    }

    public boolean isBonemealSuccess(Level p_221437_, RandomSource p_221438_, BlockPos p_221439_, BlockState p_221440_) {
        return true;
    }

    public void performBonemeal(ServerLevel p_221427_, RandomSource p_221428_, BlockPos p_221429_, BlockState p_221430_) {
        p_221427_.setBlock(p_221429_.below(), SablenutBlock.createNewHangingNut(), 2);
    }
}
