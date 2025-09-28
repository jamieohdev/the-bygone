package com.jamiedev.bygone.common.worldgen.feature.config;

import com.jamiedev.bygone.common.worldgen.feature.SableBranchFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SableBranchConfig(int spreadWidth, int spreadHeight, int maxHeight) implements FeatureConfiguration {
    public static final Codec<SableBranchConfig> CODEC = RecordCodecBuilder.create((p_191375_)
            -> p_191375_.group(ExtraCodecs.POSITIVE_INT.fieldOf("spread_width")
            .forGetter(SableBranchConfig::spreadWidth), ExtraCodecs.POSITIVE_INT.fieldOf("spread_height")
            .forGetter(SableBranchConfig::spreadHeight), ExtraCodecs.POSITIVE_INT.fieldOf("max_height")
            .forGetter(SableBranchConfig::maxHeight)).apply(p_191375_, SableBranchConfig::new));

    public SableBranchConfig(int spreadWidth, int spreadHeight, int maxHeight) {
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