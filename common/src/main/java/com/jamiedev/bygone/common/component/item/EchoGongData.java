package com.jamiedev.bygone.common.component.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record EchoGongData(int charge){
    public static final Codec<EchoGongData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("charge").forGetter(EchoGongData::charge)
    ).apply(i, EchoGongData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf,EchoGongData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, EchoGongData::charge,
            EchoGongData::new
    );

    public static final EchoGongData EMPTY = new EchoGongData(0);
}
