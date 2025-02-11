package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
public class JamiesModDimension
{
    public static final ResourceKey<LevelStem> BYGONE = ResourceKey.create(Registries.LEVEL_STEM,
            JamiesModFabric.getModId( "bygone"));
    public static final ResourceKey<Level> BYGONE_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            JamiesModFabric.getModId( "bygone"));

    public static final ResourceKey<DimensionType> BYGONE_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            JamiesModFabric.getModId( "bygone_type"));
}

