package com.jamiedev.bygone.common.block.shelf;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

public class ShelfPurpleFungiVinesBodyBlock extends GrowingPlantBodyBlock implements BonemealableBlock, ShelfVines {
    public static final MapCodec<ShelfPurpleFungiVinesBodyBlock> CODEC = simpleCodec(ShelfPurpleFungiVinesBodyBlock::new);

    @Override
    public MapCodec<ShelfPurpleFungiVinesBodyBlock> codec() {
        return CODEC;
    }

    public ShelfPurpleFungiVinesBodyBlock(Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) BGBlocks.PURPLE_FUNGI_VINES;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return new ItemStack(BGBlocks.PURPLE_FUNGI_VINES_PLANT);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level world, @NotNull RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, @NotNull RandomSource random, BlockPos pos, BlockState state) {

    }

}
