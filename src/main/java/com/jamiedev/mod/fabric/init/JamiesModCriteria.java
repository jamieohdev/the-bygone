package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class JamiesModCriteria
{
    public static final KilledTrigger KILLED_BY_BLEMISH_CRITERION = new KilledTrigger();

    public static void init() {
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, JamiesModFabric.getModId("killed_by_blemish"), KILLED_BY_BLEMISH_CRITERION);
    }

    public static <T extends CriterionTrigger<?>> T register(String id, T criterion) {
        return (T) Registry.register(BuiltInRegistries.TRIGGER_TYPES, ResourceLocation.fromNamespaceAndPath(JamiesModFabric.MOD_ID, id), criterion);
    }
}
