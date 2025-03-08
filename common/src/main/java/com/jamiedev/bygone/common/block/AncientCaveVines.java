package com.jamiedev.bygone.common.block;

import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface AncientCaveVines {
    VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    BooleanProperty BERRIES = BlockStateProperties.BERRIES;

    static InteractionResult pickBerries(@Nullable Entity picker, BlockState state, Level world, BlockPos pos) {
        if (state.getValue(BERRIES)) {
            Block.popResource(world, pos, new ItemStack(Blocks.GLOWSTONE, 1));
            float f = Mth.randomBetween(world.random, 0.8F, 1.2F);
            world.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);
            BlockState blockState = state.setValue(BERRIES, false);
            world.setBlock(pos, blockState, 2);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(picker, blockState));
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    static boolean hasBerries(BlockState state) {
        return state.hasProperty(BERRIES) && state.getValue(BERRIES);
    }

    static ToIntFunction<BlockState> getLuminanceSupplier(int luminance) {
        return (state) -> {
            return (Boolean)state.getValue(BlockStateProperties.BERRIES) ? luminance : 0;
        };
    }
}
