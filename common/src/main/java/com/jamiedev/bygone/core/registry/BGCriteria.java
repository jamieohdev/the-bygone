package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BGCriteria
{
    public static final KilledTrigger KILLED_BY_BLEMISH_CRITERION = new KilledTrigger();

    public static void init() {
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, Bygone.id("killed_by_blemish"), KILLED_BY_BLEMISH_CRITERION);
    }

    public static <T extends CriterionTrigger<?>> T register(String id, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, id), criterion);
    }
}
