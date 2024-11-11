package com.jamiedev.mod.common.network;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record SyncPlayerHookS2C(int hookId, UUID playerUUID) implements CustomPayload {
    public static final CustomPayload.Id<SyncPlayerHookS2C> PACkET_ID = new Id<>(Identifier.of(JamiesModFabric.MOD_ID, "sync_player_hook"));
    public static final PacketCodec<PacketByteBuf, SyncPlayerHookS2C> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SyncPlayerHookS2C::hookId,
            Uuids.PACKET_CODEC, SyncPlayerHookS2C::playerUUID,
            SyncPlayerHookS2C::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACkET_ID;
    }
}
