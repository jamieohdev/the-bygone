package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.intprovider.ClampedNormalIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.ArrayList;
import java.util.List;
public class JamiesModPlacedFeatures
{
    public static List<RegistryKey<PlacedFeature>> features = new ArrayList<>();

    public static RegistryKey<PlacedFeature> of(String id){
        RegistryKey<PlacedFeature> registryKey = RegistryKey.of(RegistryKeys.PLACED_FEATURE, JamiesModFabric.getModId(id));
        features.add(registryKey);
        return registryKey;
    }
    public static final RegistryKey<PlacedFeature> ANCIENT_TREE_CHECKED = of("ancient_tree_checked");
    public static final RegistryKey<PlacedFeature> POINTED_AMBER = PlacedFeatures.of("pointed_amber");
    public static final RegistryKey<PlacedFeature> AMBER_CLUSTER = PlacedFeatures.of("amber_cluster");

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> holderGetter = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        RegistryEntry<ConfiguredFeature<?, ?>> registryEntry4 = holderGetter.getOrThrow(JamiesModConfiguredFeatures.AMBER_CLUSTER);
        RegistryEntry<ConfiguredFeature<?, ?>> registryEntry6 = holderGetter.getOrThrow(JamiesModConfiguredFeatures.POINTED_AMBER);
        RegistryEntry<ConfiguredFeature<?, ?>> fir = holderGetter.getOrThrow(JamiesModConfiguredFeatures.ANCIENT_TREE);
        PlacedFeatures.register(featureRegisterable, ANCIENT_TREE_CHECKED, fir, PlacedFeatures.wouldSurvive(JamiesModBlocks.ANCIENT_SAPLING));
        PlacedFeatures.register(featureRegisterable, POINTED_AMBER, registryEntry6, new PlacementModifier[]{
                CountPlacementModifier.of(UniformIntProvider.create(192, 256)),
                SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
                CountPlacementModifier.of(UniformIntProvider.create(1, 5)),
                RandomOffsetPlacementModifier.of(ClampedNormalIntProvider.of(0.0F, 3.0F, -10, 10),
                        ClampedNormalIntProvider.of(0.0F, 0.6F, -2, 2)), BiomePlacementModifier.of()});
        PlacedFeatures.register(featureRegisterable, AMBER_CLUSTER, registryEntry4, new PlacementModifier[]{CountPlacementModifier.of(
                UniformIntProvider.create(48, 96)), SquarePlacementModifier.of(), PlacedFeatures.BOTTOM_TO_120_RANGE,
                BiomePlacementModifier.of()});

    }


    public static void init() {

//        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.LUSH_CAVES), GenerationStep.Feature.VEGETAL_DECORATION, AZALEA_BUSH);
    }
}
