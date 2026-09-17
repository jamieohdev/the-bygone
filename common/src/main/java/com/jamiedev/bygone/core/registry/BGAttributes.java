package com.jamiedev.bygone.core.registry;

import com.google.common.base.Suppliers;
import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;

public class BGAttributes {

	public static Attribute PHASING_DURATION_VALUE = new RangedAttribute("attribute.name.phasing_duration",0, 0, 1024)
			.setSyncable(true);

	public static Supplier<Holder<Attribute>> PHASING_DURATION = Suppliers.memoize(() ->
			Registry.registerForHolder(
					BuiltInRegistries.ATTRIBUTE,
					Bygone.id("phasing_duration"),
					BGAttributes.PHASING_DURATION_VALUE
			)
	);

	public static void init() {}

}
