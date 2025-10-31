package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.worldgen.feature.*;
import com.jamiedev.bygone.common.worldgen.feature.config.MegalithConfig;
import com.jamiedev.bygone.common.worldgen.feature.config.SableBranchConfig;
import com.jamiedev.bygone.common.worldgen.feature.config.SmallCloudConfig;
import com.jamiedev.bygone.common.worldgen.feature.config.ThornySableBranchConfig;
import com.jamiedev.bygone.common.worldgen.feature.AncientForestVegetationFeature;
import com.jamiedev.bygone.common.worldgen.feature.AncientForestVegetationFeatureConfig;
import com.jamiedev.bygone.common.worldgen.feature.AncientTreeFeature;
import com.jamiedev.bygone.common.worldgen.feature.AncientTreeFeatureConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public class BGFeatures
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

    public static final Feature<MegalithConfig> MEGALITH = register("megalith", new MegalithFeature(MegalithConfig.CODEC));

    public static  final Feature<SableBranchConfig> SABLE_BRANCH = register("sable_branchs",
            new SableBranchFeature(SableBranchConfig.CODEC));

    public static  final Feature<ThornySableBranchConfig> THORNY_SABLE_BRANCH = register("thorny_sable_branchs",
            new ThornySableBranchFeature(ThornySableBranchConfig.CODEC));

    public static  final Feature<SableBranchConfig> SABLE_GRASS = register("sable_grass",
            new SableGrassFeature(SableBranchConfig.CODEC));

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Bygone.id(name), feature);
    }

    public static void init() {}
}

