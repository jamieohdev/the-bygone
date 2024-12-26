package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

public class JamiesModPOI
{
    public static final TagKey<PointOfInterestType> COPPERBUG_HOME = of("copperbug_home");


    private static TagKey<PointOfInterestType> of(String id) {
        return TagKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, JamiesModFabric.getModId(id));
    }
}
