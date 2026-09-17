package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.criteria.SurviveHauntingsTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BGCriteria {
    public static final KilledTrigger KILLED_BY_BLEMISH_CRITERION = new KilledTrigger();
    public static final SurviveHauntingsTrigger SURVIVE_HAUNTINGS_CRITERION = new SurviveHauntingsTrigger();

    public static void init() {
        register("killed_by_blemish", KILLED_BY_BLEMISH_CRITERION);
        register("survive_hauntings", SURVIVE_HAUNTINGS_CRITERION);
    }

    public static <T extends CriterionTrigger<?>> T register(String id, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Bygone.id(id), criterion);
    }
}
