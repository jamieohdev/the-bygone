package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

@SuppressWarnings("UnstableApiUsage")
public class AttachmentTypesFabric {

    public static final AttachmentType<HookEntity> GRAPPLING = AttachmentRegistry.<HookEntity>builder()
            .buildAndRegister(Bygone.id("hook"));

    public static final AttachmentType<Integer> TIME_IN_BYGONE = AttachmentRegistry.<Integer>builder()
            .initializer(() -> 0)
            .persistent(Codec.INT)
            .buildAndRegister(Bygone.id("time_in_bygone"));

}
