package com.jamiedev.mod.init;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.worldgen.feature.*;
import com.jamiedev.mod.worldgen.feature.config.SmallCloudConfig;
import com.jamiedev.mod.worldgen.structure.AncientForestVegetationFeature;
import com.jamiedev.mod.worldgen.structure.AncientForestVegetationFeatureConfig;
import com.jamiedev.mod.worldgen.structure.AncientTreeFeature;
import com.jamiedev.mod.worldgen.structure.AncientTreeFeatureConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.*;

public class JamiesModFeatures
{
    public static final Feature<AncientTreeFeatureConfig> ANCIENT_TREE = register("ancient_tree", new AncientTreeFeature(AncientTreeFeatureConfig.CODEC));

    public static final Feature<RandomPatchFeatureConfig>  FLOWER = register("ancient_flowers", new RandomPatchFeature(RandomPatchFeatureConfig.CODEC));
    public static final Feature<AncientForestVegetationFeatureConfig> ANCIENT_FOREST_VEGATATION = register("underhang_vegetation", new AncientForestVegetationFeature(AncientForestVegetationFeatureConfig.VEGETATION_CODEC));

    public static final Feature<SmallCloudConfig> SMALL_CLOUD = register("small_cloud", new SmallCloudFeature(SmallCloudConfig.CODEC));

    public static final Feature<SingleStateFeatureConfig> AMBER = register("amber", new AmberFeature(SingleStateFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> AMBER_UNDER = register("amber_under", new AmberUnderFeature(DefaultFeatureConfig.CODEC));

    public static  final Feature<DefaultFeatureConfig> PRIMORDIAL_CORAL_CLAW = register("primordial_coral_claw", new PrimordialCoralClawFeature(DefaultFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> PRIMORDIAL_CORAL_MUSHROOM = register("primordial_coral_mushroom", new PrimordialCoralMushroomFeature(DefaultFeatureConfig.CODEC));
    public static  final Feature<DefaultFeatureConfig> PRIMORDIAL_CORAL_TREE = register("primordial_coral_tree", new PrimordialCoralTreeFeature(DefaultFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registries.FEATURE, JamiesMod.getModId(name), feature);
    }

    public static void init() {
        Registry.register(Registries.FEATURE, JamiesMod.getModId("ancient_tree"), ANCIENT_TREE);
        Registry.register(Registries.FEATURE, JamiesMod.getModId("underhang_vegetation"), ANCIENT_FOREST_VEGATATION);
        Registry.register(Registries.FEATURE, JamiesMod.getModId("ancient_flowers"), FLOWER);
        Registry.register(Registries.FEATURE, JamiesMod.getModId("small_cloud"), SMALL_CLOUD);
        Registry.register(Registries.FEATURE, JamiesMod.getModId("amber"), AMBER);
        Registry.register(Registries.FEATURE, JamiesMod.getModId("amber_under"), AMBER_UNDER);

        Registry.register(Registries.FEATURE, JamiesMod.getModId("primordial_coral_claw"), PRIMORDIAL_CORAL_CLAW);
        Registry.register(Registries.FEATURE, JamiesMod.getModId("primordial_coral_mushroom"), PRIMORDIAL_CORAL_MUSHROOM);
        Registry.register(Registries.FEATURE, JamiesMod.getModId("primordial_coral_tree"), PRIMORDIAL_CORAL_TREE);
    }
}

