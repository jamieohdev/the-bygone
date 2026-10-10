package com.jamiedev.bygone.core.mixin;

import java.util.Arrays;

import net.minecraft.world.level.material.FogType;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

// enum doesn't support neoforge's IExtensibleEnum
// random priority for ensuring consistent mixin order
@Mixin(value = FogType.class, priority = 276921140)
public class FogTypeMixin{
    @Shadow @Final @Mutable private static FogType[] $VALUES;
    @Invoker("<init>") private static FogType init(String name, int id){
        return null;
    }
    static{
        $VALUES = Arrays.copyOf($VALUES, $VALUES.length + 1);
        $VALUES[$VALUES.length - 1] = init("bygone:litho", $VALUES.length - 1);
    }
}
