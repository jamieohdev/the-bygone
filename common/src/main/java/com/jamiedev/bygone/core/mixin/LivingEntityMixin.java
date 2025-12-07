package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.item.VerdigrisBladeItem;
import com.jamiedev.bygone.core.registry.BGMobEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    protected ItemStack useItem;
    @Shadow
    protected int useItemRemaining;

    @Shadow
    public abstract boolean isUsingItem();

    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public void scaleWaterTravelSpeed(LivingEntity instance, float distance, Vec3 direction, Operation<Void> original) {
        MobEffectInstance carapaceEffect = instance.getEffect(BGMobEffects.CARAPACE.get());
        float modifiedDistance = distance;
        if (carapaceEffect != null && instance.getFluidHeight(FluidTags.WATER) > 0) {
            modifiedDistance = distance * (1 + 0.2f * (carapaceEffect.getAmplifier() + 1));
        }
        original.call(instance, modifiedDistance, direction);
    }

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true)
    //Fix for compatibility with Guarding mod git issue #20
    private void isBlocking(CallbackInfoReturnable<Boolean> cir) {
        Item item = this.useItem.getItem();
        if (item instanceof VerdigrisBladeItem) {
            if (this.isUsingItem() && !this.useItem.isEmpty()) {

                boolean canItemBlock = item.getUseAnimation(this.useItem) == UseAnim.BLOCK;
                boolean isUsingItemForLongEnough = (item.getUseDuration(
                        this.useItem,
                        (LivingEntity) (Object) this
                ) - this.useItemRemaining) >= ShieldItem.EFFECTIVE_BLOCK_DELAY;
                cir.setReturnValue(canItemBlock && isUsingItemForLongEnough);
            } else {
                cir.setReturnValue(false);
            }
        }
    }
}
