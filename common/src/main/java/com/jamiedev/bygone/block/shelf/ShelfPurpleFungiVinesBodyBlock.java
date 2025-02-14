package com.jamiedev.bygone.block.shelf;

import com.jamiedev.bygone.block.AncientCaveVines;
import com.jamiedev.bygone.init.JamiesModBlocks;
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
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

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
        return (GrowingPlantHeadBlock) JamiesModBlocks.PURPLE_FUNGI_VINES;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return new ItemStack(JamiesModBlocks.PURPLE_FUNGI_VINES_PLANT);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return AncientCaveVines.pickBerries(player, state, world, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {

    }

}
