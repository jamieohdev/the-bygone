package com.jamiedev.bygone.core.network;

import com.jamiedev.bygone.Bygone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record EnforcePacket(boolean enforce) implements CustomPacketPayload {
    public static final Type<EnforcePacket> TYPE = new Type<>(Bygone.id("sync_progression_status"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EnforcePacket> STREAM_CODEC = CustomPacketPayload.codec(EnforcePacket::write, EnforcePacket::new);

    public static boolean enforcedProgression = true;

    public EnforcePacket(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.enforce);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
