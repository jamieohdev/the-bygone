package com.jamiedev.bygone.common.component.entity;

import com.jamiedev.bygone.core.registry.BGAttachmentTypes;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;

import io.netty.buffer.ByteBuf;

import java.lang.ref.WeakReference;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

public final class HookData{
    public static final StreamCodec<ByteBuf,HookData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, HookData::id,
            HookData::new
    );

    private final int id;
    public HookData(int id){ this.id = id; }
    public int id(){ return id; }



    // IK I SAID NO MUTABILITY! but this is just a cache. no actual state is held here.
    private WeakReference<HookEntity> hook = null;

    public static HookEntity getHook(Entity entity){
        if(!entity.has(BGAttachmentTypes.HOOK)) return null;
        HookData data = entity.get(BGAttachmentTypes.HOOK);
        if(data == null) return null;

        if(data.hook == null || data.hook.refersTo(null) || data.hook.get().isRemoved()){
            if(entity.level().getEntity(data.id) instanceof HookEntity hook && !hook.isRemoved()){
                data.hook = new WeakReference<>(hook);
            } else{ // we're outdated (or chunks are unloaded)
                if(!entity.level().isClientSide) entity.remove(BGAttachmentTypes.HOOK); // we're outdated
                data.hook = null;
            }
        }
        return data.hook == null ? null : data.hook.get();
    }
}
