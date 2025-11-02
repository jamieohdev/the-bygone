package com.jamiedev.bygone.client;


import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class RecordHookHandler {

    public static void handle(Supplier<Supplier<Minecraft>> minecraft, RecordHookPacket packet) {
        ClientLevel level = minecraft.get().get().level;
        if (level == null) {
            return;
        }
        HookEntity entity = BGEntityTypes.HOOK.get().create(level);
        if (entity == null) {
            return;
        }
        entity.setDeltaMovement(packet.x(), packet.y(), packet.z());
        entity.setPosRaw(packet.x(), packet.y(), packet.z());
        entity.getX(packet.xRot());
        entity.getViewVector(packet.yRot());
        entity.setId(packet.id());
        entity.setUUID(packet.uuid());

        if (packet.hasGrapplingPlayer() && level.getEntity(packet.grapplingPlayerId()) instanceof Player player) {
            entity.setOwner(player);
        }
    }
}
