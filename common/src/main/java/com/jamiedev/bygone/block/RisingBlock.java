package com.jamiedev.bygone.block;

import com.jamiedev.bygone.common.entities.RisingBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RisingBlock  extends Block implements Fallable {
    public RisingBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    protected abstract MapCodec<? extends RisingBlock> codec();

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleTick(pos, this, this.getRiseDelay());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        world.scheduleTick(pos, this, this.getRiseDelay());
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (canRiseThrough(world.getBlockState(pos.above())) && pos.getY() >= world.getMaxBuildHeight()) {
            RisingBlockEntity fallingBlockEntity = RisingBlockEntity.fall(world, pos, state);
            this.configureRisingBlockEntity(fallingBlockEntity);
        }
    }

    protected void configureRisingBlockEntity(RisingBlockEntity entity) {
    }

    protected int getRiseDelay() {
        return 2;
    }

    public static boolean canRiseThrough(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced();
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.above();
            if (canRiseThrough(world.getBlockState(blockPos))) {
                ParticleUtils.spawnParticleBelow(world, pos, random, new BlockParticleOption(ParticleTypes.FALLING_DUST, state));
            }
        }

    }

    public int getColor(BlockState state, BlockGetter world, BlockPos pos) {
        return -16777216;
    }
}
