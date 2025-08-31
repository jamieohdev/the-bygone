package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.core.network.SyncPlayerHookS2C;
import com.jamiedev.bygone.core.network.UpdraftMovementS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public class ClientPacketHandler {

    public static void handle(SyncPlayerHookS2C packet) {
        int hookId = packet.hookId();
        UUID playerUUID = packet.playerUUID();
        ClientLevel world = Minecraft.getInstance().level;
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
    
    public static void handle(UpdraftMovementS2C packet) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x, packet.velocityY(), motion.z);
            player.resetFallDistance();
        }
    }
}
