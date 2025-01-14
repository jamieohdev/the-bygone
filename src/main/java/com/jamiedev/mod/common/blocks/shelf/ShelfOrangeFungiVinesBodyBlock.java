package com.jamiedev.mod.common.blocks.shelf;

import com.jamiedev.mod.common.blocks.AncientCaveVines;
import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class ShelfOrangeFungiVinesBodyBlock extends AbstractPlantBlock implements Fertilizable, ShelfVines {
    public static final MapCodec<ShelfOrangeFungiVinesBodyBlock> CODEC = createCodec(ShelfOrangeFungiVinesBodyBlock::new);

    public MapCodec<ShelfOrangeFungiVinesBodyBlock> getCodec() {
        return CODEC;
    }

    public ShelfOrangeFungiVinesBodyBlock(AbstractBlock.Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false);
    }

    protected AbstractPlantStemBlock getStem() {
        return (AbstractPlantStemBlock) JamiesModBlocks.ORANGE_FUNGI_VINES;
    }

    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(JamiesModBlocks.ORANGE_FUNGI_PLANT);
    }

    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return AncientCaveVines.pickBerries(player, state, world, pos);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {

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
