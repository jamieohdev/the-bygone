package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.item.CustomizableBundleContents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleContents.Mutable.class)
public abstract class BundleContentsMutableMixin {

	@WrapOperation(
		method = {"tryInsert", "tryTransfer", "removeOne", "getMaxAmountToAdd"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/component/BundleContents;getWeight(Lnet/minecraft/world/item/ItemStack;)Lorg/apache/commons/lang3/math/Fraction;"
		)
	)
	public Fraction wrapGetOccupancy(ItemStack stack, Operation<Fraction> original) {
		BundleContents.Mutable self = (BundleContents.Mutable)(Object)this;
		if (self instanceof CustomizableBundleContents contents) {
			if (!contents.getItem().acceptsStack(stack)) {
				return Fraction.getFraction(2, 1);
			}
			return contents.getItem().getStackWeight(stack);
		}
		return original.call(stack);
	}

	@Inject(method = "tryInsert(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
	public void beforeTryInsert(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		BundleContents.Mutable self = (BundleContents.Mutable)(Object)this;
		if (self instanceof CustomizableBundleContents contents && !contents.getItem().acceptsStack(stack)) {
			cir.setReturnValue(0);
		}
	}

}
