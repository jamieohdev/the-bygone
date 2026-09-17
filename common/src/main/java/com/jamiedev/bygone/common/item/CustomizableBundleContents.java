package com.jamiedev.bygone.common.item;

import net.minecraft.world.item.component.BundleContents;

public class CustomizableBundleContents extends BundleContents.Mutable {

	protected final CustomizableBundleItem item;

	public CustomizableBundleContents(BundleContents contents, CustomizableBundleItem item) {
		super(contents);
		this.item = item;
	}

	public CustomizableBundleItem getItem() {
		return this.item;
	}

}
