package com.jamiedev.bygone.mixin;

import com.jamiedev.bygone.Bygone;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin extends Block
{

    public FarmlandBlockMixin(Properties settings) {
        super(settings);
    }

    @WrapOperation(
            method = "randomTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;isNearWater(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean onRandomTick(LevelReader world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) || Bygone.isSprinklerNearby(world, pos);
    }
}
