package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

public class BGBiomes {
    public static List<ResourceKey<Biome>> biomes = new ArrayList<>();
    /**
     * ALPHAHANG))))
     * ANCIENT_FOREST))))
     * es.AMBER_DESERT))))
     * Biomes.PRIMORDIAL_OCEAN)
     * Biomes.PRIMORDIAL_BEACH)
     * s.SHELFHOLLOW))))
     * omes.MEGALITH_FIELD)))
     */

    public static final ResourceKey<Biome> ALPHAHANG = createBiomeKey("alphahang");
    public static final ResourceKey<Biome> ANCIENT_FOREST = createBiomeKey("underhang");
    public static final ResourceKey<Biome> AMBER_DESERT = createBiomeKey("amber_desert");
    public static final ResourceKey<Biome> MEGALITH_FIELD = createBiomeKey("megalith_field");
    public static final ResourceKey<Biome> PRIMORDIAL_BEACH = createBiomeKey("primordial_shores");
    public static final ResourceKey<Biome> PRIMORDIAL_OCEAN = createBiomeKey("primordial_ocean");
    public static final ResourceKey<Biome> SABLE_FOREST = createBiomeKey("sable_forest");
    public static final ResourceKey<Biome> SHELFHOLLOW = createBiomeKey("shelfhollow");

    public static ResourceKey<Biome> createBiomeKey(String id) {
        ResourceKey<Biome> registryKey = ResourceKey.create(Registries.BIOME, Bygone.id(id));
        biomes.add(registryKey);
        return registryKey;
    }

    public static void bootstrap(BootstrapContext<Biome> bootstapContext) {
        HolderGetter<PlacedFeature> placeddFeatureHolder = bootstapContext.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> configuredWorldCarverHolderGetter = bootstapContext.lookup(Registries.CONFIGURED_CARVER);
    }

    public static void init() {
    }
}
