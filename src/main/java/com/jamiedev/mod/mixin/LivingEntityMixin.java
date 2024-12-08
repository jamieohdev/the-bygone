package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.items.VerdigrisBladeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
        if ((LivingEntity) (Object) this instanceof PlayerEntity player && player.getActiveItem().getItem() instanceof VerdigrisBladeItem) {
            if (status == 29) ci.cancel();
            this.playSound(SoundEvents.ENTITY_GENERIC_HURT);
        }
    }
}
