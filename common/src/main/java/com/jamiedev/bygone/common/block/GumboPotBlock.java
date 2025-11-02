package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GumboPotBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final CauldronInteraction.InteractionMap NOOP = CauldronInteraction.newInteractionMap("noop");
    public static final Property<Boolean> HEATED = BooleanProperty.create("heated");
    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 8;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", MIN_LEVEL, MAX_LEVEL);
    public static final MapCodec<GumboPotBlock> CODEC = simpleCodec(GumboPotBlock::new);

    public GumboPotBlock(Properties properties) {
        super(properties, NOOP);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, MIN_LEVEL).setValue(HEATED, false));
    }

    public static boolean canFitAdditionalIngredients(BlockState state) {
        return state.getOptionalValue(LEVEL).map(i -> i < MAX_LEVEL).orElse(false);
    }

    public static boolean canScoopBowl(BlockState state) {
        return state.getOptionalValue(LEVEL).map(i -> i > MIN_LEVEL).orElse(false);
    }

    public void animateTick(@NotNull BlockState state, Level level, BlockPos pos, RandomSource random) {

        if (state.getOptionalValue(HEATED).orElse(false) && state.getOptionalValue(LEVEL)
                .orElse(MIN_LEVEL) > MIN_LEVEL) {
            double surfaceY = (double) pos.getY() + this.getContentHeight(state);
            double smokeX = (double) pos.getX() + (2f / 16f) + (double) random.nextFloat() * (12f / 16f);
            double smokeZ = (double) pos.getZ() + (2f / 16f) + (double) random.nextFloat() * (12f / 16f);
            level.addParticle(
                    (random.nextFloat() < 0.1) ? ParticleTypes.WHITE_SMOKE : ParticleTypes.SMOKE,
                    smokeX,
                    surfaceY,
                    smokeZ,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }


    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(
                HEATED,
                context.getLevel().getBlockState(context.getClickedPos().below()).is(JamiesModTag.HEATER_BLOCKS)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEVEL, HEATED);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        return (direction != Direction.DOWN) ? state : state.setValue(
                HEATED,
                neighborState.is(JamiesModTag.HEATER_BLOCKS)
        );
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends GumboPotBlock> codec() {
        return CODEC;
    }

    @Override
    protected double getContentHeight(BlockState state) {
        int level = state.getOptionalValue(LEVEL).orElse(0);
        return (4 + Mth.floor((3f * level) / 2f)) / 16.0;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        boolean isClientSide = level.isClientSide();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof GumboPotBlockEntity gumboPotBlockEntity) {

            boolean canAddIngredient = gumboPotBlockEntity.canAddIngredient(stack);

            if (canAddIngredient) {
                if (!isClientSide) {
                    gumboPotBlockEntity.addIngredient(stack);
                    stack.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(LEVEL, state.getValue(LEVEL) + 1));
                }

                return ItemInteractionResult.sidedSuccess(isClientSide);
            }

            boolean canScoopBowl = gumboPotBlockEntity.canScoopBowl(stack);


            if (canScoopBowl) {
                if (level instanceof ServerLevel serverLevel) {
                    ItemStack scoopedBowl = gumboPotBlockEntity.scoopBowl(stack);

                    stack.shrink(1);
                    player.addItem(scoopedBowl);
                    level.setBlockAndUpdate(pos, state.setValue(LEVEL, state.getValue(LEVEL) - 1));
                }

                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }


        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean isFull(@NotNull BlockState state) {
        return !GumboPotBlock.canFitAdditionalIngredients(state);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GumboPotBlockEntity(blockPos, blockState);
    }

}
