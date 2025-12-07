package com.jamiedev.bygone.common.entity;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum MurklingVariants implements StringRepresentable
{
    FUSCHIA(0, "fuschia"),
    OLIVE(1, "olive"),
    TEAL(2, "teal");

    public static final Codec<MurklingVariants> CODEC = StringRepresentable.fromEnum(MurklingVariants::values);
    private static final IntFunction<MurklingVariants> BY_ID = ByIdMap.continuous(MurklingVariants::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String name;

    MurklingVariants(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MurklingVariants byId(int id) {
        return BY_ID.apply(id);
    }

    public int getId() {
        return this.id;
    }

    public String getSerializedName() {
        return this.name;
    }    
}
