package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AncientLeavesBlock extends LeavesBlock {
    public static final MapCodec<AncientLeavesBlock> CODEC = simpleCodec(AncientLeavesBlock::new);

    public AncientLeavesBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<AncientLeavesBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, world, pos, random);
        if (random.nextInt(10) == 0) {
            BlockPos blockPos = pos.below();
            BlockState blockState = world.getBlockState(blockPos);
            if (!isFaceFull(blockState.getCollisionShape(world, blockPos), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(world, pos, random, (ParticleOptions) BGParticleTypes.ANCIENT_LEAVES);
            }
        }
    }
}
