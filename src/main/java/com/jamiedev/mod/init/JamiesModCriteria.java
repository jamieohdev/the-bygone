package com.jamiedev.mod.init;

import com.jamiedev.mod.JamiesMod;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class JamiesModCriteria
{
    public static final OnKilledCriterion KILLED_BY_BLEMISH_CRITERION = register("killed_by_blemish", new OnKilledCriterion());

    public static void registerCriteria() {
    }

    public static <T extends Criterion<?>> T register(String id, T criterion) {
        return (T) Registry.register(Registries.CRITERION, Identifier.of(JamiesMod.MOD_ID, id), criterion);
    }
}
