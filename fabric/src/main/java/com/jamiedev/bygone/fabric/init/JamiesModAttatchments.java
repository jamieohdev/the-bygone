package com.jamiedev.bygone.fabric.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entities.projectile.HookEntity;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

@SuppressWarnings("UnstableApiUsage")
public class JamiesModAttatchments
{

    interface Attatchments
    {
        String GRAPPLING = "hook";
    }

    public static final AttachmentType<HookEntity> GRAPPLING = AttachmentRegistry.<HookEntity>builder()
            .buildAndRegister(Bygone.getModId(Attatchments.GRAPPLING));



}
