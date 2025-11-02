package com.jamiedev.bygone.core.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ModPacket<T extends FriendlyByteBuf> extends CustomPacketPayload {

    static <T extends FriendlyByteBuf, P extends ModPacket<T>> Type<P> type(Class<P> pClass) {
        return new Type<>(PacketHandler.packet(pClass));
    }

}
