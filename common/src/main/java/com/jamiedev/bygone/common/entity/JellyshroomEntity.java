package com.jamiedev.bygone.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.level.Level;

public class JellyshroomEntity extends FlyingMob
{
    protected JellyshroomEntity(EntityType<? extends FlyingMob> entityType, Level world) {
        super(entityType, world);
    }
}
