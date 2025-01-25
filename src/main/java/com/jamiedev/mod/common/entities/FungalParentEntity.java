package com.jamiedev.mod.common.entities;

import com.jamiedev.mod.common.entities.ai.FollowFungalParentGoal;
import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import com.jamiedev.mod.fabric.init.JamiesModSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class FungalParentEntity  extends AnimalEntity implements Angerable {

    EndermanEntity ref;

    private static final TrackedData<Boolean> WARNING;
    private static final float field_30352 = 6.0F;
    private float lastWarningAnimationProgress;
    private float warningAnimationProgress;
    private int warningSoundCooldown;
    private static final UniformIntProvider ANGER_TIME_RANGE;
    private int angerTime;
    @Nullable
    private UUID angryAt;

    public FungalParentEntity(EntityType<? extends FungalParentEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (PassiveEntity)JamiesModEntityTypes.FUNGAL_PARENT.create(world);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FungalParentEntity.AttackGoal());
        this.goalSelector.add(1, new EscapeDangerGoal(this, 4.0, (polarBear) -> {
            return polarBear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES;
        }));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.add(5, new WanderAroundGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 63.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new FungalParentEntity.FungalParentRevengeGoal());
        this.targetSelector.add(2, new FungalParentEntity.ProtectBabiesGoal());
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, GlareEntity.class, 10, true, true, (Predicate)null));
        this.targetSelector.add(5, new UniversalAngerGoal<>(this, true));
    }

    public static DefaultAttributeContainer.Builder createFungieAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    public static boolean canSpawn(EntityType<FungalParentEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        RegistryEntry<Biome> registryEntry = world.getBiome(pos);
        if (!registryEntry.isIn(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
            return isValidNaturalSpawn(type, world, spawnReason, pos, random);
        } else {
            return isLightLevelValidForNaturalSpawn(world, pos) && world.getBlockState(pos.down()).isIn(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.getWorld(), nbt);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
    }

    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    public int getAngerTime() {
        return this.angerTime;
    }

    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? JamiesModSoundEvents.FUNGUSPARENT_AMBIENT_BABY_ADDITIONS_EVENT : JamiesModSoundEvents.FUNGUSPARENT_AMBIENT_ADDITIONS_EVENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return JamiesModSoundEvents.FUNGUSPARENT_HURT_ADDITIONS_EVENT;
    }

    protected SoundEvent getDeathSound() {
        return JamiesModSoundEvents.FUNGUSPARENT_DEATH_ADDITIONS_EVENT;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundCooldown <= 0) {
            this.playSound(SoundEvents.BLOCK_CAMPFIRE_CRACKLE);
            this.warningSoundCooldown = 40;
        }

    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(WARNING, false);
    }

    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.warningAnimationProgress != this.lastWarningAnimationProgress) {
                this.calculateDimensions();
            }

            this.lastWarningAnimationProgress = this.warningAnimationProgress;
            if (this.isWarning()) {
                this.warningAnimationProgress = MathHelper.clamp(this.warningAnimationProgress + 1.0F, 0.0F, 6.0F);
            } else {
                this.warningAnimationProgress = MathHelper.clamp(this.warningAnimationProgress - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundCooldown > 0) {
            --this.warningSoundCooldown;
        }

        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld)this.getWorld(), true);
        }

    }

    public EntityDimensions getBaseDimensions(EntityPose pose) {
        if (this.warningAnimationProgress > 0.0F) {
            float f = this.warningAnimationProgress / 6.0F;
            float g = 1.0F + f;
            return super.getBaseDimensions(pose).scaled(1.0F, g);
        } else {
            return super.getBaseDimensions(pose);
        }
    }

    public boolean isWarning() {
        return (Boolean)this.dataTracker.get(WARNING);
    }

    public void setWarning(boolean warning) {
        this.dataTracker.set(WARNING, warning);
    }

    public float getWarningAnimationProgress(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.lastWarningAnimationProgress, this.warningAnimationProgress) / 6.0F;
    }

    protected float getBaseMovementSpeedMultiplier() {
        return 0.98F;
    }

    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(1.0F);
        }

        return super.initialize(world, difficulty, spawnReason, (EntityData)entityData);
    }

    static {
        WARNING = DataTracker.registerData(FungalParentEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    }

    private class AttackGoal extends MeleeAttackGoal {
        public AttackGoal() {
            super(FungalParentEntity.this, 1.25, true);
        }

        protected void attack(LivingEntity target) {
            if (this.canAttack(target)) {
                this.resetCooldown();
                this.mob.tryAttack(target);
                FungalParentEntity.this.setWarning(false);
            } else if (this.mob.squaredDistanceTo(target) < (double)((target.getWidth() + 3.0F) * (target.getWidth() + 3.0F))) {
                if (this.isCooledDown()) {
                    FungalParentEntity.this.setWarning(false);
                    this.resetCooldown();
                }

                if (this.getCooldown() <= 10) {
                    FungalParentEntity.this.setWarning(true);
                    FungalParentEntity.this.playWarningSound();
                }
            } else {
                this.resetCooldown();
                FungalParentEntity.this.setWarning(false);
            }

        }

        public void stop() {
            FungalParentEntity.this.setWarning(false);
            super.stop();
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);
        if (bl && this.getMainHandStack().isEmpty() && target instanceof LivingEntity) {
            float f = this.getWorld().getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
            ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * (int)f), this);
        }

        return bl;
    }

    class FungalParentRevengeGoal extends RevengeGoal {
        public FungalParentRevengeGoal() {
            super(FungalParentEntity.this, new Class[0]);
        }

        public void start() {
            super.start();
            if (FungalParentEntity.this.isBaby()) {
                this.callSameTypeForRevenge();
                this.stop();
            }

        }

        protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
            if (mob instanceof FungalParentEntity && !mob.isBaby()) {
                super.setMobEntityTarget(mob, target);
            }

        }
    }

    class ProtectBabiesGoal extends ActiveTargetGoal<PlayerEntity> {
        public ProtectBabiesGoal() {
            super(FungalParentEntity.this, PlayerEntity.class, 20, true, true, (Predicate)null);
        }

        public boolean canStart() {
            if (FungalParentEntity.this.isBaby()) {
                return false;
            } else {
                if (super.canStart()) {
                    List<FungalParentEntity> list = 
                            FungalParentEntity.this.getWorld().getNonSpectatingEntities(FungalParentEntity.class, 
                                    FungalParentEntity.this.getBoundingBox().expand(8.0, 4.0, 8.0));

                    Iterator var2 = list.iterator();

                    while(var2.hasNext()) {
                        FungalParentEntity polarBearEntity = (FungalParentEntity)var2.next();


                        if (polarBearEntity.isBaby()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected double getFollowRange() {
            return super.getFollowRange() * 0.5;
        }
    }
}
