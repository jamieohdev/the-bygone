package com.jamiedev.bygone.core.network;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.ClientPacketHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record UpdraftMovementS2C(double velocityY,
                                 boolean isDescending) implements S2CModPacket<RegistryFriendlyByteBuf> {
    public static final CustomPacketPayload.Type<UpdraftMovementS2C> PACKET_ID = new Type<>(Bygone.id("updraft_movement"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdraftMovementS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, UpdraftMovementS2C::velocityY,
            ByteBufCodecs.BOOL, UpdraftMovementS2C::isDescending,
            UpdraftMovementS2C::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.handle(this);
    }
}