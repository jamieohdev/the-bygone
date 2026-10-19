package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
// please use these packages for any records you wanna attach. see BGDataComponentTypes for examples
// import com.jamiedev.bygone.common.component.block.*;
// import com.jamiedev.bygone.common.component.entity.*;
// import com.jamiedev.bygone.common.component.chunk.*;
import com.jamiedev.bygone.core.platform.InterAttachmentType;

import net.minecraft.network.codec.ByteBufCodecs;

public class BGInterAttachmentTypes{
    public static final InterAttachmentType<Integer> LAST_TICK_IN_PORTAL =
            new InterAttachmentType.Builder(Bygone.id("last_tick_in_portal")).withDefault(0).build();



    public static void init(){}
}
