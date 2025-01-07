package com.jamiedev.mod.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.world.World;

public class JellyshroomEntity extends FlyingEntity
{
    protected JellyshroomEntity(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
    }
}
