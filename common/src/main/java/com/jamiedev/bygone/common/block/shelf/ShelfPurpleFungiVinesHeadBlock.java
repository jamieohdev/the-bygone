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
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

public class ShelfPurpleFungiVinesHeadBlock extends GrowingPlantHeadBlock implements BonemealableBlock, ShelfVines {
    public static final MapCodec<ShelfPurpleFungiVinesHeadBlock> CODEC = simpleCodec(ShelfPurpleFungiVinesHeadBlock::new);
    private static final float GROW_CHANCE = 0.11F;

    @Override
    public MapCodec<ShelfPurpleFungiVinesHeadBlock> codec() {
        return CODEC;
    }

    public ShelfPurpleFungiVinesHeadBlock(Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
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
        return BGBlocks.PURPLE_FUNGI_VINES_PLANT;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return new ItemStack(BGBlocks.PURPLE_FUNGI_VINES_PLANT);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
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
