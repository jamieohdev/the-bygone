package com.jamiedev.mod.common.client.network;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.entities.projectile.HookEntity;
import com.jamiedev.mod.common.network.SyncPlayerHookS2C;
import com.jamiedev.mod.common.util.PlayerWithHook;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class SyncPlayerHookPacketHandler {

    public static void handle(SyncPlayerHookS2C packet, ClientPlayNetworking.Context context) {
        int hookId = packet.hookId();
        UUID playerUUID = packet.playerUUID();
        ClientWorld world = context.client().world;
        Entity entity = world.getEntityById(hookId);
        HookEntity hook = entity instanceof HookEntity ? (HookEntity)entity : null;
        PlayerEntity playerByUuid = world.getPlayerByUuid(playerUUID);
        if(playerByUuid != null){
            JamiesModFabric.LOGGER.info("Syncing {} to {}", hook, playerByUuid);
            ((PlayerWithHook)playerByUuid).bygone$setHook(hook);
        } else{
            JamiesModFabric.LOGGER.error("Could not find player with UUID {}, unable to sync their hook", playerUUID);
        }
    }
}
