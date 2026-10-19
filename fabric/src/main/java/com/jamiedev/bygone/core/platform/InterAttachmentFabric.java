package com.jamiedev.bygone.core.platform;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import java.util.List;

public class InterAttachmentFabric{
    private static AttachmentType<?>[] attachments = null;

    public static <T> AttachmentType<T> get(InterAttachmentType<T> inter){
        if(attachments == null) throw new IllegalStateException("tried to retrieve attachment too early: " + inter);
        return (AttachmentType<T>)(Object)attachments[inter.index]; // we know this is safe bc of how we make the array
    }



    public static void register(){
        List<InterAttachmentType<?>> inters = InterAttachmentType.register();
        attachments = new AttachmentType<?>[inters.size()];

        for(InterAttachmentType<?> inter : inters) attachments[inter.index] = from(inter);
    }
    private static <T> AttachmentType<T> from(InterAttachmentType<T> inter){
        return AttachmentRegistry.create(inter.id, builder -> {
            builder.initializer(() -> inter.t);
            if(inter.codec != null) builder.persistent(inter.codec);
            if(inter.streamCodec != null) builder.syncWith(inter.streamCodec, AttachmentSyncPredicate.all());
            if(inter.copy) builder.copyOnDeath();
        });
    }
}
