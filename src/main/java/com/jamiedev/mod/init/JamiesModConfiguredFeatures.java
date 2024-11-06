package com.jamiedev.mod.init;

import com.jamiedev.mod.JamiesMod;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.ArrayList;
import java.util.List;

public class JamiesModConfiguredFeatures
{
    public static List<RegistryKey<ConfiguredFeature<?, ?>>> features = new ArrayList<>();

    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id){
        RegistryKey<ConfiguredFeature<?, ?>> registryKey = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, JamiesMod.getModId(id));
        features.add(registryKey);
        return registryKey;
    }
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANCIENT_TREE = of("ancient_tree");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        TagMatchRuleTest ruleTest = new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD);

        RegistryEntryLookup<PlacedFeature> placedFeatures = featureRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);


        RegistryEntry<PlacedFeature> firChecked = placedFeatures.getOrThrow(JamiesModPlacedFeatures.ANCIENT_TREE_CHECKED);
        ConfiguredFeatures.register(featureRegisterable, ANCIENT_TREE, Feature.TREE, naturalFirConfig().build());

    }

    private static TreeFeatureConfig.Builder naturalFirConfig() {
        return grownFirConfig().decorators(List.of(new AlterGroundTreeDecorator(BlockStateProvider.of(Blocks.PODZOL))));
    }

    private static TreeFeatureConfig.Builder grownFirConfig() {
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
