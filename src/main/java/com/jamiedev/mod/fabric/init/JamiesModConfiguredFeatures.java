package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.common.worldgen.feature.PointedAmberClusterFeatureConfig;
import com.jamiedev.mod.common.worldgen.feature.PointedAmberFeatureConfig;
import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.ClampedNormalFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.EnvironmentScanPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RandomOffsetPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.ArrayList;
import java.util.List;

public class JamiesModConfiguredFeatures
{
    public static List<RegistryKey<ConfiguredFeature<?, ?>>> features = new ArrayList<>();

    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id){
        RegistryKey<ConfiguredFeature<?, ?>> registryKey = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, JamiesModFabric.getModId(id));
        features.add(registryKey);
        return registryKey;
    }
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANCIENT_TREE = of("ancient_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> POINTED_AMBER = ConfiguredFeatures.of("pointed_amber");
    public static final RegistryKey<ConfiguredFeature<?, ?>> AMBER_CLUSTER = ConfiguredFeatures.of("amber_cluster");
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        TagMatchRuleTest ruleTest = new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD);

        RegistryEntryLookup<PlacedFeature> placedFeatures = featureRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        
        ConfiguredFeatures.register(featureRegisterable, ANCIENT_TREE, Feature.TREE, naturalAncientConfig().build());
        ConfiguredFeatures.register(featureRegisterable, POINTED_AMBER, Feature.SIMPLE_RANDOM_SELECTOR,
                new SimpleRandomFeatureConfig(RegistryEntryList.of(new RegistryEntry[]{PlacedFeatures.createEntry(JamiesModFeatures.POINTED_AMBER,
                        new PointedAmberFeatureConfig(0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[]{EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 12), RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1))}), PlacedFeatures.createEntry(JamiesModFeatures.POINTED_AMBER, new PointedAmberFeatureConfig(0.2F, 0.7F, 0.5F, 0.5F), new PlacementModifier[]{EnvironmentScanPlacementModifier.of(Direction.UP, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 12), RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(-1))})})));
        ConfiguredFeatures.register(featureRegisterable, AMBER_CLUSTER, JamiesModFeatures.AMBER_CLUSTER, new PointedAmberClusterFeatureConfig(12, UniformIntProvider.create(3, 6), UniformIntProvider.create(2, 8), 1, 3, UniformIntProvider.create(2, 4), UniformFloatProvider.create(0.3F, 0.7F), ClampedNormalFloatProvider.create(0.1F, 0.3F, 0.1F, 0.9F), 0.1F, 3, 8));

    }

    private static TreeFeatureConfig.Builder naturalAncientConfig() {
        return grownAncientConfig().decorators(List.of(new AlterGroundTreeDecorator(BlockStateProvider.of(Blocks.PODZOL))));
    }

    private static TreeFeatureConfig.Builder grownAncientConfig() {
        return new TreeFeatureConfig.Builder(

                BlockStateProvider.of(JamiesModBlocks.ANCIENT_LOG),
                new StraightTrunkPlacer(6, 1, 2),

                BlockStateProvider.of(JamiesModBlocks.ANCIENT_LEAVES),
                new SpruceFoliagePlacer(
                        UniformIntProvider.create(1, 3),
                        UniformIntProvider.create(0, 1),
                        UniformIntProvider.create(3, 4)),
                new TwoLayersFeatureSize(2, 0, 2))
                .ignoreVines();
    }

    public static void init() {
    }

}
