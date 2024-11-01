package com.jamiedev.mod.entities;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.jamiedev.mod.datagen.JamiesModTags;
import com.jamiedev.mod.entities.ai.GlarePathHolder;
import com.jamiedev.mod.init.JamiesModBlocks;
import com.jamiedev.mod.init.JamiesModEntityTypes;
import com.jamiedev.mod.entities.ai.GlareBrain;
import com.jamiedev.mod.init.JamiesModTag;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.damage.DamageSource;

public class GlareEntity extends AnimalEntity implements Flutterer
{

    protected static final ImmutableList<SensorType<? extends Sensor<? super GlareEntity>>> SENSORS;
    protected static final ImmutableList MEMORY_MODULES;

    /*
    Glare will be pretty different, has different sizes that the smaller ones gather to.
    rn its basically like a zombie glare lmao
     */
    private static final TrackedData<Integer> GLARE_SIZE;

    public static final int MAX_SIZE = 127;
    private Vec2f targetEyesPositionOffset;

    public GlarePathHolder glarePathHolder = new GlarePathHolder();

    public GlareEntity(EntityType<? extends GlareEntity> entityType, World world) {
        super(JamiesModEntityTypes.GLARE, world);
        this.moveControl = new GlareMoveControl(this, 20, true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
        this.setCanPickUpLoot(true);

        this.targetEyesPositionOffset = new Vec2f(0.0F, 0.0F);
    }

    @Override
    protected Brain.Profile<GlareEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }
    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return GlareBrain.create(dynamic);
    }
    @Override
    @SuppressWarnings("all")
    public Brain<GlareEntity> getBrain() {
        return (Brain<GlareEntity>) super.getBrain();
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(GLARE_SIZE, 1);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (GLARE_SIZE.equals(data)) {
            this.setYaw(this.headYaw);
            this.bodyYaw = this.headYaw;
            this.onSizeChanged();

        }

        super.onTrackedDataSet(data);
    }

    protected void initGoals() {

        this.goalSelector.add(1, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(2, new TemptGoal(this, 1.2, (stack) -> {
            return stack.isOf(Items.DIRT);
        }, false));
        this.goalSelector.add(3, new FollowParentGoal(this, 1.1));

    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (!this.getWorld().isClient() && this.isAlive() && this.age % 10 == 0) {
            this.heal(1.0F);
        }
    }

    @VisibleForTesting
    public void setSize(int size) {
        this.dataTracker.set(GLARE_SIZE, MathHelper.clamp(size, 0, 2));
    }

    public int getSize() {
        return (Integer)this.dataTracker.get(GLARE_SIZE);
    }

    private void onSizeChanged() {
        this.calculateDimensions();
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue((double)(6 + this.getSize()));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Size", this.getSize());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setSize(nbt.getInt("Size") + 1);
        super.readCustomDataFromNbt(nbt);
    }

    public EntityDimensions getBaseDimensions(EntityPose pose) {
        int i = this.getSize();
        EntityDimensions entityDimensions = super.getBaseDimensions(pose);
        return entityDimensions.scaled(1.0F + 0.15F * (float)i);
    }


    public Vec2f getTargetEyesPositionOffset() {
        return this.targetEyesPositionOffset;
    }

    public void setTargetEyesPositionOffset(float xEyePositionOffset, float yEyePositionOffset) {
        this.targetEyesPositionOffset = new Vec2f(xEyePositionOffset, yEyePositionOffset);
    }

    private void updateTargetEyesPositionOffset() {
        if (this.getRandom().nextBetween(0, 2) != 0) {
            return;
        }

        this.setTargetEyesPositionOffset(
                -0.5F + this.getRandom().nextFloat(),
                -0.4F + this.getRandom().nextFloat() * (0.4F - -0.4F)
        );
    }

    public static Builder createGlareAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.10000000149011612)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.10000000149011612)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateTargetEyesPositionOffset();
    }

    @Override
    protected void mobTick() {
        this.getWorld().getProfiler().push("glareBrain");
        this.getBrain().tick((ServerWorld) this.getWorld(), this);
        this.getWorld().getProfiler().pop();
        this.getWorld().getProfiler().push("glareActivityUpdate");
        GlareBrain.updateActivities(this);
        this.getWorld().getProfiler().pop();

        super.mobTick();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement()) {
            if (this.isTouchingWater()) {
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.800000011920929));
            } else if (this.isInLava()) {
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.5));
            } else {
                this.updateVelocity(this.getMovementSpeed(), movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.9100000262260437));
            }
        }

        this.updateLimbs(false);
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_AZALEA_LEAVES_BREAK;
    }

    @Override
    public void stopMovement() {
        this.getBrain().forget(MemoryModuleType.AVOID_TARGET);
        this.getBrain().forget(MemoryModuleType.WALK_TARGET);
        this.getBrain().forget(MemoryModuleType.LOOK_TARGET);

        this.getNavigation().setSpeed(0);
        this.getNavigation().stop();
        this.getMoveControl().moveTo(this.getX(), this.getY(), this.getZ(), 0);
        this.getMoveControl().tick();
        this.getLookControl().lookAt(this.getLookControl().getLookX(), this.getLookControl().getLookY(), this.getLookControl().getLookZ());
        this.getLookControl().lookAt(Vec3d.ZERO);
        this.getLookControl().tick();

        this.setJumping(false);
        this.setMovementSpeed(0.0F);
        this.prevHorizontalSpeed = 0.0F;
        this.horizontalSpeed = 0.0F;
        this.sidewaysSpeed = 0.0F;
        this.upwardSpeed = 0.0F;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.DIRT);
    }

    PigEntity ref;

    @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (GlareEntity)JamiesModEntityTypes.GLARE.create(world);
    }

    public boolean isInAir() {
        return !this.isOnGround();
    }

    protected void swimUpward(TagKey<Fluid> tagKey) {
        this.setVelocity(this.getVelocity().add(0.0, 0.01, 0.0));
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, this.getStandingEyeHeight() * 0.6D, 0.0D);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world)
        {
            public boolean isValidPosition(BlockPos pos) {
                boolean isValidPos = !this.world.getBlockState(pos.down()).isAir() && !this.world.getBlockState(pos.down()).isLiquid();

                return isValidPos;
            }
        };

        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);
        birdNavigation.setCanEnterOpenDoors(true);

        return birdNavigation;
    }

    final class GlareMoveControl extends FlightMoveControl
    {
        public GlareMoveControl(GlareEntity glare, int maxPitchChange, boolean noGravity) {
            super(glare, maxPitchChange, noGravity);
        }

        @Override
        public void tick() {
            if (GlareEntity.this.isAttacking()) {
                return;
            }

            super.tick();
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random random = world.getRandom();
        int i = random.nextInt(8);
        if (i < 2 && random.nextFloat() < 0.5F * difficulty.getClampedLocalDifficulty()) {
            ++i;
        }

        int j = 1 << i;
        this.setSize(j);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        boolean bl = SpawnReason.isTrialSpawner(spawnReason) || isLightLevelValidForNaturalSpawn(serverWorldAccess, blockPos);
        return serverWorldAccess.getBlockState(blockPos.down()).isOf(Blocks.MOSS_BLOCK) || serverWorldAccess.getBlockState(blockPos.down()).isOf(JamiesModBlocks.MOSSY_CLAYSTONE) && bl;
    }

    protected static boolean isLightLevelValidForNaturalSpawn(BlockRenderView world, BlockPos pos) {
        return world.getBaseLightLevel(pos, 0) > 1;
    }

    public static boolean canSpawn(EntityType<GlareEntity> glareEntityEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
       return serverWorldAccess.getBlockState(blockPos.down()).isOf(Blocks.MOSS_BLOCK) || serverWorldAccess.getBlockState(blockPos.down()).isOf(JamiesModBlocks.MOSSY_CLAYSTONE);
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return true;
    }


    static {
        GLARE_SIZE = DataTracker.registerData(GlareEntity.class, TrackedDataHandlerRegistry.INTEGER);
        SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS);
        MEMORY_MODULES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.IS_PANICKING, new MemoryModuleType[0]);
    }

    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }
}
