package com.jamiedev.bygone.common.entity;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum BigBeakVariants implements StringRepresentable {
    NORMAL(0, "normal"),
    TROPICAL(1, "tropical"),
    PEACHY(2, "peachy"),
    BLUEBILL(3, "bluebill"),
    FROSTY(4, "frosty"),
    NOMAD(5, "nomad"),
    SAVANNA(6, "savanna"),
    TRANS(7, "trans"),
    LESBIAN(8, "lesbian");

    public static final Codec<BigBeakVariants> CODEC = StringRepresentable.fromEnum(BigBeakVariants::values);
    private static final IntFunction<BigBeakVariants> BY_ID = ByIdMap.continuous(BigBeakVariants::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String name;

    private BigBeakVariants(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public static BigBeakVariants byId(int id) {
        return (BigBeakVariants)BY_ID.apply(id);
    }

    public String getSerializedName() {
        return this.name;
    }
}
