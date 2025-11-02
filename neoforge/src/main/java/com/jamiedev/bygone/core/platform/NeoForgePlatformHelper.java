package com.jamiedev.bygone.core.platform;

import com.jamiedev.bygone.PacketHandlerNeoForge;
import com.jamiedev.bygone.core.network.C2SModPacket;
import com.jamiedev.bygone.core.network.S2CModPacket;
import com.jamiedev.bygone.core.platform.services.IPlatformHelper;
import com.jamiedev.bygone.core.registry.AttachmentTypesNeoForge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgePlatformHelper implements IPlatformHelper {

    public static PayloadRegistrar registrar;

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <MSG extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar.playToClient(type, streamCodec, (p, t) -> p.handleClient());
    }

    @Override
    public <MSG extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar.playToServer(type, streamCodec, (p, t) -> p.handleServer((ServerPlayer) t.player()));
    }


    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        PacketHandlerNeoForge.sendToClient(msg, player);
    }

    @Override
    public void sendToServer(C2SModPacket<?> msg) {
        PacketHandlerNeoForge.sendToServer(msg);
    }

    @Override
    public void sendToTracking(S2CModPacket<?> msg, Entity entity, boolean includeSelf) {
        if (includeSelf) PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, msg);
        else PacketDistributor.sendToPlayersTrackingEntity(entity, msg);
    }

    @Override
    public int getTimeInBygone(Entity entity) {
        return entity.getData(AttachmentTypesNeoForge.TIME_IN_BYGONE);
    }

    @Override
    public void setTimeInBygone(Entity entity, int time) {
        entity.setData(AttachmentTypesNeoForge.TIME_IN_BYGONE, time);
    }
}