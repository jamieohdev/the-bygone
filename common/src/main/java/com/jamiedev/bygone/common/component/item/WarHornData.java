package com.jamiedev.bygone.common.component.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.UUID;

public record WarHornData(List<UUID> activeVexes, int cooldownSeconds, int vexTimeLeft){
    public static final Codec<WarHornData> CODEC = RecordCodecBuilder.create(i -> i.group(
            UUIDUtil.CODEC.listOf().fieldOf("active_vexes").forGetter(WarHornData::activeVexes),
            Codec.INT.fieldOf("cooldown_seconds").forGetter(WarHornData::cooldownSeconds),
            Codec.INT.fieldOf("vex_time_left").forGetter(WarHornData::vexTimeLeft)
    ).apply(i, WarHornData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf,WarHornData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), WarHornData::activeVexes,
            ByteBufCodecs.VAR_INT, WarHornData::cooldownSeconds,
            ByteBufCodecs.VAR_INT, WarHornData::vexTimeLeft,
            WarHornData::new
    );

    public static final WarHornData EMPTY = new WarHornData(List.of(), 0, 0);
}
