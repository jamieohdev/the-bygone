package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.worldgen.feature.*;
import com.jamiedev.bygone.common.worldgen.feature.config.*;
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

public class BGFeatures {
    public static final Feature<AncientTreeFeatureConfig> ANCIENT_TREE = register(
            "ancient_tree",
            new AncientTreeFeature(AncientTreeFeatureConfig.CODEC)
    );
    public static final Feature<NoneFeatureConfiguration> ANCIENT_VINES = register(
            "ancient_vines",
            new AncientVinesFeature(NoneFeatureConfiguration.CODEC)
    );
    public static final Feature<RandomPatchConfiguration> ANCIENT_FLOWERS = register(
            "ancient_flowers",
            new RandomPatchFeature(RandomPatchConfiguration.CODEC)
    );
    public static final Feature<AncientForestVegetationFeatureConfig> ANCIENT_FOREST_VEGETATION = register(
            "underhang_vegetation",
            new AncientForestVegetationFeature(AncientForestVegetationFeatureConfig.VEGETATION_CODEC)
    );

    public static final Feature<SmallCloudConfig> SMALL_CLOUD = register(
            "small_cloud",
            new SmallCloudFeature(SmallCloudConfig.CODEC)
    );

    public static final Feature<BlockStateConfiguration> AMBER = register(
            "amber",
            new AmberFeature(BlockStateConfiguration.CODEC)
    );
    public static final Feature<NoneFeatureConfiguration> AMBER_UNDER = register(
            "amber_under",
            new AmberUnderFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final Feature<PointedAmberFeatureConfig> POINTED_AMBER = register(
            "pointed_amber",
            new PointedAmberFeature(PointedAmberFeatureConfig.CODEC)
    );
    public static final Feature<PointedAmberClusterFeatureConfig> AMBER_CLUSTER = register(
            "amber_cluster",
            new PointedAmberClusterFeature(PointedAmberClusterFeatureConfig.CODEC)
    );
    public static final Feature<BlockStateConfiguration> BYCORAL_CLAW = register(
            "bycoral_claw",
            new BycoralClawFeature(BlockStateConfiguration.CODEC)
    );
    public static final Feature<BlockStateConfiguration> BYCORAL_MUSHROOM = register(
            "bycoral_mushroom",
            new BycoralMushroomFeature(BlockStateConfiguration.CODEC)
    );
    public static final Feature<BlockStateConfiguration> BYCORAL_TREE = register(
            "bycoral_tree",
            new BycoralTreeFeature(BlockStateConfiguration.CODEC)
    );
    public static final Feature<BlockStateConfiguration> BYCORAL_PILLARS = register(
            "bycoral_pillars",
            new BycoralPillarsFeature(BlockStateConfiguration.CODEC)
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_VEGETATION = FeatureUtils.createKey(
            "alpha_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH = FeatureUtils.createKey("alpha_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOSS_PATCH_BONEMEAL = FeatureUtils.createKey(
            "alpha_moss_patch_bonemeal");

    public static final Feature<DiskShelfFungiConfig> DISK_SHELF_FUNGI = register(
            "disk_shelf_fungi",
            new DiskShelfFungiFeature(DiskShelfFungiConfig.CODEC)
    );

    public static final Feature<FeatureFilledGeodeConfig> FEATURE_FILLED_GEODE = register(
            "feature_filled_geode",
            new FeatureFilledGeodeFeature(FeatureFilledGeodeConfig.CODEC)
    );
    public static final Feature<NoneFeatureConfiguration> FUNGI_VINES = register(
            "fungi_vines",
            new TestFungiVineFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final Feature<MegalithConfig> MEGALITH = register(
            "megalith",
            new MegalithFeature(MegalithConfig.CODEC)
    );

    public static final Feature<SableBranchConfig> SABLE_BRANCH = register(
            "sable_branches",
            new SableBranchFeature(SableBranchConfig.CODEC)
    );

    public static final Feature<ThornySableBranchConfig> THORNY_SABLE_BRANCH = register(
            "thorny_sable_branches",
            new ThornySableBranchFeature(ThornySableBranchConfig.CODEC)
    );

    public static final Feature<SableBranchConfig> SABLE_GRASS = register(
            "sable_grass",
            new SableGrassFeature(SableBranchConfig.CODEC)
    );

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Bygone.id(name), feature);
    }

    public static void init() {
    }
}

