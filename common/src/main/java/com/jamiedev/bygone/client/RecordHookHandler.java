package com.jamiedev.bygone.client;


import com.jamiedev.bygone.entities.projectile.HookEntity;
import com.jamiedev.bygone.init.JamiesModEntityTypes;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
public class RecordHookHandler {

    public static void handle(Supplier<Supplier<Minecraft>> minecraft, RecordHookPacket packet) {
        ClientLevel level = minecraft.get().get().level;
        if(level == null) {
            return;
        }
        HookEntity entity = JamiesModEntityTypes.HOOK.create(level);
        if (entity == null) {
            return;
        }
        entity.setDeltaMovement(packet.x(), packet.y(), packet.z());
        entity.setPosRaw(packet.x(), packet.y(), packet.z());
        entity.getX(packet.xRot());
        entity.getViewVector(packet.yRot());
        entity.setId(packet.id());
        entity.setUUID(packet.uuid());

        if(packet.hasGrapplingPlayer() && level.getEntity(packet.grapplingPlayerId()) instanceof Player player) {
            entity.setOwner(player);
        }
    }
}
