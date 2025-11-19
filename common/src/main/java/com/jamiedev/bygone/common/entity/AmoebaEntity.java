package com.jamiedev.bygone.common.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.entity.ai.AmoebaAI;
import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AmoebaEntity extends AbstractFish {
    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 5;
    public static final int MAX_NATURAL_SIZE = 4;
    protected static final ImmutableList<SensorType<? extends Sensor<? super AmoebaEntity>>> SENSOR_TYPES;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    private static final EntityDataAccessor<Integer> ID_SIZE;

    static {
        SENSOR_TYPES = ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.HURT_BY,
                SensorType.FROG_TEMPTATIONS
        );
        MEMORY_TYPES = ImmutableList.of(
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.PATH,
                MemoryModuleType.NEAREST_VISIBLE_ADULT,
                MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
                MemoryModuleType.IS_TEMPTED,
                MemoryModuleType.TEMPTING_PLAYER,
                MemoryModuleType.BREED_TARGET,
                MemoryModuleType.IS_PANICKING
        );
        ID_SIZE = SynchedEntityData.defineId(AmoebaEntity.class, EntityDataSerializers.INT);
    }

    public AmoebaEntity(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 1.0F).add(Attributes.MAX_HEALTH, 6.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new AvoidBlockGoal(this, 32, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.AMOEBA_REPELLENTS);
        }));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AquifawnEntity.class, 8.0F, (double)1.0F, (double)1.0F));
    }

    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    protected Brain.@NotNull Provider<AmoebaEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return AmoebaAI.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (ID_SIZE.equals(key)) {
            this.refreshDimensions();
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;

        }

        super.onSyncedDataUpdated(key);
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_SIZE, MIN_SIZE);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Size", this.getSize() - 1);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        this.setSize(compound.getInt("Size") + 1, false);
        super.readAdditionalSaveData(compound);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(
                0, new AvoidBlockGoal(
                        this, 32, 1.4, 1.6, (pos) -> {
                    BlockState state = this.level().getBlockState(pos);
                    return state.is(JamiesModTag.AMOEBA_REPELLENTS);
                }
                )
        );
    }

    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.TADPOLE_FLOP;
    }

    @VisibleForTesting
    public void setSize(int size, boolean resetHealth) {
        int i = Mth.clamp(size, MIN_SIZE, 127);
        this.entityData.set(ID_SIZE, i);
        this.reapplyPosition();
        this.refreshDimensions();
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(i * i);
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.2F + 0.1F * (float) i);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }

        this.xpReward = i;
    }

    public int getSize() {
        return this.entityData.get(ID_SIZE);
    }

    public void refreshDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPos(x, y, z);
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return true;
    }

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.AMOEBA_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return BGSoundEvents.AMOEBA_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.AMOEBA_DEATH_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return BGSoundEvents.AMOEBA_FLOP_ADDITIONS_EVENT;
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return BGItems.AMOEBA_BUCKET.get().getDefaultInstance();
    }
}
