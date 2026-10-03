package com.jamiedev.bygone.core.mixin;

import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FogType.class)
public interface FogTypeInvoker {
    @Invoker("<init>")
    static FogType init(String name, int index) {
        return null;
    }
}