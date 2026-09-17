package com.jamiedev.bygone.core.network;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncWeatherS2C(CompoundTag tag) implements S2CModPacket<RegistryFriendlyByteBuf> {
    public static final CustomPacketPayload.Type<SyncWeatherS2C> PACkET_ID = new CustomPacketPayload.Type<>(Bygone.id("sync_weather"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWeatherS2C> CODEC = StreamCodec.composite(
        ByteBufCodecs.COMPOUND_TAG, SyncWeatherS2C::tag,
        SyncWeatherS2C::new
    );

    @Override
    public void handleClient() {
        BygoneWeather.Client.getInstance().updateContext(tag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACkET_ID;
    }
}
