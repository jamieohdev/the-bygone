package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class JamiesModCriteria
{
    public static final OnKilledCriterion KILLED_BY_BLEMISH_CRITERION = new OnKilledCriterion();

    public static void init() {
        Registry.register(Registries.CRITERION, JamiesModFabric.getModId("killed_by_blemish"), KILLED_BY_BLEMISH_CRITERION);
    }

    public static <T extends Criterion<?>> T register(String id, T criterion) {
        return (T) Registry.register(Registries.CRITERION, Identifier.of(JamiesModFabric.MOD_ID, id), criterion);
    }
}
