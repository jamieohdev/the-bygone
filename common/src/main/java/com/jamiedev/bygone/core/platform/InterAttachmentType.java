package com.jamiedev.bygone.core.platform;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
  * hi bygone devs! this class handles cross-platform data attachments!
  * these can be attached to entities, tile entities, and chunks in order to
  * both save and sync data. the api is pretty similar to that provided by
  * neoforge and fabric: initialize Builder to get a builder, and configure
  * it from there. registration is handled automatically.
  *
  * to access attachments, the methods get(InterAttachmentType<T>),
  * has(InterAttachmentType<T>), set(InterAttachmentType<T>,T), and
  * remove(InterAttachmentType<T>) have been provided in Entity, BlockEntity,
  * and ChunkAccess.
  *
  * these are two VERY IMPORTANT NOTES for using InterAttachmentType:
  * 1. NEVER, EVER MUTATE THE ATTACHMENT. attachments MUST BE IMMUTABLE.
  *    if you mutate an attachment, it WILL NOT BE SAVED, and it WILL NOT SYNC.
  *    if you need to modify an attachment value, ALWAYS CALL SET WITH A NEW
  *    INSTANCE. as such, complicated attachment classes should have
  *    methods that return new instances with mutated values.
  * 2. all fields and methods in InterAttachmentType are to be considered
  *    implementation detail. you should not need to access them! they are there
  *    purely for handling actual registration and attachment functionality.
  *
  * okay thank you have fun!!!
  **/
public final class InterAttachmentType<T>{
    private static List<InterAttachmentType<?>> attachments = new ArrayList<>();
    public static List<InterAttachmentType<?>> register(){
        if(attachments == null) throw new IllegalStateException("attempted to register attachments twice!");
        List<InterAttachmentType<?>> ret = attachments;
        attachments = null;
        return ret;
    }

    public final ResourceLocation id;
    public final T t;
    public final Codec<T> codec;
    public final StreamCodec<? super RegistryFriendlyByteBuf,T> streamCodec;
    public final boolean copy;
    public final int index;
    InterAttachmentType(ResourceLocation id, T t, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf,T> streamCodec, boolean copy){
        this.id = id;
        this.t = t;
        this.codec = codec;
        this.streamCodec = streamCodec;
        this.copy = copy;

        if(attachments == null) throw new IllegalStateException("attempted to create attachment late: " + id);
        this.index = attachments.size();
        attachments.add(this);
    }
    @Override public String toString(){ return "InterDataAttachment[" + id.toString() + "]"; }



    public static final class Builder<T>{
        private final ResourceLocation id;
        private T t;
        private Codec<T> codec = null;
        private StreamCodec<? super RegistryFriendlyByteBuf,T> streamCodec = null;
        private boolean copy = false;

        public Builder(ResourceLocation id){ this.id = id; }
        public Builder<T> withDefault(T t){ this.t = t; return this; }
        public Builder<T> saveWith(Codec<T> codec){ this.codec = codec; return this; }
        public Builder<T> syncWith(StreamCodec<? super RegistryFriendlyByteBuf,T> streamCodec){ this.streamCodec = streamCodec; return this; }
        /** only relevant when attached to players! requires a saving codec! **/
        public Builder<T> copyOnDeath(){ this.copy = true; return this; }
        public InterAttachmentType<T> build(){
            if(this.copy && this.codec == null) throw new IllegalStateException("attachment copying requires serialization: " + this.id);
            return new InterAttachmentType<T>(this.id, this.t, this.codec, this.streamCodec, this.copy);
        }
    }
}
