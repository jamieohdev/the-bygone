package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.item.VerdigrisBladeItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getUseItem();

    @Shadow public abstract void makeSound(@Nullable SoundEvent sound);

    @Shadow protected ItemStack useItem;

    @Shadow protected int useItemRemaining;

    @Shadow public abstract boolean isUsingItem();

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true) //Fix for compatibility with Guarding mod git issue #20
    private void jamies_mod$isBlocking(CallbackInfoReturnable<Boolean> cir) {
        if(this.useItem.getItem() instanceof VerdigrisBladeItem)
            if (this.isUsingItem() && !this.useItem.isEmpty()) {
                Item item = this.useItem.getItem();
                cir.setReturnValue(item.getUseAnimation(this.useItem) != UseAnim.BLOCK ? false : item.getUseDuration(this.useItem, (LivingEntity) (Object)this) - this.useItemRemaining >= 5);
            } else {
                cir.setReturnValue(false);
            }
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    public void jamies_mod$handleStatus(byte status, CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof Player player && player.getUseItem().getItem() instanceof VerdigrisBladeItem) {
            if (status == EntityEvent.ATTACK_BLOCKED) ci.cancel();
            this.makeSound(SoundEvents.GENERIC_HURT);
        }
    }
}
