package com.jamiedev.bygone.common.entity;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.entity.ai.SabeastAI;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class SabeastEntity extends Monster {

    protected static final ImmutableList<SensorType<? extends Sensor<? super SabeastEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.UNIVERSAL_ANGER,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM
    );


    public SabeastEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double)50.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.22F)
                .add(Attributes.ATTACK_DAMAGE, (double)8.0F)
                .add(Attributes.STEP_HEIGHT, (double)1.0F);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("sabeastBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        SabeastAI.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    protected Brain.Provider<SabeastEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return SabeastAI.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public Brain<SabeastEntity> getBrain() {
        return (Brain<SabeastEntity>)super.getBrain();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)) {
            return super.hurt(source, amount*4);
        }

        return super.hurt(source, (float) (amount*0.8));
    }

    static {

    }
}
