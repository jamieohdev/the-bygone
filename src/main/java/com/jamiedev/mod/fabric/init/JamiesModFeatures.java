package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.worldgen.feature.*;
import com.jamiedev.mod.common.worldgen.feature.config.SmallCloudConfig;
import com.jamiedev.mod.common.worldgen.structure.AncientForestVegetationFeature;
import com.jamiedev.mod.common.worldgen.structure.AncientForestVegetationFeatureConfig;
import com.jamiedev.mod.common.worldgen.structure.AncientTreeFeature;
import com.jamiedev.mod.common.worldgen.structure.AncientTreeFeatureConfig;
import net.minecraft.registry.*;
import net.minecraft.world.gen.feature.*;

public class JamiesModFeatures
{
    public static final Feature<AncientTreeFeatureConfig> ANCIENT_TREE = register("ancient_tree", new AncientTreeFeature(AncientTreeFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> ANCIENT_VINES = register("ancient_vines", new AncientVinesFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<RandomPatchFeatureConfig> ANCIENT_FLOWERS = register("ancient_flowers", new RandomPatchFeature(RandomPatchFeatureConfig.CODEC));
    public static final Feature<AncientForestVegetationFeatureConfig> ANCIENT_FOREST_VEGATATION = register("underhang_vegetation", new AncientForestVegetationFeature(AncientForestVegetationFeatureConfig.VEGETATION_CODEC));

    public static final Feature<SmallCloudConfig> SMALL_CLOUD = register("small_cloud", new SmallCloudFeature(SmallCloudConfig.CODEC));

    public static final Feature<SingleStateFeatureConfig> AMBER = register("amber", new AmberFeature(SingleStateFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> AMBER_UNDER = register("amber_under", new AmberUnderFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<PointedAmberFeatureConfig> POINTED_AMBER = register("pointed_amber", new PointedAmberFeature(PointedAmberFeatureConfig.CODEC));
    public static final Feature<PointedAmberClusterFeatureConfig> AMBER_CLUSTER  = register("amber_cluster", new PointedAmberClusterFeature(PointedAmberClusterFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> PRIMORDIAL_CORAL_CLAW = register("primordial_coral_claw", new PrimordialCoralClawFeature(DefaultFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> PRIMORDIAL_CORAL_MUSHROOM = register("primordial_coral_mushroom", new PrimordialCoralMushroomFeature(DefaultFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> PRIMORDIAL_CORAL_TREE = register("primordial_coral_tree", new PrimordialCoralTreeFeature(DefaultFeatureConfig.CODEC));

    public static final RegistryKey<ConfiguredFeature<?, ?>> MOSS_VEGETATION = ConfiguredFeatures.of("alpha_moss_vegetation");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOSS_PATCH = ConfiguredFeatures.of("alpha_moss_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOSS_PATCH_BONEMEAL = ConfiguredFeatures.of("alpha_moss_patch_bonemeal");

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registries.FEATURE, JamiesModFabric.getModId(name), feature);
    }

    public static void init() {
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("ancient_tree"), ANCIENT_TREE);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("ancient_vines"), ANCIENT_VINES);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("underhang_vegetation"), ANCIENT_FOREST_VEGATATION);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("ancient_flowers"), ANCIENT_FLOWERS);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("small_cloud"), SMALL_CLOUD);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("amber"), AMBER);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("amber_under"), AMBER_UNDER);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("pointed_amber"), POINTED_AMBER);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("amber_cluster"), AMBER_CLUSTER);

        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("primordial_coral_claw"), PRIMORDIAL_CORAL_CLAW);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("primordial_coral_mushroom"), PRIMORDIAL_CORAL_MUSHROOM);
        Registry.register(Registries.FEATURE, JamiesModFabric.getModId("primordial_coral_tree"), PRIMORDIAL_CORAL_TREE);



    }
}

