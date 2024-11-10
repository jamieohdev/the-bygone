package com.jamiedev.mod.common.init;

import com.jamiedev.mod.common.JamiesMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
public class JamiesModDimension
{
    public static final RegistryKey<DimensionOptions> BYGONE = RegistryKey.of(RegistryKeys.DIMENSION,
            JamiesMod.getModId( "bygone"));
    public static final RegistryKey<World> BYGONE_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            JamiesMod.getModId( "bygone"));

    public static final RegistryKey<DimensionType> BYGONE_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            JamiesMod.getModId( "bygone_type"));
}

