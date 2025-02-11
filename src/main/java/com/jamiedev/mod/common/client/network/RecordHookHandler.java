package com.jamiedev.mod.common.client.network;


import com.jamiedev.mod.common.entities.projectile.HookEntity;
import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Supplier;
public class RecordHookHandler {

    public static void handle(Supplier<Supplier<MinecraftClient>> minecraft, RecordHookPacket packet) {
        ClientWorld level = minecraft.get().get().world;
        if(level == null) {
            return;
        }
        HookEntity entity = JamiesModEntityTypes.HOOK.create(level);
        if (entity == null) {
            return;
        }
        entity.setVelocity(packet.x(), packet.y(), packet.z());
        entity.setPos(packet.x(), packet.y(), packet.z());
        entity.offsetX(packet.xRot());
        entity.getRotationVec(packet.yRot());
        entity.setId(packet.id());
        entity.setUuid(packet.uuid());

        if(packet.hasGrapplingPlayer() && level.getEntityById(packet.grapplingPlayerId()) instanceof PlayerEntity player) {
            entity.setOwner(player);
        }
    }
}
