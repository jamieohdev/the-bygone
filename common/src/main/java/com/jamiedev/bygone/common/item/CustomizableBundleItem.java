package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.mixin.BundleContentsMixin;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

public interface CustomizableBundleItem {

	default int getMaxWeight() {
		return 64;
	}

	default boolean acceptsStack(ItemStack stack) {
		return !stack.isEmpty();
	}

	default Fraction getStackWeight(ItemStack stack) {
		Fraction result = BundleContentsMixin.invokeGetWeight(stack);
		Fraction defaultResult = Fraction.getFraction(1, stack.getMaxStackSize());
		if (!result.equals(defaultResult)) return result;

		int percent = (int) Math.ceil((double) stack.getMaxStackSize() / this.getMaxWeight());
		return Fraction.getFraction(percent, this.getMaxWeight());
	}

}
