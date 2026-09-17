package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public abstract class SpectralMob extends LivingEntity {

    private static final EntityDataAccessor<Boolean> DATA_REPEL_RUN;
    private int checkRepelTicks = 0;

    static {
        DATA_REPEL_RUN = SynchedEntityData.defineId(SpectralMob.class, EntityDataSerializers.BOOLEAN);
    }


    protected SpectralMob(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public boolean getDataRepelRun() {
        return this.entityData.get(DATA_REPEL_RUN);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_REPEL_RUN, false);
    }

    private boolean collidingHurtSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        });
    }

    private boolean collidingSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.SPECTRAL_BLOCKS);
        });
    }

    public void tick() {
        this.setNoGravity(true);
        super.tick();

        Level level = this.level();

        if (!level.isClientSide()) {
            if (this.checkRepelTicks <= 0) {
                Optional<BlockPos> repelPos = BlockPos.findClosestMatch(this.blockPosition(), 8, 4, pos -> level.getBlockState(pos).is(JamiesModTag.HURT_SPECTRAL_BLOCKS));
                if (repelPos.isPresent()) {
                    this.entityData.set(DATA_REPEL_RUN, true);
                } else {
                    this.entityData.set(DATA_REPEL_RUN, false);
                }
                this.checkRepelTicks = 20;
            }
        }
        if (this.checkRepelTicks > 0) {
            this.checkRepelTicks--;
        }

        if  (collidingHurtSpectralBlocks())
        {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
        }

        noPhysics = !collidingSpectralBlocks();
    }
}
