package com.jamiedev.bygone.platform;

import com.jamiedev.bygone.network.C2SModPacket;
import com.jamiedev.bygone.network.S2CModPacket;
import com.jamiedev.bygone.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Collection;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <MSG extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        PayloadTypeRegistry.playS2C().register(type,streamCodec);//payload needs to be registered on server/client, packethandler is client only
        if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(type,(payload, context) -> context.client().execute(payload::handleClient));
        }
    }

    @Override
    public <MSG extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        PayloadTypeRegistry.playC2S().register(type,streamCodec);
        ServerPlayNetworking.registerGlobalReceiver(type,(payload, context) -> context.player().server.execute(() -> payload.handleServer(context.player())));
    }

    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        ServerPlayNetworking.send(player,msg);
    }

    @Override
    public void sendToServer(C2SModPacket<?> msg) {
        ClientPlayNetworking.send(msg);
    }

    @Override
    public void sendToTracking(S2CModPacket<?> msg, Entity entity, boolean includeSelf) {
        Collection<ServerPlayer> trackingPlayers = PlayerLookup.tracking(entity);

        for (ServerPlayer tracking : trackingPlayers) {
            sendToClient(msg,tracking);
        }

        if (includeSelf && entity instanceof ServerPlayer self) {
            sendToClient(msg,self);
        }
    }
}
