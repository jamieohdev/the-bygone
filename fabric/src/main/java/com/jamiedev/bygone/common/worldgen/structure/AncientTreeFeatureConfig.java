package com.jamiedev.bygone.common.worldgen.structure;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record AncientTreeFeatureConfig (int spreadWidth, int spreadHeight, int maxHeight) implements FeatureConfiguration {
    public static final Codec<AncientTreeFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("spread_width").forGetter(AncientTreeFeatureConfig::spreadWidth),
                ExtraCodecs.POSITIVE_INT.fieldOf("spread_height").forGetter(AncientTreeFeatureConfig::spreadHeight),
                ExtraCodecs.POSITIVE_INT.fieldOf("max_height").forGetter(AncientTreeFeatureConfig::maxHeight)).apply(instance, AncientTreeFeatureConfig::new);
    });

    public AncientTreeFeatureConfig(int spreadWidth, int spreadHeight, int maxHeight) {
        this.spreadWidth = spreadWidth;
        this.spreadHeight = spreadHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public int spreadWidth() {
        return this.spreadWidth;
    }

    @Override
    public int spreadHeight() {
        return this.spreadHeight;
    }

    @Override
    public int maxHeight() {
        return this.maxHeight;
    }
}
