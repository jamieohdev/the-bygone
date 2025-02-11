package com.jamiedev.mod.common.network;

import com.jamiedev.mod.fabric.JamiesModFabric;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncPlayerHookS2C(int hookId, UUID playerUUID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPlayerHookS2C> PACkET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath(JamiesModFabric.MOD_ID, "sync_player_hook"));
    public static final StreamCodec<FriendlyByteBuf, SyncPlayerHookS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncPlayerHookS2C::hookId,
            UUIDUtil.STREAM_CODEC, SyncPlayerHookS2C::playerUUID,
            SyncPlayerHookS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACkET_ID;
    }
}
