package com.jamiedev.bygone.mixin;

import com.jamiedev.bygone.common.util.HangingSignFlags;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HangingSignBlockEntity.class)
public class HangingSignBlockEntityMixin {
    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/SignBlockEntity;<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private static void modifyArgs(Args args) {
        if (HangingSignFlags.getBETypeFlag() != null) {
            args.set(0, HangingSignFlags.getBETypeFlag());
            HangingSignFlags.removeBETypeFlag();
        }
    }
}