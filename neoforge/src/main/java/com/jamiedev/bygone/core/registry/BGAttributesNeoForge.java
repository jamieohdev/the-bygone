package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class BGAttributesNeoForge {

	public static final DeferredRegister<Attribute> ATTRIBUTES =
			DeferredRegister.create(Registries.ATTRIBUTE, Bygone.MOD_ID);

	public static final DeferredHolder<Attribute, Attribute> PHASING_DURATION =
			ATTRIBUTES.register("phasing_duration", () -> BGAttributes.PHASING_DURATION_VALUE);

	public static void init(IEventBus modEventBus) {
		BGAttributes.PHASING_DURATION = () -> PHASING_DURATION;
		ATTRIBUTES.register(modEventBus);
	}

}
