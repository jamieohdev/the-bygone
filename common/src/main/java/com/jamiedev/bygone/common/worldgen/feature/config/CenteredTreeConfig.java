package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record CenteredTreeConfig(List<ResourceLocation> templates) implements FeatureConfiguration {
    public static final Codec<CenteredTreeConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.listOf().fieldOf("templates")
                .forGetter(CenteredTreeConfig::templates)
    ).apply(instance, CenteredTreeConfig::new));
}