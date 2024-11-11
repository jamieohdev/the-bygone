package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.common.compounds.CasterComponent;
import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CasterBlockEntity extends BlockEntity implements CasterComponent {
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

    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(onCooldown);
    }

    public void applySyncPacket(RegistryByteBuf buf) {
        onCooldown = buf.readBoolean();
    }
}
