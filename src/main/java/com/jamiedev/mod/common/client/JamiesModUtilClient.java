package com.jamiedev.mod.common.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public class JamiesModUtilClient {
    public static ClientPlayerEntity getClientPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static ClientWorld getClientLevel() {
        return MinecraftClient.getInstance().world;
    }

}
