package com.jamiedev.mod.common.init;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.densityfunction.DensityFunction;

import java.util.ArrayList;
import java.util.List;

public class JamiesModDensityFunctionTypes
{
    public static List<RegistryKey<DensityFunction>> features = new ArrayList<>();

    public static RegistryKey<DensityFunction> of(String id){
       // RegistryKey<DensityFunction> registryKey = RegistryKey.of((RegistryKey<? extends Registry<DensityFunction>>) RegistryKeys.DENSITY_FUNCTION_TYPE, JamiesMod.getModId(id));
       // features.add(registryKey);
        return null;
    }
    public static final RegistryKey<DensityFunction> TEST = of("test");
}
