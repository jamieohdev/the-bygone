package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
public class JamiesModBiomes
{
    public static List<ResourceKey<Biome>> biomes = new ArrayList<>();
    public static ResourceKey<Biome> createBiomeKey(String id){
        ResourceKey<Biome> registryKey = ResourceKey.create(Registries.BIOME, Bygone.id(Bygone.MOD_ID));
        biomes.add(registryKey);
        return registryKey;
    }

    public static final ResourceKey<Biome> ANCIENT_FOREST = createBiomeKey("underhang");
    public static final ResourceKey<Biome> CALM = createBiomeKey("calm");
    public static final ResourceKey<Biome> PILLARS = createBiomeKey("pillars");
    public static final ResourceKey<Biome> PRIMORDIAL_OCEAN = createBiomeKey("primordial_ocean");

    public static void bootstrap(BootstrapContext<Biome> bootstapContext) {
        HolderGetter<PlacedFeature> placeddFeatureHolder = bootstapContext.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> configuredWorldCarverHolderGetter = bootstapContext.lookup(Registries.CONFIGURED_CARVER);


        // bootstapContext.register(WOODED_MEADOW, JamiesModBiomeCreator.createWoodedMeadow(placeddFeatureHolder, configuredWorldCarverHolderGetter));
    }

    public static void init(){
    }
}
