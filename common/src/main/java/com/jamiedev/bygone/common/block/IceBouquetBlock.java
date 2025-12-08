package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class IceBouquetBlock extends Block {
    public static final MapCodec<IceBouquetBlock> CODEC = simpleCodec(IceBouquetBlock::new);
    protected static final VoxelShape DOWN_AABB = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 1.0F, 16.0F);
    public final float iceDamage;

    public IceBouquetBlock(Properties properties) {
        this(properties, 2.0f);
    }

    public IceBouquetBlock(Properties properties, float iceDamage) {
        super(properties);
        this.iceDamage = iceDamage;
    }

    public static boolean canSurviveOnBlock(BlockState state) {
        return !state.is(JamiesModTag.ICE_BOUQUET_FORBIDDEN);
    }

    @Override
    public @NotNull MapCodec<IceBouquetBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, RandomSource random) {
        if (random.nextInt(24) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5F,
                    pos.getY() + 0.5F,
                    pos.getZ() + 0.5F,
                    BGSoundEvents.WRAITH_FLY_ADDITIONS_EVENT,
                    SoundSource.BLOCKS,
                    1.0F + random.nextFloat(),
                    random.nextFloat() * 0.7F + 0.3F,
                    false
            );
        }
    }

    @Override
    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return this.canSurvive(state, level, currentPos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        return canSurviveOnBlock(belowState) && belowState.isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return DOWN_AABB;
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {

        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getType().is(JamiesModTag.ICE_BOUQUET_HEALS)) {
                livingEntity.heal(0.10F);
            } else {
                livingEntity.hurt(level.damageSources().freeze(), this.iceDamage);
            }

        }

        super.entityInside(state, level, pos, entity);
    }
}
