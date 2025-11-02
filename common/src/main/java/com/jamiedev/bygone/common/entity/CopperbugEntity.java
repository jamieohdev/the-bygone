package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class CopperbugEntity extends Animal implements NeutralMob {
    private static final EntityDataAccessor<Byte> COPPERBUG_FLAGS;

    private static final EntityDataAccessor<Boolean> WARNING;
    private static final float field_30352 = 6.0F;
    private static final UniformInt ANGER_TIME_RANGE;
    private static final int field_30274 = 200;
    private static final int field_30275 = 200;

    static {
        COPPERBUG_FLAGS = SynchedEntityData.defineId(CopperbugEntity.class, EntityDataSerializers.BYTE);
        WARNING = SynchedEntityData.defineId(CopperbugEntity.class, EntityDataSerializers.BOOLEAN);
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
    private int warningSoundCooldown;
    private int angerTime;
    @Nullable
    private UUID angryAt;
    private int cannotEnterNestTicks;
    private int copperUpdatedSinceScraping;

    public CopperbugEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.ticksUntilCanScraping = Mth.nextInt(this.random, 20, 60);
    }

    public static AttributeSupplier.Builder createCopperbugAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    public static boolean canSpawn(EntityType<CopperbugEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, @NotNull RandomSource random) {
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
        this.goalSelector.addGoal(1, new CopperbugEntity.AttackGoal());
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0, (polarBear) -> {
            return polarBear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES;
        }));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.scrapeGoal = new ScrapeGoal(this);
        this.goalSelector.addGoal(4, this.scrapeGoal);
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        //this.goalSelector.addGoal(7, new OxidizeCopperGoal());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(CopperbugEntity.class));
        this.targetSelector.addGoal(1, new CopperbugEntity.CopperbugRevengeGoal());
        this.targetSelector.addGoal(2, new CopperbugEntity.IsCopperOrVerdigrisGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Slime.class, 10, true, true, null));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
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
        this.setHasOxidization(nbt.getBoolean("HasOxidization"));
        this.ticksSinceScraping = nbt.getInt("TicksSinceScraping");
        this.cannotEnterNestTicks = nbt.getInt("CannotEnterNestTicks");
        this.copperUpdatedSinceScraping = nbt.getInt("CopperUpdatedSinceScraping");

        this.readPersistentAngerSaveData(this.level(), nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        if (this.hasNest()) {
            nbt.put("nest_pos", NbtUtils.writeBlockPos(this.getNestPos()));
        }

        if (this.hasCopperBlock()) {
            nbt.put("copper_pos", NbtUtils.writeBlockPos(this.getCopperBlockPos()));
        }

        nbt.putBoolean("HasOxidization", this.hasOxidization());
        nbt.putInt("TicksSinceScraping", this.ticksSinceScraping);
        nbt.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
        nbt.putInt("CopperUpdatedSinceScraping", this.copperUpdatedSinceScraping);

        this.addPersistentAngerSaveData(nbt);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COPPERBUG_FLAGS, (byte) 0);
        builder.define(WARNING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hasOxidization() && this.getCopperOxidizedSinceScraping() < 10 && this.random.nextFloat() < 0.05F) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.addParticle(this.level(), this.getX() - 0.30000001192092896, this.getX() + 0.30000001192092896, this.getZ() - 0.30000001192092896, this.getZ() + 0.30000001192092896, this.getY(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }

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
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.cannotEnterNestTicks > 0) {
                --this.cannotEnterNestTicks;
            }

            if (this.ticksLeftToFindNest > 0) {
                --this.ticksLeftToFindNest;
            }

            if (this.ticksUntilCanScraping > 0) {
                --this.ticksUntilCanScraping;
            }
        }

    }

    private void addParticle(Level world, double lastX, double x, double lastZ, double z, double y, ParticleOptions effect) {
        world.addParticle(effect, Mth.lerp(world.random.nextDouble(), lastX, x), y, Mth.lerp(world.random.nextDouble(), lastZ, z), 0.0, 0.0, 0.0);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.warningAnimationProgress > 0.0F) {
            float f = this.warningAnimationProgress / 6.0F;
            float g = 1.0F + f;
            return super.getDefaultDimensions(pose).scale(1.0F, g);
        } else {
            return super.getDefaultDimensions(pose);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
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
            this.makeSound(BGSoundEvents.COPPERBUG_HURT_ADDITIONS_EVENT);
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

        if (!this.hasOxidization()) {
            ++this.ticksSinceScraping;
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level(), false);
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

    public boolean isTriggerItem() {
        if (this.isSpectator()) {
            return false;
        } else {
            boolean bl;
            bl = this.getItemBySlot(EquipmentSlot.MAINHAND).is(JamiesModTag.COPPER_BLOCKS)
                    && this.getItemBySlot(EquipmentSlot.MAINHAND).is(JamiesModTag.VERDAGRIS_ITEMS)
                    && this.getItemBySlot(EquipmentSlot.OFFHAND).is(JamiesModTag.COPPER_BLOCKS)
                    && this.getItemBySlot(EquipmentSlot.OFFHAND).is(JamiesModTag.VERDAGRIS_ITEMS);
            return bl;
        }
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

    private class AttackGoal extends MeleeAttackGoal {
        public AttackGoal() {
            super(CopperbugEntity.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                CopperbugEntity.this.setWarning(false);
            } else if (this.mob.distanceToSqr(target) < (double) ((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
                if (this.isTimeToAttack()) {
                    CopperbugEntity.this.setWarning(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    CopperbugEntity.this.setWarning(true);
                    CopperbugEntity.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                CopperbugEntity.this.setWarning(false);
            }

        }

        @Override
        public void stop() {
            CopperbugEntity.this.setWarning(false);
            super.stop();
        }
    }

    class IsCopperOrVerdigrisGoal extends NearestAttackableTargetGoal<Player> {
        public IsCopperOrVerdigrisGoal() {
            super(CopperbugEntity.this, Player.class, 20, true, true, null);
        }

        @Override
        public boolean canUse() {
            if (!CopperbugEntity.this.isBaby()) {
                if (super.canUse()) {
                    List<CopperbugEntity> list = CopperbugEntity.this.level().getEntitiesOfClass(CopperbugEntity.class, CopperbugEntity.this.getBoundingBox().inflate(8.0, 4.0, 8.0));

                    for (CopperbugEntity CopperbugEntity : list) {
                        if (CopperbugEntity.isTriggerItem()) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }


        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5;
        }
    }

    class CopperbugRevengeGoal extends HurtByTargetGoal {
        public CopperbugRevengeGoal() {
            super(CopperbugEntity.this);
        }

        @Override
        public void start() {
            super.start();
            if (CopperbugEntity.this.isTriggerItem()) {
                this.alertOthers();
                this.stop();
            }

        }

        @Override
        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof CopperbugEntity || !((CopperbugEntity) mob).isTriggerItem()) {
                super.alertOther(mob, target);
            }

        }
    }

    class ScrapeGoal extends Goal {

        private final Predicate<BlockState> copperPredicate = (state) -> {
            if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
                return false;
            } else return state.is(JamiesModTag.COPPER_BLOCKS_1);
        };
        private final CopperbugEntity bug;
        private final Level level;
        private int pollinationTicks;
        private int lastPollinationTick;
        private boolean running;
        @Nullable
        private Vec3 nextTarget;
        private int ticks;


        public ScrapeGoal(CopperbugEntity bug) {
            super();
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.bug = bug;
            this.level = bug.level();
        }

        @Override
        public boolean canUse() {
            if (CopperbugEntity.this.ticksUntilCanScraping > 0) {
                return false;
            } else if (CopperbugEntity.this.hasOxidization()) {
                return false;
            } else {
                Optional<BlockPos> optional = this.getFlower();
                if (optional.isPresent()) {
                    CopperbugEntity.this.copperPos = optional.get();
                    CopperbugEntity.this.navigation.moveTo((double) CopperbugEntity.this.copperPos.getX() + 0.5, (double) CopperbugEntity.this.copperPos.getY() + 0.5, (double) CopperbugEntity.this.copperPos.getZ() + 0.5, 1.2000000476837158);
                    return true;
                } else {
                    CopperbugEntity.this.ticksUntilCanScraping = Mth.nextInt(CopperbugEntity.this.random, 20, 60);
                    return false;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (!this.running) {
                return false;
            } else if (!CopperbugEntity.this.hasCopperBlock()) {
                return false;
            } else if (CopperbugEntity.this.level().isRaining()) {
                return false;
            } else if (this.completedPollination()) {
                return CopperbugEntity.this.random.nextFloat() < 0.2F;
            } else if (CopperbugEntity.this.tickCount % 20 == 0 && !CopperbugEntity.this.isCopperBlock(CopperbugEntity.this.copperPos)) {
                CopperbugEntity.this.copperPos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean completedPollination() {
            return this.pollinationTicks > 400;
        }

        boolean isRunning() {
            return this.running;
        }

        void cancel() {
            this.running = false;
        }

        @Override
        public void start() {
            this.pollinationTicks = 0;
            this.ticks = 0;
            this.lastPollinationTick = 0;
            this.running = true;
            CopperbugEntity.this.resetScrapingTicks();
        }

        @Override
        public void stop() {
            if (this.completedPollination()) {
                CopperbugEntity.this.setHasOxidization(true);
            }

            this.running = false;
            CopperbugEntity.this.navigation.stop();
            CopperbugEntity.this.ticksUntilCanScraping = 200;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            ++this.ticks;
            if (this.ticks > 600) {
                CopperbugEntity.this.copperPos = null;
            } else {
                assert CopperbugEntity.this.copperPos != null;
                Vec3 vec3d = Vec3.atBottomCenterOf(CopperbugEntity.this.copperPos).add(0.0, 0.6000000238418579, 0.0);
                if (vec3d.distanceTo(CopperbugEntity.this.position()) > 1.0) {
                    this.nextTarget = vec3d;
                    this.moveToNextTarget();
                } else {
                    if (this.nextTarget == null) {
                        this.nextTarget = vec3d;
                    }

                    boolean bl = CopperbugEntity.this.position().distanceTo(this.nextTarget) <= 0.1;
                    boolean bl2 = true;
                    if (!bl && this.ticks > 600) {
                        CopperbugEntity.this.copperPos = null;
                    } else {
                        if (bl) {
                            boolean bl3 = CopperbugEntity.this.random.nextInt(25) == 0;
                            if (bl3) {
                                this.nextTarget = new Vec3(vec3d.x() + (double) this.getRandomOffset(), vec3d.y(), vec3d.z() + (double) this.getRandomOffset());
                                CopperbugEntity.this.navigation.stop();
                            } else {
                                bl2 = false;
                            }

                            CopperbugEntity.this.getLookControl().setLookAt(vec3d.x(), vec3d.y(), vec3d.z());
                        }

                        if (bl2) {
                            this.moveToNextTarget();
                        }

                        ++this.pollinationTicks;
                        if (!this.level.isClientSide) {
                            if (CopperbugEntity.this.random.nextFloat() < 0.05F && this.pollinationTicks > this.lastPollinationTick + 60) {
                                this.lastPollinationTick = this.pollinationTicks;

                                Optional<BlockState> optional1 = WeatheringCopper.getPrevious(this.level.getBlockState(CopperbugEntity.this.copperPos));
                                if (optional1.isPresent()) {
                                    CopperbugEntity.this.playSound(BGSoundEvents.COPPERBUG_EAT_ADDITIONS_EVENT, 1.0F, 1.0F);
                                    this.level.levelEvent(3005, CopperbugEntity.this.copperPos, 0);
                                    level.setBlock(CopperbugEntity.this.copperPos, optional1.get(), 11);
                                    level.gameEvent(GameEvent.BLOCK_CHANGE, CopperbugEntity.this.copperPos, GameEvent.Context.of(CopperbugEntity.this, optional1.get()));
                                }
                            }
                        }

                    }
                }
            }
        }

        private void moveToNextTarget() {
            CopperbugEntity.this.getMoveControl().setWantedPosition(this.nextTarget.x(), this.nextTarget.y(), this.nextTarget.z(), 0.3499999940395355);
        }

        private float getRandomOffset() {
            return (CopperbugEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> getFlower() {
            return this.findFlower(this.copperPredicate, 5.0);
        }

        private Optional<BlockPos> findFlower(Predicate<BlockState> predicate, double searchDistance) {
            BlockPos blockPos = CopperbugEntity.this.blockPosition();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for (int i = 0; (double) i <= searchDistance; i = i > 0 ? -i : 1 - i) {
                for (int j = 0; (double) j < searchDistance; ++j) {
                    for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutable.setWithOffset(blockPos, k, i - 1, l);
                            if (blockPos.closerThan(mutable, searchDistance) && predicate.test(CopperbugEntity.this.level().getBlockState(mutable))) {
                                return Optional.of(mutable);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
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
