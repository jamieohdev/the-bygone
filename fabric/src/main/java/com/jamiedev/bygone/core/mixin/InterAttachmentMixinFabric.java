package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.extension.InterAttachmentExtension;
import com.jamiedev.bygone.core.platform.InterAttachmentFabric;
import com.jamiedev.bygone.core.platform.InterAttachmentType;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ BlockEntity.class, ChunkAccess.class, Entity.class })
public abstract class InterAttachmentMixinFabric implements InterAttachmentExtension{
    @Unique private final AttachmentTarget HOLDER = (AttachmentTarget)(Object)this;

    @Override public <T> T get(InterAttachmentType<T> type){ return this.HOLDER.getAttachedOrCreate(InterAttachmentFabric.get(type)); }
    @Override public <T> boolean has(InterAttachmentType<T> type){ return this.HOLDER.hasAttached(InterAttachmentFabric.get(type)); }
    @Override public <T> void set(InterAttachmentType<T> type, T t){ this.HOLDER.setAttached(InterAttachmentFabric.get(type), t); }
    @Override public <T> void remove(InterAttachmentType<T> type){ this.HOLDER.removeAttached(InterAttachmentFabric.get(type)); }
}
