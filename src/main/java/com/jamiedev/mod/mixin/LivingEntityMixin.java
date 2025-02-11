package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.items.VerdigrisBladeItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
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

    @Shadow protected abstract void playHurtSound(DamageSource damageSource);

    @Shadow public abstract @Nullable DamageSource getRecentDamageSource();

    @Shadow public abstract void playSound(@Nullable SoundEvent sound);

    // Credits to https://github.com/Quplet/NoShieldDelay (MIT Licensed)
    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int jamies_mod$isBlocking(int constant) {
        return this.getActiveItem().getItem() instanceof VerdigrisBladeItem ? 0 : constant;
    }

    @Inject(method = "handleStatus", at = @At("HEAD"), cancellable = true)
    public void jamies_mod$handleStatus(byte status, CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof Player player && player.getUseItem().getItem() instanceof VerdigrisBladeItem) {
            if (status == 29) ci.cancel();
            this.playSound(SoundEvents.GENERIC_HURT);
        }
    }
}
