package com.jamiedev.bygone.core.registry;

import com.google.common.base.Suppliers;
import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class BGAttributesFabric {

	public static final Holder<Attribute> PHASING_DURATION = Registry.registerForHolder(
			BuiltInRegistries.ATTRIBUTE,
			Bygone.id("phasing_duration"),
			BGAttributes.PHASING_DURATION_VALUE
	);

	public static void init() {
		BGAttributes.PHASING_DURATION = Suppliers.memoize(() -> PHASING_DURATION);
	}

}
