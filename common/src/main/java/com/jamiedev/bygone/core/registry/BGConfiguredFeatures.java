package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.worldgen.feature.PointedAmberClusterFeatureConfig;
import com.jamiedev.bygone.common.worldgen.feature.PointedAmberFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.ArrayList;
import java.util.List;

public class BGConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SABLE_MOSS_VEGETATION = FeatureUtils.createKey("sable_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ALPHA_MOSS_VEGETATION = FeatureUtils.createKey("alpha_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> POINTED_AMBER = FeatureUtils.createKey("pointed_amber");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMBER_CLUSTER = FeatureUtils.createKey("amber_cluster");
    public static List<ResourceKey<ConfiguredFeature<?, ?>>> features = new ArrayList<>();
    public static final ResourceKey<ConfiguredFeature<?, ?>> SABLE_MOSS_PATCH_BONEMEAL = of("sable_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ALPHA_MOSS_PATCH_BONEMEAL = of("alpha_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_TREE = of("underhang/small_underhang_trees");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_TREE_MEDIUM = of("underhang/medium_underhang_trees");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SABLE_TREE = of("small_sable_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SABLE_TREE_MEDIUM = of("sable_tree");

    public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
        ResourceKey<ConfiguredFeature<?, ?>> registryKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, Bygone.id(id));
        features.add(registryKey);
        return registryKey;
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> featureRegisterable) {
        TagMatchTest ruleTest = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);

        HolderGetter<PlacedFeature> placedFeatures = featureRegisterable.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = featureRegisterable.lookup(Registries.CONFIGURED_FEATURE);

        FeatureUtils.register(
                featureRegisterable,
                ALPHA_MOSS_VEGETATION,
                Feature.FLOWER,
                new RandomPatchConfiguration(
                        96,
                        6,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        new NoiseProvider(
                                                2345L,
                                                new NormalNoise.NoiseParameters(0, 1.0),
                                                0.020833334F,
                                                List.of(
                                                        Blocks.DANDELION.defaultBlockState(),
                                                        Blocks.POPPY.defaultBlockState(),
                                                        Blocks.ALLIUM.defaultBlockState(),
                                                        Blocks.AZURE_BLUET.defaultBlockState(),
                                                        Blocks.RED_TULIP.defaultBlockState(),
                                                        Blocks.ORANGE_TULIP.defaultBlockState(),
                                                        Blocks.WHITE_TULIP.defaultBlockState(),
                                                        Blocks.PINK_TULIP.defaultBlockState(),
                                                        Blocks.OXEYE_DAISY.defaultBlockState(),
                                                        Blocks.CORNFLOWER.defaultBlockState(),
                                                        Blocks.LILY_OF_THE_VALLEY.defaultBlockState()
                                                )
                                        )
                                )
                        )
                )
        );

        FeatureUtils.register(featureRegisterable, ALPHA_MOSS_PATCH_BONEMEAL, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(
                BlockTags.MOSS_REPLACEABLE, BlockStateProvider.simple(Blocks.MOSS_BLOCK),
                PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(ALPHA_MOSS_VEGETATION)
                ), CaveSurface.FLOOR, ConstantInt.of(1), 0.0F, 5,
                0.6F, UniformInt.of(1, 2), 0.75F));

        FeatureUtils.register(featureRegisterable, SABLE_MOSS_PATCH_BONEMEAL, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(
                BlockTags.MOSS_REPLACEABLE, BlockStateProvider.simple(BGBlocks.SABLE_MOSS_BLOCK.get()),
                PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(SABLE_MOSS_VEGETATION)
                ), CaveSurface.FLOOR, ConstantInt.of(1), 0.0F, 5,
                0.6F, UniformInt.of(1, 2), 0.75F));


        FeatureUtils.register(featureRegisterable, ANCIENT_TREE, Feature.TREE, naturalAncientConfig().build());
        FeatureUtils.register(featureRegisterable, ANCIENT_TREE_MEDIUM, Feature.TREE, naturalAncientConfig().build());
        FeatureUtils.register(featureRegisterable, POINTED_AMBER, Feature.SIMPLE_RANDOM_SELECTOR,
                new SimpleRandomFeatureConfiguration(HolderSet.direct(PlacementUtils.inlinePlaced(BGFeatures.POINTED_AMBER,
                        new PointedAmberFeatureConfig(0.2F, 0.7F, 0.5F, 0.5F), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1))), PlacementUtils.inlinePlaced(BGFeatures.POINTED_AMBER, new PointedAmberFeatureConfig(0.2F, 0.7F, 0.5F, 0.5F), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(-1))))));
        FeatureUtils.register(featureRegisterable, AMBER_CLUSTER, BGFeatures.AMBER_CLUSTER, new PointedAmberClusterFeatureConfig(12, UniformInt.of(3, 6), UniformInt.of(2, 8), 1, 3, UniformInt.of(2, 4), UniformFloat.of(0.3F, 0.7F), ClampedNormalFloat.of(0.1F, 0.3F, 0.1F, 0.9F), 0.1F, 3, 8));

    }

    private static TreeConfiguration.TreeConfigurationBuilder naturalAncientConfig() {
        return grownAncientConfig().decorators(List.of(new AlterGroundDecorator(BlockStateProvider.simple(Blocks.PODZOL))));
    }

    private static TreeConfiguration.TreeConfigurationBuilder grownAncientConfig() {
        return new TreeConfiguration.TreeConfigurationBuilder(

                BlockStateProvider.simple(BGBlocks.ANCIENT_LOG.get()),
                new StraightTrunkPlacer(6, 1, 2),

                BlockStateProvider.simple(BGBlocks.ANCIENT_LEAVES.get()),
                new SpruceFoliagePlacer(
                        UniformInt.of(1, 3),
                        UniformInt.of(0, 1),
                        UniformInt.of(3, 4)),
                new TwoLayersFeatureSize(2, 0, 2))
                .ignoreVines();
    }

    public static void init() {
    }

}
