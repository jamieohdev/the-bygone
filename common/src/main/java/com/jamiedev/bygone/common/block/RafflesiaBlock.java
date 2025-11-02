package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RafflesiaBlock extends Block {
    public static final MapCodec<SporeBlossomBlock> CODEC = simpleCodec(SporeBlossomBlock::new);
    private static final VoxelShape SHAPE = Block.box(2.0D, 1.0D, 2.0D,
            14.0D, 4.0D, 14.0D);
    private static final int field_31252 = 14;
    private static final int field_31253 = 10;
    private static final int field_31254 = 10;

    public RafflesiaBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<SporeBlossomBlock> codec() {
        return CODEC;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(BlockTags.DIRT) || floor.is(Blocks.FARMLAND);
    }


    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        return this.canPlantOnTop(world.getBlockState(blockPos), world, blockPos);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !this.canSurvive(state, world, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, @NotNull RandomSource random) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        double d = i + random.nextDouble();
        double e = j + 0.7;
        double f = k + random.nextDouble();
        world.addParticle((ParticleOptions) BGParticleTypes.RAFFLESIA_SPORES, d, e, f, 0.0, 0.0, 0.0);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int l = 0; l < 14; ++l) {
            mutable.set(i + Mth.nextInt(random, -10, 10), j - random.nextInt(10), k + Mth.nextInt(random, -10, 10));
            BlockState blockState = world.getBlockState(mutable);
            if (!blockState.isCollisionShapeFullBlock(world, mutable)) {
                world.addParticle((ParticleOptions) BGParticleTypes.RAFFLESIA_SPORES, (double) mutable.getX() + random.nextDouble(), (double) mutable.getY() + random.nextDouble(), (double) mutable.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
            }
        }

    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
