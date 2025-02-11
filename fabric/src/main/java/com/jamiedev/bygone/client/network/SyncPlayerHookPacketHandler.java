package com.jamiedev.bygone.client.network;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entities.projectile.HookEntity;
import com.jamiedev.bygone.network.SyncPlayerHookS2C;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;

public class SyncPlayerHookPacketHandler {

    public static void handle(SyncPlayerHookS2C packet, ClientPlayNetworking.Context context) {
        int hookId = packet.hookId();
        UUID playerUUID = packet.playerUUID();
        ClientLevel world = context.client().level;
        Entity entity = world.getEntity(hookId);
        HookEntity hook = entity instanceof HookEntity ? (HookEntity)entity : null;
        Player playerByUuid = world.getPlayerByUUID(playerUUID);
        if(playerByUuid != null){
            Bygone.LOGGER.info("Syncing {} to {}", hook, playerByUuid);
            ((PlayerWithHook)playerByUuid).bygone$setHook(hook);
        } else{
            Bygone.LOGGER.error("Could not find player with UUID {}, unable to sync their hook", playerUUID);
        }
    }
}
