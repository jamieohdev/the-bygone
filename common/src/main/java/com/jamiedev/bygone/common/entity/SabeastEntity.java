package com.jamiedev.bygone.common.entity;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.entity.ai.SabeastAI;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class SabeastEntity extends Monster {

    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID;
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    private int warningSoundTicks;
    private static final float STAND_ANIMATION_TICKS = 6.0F;
    
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

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, (double)2.0F, (p_350292_) -> p_350292_.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new SabeastEntityMeleeAttackGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (Predicate)null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, MoobooEntity.class, 10, true, true, (Predicate)null));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        
        super.defineSynchedData(builder);
        builder.define(DATA_STANDING_ID, false);
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

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.refreshDimensions();
            }

            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding()) {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }

        if (!this.level().isClientSide) {

        }

    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.clientSideStandAnimation > 0.0F) {
            float f = this.clientSideStandAnimation / 6.0F;
            float f1 = 1.0F + f;
            return super.getDefaultDimensions(pose).scale(1.0F, f1);
        } else {
            return super.getDefaultDimensions(pose);
        }
    }

    public boolean isStanding() {
        return (Boolean)this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean standing) {
        this.entityData.set(DATA_STANDING_ID, standing);
    }

    public float getStandingAnimationScale(float partialTick) {
        return Mth.lerp(partialTick, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }


    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.makeSound(SoundEvents.POLAR_BEAR_WARNING);
            this.warningSoundTicks = 40;
        }

    }

    static {
        DATA_STANDING_ID = SynchedEntityData.defineId(SabeastEntity.class, EntityDataSerializers.BOOLEAN);
    }

    class SabeastEntityMeleeAttackGoal extends MeleeAttackGoal {
        public SabeastEntityMeleeAttackGoal() {
            super(SabeastEntity.this, (double)1.25F, true);
        }

        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                SabeastEntity.this.setStanding(false);
            } else if (this.mob.distanceToSqr(target) < (double)((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
                if (this.isTimeToAttack()) {
                    SabeastEntity.this.setStanding(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    SabeastEntity.this.setStanding(true);
                    SabeastEntity.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                SabeastEntity.this.setStanding(false);
            }

        }

        public void stop() {
            SabeastEntity.this.setStanding(false);
            super.stop();
        }
    }
}
