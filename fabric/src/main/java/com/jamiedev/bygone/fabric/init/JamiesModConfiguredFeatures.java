package com.jamiedev.bygone.fabric.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.worldgen.feature.PointedAmberClusterFeatureConfig;
import com.jamiedev.bygone.common.worldgen.feature.PointedAmberFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import java.util.ArrayList;
import java.util.List;

public class JamiesModConfiguredFeatures
{
    public static List<ResourceKey<ConfiguredFeature<?, ?>>> features = new ArrayList<>();

    public static ResourceKey<ConfiguredFeature<?, ?>> of(String id){
        ResourceKey<ConfiguredFeature<?, ?>> registryKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, Bygone.getModId(id));
        features.add(registryKey);
        return registryKey;
    }
    public static final ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_TREE = of("ancient_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> POINTED_AMBER = FeatureUtils.createKey("pointed_amber");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMBER_CLUSTER = FeatureUtils.createKey("amber_cluster");
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> featureRegisterable) {
        TagMatchTest ruleTest = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);

        HolderGetter<PlacedFeature> placedFeatures = featureRegisterable.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = featureRegisterable.lookup(Registries.CONFIGURED_FEATURE);
        
        FeatureUtils.register(featureRegisterable, ANCIENT_TREE, Feature.TREE, naturalAncientConfig().build());
        FeatureUtils.register(featureRegisterable, POINTED_AMBER, Feature.SIMPLE_RANDOM_SELECTOR,
                new SimpleRandomFeatureConfiguration(HolderSet.direct(new Holder[]{PlacementUtils.inlinePlaced(JamiesModFeatures.POINTED_AMBER,
                        new PointedAmberFeatureConfig(0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[]{EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1))}), PlacementUtils.inlinePlaced(JamiesModFeatures.POINTED_AMBER, new PointedAmberFeatureConfig(0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[]{EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(-1))})})));
        FeatureUtils.register(featureRegisterable, AMBER_CLUSTER, JamiesModFeatures.AMBER_CLUSTER, new PointedAmberClusterFeatureConfig(12, UniformInt.of(3, 6), UniformInt.of(2, 8), 1, 3, UniformInt.of(2, 4), UniformFloat.of(0.3F, 0.7F), ClampedNormalFloat.of(0.1F, 0.3F, 0.1F, 0.9F), 0.1F, 3, 8));

    }

    private static TreeConfiguration.TreeConfigurationBuilder naturalAncientConfig() {
        return grownAncientConfig().decorators(List.of(new AlterGroundDecorator(BlockStateProvider.simple(Blocks.PODZOL))));
    }

    private static TreeConfiguration.TreeConfigurationBuilder grownAncientConfig() {
        return new TreeConfiguration.TreeConfigurationBuilder(

                BlockStateProvider.simple(JamiesModBlocks.ANCIENT_LOG),
                new StraightTrunkPlacer(6, 1, 2),

                BlockStateProvider.simple(JamiesModBlocks.ANCIENT_LEAVES),
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
