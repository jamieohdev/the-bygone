package com.jamiedev.bygone.common.entities;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.entities.ai.GlareBrain;
import com.jamiedev.bygone.common.entities.ai.GlarePathHolder;
import com.jamiedev.bygone.fabric.init.JamiesModBlocks;
import com.jamiedev.bygone.fabric.init.JamiesModEntityTypes;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GlareEntity extends Animal implements FlyingAnimal
{

    protected static final ImmutableList<SensorType<? extends Sensor<? super GlareEntity>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;

    /*
    Glare will be pretty different, has different sizes that the smaller ones gather to.
    rn its basically like a zombie glare lmao
     */
    private static final EntityDataAccessor<Integer> GLARE_SIZE;

    public static final int MAX_SIZE = 127;
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private Vec2 targetEyesPositionOffset;

    public GlarePathHolder glarePathHolder = new GlarePathHolder();

    public GlareEntity(EntityType<? extends GlareEntity> entityType, Level world) {
        super(JamiesModEntityTypes.GLARE, world);
        this.moveControl = new GlareMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.LAVA, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
        this.setCanPickUpLoot(true);

        this.targetEyesPositionOffset = new Vec2(0.0F, 0.0F);
    }

    @Override
    protected Brain.Provider<GlareEntity> brainProvider() {
        return Brain.provider(MEMORY_MODULES, SENSORS);
    }
    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return GlareBrain.create(dynamic);
    }
    @Override
    @SuppressWarnings("all")
    public Brain<GlareEntity> getBrain() {
        return (Brain<GlareEntity>) super.getBrain();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GLARE_SIZE, 1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (GLARE_SIZE.equals(data)) {
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;
            this.onSizeChanged();

        }

        super.onSyncedDataUpdated(data);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2, (stack) -> {
            return stack.is(Items.DIRT);
        }, false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1));

    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide() && this.isAlive() && this.tickCount % 10 == 0) {
            this.heal(1.0F);
        }
    }

    @VisibleForTesting
    public void setSize(int size) {
        this.entityData.set(GLARE_SIZE, Mth.clamp(size, 0, 2));
    }

    public int getSize() {
        return this.entityData.get(GLARE_SIZE);
    }

    private void onSizeChanged() {
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6 + this.getSize());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Size", this.getSize());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.setSize(nbt.getInt("Size") + 1);
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        int i = this.getSize();
        EntityDimensions entityDimensions = super.getDefaultDimensions(pose);
        return entityDimensions.scale(1.0F + 0.15F * (float)i);
    }


    public Vec2 getTargetEyesPositionOffset() {
        return this.targetEyesPositionOffset;
    }

    public void setTargetEyesPositionOffset(float xEyePositionOffset, float yEyePositionOffset) {
        this.targetEyesPositionOffset = new Vec2(xEyePositionOffset, yEyePositionOffset);
    }

    private void updateTargetEyesPositionOffset() {
        if (this.getRandom().nextIntBetweenInclusive(0, 2) != 0) {
            return;
        }

        this.setTargetEyesPositionOffset(
                -0.5F + this.getRandom().nextFloat(),
                -0.4F + this.getRandom().nextFloat() * (0.4F - -0.4F)
        );
    }

    public static Builder createGlareAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FLYING_SPEED, 0.10000000149011612)
                .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateTargetEyesPositionOffset();
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("glareBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("glareActivityUpdate");
        GlareBrain.updateActivities(this);
        this.level().getProfiler().pop();

        super.customServerAiStep();
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, movementInput);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, movementInput);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                this.moveRelative(this.getSpeed(), movementInput);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9100000262260437));
            }
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.AZALEA_LEAVES_BREAK;
    }

    @Override
    public void stopInPlace() {
        this.getBrain().eraseMemory(MemoryModuleType.AVOID_TARGET);
        this.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);

        this.getNavigation().setSpeedModifier(0);
        this.getNavigation().stop();
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0);
        this.getMoveControl().tick();
        this.getLookControl().setLookAt(this.getLookControl().getWantedX(), this.getLookControl().getWantedY(), this.getLookControl().getWantedZ());
        this.getLookControl().setLookAt(Vec3.ZERO);
        this.getLookControl().tick();

        this.setJumping(false);
        this.setSpeed(0.0F);
        this.walkDistO = 0.0F;
        this.walkDist = 0.0F;
        this.xxa = 0.0F;
        this.yya = 0.0F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.DIRT);
    }

    Pig ref;

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return JamiesModEntityTypes.GLARE.create(world);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKey) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, this.getEyeHeight() * 0.6D, 0.0D);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world)
        {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                boolean isValidPos = !this.level.getBlockState(pos.below()).isAir() && !this.level.getBlockState(pos.below()).liquid();

                return isValidPos;
            }
        };

        birdNavigation.setCanOpenDoors(false);
        birdNavigation.setCanFloat(false);
        birdNavigation.setCanPassDoors(true);

        return birdNavigation;
    }

    final class GlareMoveControl extends FlyingMoveControl
    {
        public GlareMoveControl(GlareEntity glare, int maxPitchChange, boolean noGravity) {
            super(glare, maxPitchChange, noGravity);
        }

        @Override
        public void tick() {
            if (GlareEntity.this.isAggressive()) {
                return;
            }

            super.tick();
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        RandomSource random = world.getRandom();
        int i = random.nextInt(8);
        if (i < 2 && random.nextFloat() < 0.5F * difficulty.getSpecialMultiplier()) {
            ++i;
        }

        int j = 1 << i;
        this.setSize(j);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random) {
        boolean bl = MobSpawnType.ignoresLightRequirements(spawnReason) || isBrightEnoughToSpawn(serverWorldAccess, blockPos);
        return serverWorldAccess.getBlockState(blockPos.below()).is(Blocks.MOSS_BLOCK) || serverWorldAccess.getBlockState(blockPos.below()).is(JamiesModBlocks.MOSSY_CLAYSTONE) && bl;
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter world, BlockPos pos) {
        return world.getRawBrightness(pos, 0) > 1;
    }

    public static boolean canSpawn(EntityType<GlareEntity> glareEntityEntityType, ServerLevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random) {
       return serverWorldAccess.getBlockState(blockPos.below()).is(Blocks.MOSS_BLOCK) || serverWorldAccess.getBlockState(blockPos.below()).is(JamiesModBlocks.MOSSY_CLAYSTONE);
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return true;
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(100) + 100;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

    }

    static {
        GLARE_SIZE = SynchedEntityData.defineId(GlareEntity.class, EntityDataSerializers.INT);
        SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS);
        MEMORY_MODULES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK_POSITION, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.IS_PANICKING);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }
}
