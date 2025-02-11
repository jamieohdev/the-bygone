package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.util.HangingSignFlags;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HangingSignBlockEntity.class)
public class HangingSignBlockEntityMixin {
    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/SignBlockEntity;<init>(Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private static void modifyArgs(Args args) {
        if (HangingSignFlags.getBETypeFlag() != null) {
            args.set(0, HangingSignFlags.getBETypeFlag());
            HangingSignFlags.removeBETypeFlag();
        }
    }
}