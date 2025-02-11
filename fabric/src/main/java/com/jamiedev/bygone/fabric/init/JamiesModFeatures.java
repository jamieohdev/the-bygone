package com.jamiedev.bygone.fabric.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.worldgen.feature.*;
import com.jamiedev.bygone.common.worldgen.feature.config.SmallCloudConfig;
import com.jamiedev.bygone.common.worldgen.structure.AncientForestVegetationFeature;
import com.jamiedev.bygone.common.worldgen.structure.AncientForestVegetationFeatureConfig;
import com.jamiedev.bygone.common.worldgen.structure.AncientTreeFeature;
import com.jamiedev.bygone.common.worldgen.structure.AncientTreeFeatureConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class JamiesModFeatures
{
    public static final Feature<AncientTreeFeatureConfig> ANCIENT_TREE = register("ancient_tree", new AncientTreeFeature(AncientTreeFeatureConfig.CODEC));
    public static  final Feature<NoneFeatureConfiguration> ANCIENT_VINES = register("ancient_vines", new AncientVinesFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<RandomPatchConfiguration> ANCIENT_FLOWERS = register("ancient_flowers", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
    public static final Feature<AncientForestVegetationFeatureConfig> ANCIENT_FOREST_VEGATATION = register("underhang_vegetation", new AncientForestVegetationFeature(AncientForestVegetationFeatureConfig.VEGETATION_CODEC));

    public static final Feature<SmallCloudConfig> SMALL_CLOUD = register("small_cloud", new SmallCloudFeature(SmallCloudConfig.CODEC));

    public static final Feature<BlockStateConfiguration> AMBER = register("amber", new AmberFeature(BlockStateConfiguration.CODEC));
    public static  final Feature<NoneFeatureConfiguration> AMBER_UNDER = register("amber_under", new AmberUnderFeature(NoneFeatureConfiguration.CODEC));

    public static final Feature<PointedAmberFeatureConfig> POINTED_AMBER = register("pointed_amber", new PointedAmberFeature(PointedAmberFeatureConfig.CODEC));
    public static final Feature<PointedAmberClusterFeatureConfig> AMBER_CLUSTER  = register("amber_cluster", new PointedAmberClusterFeature(PointedAmberClusterFeatureConfig.CODEC));
    public static  final Feature<NoneFeatureConfiguration> PRIMORDIAL_CORAL_CLAW = register("primordial_coral_claw", new PrimordialCoralClawFeature(NoneFeatureConfiguration.CODEC));
    public static  final Feature<NoneFeatureConfiguration> PRIMORDIAL_CORAL_MUSHROOM = register("primordial_coral_mushroom", new PrimordialCoralMushroomFeature(NoneFeatureConfiguration.CODEC));
    public static  final Feature<NoneFeatureConfiguration> PRIMORDIAL_CORAL_TREE = register("primordial_coral_tree", new PrimordialCoralTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_VEGETATION = FeatureUtils.createKey("alpha_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH = FeatureUtils.createKey("alpha_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH_BONEMEAL = FeatureUtils.createKey("alpha_moss_patch_bonemeal");

    public static  final Feature<NoneFeatureConfiguration> SHELF_FUNGI = register("shelf_fungi", new ShelfFungiFeature(NoneFeatureConfiguration.CODEC));
    public static  final Feature<NoneFeatureConfiguration> FUNGI_VINES = register("fungi_vines", new TestFungiVineFeature(NoneFeatureConfiguration.CODEC));

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId(name), feature);
    }

    public static void init() {
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("ancient_tree"), ANCIENT_TREE);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("ancient_vines"), ANCIENT_VINES);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("underhang_vegetation"), ANCIENT_FOREST_VEGATATION);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("ancient_flowers"), ANCIENT_FLOWERS);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("small_cloud"), SMALL_CLOUD);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("amber"), AMBER);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("amber_under"), AMBER_UNDER);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("pointed_amber"), POINTED_AMBER);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("amber_cluster"), AMBER_CLUSTER);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("shelf_fungi"), SHELF_FUNGI);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("fungi_vines"), FUNGI_VINES);

        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("primordial_coral_claw"), PRIMORDIAL_CORAL_CLAW);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("primordial_coral_mushroom"), PRIMORDIAL_CORAL_MUSHROOM);
        Registry.register(BuiltInRegistries.FEATURE, Bygone.getModId("primordial_coral_tree"), PRIMORDIAL_CORAL_TREE);



    }
}

