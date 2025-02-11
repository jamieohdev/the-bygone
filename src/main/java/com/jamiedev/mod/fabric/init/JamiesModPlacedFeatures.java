package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import java.util.ArrayList;
import java.util.List;
public class JamiesModPlacedFeatures
{
    public static List<ResourceKey<PlacedFeature>> features = new ArrayList<>();

    public static ResourceKey<PlacedFeature> of(String id){
        ResourceKey<PlacedFeature> registryKey = ResourceKey.create(Registries.PLACED_FEATURE, JamiesModFabric.getModId(id));
        features.add(registryKey);
        return registryKey;
    }
    public static final ResourceKey<PlacedFeature> ANCIENT_TREE_CHECKED = of("ancient_tree_checked");
    public static final ResourceKey<PlacedFeature> POINTED_AMBER = PlacementUtils.createKey("pointed_amber");
    public static final ResourceKey<PlacedFeature> AMBER_CLUSTER = PlacementUtils.createKey("amber_cluster");

    public static void bootstrap(BootstrapContext<PlacedFeature> featureRegisterable) {
        HolderGetter<ConfiguredFeature<?, ?>> holderGetter = featureRegisterable.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> registryEntry4 = holderGetter.getOrThrow(JamiesModConfiguredFeatures.AMBER_CLUSTER);
        Holder<ConfiguredFeature<?, ?>> registryEntry6 = holderGetter.getOrThrow(JamiesModConfiguredFeatures.POINTED_AMBER);
        Holder<ConfiguredFeature<?, ?>> fir = holderGetter.getOrThrow(JamiesModConfiguredFeatures.ANCIENT_TREE);
        PlacementUtils.register(featureRegisterable, ANCIENT_TREE_CHECKED, fir, PlacementUtils.filteredByBlockSurvival(JamiesModBlocks.ANCIENT_SAPLING));
        PlacementUtils.register(featureRegisterable, POINTED_AMBER, registryEntry6, new PlacementModifier[]{
                CountPlacement.of(UniformInt.of(192, 256)),
                InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                CountPlacement.of(UniformInt.of(1, 5)),
                RandomOffsetPlacement.of(ClampedNormalInt.of(0.0F, 3.0F, -10, 10),
                        ClampedNormalInt.of(0.0F, 0.6F, -2, 2)), BiomeFilter.biome()});
        PlacementUtils.register(featureRegisterable, AMBER_CLUSTER, registryEntry4, new PlacementModifier[]{CountPlacement.of(
                UniformInt.of(48, 96)), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BiomeFilter.biome()});

    }


    public static void init() {

//        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.LUSH_CAVES), GenerationStep.Feature.VEGETAL_DECORATION, AZALEA_BUSH);
    }
}
