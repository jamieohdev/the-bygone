package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record FeatureFilledGeodeBlockAndFeatureSettings(BlockStateProvider fillingProvider,
        BlockStateProvider innerLayerProvider,
        BlockStateProvider alternateInnerLayerProvider,
        BlockStateProvider middleLayerProvider,
        BlockStateProvider outerLayerProvider,
        WeightedRandomList<WeightedEntry.Wrapper<Holder<PlacedFeature>>> innerAboveFeatures,
        WeightedRandomList<WeightedEntry.Wrapper<Holder<PlacedFeature>>> innerFeatures,
        WeightedRandomList<WeightedEntry.Wrapper<Holder<PlacedFeature>>> innerBelowFeatures,
        TagKey<Block> cannotReplace,
        TagKey<Block> invalidBlocks) {
    public static final Codec<FeatureFilledGeodeBlockAndFeatureSettings> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockStateProvider.CODEC.fieldOf("filling_provider")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::fillingProvider),
            BlockStateProvider.CODEC.fieldOf("inner_layer_provider")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::innerLayerProvider),
            BlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::alternateInnerLayerProvider),
            BlockStateProvider.CODEC.fieldOf("middle_layer_provider")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::middleLayerProvider),
            BlockStateProvider.CODEC.fieldOf("outer_layer_provider")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::outerLayerProvider),
            WeightedRandomList.codec(WeightedEntry.Wrapper.codec(PlacedFeature.CODEC))
                    .fieldOf("inner_above_features")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::innerAboveFeatures),
            WeightedRandomList.codec(WeightedEntry.Wrapper.codec(PlacedFeature.CODEC))
                    .fieldOf("inner_features")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::innerFeatures),
            WeightedRandomList.codec(WeightedEntry.Wrapper.codec(PlacedFeature.CODEC))
                    .fieldOf("inner_below_features")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::innerBelowFeatures),
            TagKey.hashedCodec(Registries.BLOCK)
                    .fieldOf("cannot_replace")
                    .forGetter(FeatureFilledGeodeBlockAndFeatureSettings::cannotReplace),
            TagKey.hashedCodec(Registries.BLOCK)
                    .fieldOf("invalid_blocks")
                    .forGetter((p_204564_) -> p_204564_.invalidBlocks)
    ).apply(instance, FeatureFilledGeodeBlockAndFeatureSettings::new));

}
