package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.data.CopperbugFamilyData;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class CopperbugEntity extends Animal implements NeutralMob {
    public static final int ATTACK_OPEN_TICKS = 10;
    public static final int ATTACK_CLOSE_TICKS = 2;
    public static final int ATTACK_RECOVERY_TICKS = 4;
    public static final int ATTACK_ANIMATION_TICKS = ATTACK_OPEN_TICKS + ATTACK_CLOSE_TICKS + ATTACK_RECOVERY_TICKS;
    public static final int OXIDATION_STAGE_TICKS = 24000;
    public static final int MAX_OXIDATION_STAGE = 3;
    public static final int FRIENDLY_TICKS = 6000;
    public static final int MAX_FAMILY_SIZE = 8;
    private static final ResourceLocation BABY_SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "copperbug_baby_speed");
    private static final int TERRITORY_RADIUS = 12;
    private static final int CLEANING_RADIUS = 6;
    private static final double INTRUDER_RANGE = 6.0;
    private static final double COPPER_INTRUDER_RANGE = 10.0;
    @Nullable
    private UUID familyId;
    @Nullable
    private BlockPos homePos;
    private static final EntityDataAccessor<Byte> COPPERBUG_FLAGS;

    private static final EntityDataAccessor<Boolean> WARNING;
    private static final EntityDataAccessor<Integer> PUFF_LEVEL;
    private static final EntityDataAccessor<Integer> CLEANING_MODE;
    private static final EntityDataAccessor<Integer> OXIDATION_STAGE;
    private static final float field_30352 = 6.0F;
    private static final UniformInt ANGER_TIME_RANGE;
    private static final int field_30274 = 200;
    private static final int field_30275 = 200;

    static {
        COPPERBUG_FLAGS = SynchedEntityData.defineId(CopperbugEntity.class, EntityDataSerializers.BYTE);
        WARNING = SynchedEntityData.defineId(CopperbugEntity.class, EntityDataSerializers.BOOLEAN);
        PUFF_LEVEL = SynchedEntityData.defineId(CopperbugEntity.class, EntityDataSerializers.INT);
        CLEANING_MODE = SynchedEntityData.defineId(CopperbugEntity.class, EntityDataSerializers.INT);
        OXIDATION_STAGE = SynchedEntityData.defineId(CopperbugEntity.class, EntityDataSerializers.INT);
        ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    }

    CopperbugEntity ref;
    int ticksSinceScraping;
    int ticksLeftToFindNest;
    int ticksUntilCanScraping;
    @Nullable
    BlockPos copperPos;
    @Nullable
    BlockPos nestPos;
    ScrapeGoal scrapeGoal;
    MoveToCopperGoal moveToCopperGoal;
    private float lastWarningAnimationProgress;
    private float warningAnimationProgress;
    private float lastPuffAnimationProgress;
    private float puffAnimationProgress;
    private float puffAnimationVelocity;
    private float lastCleaningAnimationProgress;
    private float cleaningAnimationProgress;
    private float lastRaisedCleaningAnimationProgress;
    private float raisedCleaningAnimationProgress;
    private int cleaningAnimationTicks;
    private int oxidationTicks;
    private int friendlyTicks;
    private boolean familyRegistered;
    @Nullable
    private BlockPos offeredCopperPos;
    @Nullable
    private ItemEntity offeredCopperItem;
    private final DynamicGameEventListener<CopperOfferingListener> copperOfferingListener;
    private boolean warningActive;
    private int warningSoundCooldown;
    private int attackAnimationTicks;
    private int angerTime;
    @Nullable
    private UUID angryAt;
    private int cannotEnterNestTicks;
    private int copperUpdatedSinceScraping;

    public CopperbugEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.ticksUntilCanScraping = Mth.nextInt(this.random, 20, 60);
        this.copperOfferingListener = new DynamicGameEventListener<>(new CopperOfferingListener());
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> listenerConsumer) {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (!this.isRemoved() && this.isAlive()) {
                this.registerFamilyMember();
            }
            listenerConsumer.accept(this.copperOfferingListener, serverLevel);
        }
    }

    public static AttributeSupplier.Builder createCopperbugAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }

        movementSpeed.removeModifier(BABY_SPEED_MODIFIER_ID);
        if (this.isBaby()) {
            movementSpeed.addTransientModifier(new AttributeModifier(BABY_SPEED_MODIFIER_ID, -0.33, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    public static boolean canSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, @NotNull RandomSource random) {
        return world.getBlockState(pos.below()).is(Blocks.WATER) || world.getBlockState(pos.below()).is(Blocks.MOSS_BLOCK) || world.getBlockState(pos.below()).is(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
    }

    private static void cleanOxidationAround(Level world, BlockPos pos, BlockPos.MutableBlockPos mutablePos, int count) {
        mutablePos.set(pos);

        for (int i = 0; i < count; ++i) {
            Optional<BlockPos> optional = cleanOxidationAround(world, mutablePos);
            if (optional.isEmpty()) {
                break;
            }

            mutablePos.set(optional.get());
        }

    }

    private static Optional<BlockPos> cleanOxidationAround(Level world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.randomInCube(world.random, 10, pos, 1).iterator();

        BlockPos blockPos;
        BlockState blockState;
        do {
            if (!var2.hasNext()) {
                return Optional.empty();
            }

            blockPos = var2.next();
            blockState = world.getBlockState(blockPos);
        } while (!(blockState.getBlock() instanceof WeatheringCopper));

        BlockPos finalBlockPos = blockPos;
        WeatheringCopper.getPrevious(blockState).ifPresent((state) -> {
            world.setBlockAndUpdate(finalBlockPos, state);
        });
        world.levelEvent(3002, blockPos, -1);
        return Optional.of(blockPos);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ReturnHomeGoal());
        this.goalSelector.addGoal(2, new CopperbugEntity.AttackGoal());
        this.goalSelector.addGoal(2, new CopperbugEntity.WarnIntruderGoal());
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0, (polarBear) -> {
            return polarBear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES;
        }));
        this.goalSelector.addGoal(3, new CopperbugEntity.FollowFamilyGoal());
        this.scrapeGoal = new ScrapeGoal(this);
        this.goalSelector.addGoal(4, this.scrapeGoal);
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        //this.goalSelector.addGoal(7, new OxidizeCopperGoal());
        this.targetSelector.addGoal(1, new CopperbugEntity.CopperbugRevengeGoal());
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevel, DifficultyInstance difficultyInstance,
                                       MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        CopperbugGroupData copperbugGroupData = spawnGroupData instanceof CopperbugGroupData familyGroup
                ? familyGroup : new CopperbugGroupData(UUID.randomUUID(), this.blockPosition());
        this.familyId = copperbugGroupData.familyId;
        this.homePos = copperbugGroupData.homePos;
        this.restrictTo(this.homePos, TERRITORY_RADIUS);
        this.registerFamilyMember();
        return super.finalizeSpawn(serverLevel, difficultyInstance, spawnType, copperbugGroupData);
    }

    public boolean isFamily(CopperbugEntity copperbugEntity) {
        return this.familyId != null && this.familyId.equals(copperbugEntity.familyId);
    }

    private void registerFamilyMember() {
        if (!this.familyRegistered && this.familyId != null && this.level() instanceof ServerLevel serverLevel
                && serverLevel.getEntity(this.getUUID()) == this) {
            CopperbugFamilyData.get(serverLevel).addMember(this.familyId, this.getUUID());
            this.familyRegistered = true;
        }
    }

    @Override
    public void remove(RemovalReason removalReason) {
        if (this.familyId != null && this.level() instanceof ServerLevel serverLevel) {
            if (removalReason.shouldDestroy()) {
                CopperbugFamilyData.get(serverLevel).removeMember(this.familyId, this.getUUID());
            } else {
                this.registerFamilyMember();
            }
        }
        super.remove(removalReason);
    }

    private boolean hasFamilySpace(ServerLevel serverLevel) {
        this.initializeTerritory();
        this.registerFamilyMember();
        return CopperbugFamilyData.get(serverLevel).getFamilySize(this.familyId) < MAX_FAMILY_SIZE;
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel serverLevel, Animal mateAnimal) {
        if (!(mateAnimal instanceof CopperbugEntity mateCopperbug) || !this.isFamily(mateCopperbug)
                || this.getAge() != 0 || mateCopperbug.getAge() != 0 || !this.hasFamilySpace(serverLevel)) {
            return;
        }

        super.spawnChildFromBreeding(serverLevel, mateAnimal);
    }

    private void initializeTerritory() {
        if (this.homePos != null && this.familyId != null) {
            return;
        }

        if (this.familyId == null && this.homePos == null) {
            CopperbugEntity familyCopperbug = this.level().getEntitiesOfClass(CopperbugEntity.class,
                            this.getBoundingBox().inflate(INTRUDER_RANGE),
                            copperbugEntity -> copperbugEntity != this && copperbugEntity.familyId != null
                                    && copperbugEntity.homePos != null && copperbugEntity.isWithinRestriction(this.blockPosition()))
                    .stream().min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
            if (familyCopperbug != null) {
                this.familyId = familyCopperbug.familyId;
                this.homePos = familyCopperbug.homePos;
            }
        }

        if (this.familyId == null) {
            this.familyId = this.getUUID();
        }

        if (this.homePos == null) {
            this.homePos = this.blockPosition();
        }

        this.restrictTo(this.homePos, TERRITORY_RADIUS);
    }

    @Override
    public boolean canAttack(LivingEntity targetEntity) {
        if (this.isBaby() || this.isFriendly() || targetEntity == this || !(targetEntity instanceof Mob || targetEntity instanceof Player)) {
            return false;
        }

        if (targetEntity instanceof CopperbugEntity copperbugEntity && this.isFamily(copperbugEntity)) {
            return false;
        }

        return this.isWithinRestriction() && this.isWithinRestriction(targetEntity.blockPosition()) && super.canAttack(targetEntity);
    }

    private boolean isIntruder(LivingEntity targetEntity) {
        double intruderRange = this.isTriggerItem(targetEntity) ? COPPER_INTRUDER_RANGE : INTRUDER_RANGE;
        return this.canAttack(targetEntity) && this.distanceToSqr(targetEntity) <= intruderRange * intruderRange;
    }

    private void alertFamily(LivingEntity targetEntity) {
        this.setTarget(targetEntity);
        for (CopperbugEntity copperbugEntity : this.level().getEntitiesOfClass(CopperbugEntity.class,
                this.getBoundingBox().inflate(TERRITORY_RADIUS * 2),
                familyCopperbug -> familyCopperbug != this && familyCopperbug.isAlive()
                        && this.isFamily(familyCopperbug) && familyCopperbug.canAttack(targetEntity))) {
            copperbugEntity.setWarning(false);
            copperbugEntity.setTarget(targetEntity);
        }
    }

    public boolean isFriendly() {
        return this.friendlyTicks > 0;
    }

    private void calmFamily(@Nullable BlockPos offeredCopperPos, @Nullable ItemEntity offeredCopperItem) {
        this.becomeFriendly(offeredCopperPos, offeredCopperItem);
        for (CopperbugEntity familyCopperbug : this.level().getEntitiesOfClass(CopperbugEntity.class,
                this.getBoundingBox().inflate(TERRITORY_RADIUS * 2),
                copperbugEntity -> copperbugEntity != this && copperbugEntity.isAlive() && this.isFamily(copperbugEntity))) {
            familyCopperbug.becomeFriendly(offeredCopperPos, offeredCopperItem);
        }
    }

    private void becomeFriendly(@Nullable BlockPos offeredCopperPos, @Nullable ItemEntity offeredCopperItem) {
        this.friendlyTicks = FRIENDLY_TICKS;
        this.stopBeingAngry();
        this.setTarget(null);
        this.goalSelector.getAvailableGoals().stream()
                .filter(goal -> goal.isRunning() && (goal.getGoal() instanceof AttackGoal || goal.getGoal() instanceof WarnIntruderGoal
                        || (offeredCopperPos != null || offeredCopperItem != null) && goal.getGoal() instanceof ScrapeGoal))
                .forEach(WrappedGoal::stop);
        this.warningActive = false;
        this.setWarning(false);
        this.entityData.set(PUFF_LEVEL, 0);
        this.attackAnimationTicks = 0;
        this.level().broadcastEntityEvent(this, (byte) 62);
        this.getNavigation().stop();
        this.offeredCopperPos = offeredCopperPos != null ? offeredCopperPos.immutable() : null;
        this.offeredCopperItem = offeredCopperItem;
        if (this.offeredCopperPos != null || this.offeredCopperItem != null) {
            this.onOxidizationDelivered();
            this.ticksUntilCanScraping = 0;
        }
    }

    private void findDroppedOffering() {
        if (this.offeredCopperPos != null && !this.scrapeGoal.canScrape(this.offeredCopperPos)) {
            this.offeredCopperPos = null;
        }
        if (this.offeredCopperItem != null && !this.scrapeGoal.canScrape(this.offeredCopperItem)) {
            this.offeredCopperItem = null;
        }
        if (this.offeredCopperPos != null || this.offeredCopperItem != null || this.isFriendly() && this.ticksUntilCanScraping > 0) {
            return;
        }

        ItemEntity copperOffering = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(6.0),
                        itemEntity -> this.distanceToSqr(itemEntity) <= 36.0 && this.scrapeGoal.canScrape(itemEntity)).stream()
                .min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
        if (copperOffering != null) {
            this.calmFamily(null, copperOffering);
        }
    }

    private boolean hasCopperOffering() {
        return this.offeredCopperPos != null && this.scrapeGoal.canScrape(this.offeredCopperPos)
                || this.offeredCopperItem != null && this.scrapeGoal.canScrape(this.offeredCopperItem);
    }

    private boolean isNearHome(BlockPos blockPos) {
        return this.homePos != null && this.homePos.distSqr(blockPos) <= CLEANING_RADIUS * CLEANING_RADIUS;
    }

    @Override
    public boolean doHurtTarget(Entity targetEntity) {
        return targetEntity instanceof LivingEntity livingEntity && this.canAttack(livingEntity) && super.doHurtTarget(targetEntity);
    }

    public int getAttackAnimationTicks() {
        return this.attackAnimationTicks;
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == 62) {
            this.attackAnimationTicks = 0;
            return;
        }

        if (status == 4) {
            this.attackAnimationTicks = ATTACK_ANIMATION_TICKS;
            return;
        }

        super.handleEntityEvent(status);
    }

    public static class CopperbugGroupData extends AgeableMobGroupData {
        private final UUID familyId;
        private final BlockPos homePos;

        public CopperbugGroupData(UUID familyId, BlockPos homePos) {
            super(true);
            this.familyId = familyId;
            this.homePos = homePos.immutable();
        }
    }

    @VisibleForDebug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.nestPos = NbtUtils.readBlockPos(nbt, "nest_pos").orElse(null);
        this.copperPos = NbtUtils.readBlockPos(nbt, "copper_pos").orElse(null);
        super.readAdditionalSaveData(nbt);
        this.familyId = nbt.hasUUID("FamilyId") ? nbt.getUUID("FamilyId") : null;
        this.homePos = NbtUtils.readBlockPos(nbt, "HomePos").orElse(null);
        if (this.homePos != null) {
            this.restrictTo(this.homePos, TERRITORY_RADIUS);
        }
        this.setHasOxidization(nbt.getBoolean("HasOxidization"));
        this.setOxidationStage(nbt.getInt("OxidationStage"));
        this.oxidationTicks = Mth.clamp(nbt.getInt("OxidationTicks"), 0, OXIDATION_STAGE_TICKS - 1);
        this.friendlyTicks = Mth.clamp(nbt.getInt("FriendlyTicks"), 0, FRIENDLY_TICKS);
        this.offeredCopperPos = NbtUtils.readBlockPos(nbt, "OfferedCopperPos").orElse(null);
        this.ticksSinceScraping = nbt.getInt("TicksSinceScraping");
        this.cannotEnterNestTicks = nbt.getInt("CannotEnterNestTicks");
        this.copperUpdatedSinceScraping = nbt.getInt("CopperUpdatedSinceScraping");
        this.ticksUntilCanScraping = nbt.getInt("ScrapingCooldown");

        this.readPersistentAngerSaveData(this.level(), nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        if (this.familyId != null) {
            nbt.putUUID("FamilyId", this.familyId);
        }

        if (this.homePos != null) {
            nbt.put("HomePos", NbtUtils.writeBlockPos(this.homePos));
        }

        if (this.hasNest()) {
            nbt.put("nest_pos", NbtUtils.writeBlockPos(this.getNestPos()));
        }

        if (this.hasCopperBlock()) {
            nbt.put("copper_pos", NbtUtils.writeBlockPos(this.getCopperBlockPos()));
        }

        nbt.putBoolean("HasOxidization", this.hasOxidization());
        nbt.putInt("OxidationStage", this.getOxidationStage());
        nbt.putInt("OxidationTicks", this.oxidationTicks);
        nbt.putInt("FriendlyTicks", this.friendlyTicks);
        if (this.offeredCopperPos != null) {
            nbt.put("OfferedCopperPos", NbtUtils.writeBlockPos(this.offeredCopperPos));
        }
        nbt.putInt("TicksSinceScraping", this.ticksSinceScraping);
        nbt.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
        nbt.putInt("CopperUpdatedSinceScraping", this.copperUpdatedSinceScraping);
        nbt.putInt("ScrapingCooldown", this.ticksUntilCanScraping);

        this.addPersistentAngerSaveData(nbt);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COPPERBUG_FLAGS, (byte) 0);
        builder.define(WARNING, false);
        builder.define(PUFF_LEVEL, 0);
        builder.define(CLEANING_MODE, 0);
        builder.define(OXIDATION_STAGE, 0);
    }

    @Override
    public void tick() {
        if (this.attackAnimationTicks > 0) {
            --this.attackAnimationTicks;
        }

        if (!this.level().isClientSide) {
            this.initializeTerritory();
            this.registerFamilyMember();
            if (this.isAlive() && this.tickCount % 10 == 0) {
                this.findDroppedOffering();
            }
        }

        super.tick();

        if (this.hasOxidization() && this.getCopperOxidizedSinceScraping() < 10 && this.random.nextFloat() < 0.05F) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.addParticle(this.level(), this.getX() - 0.30000001192092896, this.getX() + 0.30000001192092896, this.getZ() - 0.30000001192092896, this.getZ() + 0.30000001192092896, this.getY(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }

        if (this.level().isClientSide) {
            this.updateCleaningAnimation();
            this.updatePuffAnimation(this.getPuffLevel() * 0.5F);
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
            this.updatePersistentAnger((ServerLevel) this.level(), true);
            LivingEntity targetEntity = this.getTarget();
            if (targetEntity != null && targetEntity.isAlive() && this.canAttack(targetEntity)) {
                this.entityData.set(PUFF_LEVEL, 2);
            } else if (!this.warningActive) {
                this.entityData.set(PUFF_LEVEL, 0);
            }
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.friendlyTicks > 0) {
                --this.friendlyTicks;
            }

            if (this.isAlive() && this.getOxidationStage() < MAX_OXIDATION_STAGE && ++this.oxidationTicks >= OXIDATION_STAGE_TICKS) {
                this.setOxidationStage(this.getOxidationStage() + 1);
            }

            if (this.cannotEnterNestTicks > 0) {
                --this.cannotEnterNestTicks;
            }

            if (this.ticksLeftToFindNest > 0) {
                --this.ticksLeftToFindNest;
            }

            if (this.ticksUntilCanScraping > 0) {
                --this.ticksUntilCanScraping;
            }

            if (this.hasOxidization() && this.ticksUntilCanScraping == 0) {
                this.onOxidizationDelivered();
            }
        }

    }

    private void addParticle(Level world, double lastX, double x, double lastZ, double z, double y, ParticleOptions effect) {
        world.addParticle(effect, Mth.lerp(world.random.nextDouble(), lastX, x), y, Mth.lerp(world.random.nextDouble(), lastZ, z), 0.0, 0.0, 0.0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mateEntity) {
        CopperbugEntity copperbugBaby = BGEntityTypes.COPPERBUG.get().create(serverLevel);
        if (copperbugBaby == null) {
            return null;
        }

        copperbugBaby.familyId = this.familyId != null ? this.familyId : this.getUUID();
        copperbugBaby.homePos = this.homePos != null ? this.homePos : this.blockPosition();
        copperbugBaby.restrictTo(copperbugBaby.homePos, TERRITORY_RADIUS);
        return copperbugBaby;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    public boolean isWarning() {
        return this.entityData.get(WARNING);
    }

    public void setWarning(boolean warning) {
        this.entityData.set(WARNING, warning);
    }

    public float getWarningAnimationProgress(float tickDelta) {
        return Mth.lerp(tickDelta, this.lastWarningAnimationProgress, this.warningAnimationProgress) / 6.0F;
    }

    public int getPuffLevel() {
        return this.entityData.get(PUFF_LEVEL);
    }

    public int getCleaningMode() {
        return this.entityData.get(CLEANING_MODE);
    }

    public int getOxidationStage() {
        return this.entityData.get(OXIDATION_STAGE);
    }

    public void setOxidationStage(int oxidationStage) {
        this.entityData.set(OXIDATION_STAGE, Mth.clamp(oxidationStage, 0, MAX_OXIDATION_STAGE));
        this.oxidationTicks = 0;
    }

    public float getCleaningAnimationProgress(float partialTick) {
        return Mth.lerp(partialTick, this.lastCleaningAnimationProgress, this.cleaningAnimationProgress);
    }

    public float getRaisedCleaningAnimationProgress(float partialTick) {
        return Mth.lerp(partialTick, this.lastRaisedCleaningAnimationProgress, this.raisedCleaningAnimationProgress);
    }

    public float getCleaningAnimationTicks(float partialTick) {
        return this.cleaningAnimationTicks + partialTick;
    }

    private void updateCleaningAnimation() {
        int cleaningMode = this.getCleaningMode();
        this.lastCleaningAnimationProgress = this.cleaningAnimationProgress;
        this.lastRaisedCleaningAnimationProgress = this.raisedCleaningAnimationProgress;
        this.cleaningAnimationProgress = Mth.approach(this.cleaningAnimationProgress, cleaningMode > 0 ? 1.0F : 0.0F, 1.0F / 6.0F);
        this.raisedCleaningAnimationProgress = Mth.approach(this.raisedCleaningAnimationProgress, cleaningMode == 2 ? 1.0F : 0.0F, 1.0F / 6.0F);
        if (cleaningMode > 0 || this.cleaningAnimationProgress > 0.0F) {
            ++this.cleaningAnimationTicks;
        } else {
            this.cleaningAnimationTicks = 0;
        }
    }

    public float getPuffAnimationProgress(float partialTick) {
        return Mth.lerp(partialTick, this.lastPuffAnimationProgress, this.puffAnimationProgress);
    }

    private void updatePuffAnimation(float targetProgress) {
        this.lastPuffAnimationProgress = this.puffAnimationProgress;
        this.puffAnimationVelocity = (this.puffAnimationVelocity + (targetProgress - this.puffAnimationProgress) * 0.25F) * 0.7F;
        this.puffAnimationProgress += this.puffAnimationVelocity;
        if (Math.abs(targetProgress - this.puffAnimationProgress) < 0.001F && Math.abs(this.puffAnimationVelocity) < 0.001F) {
            this.puffAnimationProgress = targetProgress;
            this.puffAnimationVelocity = 0.0F;
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.COPPERBUG_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return BGSoundEvents.COPPERBUG_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.COPPERBUG_DEATH_ADDITIONS_EVENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundCooldown <= 0) {
            this.makeSound(BGSoundEvents.COPPERBUG_AMBIENT_ADDITIONS_EVENT);
            this.warningSoundCooldown = 40;
        }
    }

    protected void tickWaterBreathingAir(int air) {
        this.setAirSupply(300);

    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.tickWaterBreathingAir(i);
    }

    @Override
    protected void customServerAiStep() {
        LivingEntity targetEntity = this.getTarget();
        if (targetEntity != null && (!targetEntity.isAlive() || !this.canAttack(targetEntity))) {
            this.setTarget(null);
            this.getNavigation().stop();
        }

        super.customServerAiStep();

        if (!this.hasOxidization()) {
            ++this.ticksSinceScraping;
        }

    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    public boolean isTriggerItem(LivingEntity targetEntity) {
        ItemStack mainHandItem = targetEntity.getMainHandItem();
        ItemStack offHandItem = targetEntity.getOffhandItem();
        return mainHandItem.is(JamiesModTag.COPPER_BLOCKS) || mainHandItem.is(JamiesModTag.VERDAGRIS_ITEMS)
                || offHandItem.is(JamiesModTag.COPPER_BLOCKS) || offHandItem.is(JamiesModTag.VERDAGRIS_ITEMS);
    }

    boolean isCopperBlock(BlockPos pos) {
        return this.level().isLoaded(pos) && this.level().getBlockState(pos).is(JamiesModTag.COPPER_BLOCKS_1);
    }

    public boolean hasCopperBlock() {
        return this.copperPos != null;
    }

    public BlockPos getCopperBlockPos() {
        return this.copperPos;
    }

    public void setCopperBlockPos(BlockPos copperPos) {
        this.copperPos = copperPos;
    }

    private boolean failedScrapingTooLong() {
        return this.ticksSinceScraping > 3600;
    }

    public void resetScrapingTicks() {
        this.ticksSinceScraping = 0;
    }

    private @Nullable BlockPos getNestPos() {
        return this.nestPos;
    }

    public void setNestPos(BlockPos pos) {
        this.nestPos = pos;
    }

    private boolean hasNest() {
        return this.nestPos != null;
    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), distance);
    }

    boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    public boolean hasOxidization() {
        return this.getCopperbugFlag(8);
    }

    void setHasOxidization(boolean hasNectar) {
        if (hasNectar) {
            this.resetScrapingTicks();
        }

        this.setCopperbugFlag(8, hasNectar);
    }

    private void resetCopperCounter() {
        this.copperUpdatedSinceScraping = 0;
    }

    void addCopperCounter() {
        ++this.copperUpdatedSinceScraping;
    }

    private int getCopperOxidizedSinceScraping() {
        return this.copperUpdatedSinceScraping;
    }

    public void onOxidizationDelivered() {
        this.setHasOxidization(false);
        this.resetCopperCounter();
    }

    private void setCopperbugFlag(int bit, boolean value) {
        if (value) {
            this.entityData.set(COPPERBUG_FLAGS, (byte) (this.entityData.get(COPPERBUG_FLAGS) | bit));
        } else {
            this.entityData.set(COPPERBUG_FLAGS, (byte) (this.entityData.get(COPPERBUG_FLAGS) & ~bit));
        }

    }

    private boolean getCopperbugFlag(int location) {
        return (this.entityData.get(COPPERBUG_FLAGS) & location) != 0;
    }

    private void startMovingTo(BlockPos pos) {
        Vec3 vec3d = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int) vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3 vec3d2 = AirRandomPos.getPosTowards(this, k, l, i, vec3d, 0.3141592741012573);
        if (vec3d2 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
        }
    }

    private class WarnIntruderGoal extends Goal {
        private final TargetingConditions targetingConditions = TargetingConditions.forCombat()
                .range(COPPER_INTRUDER_RANGE).selector(CopperbugEntity.this::isIntruder);
        @Nullable
        private LivingEntity threat;
        private int warningTicks;
        private int warningsIssued;

        public WarnIntruderGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (CopperbugEntity.this.isBaby() || CopperbugEntity.this.getTarget() != null
                    || !CopperbugEntity.this.isWithinRestriction()) {
                return false;
            }

            this.threat = CopperbugEntity.this.level().getNearestEntity(
                    CopperbugEntity.this.level().getEntitiesOfClass(LivingEntity.class,
                            CopperbugEntity.this.getBoundingBox().inflate(COPPER_INTRUDER_RANGE)),
                    this.targetingConditions, CopperbugEntity.this,
                    CopperbugEntity.this.getX(), CopperbugEntity.this.getY(), CopperbugEntity.this.getZ());
            return this.threat != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.threat != null && CopperbugEntity.this.getTarget() == null
                    && this.targetingConditions.test(CopperbugEntity.this, this.threat);
        }

        @Override
        public void start() {
            this.warningTicks = 0;
            this.warningsIssued = 1;
            CopperbugEntity.this.warningActive = true;
            CopperbugEntity.this.entityData.set(PUFF_LEVEL, 1);
            CopperbugEntity.this.getNavigation().stop();
            CopperbugEntity.this.warningSoundCooldown = 0;
            CopperbugEntity.this.setWarning(true);
            CopperbugEntity.this.playWarningSound();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (!this.canContinueToUse()) {
                this.stop();
                return;
            }

            CopperbugEntity.this.getNavigation().stop();
            CopperbugEntity.this.getLookControl().setLookAt(this.threat, 30.0F, 30.0F);
            if (++this.warningTicks == 20) {
                CopperbugEntity.this.setWarning(false);
            }

            if (this.warningTicks < 40) {
                return;
            }

            if (this.warningsIssued < 2) {
                ++this.warningsIssued;
                CopperbugEntity.this.entityData.set(PUFF_LEVEL, this.warningsIssued);
                this.warningTicks = 0;
                CopperbugEntity.this.setWarning(true);
                CopperbugEntity.this.playWarningSound();
                return;
            }

            CopperbugEntity.this.alertFamily(this.threat);
        }

        @Override
        public void stop() {
            this.threat = null;
            this.warningTicks = 0;
            this.warningsIssued = 0;
            CopperbugEntity.this.warningActive = false;
            if (CopperbugEntity.this.getTarget() == null) {
                CopperbugEntity.this.entityData.set(PUFF_LEVEL, 0);
            }
            CopperbugEntity.this.setWarning(false);
        }
    }

    private class AttackGoal extends MeleeAttackGoal {
        @Nullable
        private LivingEntity attackTarget;

        public AttackGoal() {
            super(CopperbugEntity.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (!CopperbugEntity.this.canAttack(target)) {
                this.attackTarget = null;
                return;
            }

            if (CopperbugEntity.this.attackAnimationTicks > 0) {
                if (CopperbugEntity.this.attackAnimationTicks == ATTACK_CLOSE_TICKS + ATTACK_RECOVERY_TICKS
                        && target == this.attackTarget && target.isAlive() && CopperbugEntity.this.canAttack(target)
                        && this.mob.distanceToSqr(target) <= 9.0 && this.mob.getSensing().hasLineOfSight(target)) {
                    Vec3 targetDirection = target.getBoundingBox().getCenter().subtract(this.mob.getBoundingBox().getCenter());
                    Vec3 lungeMovement = targetDirection.normalize().scale(Math.min(0.45, targetDirection.length() * 0.5));
                    Vec3 currentMovement = this.mob.getDeltaMovement();
                    this.mob.setDeltaMovement(lungeMovement.x, currentMovement.y + Mth.clamp(lungeMovement.y, -0.1, 0.15), lungeMovement.z);
                    this.mob.hasImpulse = true;
                }

                if (CopperbugEntity.this.attackAnimationTicks == ATTACK_RECOVERY_TICKS) {
                    if (target == this.attackTarget && target.isAlive()) {
                        CopperbugEntity.this.makeSound(BGSoundEvents.COPPERBUG_HURT_ADDITIONS_EVENT);
                        CopperbugEntity.this.warningSoundCooldown = 40;
                        if (this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target)) {
                            this.mob.doHurtTarget(target);
                        }
                    }

                    this.attackTarget = null;
                }

                return;
            }

            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.attackTarget = target;
                CopperbugEntity.this.attackAnimationTicks = ATTACK_ANIMATION_TICKS;
                CopperbugEntity.this.level().broadcastEntityEvent(CopperbugEntity.this, (byte) 4);
                CopperbugEntity.this.setWarning(false);
            }

        }

        @Override
        public void stop() {
            this.attackTarget = null;
            CopperbugEntity.this.setWarning(false);
            super.stop();
        }
    }

    private class FollowFamilyGoal extends Goal {
        @Nullable
        private CopperbugEntity parentCopperbug;

        public FollowFamilyGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!CopperbugEntity.this.isBaby() || !CopperbugEntity.this.isWithinRestriction()) {
                return false;
            }

            this.parentCopperbug = CopperbugEntity.this.level().getEntitiesOfClass(CopperbugEntity.class,
                            CopperbugEntity.this.getBoundingBox().inflate(8.0, 4.0, 8.0),
                            copperbugEntity -> copperbugEntity.isAlive() && !copperbugEntity.isBaby()
                                    && CopperbugEntity.this.isFamily(copperbugEntity)
                                    && CopperbugEntity.this.isWithinRestriction(copperbugEntity.blockPosition()))
                    .stream().min(Comparator.comparingDouble(CopperbugEntity.this::distanceToSqr)).orElse(null);
            return this.parentCopperbug != null && CopperbugEntity.this.distanceToSqr(this.parentCopperbug) >= 9.0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentCopperbug != null && this.parentCopperbug.isAlive() && CopperbugEntity.this.isBaby()
                    && CopperbugEntity.this.isFamily(this.parentCopperbug)
                    && CopperbugEntity.this.isWithinRestriction() && CopperbugEntity.this.isWithinRestriction(this.parentCopperbug.blockPosition())
                    && CopperbugEntity.this.distanceToSqr(this.parentCopperbug) >= 9.0
                    && CopperbugEntity.this.distanceToSqr(this.parentCopperbug) <= 256.0;
        }

        @Override
        public void tick() {
            CopperbugEntity.this.getNavigation().moveTo(this.parentCopperbug, 1.25);
        }

        @Override
        public void stop() {
            this.parentCopperbug = null;
            CopperbugEntity.this.getNavigation().stop();
        }
    }

    class CopperbugRevengeGoal extends HurtByTargetGoal {
        public CopperbugRevengeGoal() {
            super(CopperbugEntity.this);
            this.setAlertOthers();
        }

        @Override
        protected void alertOther(Mob mob, LivingEntity targetEntity) {
            if (mob instanceof CopperbugEntity copperbugEntity && CopperbugEntity.this.isFamily(copperbugEntity)
                    && copperbugEntity.canAttack(targetEntity)) {
                super.alertOther(mob, targetEntity);
            }
        }
    }

    private class ReturnHomeGoal extends MoveTowardsRestrictionGoal {
        public ReturnHomeGoal() {
            super(CopperbugEntity.this, 1.2);
        }

        @Override
        public boolean canUse() {
            return !CopperbugEntity.this.hasCopperOffering() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !CopperbugEntity.this.hasCopperOffering() && super.canContinueToUse();
        }
    }

    private class CopperOfferingListener implements GameEventListener {
        private final PositionSource positionSource = new EntityPositionSource(CopperbugEntity.this, CopperbugEntity.this.getEyeHeight());

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return 6;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 position) {
            if (!gameEvent.is(GameEvent.BLOCK_PLACE) || !CopperbugEntity.this.isAlive()) {
                return false;
            }

            CopperbugEntity.this.initializeTerritory();
            BlockPos copperPos = BlockPos.containing(position);
            if (!CopperbugEntity.this.scrapeGoal.canScrape(copperPos)) {
                return false;
            }

            CopperbugEntity.this.calmFamily(copperPos, null);
            return true;
        }
    }

    class ScrapeGoal extends Goal {
        private final CopperbugEntity copperbugEntity;
        @Nullable
        private CopperbugEntity familyCopperbug;
        @Nullable
        private ItemEntity copperItem;
        private int scrapeTicks;
        private int travelTicks;
        private boolean scrapedCopper;

        public ScrapeGoal(CopperbugEntity copperbugEntity) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.copperbugEntity = copperbugEntity;
        }

        private boolean canScrape(BlockPos blockPos) {
            if (!this.copperbugEntity.level().isLoaded(blockPos)) {
                return false;
            }

            BlockState blockState = this.copperbugEntity.level().getBlockState(blockPos);
            return blockState.is(JamiesModTag.COPPER_BLOCKS_1) && WeatheringCopper.getPrevious(blockState).isPresent()
                    && (!blockState.hasProperty(BlockStateProperties.WATERLOGGED) || !blockState.getValue(BlockStateProperties.WATERLOGGED));
        }

        private boolean canClean(CopperbugEntity familyCopperbug) {
            return familyCopperbug != this.copperbugEntity && familyCopperbug.isAlive() && familyCopperbug.getOxidationStage() > 0
                    && this.copperbugEntity.isFamily(familyCopperbug) && familyCopperbug.getTarget() == null
                    && !familyCopperbug.warningActive && this.copperbugEntity.isWithinRestriction(familyCopperbug.blockPosition())
                    && this.copperbugEntity.hasLineOfSight(familyCopperbug);
        }

        private boolean canScrape(ItemEntity copperItem) {
            if (!copperItem.isAlive() || copperItem.getItem().isEmpty() || !(copperItem.getItem().getItem() instanceof BlockItem blockItem)
                    || !this.copperbugEntity.hasLineOfSight(copperItem)) {
                return false;
            }

            BlockState blockState = blockItem.getBlock().defaultBlockState();
            return blockState.is(JamiesModTag.COPPER_BLOCKS_1) && WeatheringCopper.getPrevious(blockState).isPresent();
        }

        @Override
        public boolean canUse() {
            if (this.copperbugEntity.getTarget() != null || this.copperbugEntity.ticksUntilCanScraping > 0
                    || this.copperbugEntity.hasOxidization()) {
                return false;
            }

            BlockPos copperbugPos = this.copperbugEntity.blockPosition();
            this.copperItem = this.copperbugEntity.offeredCopperItem;
            if (this.copperItem != null) {
                if (this.canScrape(this.copperItem)) {
                    this.familyCopperbug = null;
                    this.copperbugEntity.copperPos = null;
                    return true;
                }
                this.copperItem = null;
                this.copperbugEntity.offeredCopperItem = null;
            }

            if (this.copperbugEntity.offeredCopperPos != null) {
                if (this.canScrape(this.copperbugEntity.offeredCopperPos)) {
                    this.familyCopperbug = null;
                    this.copperbugEntity.copperPos = this.copperbugEntity.offeredCopperPos;
                    return true;
                }
                this.copperbugEntity.offeredCopperPos = null;
            }

            if (!this.copperbugEntity.isWithinRestriction()) {
                return false;
            }

            this.familyCopperbug = this.copperbugEntity.level().getEntitiesOfClass(CopperbugEntity.class,
                            this.copperbugEntity.getBoundingBox().inflate(5.0), this::canClean).stream()
                    .min(Comparator.comparingDouble(this.copperbugEntity::distanceToSqr)).orElse(null);
            if (this.familyCopperbug != null) {
                this.copperbugEntity.copperPos = null;
                return true;
            }

            this.copperbugEntity.copperPos = BlockPos.betweenClosedStream(copperbugPos.offset(-5, -2, -5), copperbugPos.offset(5, 2, 5))
                    .filter(blockPos -> this.copperbugEntity.isNearHome(blockPos) && this.canScrape(blockPos)).map(BlockPos::immutable)
                    .min(Comparator.comparingDouble(blockPos -> blockPos.distSqr(copperbugPos))).orElse(null);
            if (this.copperbugEntity.copperPos == null) {
                this.copperbugEntity.ticksUntilCanScraping = 60;
                return false;
            }

            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.scrapedCopper || this.travelTicks >= 200 || this.copperbugEntity.getTarget() != null) {
                return false;
            }

            if (this.familyCopperbug != null) {
                return this.copperbugEntity.isWithinRestriction() && this.canClean(this.familyCopperbug);
            }

            if (this.copperItem != null) {
                return this.copperItem == this.copperbugEntity.offeredCopperItem && this.canScrape(this.copperItem);
            }

            BlockPos copperPos = this.copperbugEntity.copperPos;
            return copperPos != null && this.canScrape(copperPos)
                    && (copperPos.equals(this.copperbugEntity.offeredCopperPos)
                    || this.copperbugEntity.isWithinRestriction() && this.copperbugEntity.isNearHome(copperPos));
        }

        @Override
        public void start() {
            this.scrapeTicks = 0;
            this.travelTicks = 0;
            this.scrapedCopper = false;
            this.copperbugEntity.entityData.set(CLEANING_MODE, 0);
            this.copperbugEntity.resetScrapingTicks();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (!this.canContinueToUse()) {
                this.copperbugEntity.entityData.set(CLEANING_MODE, 0);
                return;
            }

            ++this.travelTicks;
            if (this.familyCopperbug != null) {
                this.cleanFamilyCopperbug();
                return;
            }

            if (this.copperItem != null) {
                this.scrapeDroppedCopper();
                return;
            }

            BlockPos copperPos = this.copperbugEntity.copperPos;
            if (copperPos == null) {
                return;
            }

            BlockState blockState = this.copperbugEntity.level().getBlockState(copperPos);
            VoxelShape copperShape = blockState.getCollisionShape(this.copperbugEntity.level(), copperPos);
            Vec3 scrapingPosition = this.getScrapingPosition(copperPos, copperShape);
            this.copperbugEntity.getLookControl().setLookAt(scrapingPosition.x, scrapingPosition.y, scrapingPosition.z);
            BlockHitResult scrapingHit = this.copperbugEntity.level().clip(new ClipContext(this.copperbugEntity.getEyePosition(),
                    scrapingPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.copperbugEntity));
            if (this.copperbugEntity.distanceToSqr(Vec3.atCenterOf(copperPos)) > 2.25
                    || scrapingHit.getType() == HitResult.Type.BLOCK && !scrapingHit.getBlockPos().equals(copperPos)) {
                this.scrapeTicks = 0;
                this.copperbugEntity.entityData.set(CLEANING_MODE, 0);
                if (this.travelTicks % 20 == 1) {
                    this.copperbugEntity.getNavigation().moveTo(copperPos.getX() + 0.5, copperPos.getY() + 1.0, copperPos.getZ() + 0.5, 1.2);
                }
                return;
            }

            this.copperbugEntity.getNavigation().stop();
            this.copperbugEntity.entityData.set(CLEANING_MODE, scrapingPosition.y > this.copperbugEntity.getEyeY() + 0.1 ? 2 : 1);
            ++this.scrapeTicks;
            if (this.scrapeTicks % ATTACK_ANIMATION_TICKS == ATTACK_OPEN_TICKS + ATTACK_CLOSE_TICKS) {
                this.spawnScrapingParticles(blockState, scrapingPosition);
            }
            if (this.scrapeTicks < 60) {
                return;
            }

            Optional<BlockState> scrapedState = WeatheringCopper.getPrevious(blockState);
            if (scrapedState.isEmpty()) {
                return;
            }

            CopperbugEntity mateCopperbug = this.findScrapingMate(copperPos, null);
            if (!this.copperbugEntity.level().setBlock(copperPos, scrapedState.get(), 3)) {
                return;
            }
            this.copperbugEntity.level().levelEvent(3005, copperPos, 0);
            this.copperbugEntity.level().gameEvent(GameEvent.BLOCK_CHANGE, copperPos, GameEvent.Context.of(this.copperbugEntity, scrapedState.get()));
            this.finishScraping(mateCopperbug);
        }

        private void finishScraping(@Nullable CopperbugEntity mateCopperbug) {
            this.copperbugEntity.playSound(BGSoundEvents.COPPERBUG_EAT_ADDITIONS_EVENT, 1.0F, 1.0F);
            this.copperbugEntity.setHasOxidization(true);
            this.scrapedCopper = true;
            if (mateCopperbug != null && this.copperbugEntity.level() instanceof ServerLevel serverLevel) {
                mateCopperbug.scrapeGoal.scrapedCopper = true;
                mateCopperbug.setHasOxidization(true);
                this.copperbugEntity.spawnChildFromBreeding(serverLevel, mateCopperbug);
            }
            this.copperbugEntity.calmFamily(null, null);
        }

        private void scrapeDroppedCopper() {
            if (!(this.copperItem.getItem().getItem() instanceof BlockItem blockItem)) {
                return;
            }

            this.copperbugEntity.getLookControl().setLookAt(this.copperItem);
            if (this.copperbugEntity.distanceToSqr(this.copperItem) > 2.25) {
                this.scrapeTicks = 0;
                this.copperbugEntity.entityData.set(CLEANING_MODE, 0);
                if (this.travelTicks % 20 == 1) {
                    this.copperbugEntity.getNavigation().moveTo(this.copperItem, 1.2);
                }
                return;
            }

            this.copperbugEntity.getNavigation().stop();
            this.copperbugEntity.entityData.set(CLEANING_MODE, 1);
            ++this.scrapeTicks;
            BlockState blockState = blockItem.getBlock().defaultBlockState();
            if (this.scrapeTicks % ATTACK_ANIMATION_TICKS == ATTACK_OPEN_TICKS + ATTACK_CLOSE_TICKS) {
                this.spawnScrapingParticles(blockState, this.copperItem.getBoundingBox().getCenter());
            }
            if (this.scrapeTicks < 60) {
                return;
            }

            Optional<BlockState> scrapedState = WeatheringCopper.getPrevious(blockState);
            if (scrapedState.isEmpty()) {
                return;
            }

            CopperbugEntity mateCopperbug = this.findScrapingMate(null, this.copperItem);
            ItemStack originalCopper = this.copperItem.getItem();
            ItemStack cleanedCopper = originalCopper.transmuteCopy(scrapedState.get().getBlock(), 1);
            if (originalCopper.getCount() == 1) {
                this.copperItem.setItem(cleanedCopper);
            } else {
                ItemEntity cleanedCopperItem = this.copperItem.copy();
                cleanedCopperItem.setItem(cleanedCopper);
                if (!this.copperbugEntity.level().addFreshEntity(cleanedCopperItem)) {
                    return;
                }
                this.copperItem.setItem(originalCopper.copyWithCount(originalCopper.getCount() - 1));
            }
            this.finishScraping(mateCopperbug);
        }

        private Vec3 getScrapingPosition(BlockPos copperPos, VoxelShape copperShape) {
            Vec3 copperbugEyes = this.copperbugEntity.getEyePosition();
            Vec3 scrapingPosition = copperShape.move(copperPos.getX(), copperPos.getY(), copperPos.getZ())
                    .closestPointTo(copperbugEyes.add(0.0, 0.25, 0.0)).orElse(Vec3.atCenterOf(copperPos));
            Vec3 surfaceOffset = copperbugEyes.subtract(scrapingPosition).normalize().scale(0.02);
            return scrapingPosition.add(surfaceOffset);
        }

        private void spawnScrapingParticles(BlockState blockState, Vec3 scrapingPosition) {
            if (!(this.copperbugEntity.level() instanceof ServerLevel serverLevel)) {
                return;
            }

            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                    scrapingPosition.x, scrapingPosition.y, scrapingPosition.z, 3, 0.025, 0.025, 0.025, 0.025);
        }

        @Nullable
        private CopperbugEntity findScrapingMate(@Nullable BlockPos copperPos, @Nullable ItemEntity copperItem) {
            if (this.copperbugEntity.getAge() != 0 || !this.copperbugEntity.isAlive()) {
                return null;
            }

            return this.copperbugEntity.level().getEntitiesOfClass(CopperbugEntity.class,
                            this.copperbugEntity.getBoundingBox().inflate(3.0), mateCopperbug -> this.canMateWhileScraping(mateCopperbug, copperPos, copperItem)).stream()
                    .min(Comparator.comparingDouble(this.copperbugEntity::distanceToSqr)).orElse(null);
        }

        private boolean canMateWhileScraping(CopperbugEntity mateCopperbug, @Nullable BlockPos copperPos, @Nullable ItemEntity copperItem) {
            Vec3 scrapingPosition = copperItem != null ? copperItem.position() : Vec3.atCenterOf(copperPos);
            if (mateCopperbug == this.copperbugEntity || !mateCopperbug.isAlive() || mateCopperbug.getAge() != 0
                    || !this.copperbugEntity.isFamily(mateCopperbug) || mateCopperbug.getTarget() != null || mateCopperbug.warningActive
                    || !Objects.equals(copperPos, mateCopperbug.copperPos) || mateCopperbug.getCleaningMode() == 0
                    || mateCopperbug.distanceToSqr(scrapingPosition) > 2.25 || !this.copperbugEntity.hasLineOfSight(mateCopperbug)) {
                return false;
            }

            ScrapeGoal mateScrapeGoal = mateCopperbug.scrapeGoal;
            return mateScrapeGoal.familyCopperbug == null && mateScrapeGoal.copperItem == copperItem && mateScrapeGoal.scrapeTicks >= ATTACK_ANIMATION_TICKS
                    && mateScrapeGoal.canContinueToUse();
        }

        private void cleanFamilyCopperbug() {
            this.copperbugEntity.getLookControl().setLookAt(this.familyCopperbug);
            if (this.copperbugEntity.distanceToSqr(this.familyCopperbug) > 2.25) {
                this.scrapeTicks = 0;
                this.copperbugEntity.entityData.set(CLEANING_MODE, 0);
                if (this.travelTicks % 20 == 1) {
                    this.copperbugEntity.getNavigation().moveTo(this.familyCopperbug, 1.2);
                }
                return;
            }

            this.copperbugEntity.getNavigation().stop();
            this.copperbugEntity.entityData.set(CLEANING_MODE, 1);
            if (++this.scrapeTicks < 60) {
                return;
            }

            this.familyCopperbug.setOxidationStage(this.familyCopperbug.getOxidationStage() - 1);
            this.copperbugEntity.playSound(BGSoundEvents.COPPERBUG_EAT_ADDITIONS_EVENT, 1.0F, 1.0F);
            this.scrapedCopper = true;
        }

        @Override
        public void stop() {
            this.copperbugEntity.getNavigation().stop();
            if (Objects.equals(this.copperbugEntity.offeredCopperPos, this.copperbugEntity.copperPos)) {
                this.copperbugEntity.offeredCopperPos = null;
            }
            if (this.copperbugEntity.offeredCopperItem == this.copperItem) {
                this.copperbugEntity.offeredCopperItem = null;
            }
            this.copperbugEntity.copperPos = null;
            this.familyCopperbug = null;
            this.copperItem = null;
            this.copperbugEntity.entityData.set(CLEANING_MODE, 0);
            this.copperbugEntity.ticksUntilCanScraping = 200;
        }
    }
    class MoveToCopperGoal extends Goal {
        private static final int MAX_FLOWER_NAVIGATION_TICKS = 600;
        int ticks;

        MoveToCopperGoal() {
            super();
            this.ticks = CopperbugEntity.this.level().random.nextInt(10);
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return CopperbugEntity.this.copperPos != null && !CopperbugEntity.this.hasRestriction() && this.shouldMoveToFlower() && CopperbugEntity.this.isCopperBlock(CopperbugEntity.this.copperPos) && !CopperbugEntity.this.isWithinDistance(CopperbugEntity.this.copperPos, 2);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.ticks = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.ticks = 0;
            CopperbugEntity.this.navigation.stop();
            CopperbugEntity.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (CopperbugEntity.this.copperPos != null) {
                ++this.ticks;
                if (this.ticks > this.adjustedTickDelay(600)) {
                    CopperbugEntity.this.copperPos = null;
                } else if (!CopperbugEntity.this.navigation.isInProgress()) {
                    if (CopperbugEntity.this.isTooFar(CopperbugEntity.this.copperPos)) {
                        CopperbugEntity.this.copperPos = null;
                    } else {
                        CopperbugEntity.this.startMovingTo(CopperbugEntity.this.copperPos);
                    }
                }
            }
        }

        private boolean shouldMoveToFlower() {
            return CopperbugEntity.this.ticksSinceScraping > 2400;
        }
    }

    class OxidizeCopperGoal extends Goal {
        static final int field_30299 = 30;

        OxidizeCopperGoal() {
            super();
        }

        @Override
        public boolean canUse() {
            if (CopperbugEntity.this.getCopperOxidizedSinceScraping() >= 10) {
                return false;
            } else if (CopperbugEntity.this.random.nextFloat() < 0.3F) {
                return false;
            } else {
                return CopperbugEntity.this.hasOxidization();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void tick() {

            if (CopperbugEntity.this.random.nextInt(this.adjustedTickDelay(30)) == 0) {
                for (int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = CopperbugEntity.this.blockPosition().below(i);
                    BlockState blockState = CopperbugEntity.this.level().getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    BlockState blockState2 = null;
                    if (blockState.is(JamiesModTag.COPPER_BLOCKS_1)) {
                        Optional<BlockState> optional2 = WeatheringCopper.getPrevious(blockState);

                        if (block instanceof WeatheringCopperFullBlock copperBlock) {
                            if (optional2.isPresent()) {
                                CopperbugEntity.this.level().playSound(null, blockPos, BGSoundEvents.COPPERBUG_EAT_ADDITIONS_EVENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                                CopperbugEntity.this.level().levelEvent(null, 3005, blockPos, 0);
                                // optional2;
                            }

                            if (!(copperBlock.getAge() == WeatheringCopper.WeatherState.OXIDIZED)) {
                                copperBlock.getAge();

                            }
                        }

                        if (block instanceof CropBlock cropBlock) {
                            if (!cropBlock.isMaxAge(blockState)) {
                                blockState2 = cropBlock.getStateForAge(cropBlock.getAge(blockState) + 1);
                            }
                        } else {
                            int j;
                            if (block instanceof StemBlock) {
                                j = blockState.getValue(StemBlock.AGE);
                                if (j < 7) {
                                    blockState2 = blockState.setValue(StemBlock.AGE, j + 1);
                                }
                            } else if (blockState.is(Blocks.SWEET_BERRY_BUSH)) {
                                j = blockState.getValue(SweetBerryBushBlock.AGE);
                                if (j < 3) {
                                    blockState2 = blockState.setValue(SweetBerryBushBlock.AGE, j + 1);
                                }
                            } else if (blockState.is(Blocks.CAVE_VINES) || blockState.is(Blocks.CAVE_VINES_PLANT)) {
                                ((BonemealableBlock) blockState.getBlock()).performBonemeal((ServerLevel) CopperbugEntity.this.level(), CopperbugEntity.this.random, blockPos, blockState);
                            }
                        }

                        if (blockState2 != null) {
                            CopperbugEntity.this.level().levelEvent(5011, blockPos, 15);
                            CopperbugEntity.this.level().setBlockAndUpdate(blockPos, blockState2);
                            CopperbugEntity.this.addCopperCounter();
                        }
                    }
                }

            }
        }
    }
}
