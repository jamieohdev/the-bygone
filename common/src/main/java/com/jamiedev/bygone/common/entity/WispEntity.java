package com.jamiedev.bygone.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;

public class WispEntity extends Allay
{
    public WispEntity(EntityType<? extends WispEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public void aiStep() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 0.1; ++i) {
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getRandomX(0.5D),
                        this.getRandomY(),
                        this.getRandomZ(0.5D),
                        0.0D, 0.0D, 0.0D);
            }
        }

        super.aiStep();
    }
}
