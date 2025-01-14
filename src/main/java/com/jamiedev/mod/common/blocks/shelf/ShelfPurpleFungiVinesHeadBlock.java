package com.jamiedev.mod.common.blocks.shelf;

import com.jamiedev.mod.common.blocks.AncientCaveVines;
import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class ShelfPurpleFungiVinesHeadBlock extends AbstractPlantStemBlock implements Fertilizable, ShelfVines {
    public static final MapCodec<ShelfPurpleFungiVinesHeadBlock> CODEC = createCodec(ShelfPurpleFungiVinesHeadBlock::new);
    private static final float GROW_CHANCE = 0.11F;

    public MapCodec<ShelfPurpleFungiVinesHeadBlock> getCodec() {
        return CODEC;
    }

    public ShelfPurpleFungiVinesHeadBlock(Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0)));
    }

    protected int getGrowthLength(Random random) {
        return 1;
    }

    protected boolean chooseStemState(BlockState state) {
        return state.isAir();
    }

    protected Block getPlant() {
        return JamiesModBlocks.PURPLE_FUNGI_VINES_PLANT;
    }

    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(JamiesModBlocks.PURPLE_FUNGI_VINES_PLANT);
    }


    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return false;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {

    }

}
