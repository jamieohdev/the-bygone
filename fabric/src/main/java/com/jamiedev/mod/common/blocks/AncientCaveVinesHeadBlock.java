package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public class AncientCaveVinesHeadBlock extends GrowingPlantHeadBlock implements BonemealableBlock, AncientCaveVines {
    public static final MapCodec<AncientCaveVinesHeadBlock> CODEC = simpleCodec(AncientCaveVinesHeadBlock::new);
    private static final float GROW_CHANCE = 0.11F;

    public MapCodec<AncientCaveVinesHeadBlock> codec() {
        return CODEC;
    }

    public AncientCaveVinesHeadBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, 0)).setValue(BERRIES, false));
    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }

    protected Block getBodyBlock() {
        return JamiesModBlocks.CAVE_VINES_PLANT;
    }

    protected BlockState updateBodyAfterConvertedFromHead(BlockState from, BlockState to) {
        return (BlockState)to.setValue(BERRIES, (Boolean)from.getValue(BERRIES));
    }

    protected BlockState getGrowIntoState(BlockState state, RandomSource random) {
        return (BlockState)super.getGrowIntoState(state, random).setValue(BERRIES, random.nextFloat() < 0.11F);
    }

    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return new ItemStack(JamiesModBlocks.CAVE_VINES_PLANT);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return AncientCaveVines.pickBerries(player, state, world, pos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{BERRIES});
    }

    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return !(Boolean)state.getValue(BERRIES);
    }

    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        world.setBlock(pos, (BlockState)state.setValue(BERRIES, true), 2);
    }
}
