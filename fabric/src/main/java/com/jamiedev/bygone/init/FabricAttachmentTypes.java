package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.entities.projectile.HookEntity;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class FabricAttachmentTypes {
    public static final AttachmentType<HookEntity> GRAPPLING = AttachmentRegistry.<HookEntity>builder()
            .buildAndRegister(Bygone.id(JamiesModAttachmentData.HOOK));

}
