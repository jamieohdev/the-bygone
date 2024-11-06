package com.jamiedev.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public interface AncientCaveVines {
    VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    BooleanProperty BERRIES = Properties.BERRIES;

    static ActionResult pickBerries(@Nullable Entity picker, BlockState state, World world, BlockPos pos) {
        if ((Boolean)state.get(BERRIES)) {
            Block.dropStack(world, pos, new ItemStack(Blocks.GLOWSTONE, 1));
            float f = MathHelper.nextBetween(world.random, 0.8F, 1.2F);
            world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, f);
            BlockState blockState = (BlockState)state.with(BERRIES, false);
            world.setBlockState(pos, blockState, 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(picker, blockState));
            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    static boolean hasBerries(BlockState state) {
        return state.contains(BERRIES) && (Boolean)state.get(BERRIES);
    }

    static ToIntFunction<BlockState> getLuminanceSupplier(int luminance) {
        return (state) -> {
            return (Boolean)state.get(Properties.BERRIES) ? luminance : 0;
        };
    }
}
