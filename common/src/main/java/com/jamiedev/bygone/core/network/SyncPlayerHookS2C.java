package com.jamiedev.bygone.core.network;

import com.jamiedev.bygone.Bygone;

import java.util.UUID;

import com.jamiedev.bygone.client.ClientPacketHandler;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncPlayerHookS2C(int hookId, UUID playerUUID) implements S2CModPacket<RegistryFriendlyByteBuf> {
    public static final CustomPacketPayload.Type<SyncPlayerHookS2C> PACkET_ID = new Type<>(Bygone.id("sync_player_hook"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlayerHookS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncPlayerHookS2C::hookId,
            UUIDUtil.STREAM_CODEC, SyncPlayerHookS2C::playerUUID,
            SyncPlayerHookS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACkET_ID;
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.handle(this);
    }
}
