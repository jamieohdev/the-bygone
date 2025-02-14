package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AttachmentTypesNeoForge {

    public static final AttachmentType<Integer> TIME_IN_BYGONE = Registry.register(NeoForgeRegistries.ATTACHMENT_TYPES, Bygone.id("time_in_bygone"),
            AttachmentType.builder(() -> 0).serialize(Codec.INT).build());



    public static void init() {

    }

}
