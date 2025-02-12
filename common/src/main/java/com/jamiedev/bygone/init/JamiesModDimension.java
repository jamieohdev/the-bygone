package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
public class JamiesModDimension
{
    public static final ResourceKey<LevelStem> BYGONE = ResourceKey.create(Registries.LEVEL_STEM,
            Bygone.id( "bygone"));
    public static final ResourceKey<Level> BYGONE_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            Bygone.id( "bygone"));

    public static final ResourceKey<DimensionType> BYGONE_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            Bygone.id( "bygone_type"));
}

