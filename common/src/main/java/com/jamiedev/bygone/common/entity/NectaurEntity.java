package com.jamiedev.bygone.common.entity;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.entity.ai.NectaurBrain;
import com.jamiedev.bygone.common.entity.projectile.NectaurPetalEntity;
import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NectaurEntity extends Animal implements NeutralMob, RangedAttackMob {

    protected static final ImmutableList<SensorType<? extends Sensor<? super NectaurEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            BGMemoryModuleTypes.NEAREST_NECTAUR_ALLY,
            BGMemoryModuleTypes.IS_LEADER,
            BGMemoryModuleTypes.IS_STALKING,
            BGMemoryModuleTypes.IS_IN_GROUP,
            BGMemoryModuleTypes.GROUP_LEADER,
            BGMemoryModuleTypes.NECTAUR_RANGED_COOLDOWN,
            MemoryModuleType.PATH,
            MemoryModuleType.SONIC_BOOM_COOLDOWN,
            MemoryModuleType.TOUCH_COOLDOWN,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.UNIVERSAL_ANGER,
            MemoryModuleType.NEAREST_ATTACKABLE
    );
    private static final UniformInt FIRST_ANGER_SOUND_DELAY;

    static {
        FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sneezeAnimationState = new AnimationState();
    private final int sneezeAnimationTimeout = 0;
    ZombifiedPiglin ref;
    private int playFirstAngerSoundIn;
    private int idleAnimationTimeout = 0;

    public NectaurEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.1F)
                .add(Attributes.ATTACK_DAMAGE, 4.0F)
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected Brain.Provider<NectaurEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return NectaurBrain.create(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    @SuppressWarnings("all")
    public Brain<NectaurEntity> getBrain() {
        return (Brain<NectaurEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {

        if (this.isAngry()) {
            this.maybePlayFirstAngerSound();
        }

        this.level().getProfiler().push("nectaurBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        NectaurBrain.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity entity && source.isDirect() && entity.getMainHandItem().isEmpty()) {
            entity.hurt(damageSources().generic(), 1.0F);
        } else if (source.getEntity() instanceof NectaurEntity) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType == MobSpawnType.NATURAL) {
            Level world = level.getLevel();
            float randomSpawns = this.random.nextFloat();

            if (randomSpawns < 0.30F) {

            } else if (randomSpawns < 0.70F) {

            } else if (randomSpawns < 0.85F) {
                for (int i = 0; i < 2; i++) {

                }
            } else if (randomSpawns < 0.95F) {
                for (int i = 0; i < 3; i++) {

                }
            } else if (randomSpawns < 1.00F) {
                for (int i = 0; i < 4; i++) {

                }
            }
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public boolean canBeAffected(MobEffectInstance potioneffect) {
        return (!potioneffect.is(MobEffects.MOVEMENT_SLOWDOWN) && !potioneffect.is(MobEffects.POISON)) && super.canBeAffected(potioneffect);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {

    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? BGSoundEvents.NECTAUR_SCREECH_ADDITIONS_EVENT : BGSoundEvents.NECTAUR_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.NECTAUR_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.NECTAUR_DEATH_ADDITIONS_EVENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    private void playAngerSound() {
        this.playSound(BGSoundEvents.NECTAUR_BELLOW_ADDITIONS_EVENT, this.getSoundVolume() * 2.0F, this.getVoicePitch() * 1.8F);
    }

    private void maybePlayFirstAngerSound() {
        if (this.playFirstAngerSoundIn > 0) {
            --this.playFirstAngerSoundIn;
            if (this.playFirstAngerSoundIn == 0) {
                this.playAngerSound();
            }
        }

    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(100) + 100;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {


        if (target.getRandom().nextInt(3) == 1) {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.SLOWNESS));
            this.lookAt(this, 100, 100);
            this.yBodyRot = yBodyRotO;
            NectaurPetalEntity glass = new NectaurPetalEntity(this.level(), this, itemstack1);
            double xDistance = target.getX() - this.getX();
            double yDistance = target.getY(0.3333333333333333D) - glass.getY();
            double zDistance = target.getZ() - this.getZ();
            double yMath = Math.sqrt((float) ((xDistance * xDistance) + (zDistance * zDistance)));
            glass.shoot(xDistance, yDistance + yMath * 0.10000000298023224D, zDistance, 1.6F, 11.0F);
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(glass);
        }
        if (target.getRandom().nextInt(10) == 1) {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_SLOWNESS));
            this.lookAt(this, 100, 100);
            this.yBodyRot = yBodyRotO;
            NectaurPetalEntity glass = new NectaurPetalEntity(this.level(), this, itemstack1);
            double xDistance = target.getX() - this.getX();
            double yDistance = target.getY(0.3333333333333333D) - glass.getY();
            double zDistance = target.getZ() - this.getZ();
            double yMath = Math.sqrt((float) ((xDistance * xDistance) + (zDistance * zDistance)));
            glass.shoot(xDistance, yDistance + yMath * 0.10000000298023224D, zDistance, 1.6F, 11.0F);
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(glass);
        }
        if (target.getRandom().nextInt(25) == 1) {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_POISON));
            this.lookAt(this, 100, 100);
            this.yBodyRot = yBodyRotO;
            NectaurPetalEntity glass = new NectaurPetalEntity(this.level(), this, itemstack1);
            double xDistance = target.getX() - this.getX();
            double yDistance = target.getY(0.3333333333333333D) - glass.getY();
            double zDistance = target.getZ() - this.getZ();
            double yMath = Math.sqrt((float) ((xDistance * xDistance) + (zDistance * zDistance)));
            glass.shoot(xDistance, yDistance + yMath * 0.10000000298023224D, zDistance, 1.6F, 11.0F);
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(glass);
        } else {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
            this.lookAt(this, 100, 100);
            this.yBodyRot = yBodyRotO;
            NectaurPetalEntity glass = new NectaurPetalEntity(this.level(), this, itemstack1);
            double xDistance = target.getX() - this.getX();
            double yDistance = target.getY(0.3333333333333333D) - glass.getY();
            double zDistance = target.getZ() - this.getZ();
            double yMath = Math.sqrt((float) ((xDistance * xDistance) + (zDistance * zDistance)));
            glass.shoot(xDistance, yDistance + yMath * 0.10000000298023224D, zDistance, 1.6F, 11.0F);
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(glass);
        }

    }
}