package com.jamiedev.bygone.core.mixin;

import net.minecraft.world.level.block.Portal;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.PortalProcessor.class)
public interface PortalProcessorAccessor {
    @Accessor
    Portal getPortal();
}
