package com.jamiedev.bygone.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class JamiesModUtilClient {
    public static LocalPlayer getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static ClientLevel getClientLevel() {
        return Minecraft.getInstance().level;
    }

}
