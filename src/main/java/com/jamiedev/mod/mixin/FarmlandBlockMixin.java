package com.jamiedev.mod.mixin;

import com.jamiedev.mod.fabric.init.JamiesModTag;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

import static net.minecraft.block.FarmlandBlock.MOISTURE;
import static net.minecraft.block.FarmlandBlock.setToDirt;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin extends Block
{

    public FarmlandBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    private static boolean isSprinklerNearby(WorldView world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.iterate(pos.add(-15, 0, -15), pos.add(15, 1, 15)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = (BlockPos)var2.next();
        } while(world.getBlockState(blockPos).isIn(JamiesModTag.SPRINKLERS));

        return true;
    }

    @WrapOperation(
            method = "randomTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FarmlandBlock;isWaterNearby(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z")
    )
    private boolean onRandomTick(WorldView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) || isSprinklerNearby(world, pos);
    }
}
