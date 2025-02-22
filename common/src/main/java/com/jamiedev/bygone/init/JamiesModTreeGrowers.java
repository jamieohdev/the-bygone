package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public class JamiesModTreeGrowers {

    public static final TreeGrower ANCIENT_TREE = register("ancient_tree", Optional.of(JamiesModConfiguredFeatures.ANCIENT_TREE), Optional.empty());


    private static TreeGrower register(String name, float secondaryChance, Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers)
    {
        return new TreeGrower(String.format("%s:%s", Bygone.MOD_ID, name), secondaryChance, megaTree, secondaryMegaTree, tree, secondaryTree, flowers, secondaryFlowers);
    }

    private static TreeGrower register(String name, Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree) {
        return register(name, 0, Optional.empty(), Optional.empty(), tree, secondaryTree, Optional.empty(), Optional.empty());
    }
}
