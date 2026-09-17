package com.jamiedev.bygone.common.item;

import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

public class SingleStackBundleItem extends BundleItem implements CustomizableBundleItem {

	public SingleStackBundleItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getMaxWeight() {
		return 9;
	}

	@Override
	public boolean acceptsStack(ItemStack stack) {
		return stack.getMaxStackSize() == 1;
	}

	@Override
	public Fraction getStackWeight(ItemStack stack) {
		return Fraction.getFraction(stack.getCount(), this.getMaxWeight());
	}

}
