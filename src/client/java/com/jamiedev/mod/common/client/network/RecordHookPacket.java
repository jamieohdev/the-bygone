package com.jamiedev.mod.common.client.network;


import com.jamiedev.mod.common.entities.projectile.HookEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public record RecordHookPacket(	UUID uuid,
                                   int id,
                                   double x, double y, double z,
                                   int xRot, int yRot,
                                   boolean hasGrapplingPlayer,
                                   int grapplingPlayerId
)
{

    public RecordHookPacket(HookEntity entity) {
        this(
                entity.getUuid(), entity.getId(),
                entity.getX(), entity.getY(), entity.getZ(),
                MathHelper.floor(entity.getX() * 256.0F / 360.0F),
                MathHelper.floor(entity.getY() * 256.0F / 360.0F),
                entity.getPlayerOwner() != null,
                entity.getPlayerOwner() == null ? -1 : entity.getPlayerOwner().getId()
        );
    }

    public static void encode(RecordHookPacket packet, PacketByteBuf buf) {
        buf.writeUuid(packet.uuid());
        buf.writeVarInt(packet.id());
        buf.writeDouble(packet.x());
        buf.writeDouble(packet.y());
        buf.writeDouble(packet.z());
        buf.writeByte(packet.xRot);
        buf.writeByte(packet.yRot);
        buf.writeBoolean(packet.hasGrapplingPlayer);
        buf.writeVarInt(packet.grapplingPlayerId);
    }

    public static RecordHookPacket decode(PacketByteBuf buffer) {
        return new RecordHookPacket(
                buffer.readUuid(),
                buffer.readVarInt(),
                buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                buffer.readByte(), buffer.readByte(),
                buffer.readBoolean(), buffer.readVarInt()
        );
    }
}
