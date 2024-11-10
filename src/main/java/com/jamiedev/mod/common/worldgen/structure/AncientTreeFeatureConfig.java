package com.jamiedev.mod.common.worldgen.structure;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record AncientTreeFeatureConfig (int spreadWidth, int spreadHeight, int maxHeight) implements FeatureConfig {
    public static final Codec<AncientTreeFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codecs.POSITIVE_INT.fieldOf("spread_width").forGetter(AncientTreeFeatureConfig::spreadWidth),
                Codecs.POSITIVE_INT.fieldOf("spread_height").forGetter(AncientTreeFeatureConfig::spreadHeight),
                Codecs.POSITIVE_INT.fieldOf("max_height").forGetter(AncientTreeFeatureConfig::maxHeight)).apply(instance, AncientTreeFeatureConfig::new);
    });

    public AncientTreeFeatureConfig(int spreadWidth, int spreadHeight, int maxHeight) {
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
