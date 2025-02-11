package com.jamiedev.mod.mixin;

import com.jamiedev.mod.fabric.init.JamiesModTag;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;

import static net.minecraft.world.level.block.FarmBlock.MOISTURE;
import static net.minecraft.world.level.block.FarmBlock.turnToDirt;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin extends Block
{

    public FarmlandBlockMixin(Properties settings) {
        super(settings);
    }

    @Unique
    private static boolean isSprinklerNearby(LevelReader world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.betweenClosed(pos.offset(-15, 0, -15), pos.offset(15, 1, 15)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = (BlockPos)var2.next();
        } while(world.getBlockState(blockPos).is(JamiesModTag.SPRINKLERS));

        return true;
    }

    @WrapOperation(
            method = "randomTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;isNearWater(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean onRandomTick(LevelReader world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) || isSprinklerNearby(world, pos);
    }
}
