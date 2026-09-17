package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.entity.HauntEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Allay.class)
public class AllayMixin {

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/allay/Allay;heal(F)V"))
    private void bygone$skipPassiveRegenForHaunts(Allay instance, float amount, Operation<Void> original) {
        if (instance instanceof HauntEntity) {
            return;
        }
        original.call(instance, amount);
    }
}
