package com.jamiedev.bygone.core.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.ArrayList;
import java.util.List;

public class BGDensityFunctionTypes {
    public static final ResourceKey<DensityFunction> TEST = of("test");
    public static List<ResourceKey<DensityFunction>> features = new ArrayList<>();

    public static ResourceKey<DensityFunction> of(String id) {
        // RegistryKey<DensityFunction> registryKey = RegistryKey.of((RegistryKey<? extends Registry<DensityFunction>>) RegistryKeys.DENSITY_FUNCTION_TYPE, JamiesModFabric.getModId(id));
        // features.add(registryKey);
        return null;
    }
}
