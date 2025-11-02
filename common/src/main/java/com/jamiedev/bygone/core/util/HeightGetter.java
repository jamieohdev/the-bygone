package com.jamiedev.bygone.core.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;

@FunctionalInterface
public interface HeightGetter {
    DataComponentType<Integer> MAP_HEIGHT = DataComponentType.<Integer>builder().persistent(Codec.INT).build();

    int getHeight();
}
