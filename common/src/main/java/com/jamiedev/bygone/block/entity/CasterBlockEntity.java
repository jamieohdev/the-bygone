package com.jamiedev.bygone.block.entity;

import com.jamiedev.bygone.init.JamiesModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CasterBlockEntity extends BlockEntity /*implements CasterComponent*/ {
    public int ticks = 0;
    public int cooldownTicks = 0;
    public boolean renderSpike = false;
    public boolean onCooldown = false;

    protected CasterBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(JamiesModBlockEntities.CASTER, blockPos, blockState);
    }

    public CasterBlockEntity(BlockPos pos, BlockState state) {
        this(JamiesModBlockEntities.CASTER, pos, state);
    }

    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeBoolean(onCooldown);
    }

    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        onCooldown = buf.readBoolean();
    }
}
