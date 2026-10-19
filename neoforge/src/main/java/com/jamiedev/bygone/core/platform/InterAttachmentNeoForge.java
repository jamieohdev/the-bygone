package com.jamiedev.bygone.core.platform;

import com.jamiedev.bygone.Bygone;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.List;

@EventBusSubscriber(modid = Bygone.MOD_ID)
public class InterAttachmentNeoForge{
    private static AttachmentType<?>[] attachments = null;

    public static <T> AttachmentType<T> get(InterAttachmentType<T> inter){
        if(attachments == null) throw new IllegalStateException("tried to retrieve attachment too early: " + inter);
        return (AttachmentType<T>)(Object)attachments[inter.index]; // we know this is safe bc of how we make the array
    }



    @SubscribeEvent
    public static void register(RegisterEvent event){
        event.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, helper -> {
            List<InterAttachmentType<?>> inters = InterAttachmentType.register();
            attachments = new AttachmentType<?>[inters.size()];

            for(InterAttachmentType<?> inter : inters){
                AttachmentType<?> type = from(inter);
                helper.register(inter.id, type);
                attachments[inter.index] = type;
            }
        });
    }
    private static <T> AttachmentType<T> from(InterAttachmentType<T> inter){
        AttachmentType.Builder<T> builder = AttachmentType.builder(() -> inter.t);
        if(inter.codec != null) builder = builder.serialize(inter.codec);
        if(inter.streamCodec != null) builder = builder.sync(inter.streamCodec);
        if(inter.copy) builder = builder.copyOnDeath();
        return builder.build();
    }
}
