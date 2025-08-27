package com.jamiedev.bygone.common.util;

import net.minecraft.server.MinecraftServer;

public class ServerTickHandler {
    
    /**
     * Called every server tick. This method should be called from both
     * Fabric (ServerTickEvents.END_SERVER_TICK) and NeoForge (ServerTickEvent.Post)
     */
    public static void onServerTick(MinecraftServer server) {
        ShockwaveHandler.tickAll();
    }
}