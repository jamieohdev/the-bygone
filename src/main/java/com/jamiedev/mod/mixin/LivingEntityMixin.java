package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.items.VerdigrisBladeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getActiveItem();

    // Credits to https://github.com/Quplet/NoShieldDelay (MIT Licensed)
    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int jamies_mod$isBlocking(int constant) {
        return this.getActiveItem().getItem() instanceof VerdigrisBladeItem ? 0 : constant;
    }

    @Inject(method = "handleStatus", at = @At("HEAD"), cancellable = true)
    public void jamies_mod$handleStatus(byte status, CallbackInfo ci) {
        if (status == 29) ci.cancel();
    }
}
