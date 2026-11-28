package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.registry.BGAttachmentTypes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin{
    @WrapOperation(method = "handleMovePlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;clientIsFloating:Z", opcode = Opcodes.PUTFIELD))
    private void dontKickHookers(ServerGamePacketListenerImpl self, boolean floating, Operation<Void> op){
        op.call(self, floating && !self.player.has(BGAttachmentTypes.HOOK));
    }
}
