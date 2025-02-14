package com.jamiedev.bygone.client;


import com.jamiedev.bygone.entities.projectile.HookEntity;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

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
                entity.getUUID(), entity.getId(),
                entity.getX(), entity.getY(), entity.getZ(),
                Mth.floor(entity.getX() * 256.0F / 360.0F),
                Mth.floor(entity.getY() * 256.0F / 360.0F),
                entity.getPlayerOwner() != null,
                entity.getPlayerOwner() == null ? -1 : entity.getPlayerOwner().getId()
        );
    }

    public static void encode(RecordHookPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.uuid());
        buf.writeVarInt(packet.id());
        buf.writeDouble(packet.x());
        buf.writeDouble(packet.y());
        buf.writeDouble(packet.z());
        buf.writeByte(packet.xRot);
        buf.writeByte(packet.yRot);
        buf.writeBoolean(packet.hasGrapplingPlayer);
        buf.writeVarInt(packet.grapplingPlayerId);
    }

    public static RecordHookPacket decode(FriendlyByteBuf buffer) {
        return new RecordHookPacket(
                buffer.readUUID(),
                buffer.readVarInt(),
                buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                buffer.readByte(), buffer.readByte(),
                buffer.readBoolean(), buffer.readVarInt()
        );
    }
}
