package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GumboPotBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final MapCodec<GumboPotBlock> CODEC = simpleCodec(GumboPotBlock::new);

    private static final int MIN_LEVEL = 0;
    private static final int MAX_LEVEL = 8;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", MIN_LEVEL, MAX_LEVEL);

    public GumboPotBlock(Properties properties) {
        super(properties, CauldronInteraction.EMPTY);
    }

    public static boolean canFitAdditionalIngredients(BlockState state) {
        return state.getOptionalValue(LEVEL).map(i -> i < MAX_LEVEL).orElse(false);
    }

    public static boolean canScoopBowl(BlockState state) {
        return state.getOptionalValue(LEVEL).map(i -> i > MIN_LEVEL).orElse(false);
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

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof GumboPotBlockEntity gumboPotBlockEntity) {

            boolean canAddIngredient = gumboPotBlockEntity.canAddIngredient(stack);

            if (canAddIngredient) {
                if (level instanceof ServerLevel serverLevel) {
                    gumboPotBlockEntity.addIngredient(stack);
                    stack.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(LEVEL, state.getValue(LEVEL) + 1));
                }
                System.out.println("+ STIR   " + (level.isClientSide ? "C" : "S") + " Ingredients are now: " + gumboPotBlockEntity.ingredientsView());
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            boolean canScoopBowl = gumboPotBlockEntity.canScoopBowl(stack);
            System.out.println("Stack is: " + stack);
            System.out.println("Can scoop bowl? " + canScoopBowl + "; component is " + stack.get(BGDataComponents.GUMBO_SCOOP_DATA.value()));

            if (canScoopBowl) {
                if (level instanceof ServerLevel serverLevel) {
                    ItemStack scoopedBowl = gumboPotBlockEntity.scoopBowl(stack);
                    System.out.println("Scooped bowl is: " + scoopedBowl);
                    stack.shrink(1);
                    player.addItem(scoopedBowl);
                    level.setBlockAndUpdate(pos, state.setValue(LEVEL, state.getValue(LEVEL) - 1));
                }
                System.out.println("- SCOOP  " + (level.isClientSide ? "C" : "S") + " Ingredients are now: " + gumboPotBlockEntity.ingredientsView());
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            System.out.println("- NOOP   " + (level.isClientSide ? "C" : "S") + " Ingredients are now: " + gumboPotBlockEntity.ingredientsView());
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean isFull(@NotNull BlockState state) {
        return !GumboPotBlock.canFitAdditionalIngredients(state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GumboPotBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEVEL);
    }

}
