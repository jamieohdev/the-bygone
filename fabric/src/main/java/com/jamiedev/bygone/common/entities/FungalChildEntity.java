package com.jamiedev.bygone.common.entities;

import com.jamiedev.bygone.fabric.init.JamiesModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class FungalChildEntity extends FungalParentEntity
{

    EnderMan ref;

    private static final EntityDataAccessor<Boolean> WARNING;
    private static final float field_30352 = 6.0F;
    private float lastWarningAnimationProgress;
    private float warningAnimationProgress;
    private int warningSoundCooldown;
    private static final UniformInt ANGER_TIME_RANGE;
    private int angerTime;
    @Nullable
    private UUID angryAt;

    public FungalChildEntity(EntityType<? extends FungalChildEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return (AgeableMob)JamiesModEntityTypes.FUNGAL_PARENT.create(world);
    }

    public boolean isFood(ItemStack stack) {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FungalChildEntity.AttackGoal());
        this.goalSelector.addGoal(1, new PanicGoal(this, 4.0, (polarBear) -> {
            return polarBear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES;
        }));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 63.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new FungalChildEntity.FungalParentRevengeGoal());
        this.targetSelector.addGoal(2, new FungalChildEntity.ProtectBabiesGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Fox.class, 10, true, true, (Predicate)null));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static AttributeSupplier.Builder createFungieAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    public static boolean canSpawn(EntityType<FungalParentEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        Holder<Biome> registryEntry = world.getBiome(pos);
        if (!registryEntry.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
            return checkAnimalSpawnRules(type, world, spawnReason, pos, random);
        } else {
            return isBrightEnoughToSpawn(world, pos) && world.getBlockState(pos.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
        }
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.readPersistentAngerSaveData(this.level(), nbt);
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        this.addPersistentAngerSaveData(nbt);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    public void setRemainingPersistentAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? SoundEvents.POLAR_BEAR_AMBIENT_BABY : SoundEvents.POLAR_BEAR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundCooldown <= 0) {
            this.makeSound(SoundEvents.POLAR_BEAR_WARNING);
            this.warningSoundCooldown = 40;
        }

    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WARNING, false);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.warningAnimationProgress != this.lastWarningAnimationProgress) {
                this.refreshDimensions();
            }

            this.lastWarningAnimationProgress = this.warningAnimationProgress;
            if (this.isWarning()) {
                this.warningAnimationProgress = Mth.clamp(this.warningAnimationProgress + 1.0F, 0.0F, 6.0F);
            } else {
                this.warningAnimationProgress = Mth.clamp(this.warningAnimationProgress - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundCooldown > 0) {
            --this.warningSoundCooldown;
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }

    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.warningAnimationProgress > 0.0F) {
            float f = this.warningAnimationProgress / 6.0F;
            float g = 1.0F + f;
            return super.getDefaultDimensions(pose).scale(1.0F, g);
        } else {
            return super.getDefaultDimensions(pose);
        }
    }

    public boolean isWarning() {
        return (Boolean)this.entityData.get(WARNING);
    }

    public void setWarning(boolean warning) {
        this.entityData.set(WARNING, warning);
    }

    public float getWarningAnimationProgress(float tickDelta) {
        return Mth.lerp(tickDelta, this.lastWarningAnimationProgress, this.warningAnimationProgress) / 6.0F;
    }

    protected float getWaterSlowDown() {
        return 0.98F;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        if (entityData == null) {
            entityData = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(world, difficulty, spawnReason, (SpawnGroupData)entityData);
    }

    static {
        WARNING = SynchedEntityData.defineId(FungalChildEntity.class, EntityDataSerializers.BOOLEAN);
        ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    }

    private class AttackGoal extends MeleeAttackGoal {
        public AttackGoal() {
            super(FungalChildEntity.this, 1.25, true);
        }

        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                FungalChildEntity.this.setWarning(false);
            } else if (this.mob.distanceToSqr(target) < (double)((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
                if (this.isTimeToAttack()) {
                    FungalChildEntity.this.setWarning(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    FungalChildEntity.this.setWarning(true);
                    FungalChildEntity.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                FungalChildEntity.this.setWarning(false);
            }

        }

        public void stop() {
            FungalChildEntity.this.setWarning(false);
            super.stop();
        }
    }

    class FungalParentRevengeGoal extends HurtByTargetGoal {
        public FungalParentRevengeGoal() {
            super(FungalChildEntity.this, new Class[0]);
        }

        public void start() {
            super.start();
            if (FungalChildEntity.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }

        }

        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof FungalChildEntity && !mob.isBaby()) {
                super.alertOther(mob, target);
            }

        }
    }

    class ProtectBabiesGoal extends NearestAttackableTargetGoal<Player> {
        public ProtectBabiesGoal() {
            super(FungalChildEntity.this, Player.class, 20, true, true, (Predicate)null);
        }

        public boolean canUse() {
            if (FungalChildEntity.this.isBaby()) {
                return false;
            } else {
                if (super.canUse()) {
                    List<FungalChildEntity> list =
                            FungalChildEntity.this.level().getEntitiesOfClass(FungalChildEntity.class,
                                    FungalChildEntity.this.getBoundingBox().inflate(8.0, 4.0, 8.0));

                    Iterator var2 = list.iterator();

                    while(var2.hasNext()) {
                        FungalChildEntity polarBearEntity = (FungalChildEntity)var2.next();


                        if (polarBearEntity.isBaby()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5;
        }
    }
}
