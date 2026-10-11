package com.jamiedev.bygone.common.component.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.food.FoodProperties;

public record GumboIngredientData(FoodProperties properties){
    public static final Codec<GumboIngredientData> CODEC = RecordCodecBuilder.create(i -> i.group(
            FoodProperties.DIRECT_CODEC.fieldOf("food_properties").forGetter(GumboIngredientData::properties)
    ).apply(i, GumboIngredientData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf,GumboIngredientData> STREAM_CODEC = StreamCodec.composite(
            FoodProperties.DIRECT_STREAM_CODEC, GumboIngredientData::properties,
            GumboIngredientData::new
    );
}
