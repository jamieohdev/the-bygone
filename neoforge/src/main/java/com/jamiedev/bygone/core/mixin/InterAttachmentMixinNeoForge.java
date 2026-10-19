package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.extension.InterAttachmentExtension;
import com.jamiedev.bygone.core.platform.InterAttachmentNeoForge;
import com.jamiedev.bygone.core.platform.InterAttachmentType;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;

import net.neoforged.neoforge.attachment.IAttachmentHolder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ BlockEntity.class, ChunkAccess.class, Entity.class })
public abstract class InterAttachmentMixinNeoForge implements InterAttachmentExtension{
    @Unique private final IAttachmentHolder HOLDER = (IAttachmentHolder)(Object)this;

    @Override public <T> T get(InterAttachmentType<T> type){ return this.HOLDER.getData(InterAttachmentNeoForge.get(type)); }
    @Override public <T> boolean has(InterAttachmentType<T> type){ return this.HOLDER.hasData(InterAttachmentNeoForge.get(type)); }
    @Override public <T> void set(InterAttachmentType<T> type, T t){ this.HOLDER.setData(InterAttachmentNeoForge.get(type), t); }
    @Override public <T> void remove(InterAttachmentType<T> type){ this.HOLDER.removeData(InterAttachmentNeoForge.get(type)); }
}
