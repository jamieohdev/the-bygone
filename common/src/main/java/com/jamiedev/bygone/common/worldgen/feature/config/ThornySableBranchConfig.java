package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record ThornySableBranchConfig(int spreadWidth, int spreadHeight,
                                      int maxHeight) implements FeatureConfiguration {
    public static final Codec<ThornySableBranchConfig> CODEC = RecordCodecBuilder.create((p_191375_)
            -> p_191375_.group(ExtraCodecs.POSITIVE_INT.fieldOf("spread_width")
            .forGetter(ThornySableBranchConfig::spreadWidth), ExtraCodecs.POSITIVE_INT.fieldOf("spread_height")
            .forGetter(ThornySableBranchConfig::spreadHeight), ExtraCodecs.POSITIVE_INT.fieldOf("max_height")
            .forGetter(ThornySableBranchConfig::maxHeight)).apply(p_191375_, ThornySableBranchConfig::new));

    public ThornySableBranchConfig(int spreadWidth, int spreadHeight, int maxHeight) {
        this.spreadWidth = spreadWidth;
        this.spreadHeight = spreadHeight;
        this.maxHeight = maxHeight;
    }

    public int spreadWidth() {
        return this.spreadWidth;
    }

    public int spreadHeight() {
        return this.spreadHeight;
    }

    public int maxHeight() {
        return this.maxHeight;
    }
}