package com.jamiedev.bygone.core.registry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;

public class BGDensityFunctionTypes
{
    public static List<ResourceKey<DensityFunction>> features = new ArrayList<>();

    public static ResourceKey<DensityFunction> of(String id){
       // RegistryKey<DensityFunction> registryKey = RegistryKey.of((RegistryKey<? extends Registry<DensityFunction>>) RegistryKeys.DENSITY_FUNCTION_TYPE, JamiesModFabric.getModId(id));
       // features.add(registryKey);
        return null;
    }
    public static final ResourceKey<DensityFunction> TEST = of("test");
}
