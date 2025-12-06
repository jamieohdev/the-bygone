package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.block.MalachiteDoorBlock;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {

    @ModifyReturnValue(method = "updateShape", at = @At(value = "RETURN", ordinal = 1))
    public BlockState preventWaterloggingOverriding(BlockState original, @Local(argsOnly = true, ordinal = 0) BlockState state, @Local(argsOnly = true) Direction facing) {
        if (!(original.getBlock() instanceof MalachiteDoorBlock)) {
            return original;
        }
        boolean originalIsWaterlogged = original.getOptionalValue(BlockStateProperties.WATERLOGGED).orElse(false);
        boolean stateIsWaterlogged = state.getOptionalValue(BlockStateProperties.WATERLOGGED)
                .orElse(originalIsWaterlogged);
        boolean isUpdatingFromTop = facing == Direction.UP;
        if (isUpdatingFromTop && originalIsWaterlogged) {
            return original;
        }
        return original.trySetValue(
                BlockStateProperties.WATERLOGGED,
                stateIsWaterlogged
        );
    }
}
