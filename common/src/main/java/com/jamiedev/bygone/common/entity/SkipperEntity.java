package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.ai.navigation.SkipperNavigation;
import com.jamiedev.bygone.core.registry.BGBiomes;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SkipperEntity extends PathfinderMob implements Saddleable, ItemSteerable {
    private static final int STROKE_PERIOD_TICKS = 20;
    private static final int POWER_STROKE_TICKS = 5;
    private static final double MAX_HEALTH = 10.0;
    private static final double MOVEMENT_SPEED = 0.3;
    private static final double RIVER_NOISE_LIMIT = 0.055;
    private static final float RIDDEN_SPEED_FACTOR = 0.8F;
    private static final float LAND_SPEED_FACTOR = 0.15F;
    private static final float PADDLE_FORCE = 0.9F;
    private static final float PADDLE_ANIMATION_BLEND_SPEED = 0.2F;
    private static final float BLOCKED_PADDLE_ANIMATION_AMOUNT = 0.2F;
    private static final float BLOCKED_WALK_ANIMATION_DISTANCE = 0.02F;
    private static final float SADDLE_BOB_HEIGHT = 0.025F;
    private static final double GLIDE_DRAG = 0.88;
    private static final double VERTICAL_DRAG = 0.98;
    private static final double SUBMERGED_DRAG = 0.8;
    private static final double SUBMERGED_VERTICAL_DRAG = 0.5;
    private static final double RESURFACING_FORCE = 0.08;
    private static final int WANDER_ATTEMPTS = 12;
    private static final int WANDER_RADIUS = 8;
    private static final int MAX_SPAWN_GROUP_SIZE = 3;
    private static final int LOWER_RIVER_MIN_Y = 60;
    private static final int UPPER_RIVER_MIN_Y = 120;
    private static final int RIVER_HEIGHT = 6;
    private static final double SURFACE_TOLERANCE = 1.0 / 1024.0;
    private static final double RIDDEN_COLLISION_LOOKAHEAD = 1.0 / 16.0;
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(SkipperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BOOST_TIME = SynchedEntityData.defineId(SkipperEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> STROKE_START_TICK = SynchedEntityData.defineId(SkipperEntity.class, EntityDataSerializers.LONG);
    private static final ResourceKey<NormalNoise.NoiseParameters> LOWER_RIVER = ResourceKey.create(Registries.NOISE, Bygone.id("rivers/lower"));
    private static final ResourceKey<NormalNoise.NoiseParameters> UPPER_RIVER = ResourceKey.create(Registries.NOISE, Bygone.id("rivers/upper"));
    public static final SpawnPlacementType ON_WATER_SURFACE = new SpawnPlacementType() {
        @Override
        public boolean isSpawnPositionOk(LevelReader levelReader, BlockPos blockPos, @Nullable EntityType<?> entityType) {
            return entityType != null && levelReader.getWorldBorder().isWithinBounds(blockPos) && isWaterSurface(levelReader, blockPos);
        }

        @Override
        public BlockPos adjustSpawnPosition(LevelReader levelReader, BlockPos blockPos) {
            BlockPos.MutableBlockPos candidateBlockPos = new BlockPos.MutableBlockPos(blockPos.getX(),
                Math.min(blockPos.getY() + 1, UPPER_RIVER_MIN_Y + RIVER_HEIGHT), blockPos.getZ());
            while (candidateBlockPos.getY() > LOWER_RIVER_MIN_Y) {
                int spawnY = candidateBlockPos.getY();
                if ((spawnY > UPPER_RIVER_MIN_Y || spawnY <= LOWER_RIVER_MIN_Y + RIVER_HEIGHT)
                    && isWaterSurface(levelReader, candidateBlockPos)) {
                    return candidateBlockPos.immutable();
                }
                candidateBlockPos.move(Direction.DOWN);
            }
            return blockPos;
        }
    };
    private final ItemBasedSteering itemBasedSteering = new ItemBasedSteering(this.entityData, BOOST_TIME, SADDLED);
    private long paddleAnimationStartTick = -1L;
    private float paddleAnimationAmount;
    private float previousPaddleAnimationAmount;

    public SkipperEntity(EntityType<? extends SkipperEntity> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH).add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED);
    }

    public static boolean isWaterSurface(LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos).isAir()
            && levelReader.getBlockState(blockPos.above()).isAir()
            && levelReader.getBlockState(blockPos.below()).is(Blocks.WATER)
            && levelReader.getFluidState(blockPos.below()).isSource();
    }

    public static boolean canSpawn(EntityType<?> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (!isWaterSurface(serverLevelAccessor, blockPos) || !serverLevelAccessor.getLevel().dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) {
            return false;
        }
        if (serverLevelAccessor.getBiome(blockPos).is(BGBiomes.PRIMORDIAL_OCEAN)
            || serverLevelAccessor.getBiome(blockPos).is(BGBiomes.PRIMORDIAL_BEACH)) {
            return false;
        }
        int waterY = blockPos.getY() - 1;
        ResourceKey<NormalNoise.NoiseParameters> riverNoiseKey;

        if (waterY >= LOWER_RIVER_MIN_Y && waterY < LOWER_RIVER_MIN_Y + RIVER_HEIGHT) {
            riverNoiseKey = LOWER_RIVER;
        } else if (waterY >= UPPER_RIVER_MIN_Y && waterY < UPPER_RIVER_MIN_Y + RIVER_HEIGHT) {
            riverNoiseKey = UPPER_RIVER;
        } else {
            return false;
        }
        double riverNoiseValue = serverLevelAccessor.getLevel().getChunkSource().randomState().getOrCreateNoise(riverNoiseKey).getValue(blockPos.getX(), 0.0, blockPos.getZ());
        if (Math.abs(riverNoiseValue) > RIVER_NOISE_LIMIT) {
            return false;
        }
        if (spawnType == MobSpawnType.CHUNK_GENERATION && serverLevelAccessor.getChunk(blockPos) instanceof ProtoChunk protoChunk) {
            String entityId = EntityType.getKey(entityType).toString();
            long skipperCount = protoChunk.getEntities().stream().filter(entityTag -> entityId.equals(entityTag.getString("id")))
                .limit(MAX_SPAWN_GROUP_SIZE).count();
            if (skipperCount >= MAX_SPAWN_GROUP_SIZE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return MAX_SPAWN_GROUP_SIZE;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.isUnobstructed(this) && levelReader.noCollision(this) && !levelReader.containsAnyLiquid(this.getBoundingBox());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SkipperNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.0, itemStack -> itemStack.is(BGItems.AMOEBA_GEL_ON_A_STICK.get()), false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0, 60) {
            @Override
            protected Vec3 getPosition() {
                if (!SkipperEntity.this.isOverWater()) {
                    return super.getPosition();
                }
                for (int attempt = 0; attempt < WANDER_ATTEMPTS; attempt++) {
                    BlockPos targetBlockPos = SkipperEntity.this.blockPosition().offset(
                        random.nextInt(WANDER_RADIUS * 2 + 1) - WANDER_RADIUS, 0,
                        random.nextInt(WANDER_RADIUS * 2 + 1) - WANDER_RADIUS);

                    if (isWaterSurface(level(), targetBlockPos)) {
                        return Vec3.atBottomCenterOf(targetBlockPos);
                    }
                    if (isWaterSurface(level(), targetBlockPos.above())) {
                        return Vec3.atBottomCenterOf(targetBlockPos.above());
                    }
                }
                return null;
            }
        });
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityDataBuilder) {
        super.defineSynchedData(entityDataBuilder);
        entityDataBuilder.define(SADDLED, false);
        entityDataBuilder.define(BOOST_TIME, 0);
        entityDataBuilder.define(STROKE_START_TICK, -1L);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        if (BOOST_TIME.equals(entityDataAccessor) && this.level().isClientSide) {
            this.itemBasedSteering.onSynced();
        }
        super.onSyncedDataUpdated(entityDataAccessor);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        this.itemBasedSteering.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.itemBasedSteering.readAdditionalSaveData(nbt);
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive();
    }

    @Override
    public boolean isSaddled() {
        return this.itemBasedSteering.hasSaddle();
    }

    @Override
    public void equipSaddle(ItemStack itemStack, @Nullable SoundSource soundSource) {
        this.itemBasedSteering.setSaddle(true);
        this.setPersistenceRequired();
        if (soundSource != null) {
            this.level().playSound(null, this, SoundEvents.PIG_SADDLE, soundSource, 0.5F, 1.0F);
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        if (this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        InteractionResult result = super.mobInteract(player, interactionHand);
        ItemStack itemStack = player.getItemInHand(interactionHand);
        return !result.consumesAction() && itemStack.is(Items.SADDLE) ? itemStack.interactLivingEntity(player, this, interactionHand) : result;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.isSaddled() && this.getFirstPassenger() instanceof Player player
            && player.isHolding(BGItems.AMOEBA_GEL_ON_A_STICK.get()) ? player : null;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions entityDimensions, float scale) {
        return super.getPassengerAttachmentPoint(passenger, entityDimensions, scale)
            .add(0.0, this.getSaddleBobOffset(1.0F) * scale, 0.0);
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        this.setRot(player.getYRot(), 0.0F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.getNavigation().stop();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * RIDDEN_SPEED_FACTOR * this.itemBasedSteering.boostFactor();
    }

    @Override
    public boolean boost() {
        return this.getControllingPassenger() != null && this.itemBasedSteering.boost(this.random);
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return fluidState.is(FluidTags.WATER);
    }

    public boolean isOverWater() {
        BlockPos blockPos = this.blockPosition();
        return this.level().getFluidState(blockPos).is(FluidTags.WATER)
            || this.level().getFluidState(blockPos.below()).is(FluidTags.WATER);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            boolean moving = this.isOverWater() && !this.isNoAi()
                && (this.getControllingPassenger() != null || !this.getNavigation().isDone());
            if (moving && this.entityData.get(STROKE_START_TICK) < 0L) {
                this.entityData.set(STROKE_START_TICK, this.level().getGameTime());
            } else if (!moving) {
                this.entityData.set(STROKE_START_TICK, -1L);
            }
        }
        long strokeStartTick = this.entityData.get(STROKE_START_TICK);
        if (strokeStartTick >= 0L) {
            this.paddleAnimationStartTick = strokeStartTick;
        }
        this.previousPaddleAnimationAmount = this.paddleAnimationAmount;
        float targetPaddleAnimationAmount = strokeStartTick >= 0L && this.isOverWater()
            ? (this.isRiddenMovementBlocked() ? BLOCKED_PADDLE_ANIMATION_AMOUNT : 1.0F) : 0.0F;
        this.paddleAnimationAmount = Mth.approach(this.paddleAnimationAmount,
            targetPaddleAnimationAmount, PADDLE_ANIMATION_BLEND_SPEED);
        this.itemBasedSteering.tickBoost();
        super.tick();
        if (this.isInWater()) {
            double surfaceY = this.getWaterSurfaceY();
            if (!Double.isNaN(surfaceY) && this.getY() >= surfaceY - SURFACE_TOLERANCE) {
                this.setOnGround(true);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().multiply(SUBMERGED_DRAG, SUBMERGED_VERTICAL_DRAG, SUBMERGED_DRAG).add(0.0, RESURFACING_FORCE, 0.0));
            }
            this.resetFallDistance();
        }
    }

    public float getPaddleAnimationProgress(float partialTick) {
        if (this.paddleAnimationStartTick < 0L) {
            return 0.0F;
        }
        long elapsedTicks = Math.max(0L, this.level().getGameTime() - this.paddleAnimationStartTick);
        return (elapsedTicks % STROKE_PERIOD_TICKS + partialTick) / STROKE_PERIOD_TICKS;
    }

    public float getPaddleAnimationAmount(float partialTick) {
        return Mth.lerp(partialTick, this.previousPaddleAnimationAmount, this.paddleAnimationAmount);
    }

    public float getSaddleBobOffset(float partialTick) {
        return Mth.sin(this.getPaddleAnimationProgress(partialTick) * Mth.TWO_PI)
            * SADDLE_BOB_HEIGHT * this.getPaddleAnimationAmount(partialTick);
    }

    @Override
    public void travel(Vec3 travelVector) {
        int strokeTick = this.getStrokeTick();
        boolean paddling = strokeTick >= 0 && strokeTick < POWER_STROKE_TICKS;
        if (this.isOverWater() && this.isControlledByLocalInstance()) {
            if (paddling) {
                this.moveRelative(this.getSpeed() * PADDLE_FORCE, new Vec3(travelVector.x, 0.0, travelVector.z));
            }
            double surfaceY = this.getWaterSurfaceY();
            Vec3 deltaMovement = this.getDeltaMovement();
            boolean supported = !Double.isNaN(surfaceY) && deltaMovement.y <= 0.0
                && this.getY() >= surfaceY - SURFACE_TOLERANCE && this.getY() + deltaMovement.y <= surfaceY;
            if (supported) {
                deltaMovement = new Vec3(deltaMovement.x, surfaceY - this.getY(), deltaMovement.z);
            }
            this.setDeltaMovement(deltaMovement);
            this.move(MoverType.SELF, deltaMovement);
            this.setOnGround(supported || this.onGround());
            if (supported) {
                this.resetFallDistance();
            }
            Vec3 remainingMovement = this.getDeltaMovement();
            this.setDeltaMovement(remainingMovement.x * GLIDE_DRAG,
                supported ? 0.0 : remainingMovement.y * VERTICAL_DRAG - this.getGravity(), remainingMovement.z * GLIDE_DRAG);
            this.calculateEntityAnimation(false);
        } else {
            super.travel(new Vec3(travelVector.x * LAND_SPEED_FACTOR, travelVector.y, travelVector.z * LAND_SPEED_FACTOR));
        }
    }

    @Override
    protected void updateWalkAnimation(float horizontalDistance) {
        super.updateWalkAnimation(this.isRiddenMovementBlocked()
            ? Math.max(horizontalDistance, BLOCKED_WALK_ANIMATION_DISTANCE) : horizontalDistance);
    }

    private boolean isRiddenMovementBlocked() {
        if (this.getControllingPassenger() == null) {
            return false;
        }
        float yawRadians = this.getYRot() * Mth.DEG_TO_RAD;
        Vec3 forwardMovement = new Vec3(-Mth.sin(yawRadians) * RIDDEN_COLLISION_LOOKAHEAD, 0.0,
            Mth.cos(yawRadians) * RIDDEN_COLLISION_LOOKAHEAD);
        return !this.level().noCollision(this, this.getBoundingBox().move(forwardMovement));
    }

    private int getStrokeTick() {
        long strokeStartTick = this.entityData.get(STROKE_START_TICK);
        if (!this.isOverWater() || strokeStartTick < 0L) {
            return -1;
        }
        return (int) (Math.max(0L, this.level().getGameTime() - strokeStartTick) % STROKE_PERIOD_TICKS);
    }

    private double getWaterSurfaceY() {
        BlockPos blockPos = this.blockPosition();
        if (!this.level().getFluidState(blockPos).is(FluidTags.WATER)) {
            blockPos = blockPos.below();
        }
        FluidState fluidState = this.level().getFluidState(blockPos);
        if (!fluidState.is(FluidTags.WATER) || !fluidState.isSource()
            || this.level().getFluidState(blockPos.above()).is(FluidTags.WATER)) {
            return Double.NaN;
        }
        return blockPos.getY() + fluidState.getHeight(this.level(), blockPos);
    }
}
