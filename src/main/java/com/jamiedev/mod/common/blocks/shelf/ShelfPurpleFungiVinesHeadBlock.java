package com.jamiedev.mod.common.blocks.shelf;

import com.jamiedev.mod.common.blocks.AncientCaveVines;
import com.jamiedev.mod.fabric.init.JamiesModBlocks;
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

public class ShelfPurpleFungiVinesHeadBlock extends GrowingPlantHeadBlock implements BonemealableBlock, ShelfVines {
    public static final MapCodec<ShelfPurpleFungiVinesHeadBlock> CODEC = simpleCodec(ShelfPurpleFungiVinesHeadBlock::new);
    private static final float GROW_CHANCE = 0.11F;

    public MapCodec<ShelfPurpleFungiVinesHeadBlock> codec() {
        return CODEC;
    }

    public ShelfPurpleFungiVinesHeadBlock(Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, 0)));
    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }

    protected Block getBodyBlock() {
        return JamiesModBlocks.PURPLE_FUNGI_VINES_PLANT;
    }

    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return new ItemStack(JamiesModBlocks.PURPLE_FUNGI_VINES_PLANT);
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return false;
    }

    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {

    }

}
