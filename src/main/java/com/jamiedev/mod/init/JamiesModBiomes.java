package com.jamiedev.mod.init;
import com.jamiedev.mod.*;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.ArrayList;
import java.util.List;
public class JamiesModBiomes
{
    public static List<RegistryKey<Biome>> biomes = new ArrayList<>();
    public static RegistryKey<Biome> createBiomeKey(String id){
        RegistryKey<Biome> registryKey = RegistryKey.of(RegistryKeys.BIOME, JamiesMod.getModId(JamiesMod.MOD_ID));
        biomes.add(registryKey);
        return registryKey;
    }

    public static final RegistryKey<Biome> ANCIENT_FOREST = createBiomeKey("underhang");
    public static final RegistryKey<Biome> CALM = createBiomeKey("calm");
    public static final RegistryKey<Biome> PILLARS = createBiomeKey("pillars");
    public static final RegistryKey<Biome> PRIMORDIAL_OCEAN = createBiomeKey("primordial_ocean");

    public static void bootstrap(Registerable<Biome> bootstapContext) {
        RegistryEntryLookup<PlacedFeature> placeddFeatureHolder = bootstapContext.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> configuredWorldCarverHolderGetter = bootstapContext.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);


        // bootstapContext.register(WOODED_MEADOW, JamiesModBiomeCreator.createWoodedMeadow(placeddFeatureHolder, configuredWorldCarverHolderGetter));
    }

    public static void init(){
    }
}
