package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class JamiesModPOI
{
    public static final TagKey<PoiType> COPPERBUG_HOME = of("copperbug_home");


    private static TagKey<PoiType> of(String id) {
        return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Bygone.id(id));
    }
}
