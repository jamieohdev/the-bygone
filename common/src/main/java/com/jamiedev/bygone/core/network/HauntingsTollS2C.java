package com.jamiedev.bygone.core.network;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.ClientPacketHandler;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HauntingsTollS2C() implements S2CModPacket<RegistryFriendlyByteBuf> {
    public static final CustomPacketPayload.Type<HauntingsTollS2C> PACkET_ID = new CustomPacketPayload.Type<>(Bygone.id("hauntings_toll"));
    public static final StreamCodec<RegistryFriendlyByteBuf, HauntingsTollS2C> CODEC = StreamCodec.unit(new HauntingsTollS2C());

    @Override
    public void handleClient() { ClientPacketHandler.handle(this); }

    @Override
    public Type<? extends CustomPacketPayload> type() { return PACkET_ID; }
}

