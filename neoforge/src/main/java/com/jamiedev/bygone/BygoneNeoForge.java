package com.jamiedev.bygone;


import com.jamiedev.bygone.client.BygoneClientNeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Bygone.MOD_ID)
public class BygoneNeoForge {

    public BygoneNeoForge(IEventBus eventBus, Dist dist) {
        eventBus.addListener(PacketHandlerNeoForge::register);
        if (dist.isClient()) {
            BygoneClientNeoForge.init(eventBus);
        }
        Bygone.init();
    }
}