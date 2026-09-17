package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.item.CustomizableBundleContents;
import com.jamiedev.bygone.common.item.CustomizableBundleItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BundleItem.class)
public class BundleItemMixin {

	@ModifyConstant(method = "appendHoverText", constant = @Constant(intValue = 64))
	private int changeCapacity(int original) {
		if (this instanceof CustomizableBundleItem bundle) return bundle.getMaxWeight();
		return original;
	}

	@WrapOperation(
		method = {"overrideStackedOnOther", "overrideOtherStackedOnMe"},
		at = @At(
			value = "NEW",
			target = "(Lnet/minecraft/world/item/component/BundleContents;)Lnet/minecraft/world/item/component/BundleContents$Mutable;"
		)
	)
	public BundleContents.Mutable setBuilderBundle(BundleContents base, Operation<BundleContents.Mutable> original) {
		BundleItem self = (BundleItem)(Object)this;
		if (self instanceof CustomizableBundleItem bundle) return new CustomizableBundleContents(base, bundle);
		return original.call(base);
	}

}
