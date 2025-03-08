package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.phys.BlockHitResult;

public class AncientCaveVinesHeadBlock extends GrowingPlantHeadBlock implements BonemealableBlock, AncientCaveVines {
    public static final MapCodec<AncientCaveVinesHeadBlock> CODEC = simpleCodec(AncientCaveVinesHeadBlock::new);
    private static final float GROW_CHANCE = 0.11F;

    @Override
    public MapCodec<AncientCaveVinesHeadBlock> codec() {
        return CODEC;
    }

    public AncientCaveVinesHeadBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(BERRIES, false));
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }

    @Override
    protected Block getBodyBlock() {
        return BGBlocks.CAVE_VINES_PLANT;
    }

    @Override
    protected BlockState updateBodyAfterConvertedFromHead(BlockState from, BlockState to) {
        return to.setValue(BERRIES, from.getValue(BERRIES));
    }

    @Override
    protected BlockState getGrowIntoState(BlockState state, @NotNull RandomSource random) {
        return super.getGrowIntoState(state, random).setValue(BERRIES, random.nextFloat() < 0.11F);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return new ItemStack(BGBlocks.CAVE_VINES_PLANT);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return AncientCaveVines.pickBerries(player, state, world, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BERRIES);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return !(Boolean)state.getValue(BERRIES);
    }

    @Override
    public boolean isBonemealSuccess(Level world, @NotNull RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, @NotNull RandomSource random, BlockPos pos, BlockState state) {
        world.setBlock(pos, state.setValue(BERRIES, true), 2);
    }
}
