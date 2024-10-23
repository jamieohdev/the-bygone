package com.jamiedev.mod.blocks.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CasterBlock extends Block {
    public CasterBlock(Settings settings) {
        super(settings);
    }
    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        Item item = stack.getItem();

        // Just a test
        if (item != ItemStack.EMPTY.getItem()) {
            world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState());
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
