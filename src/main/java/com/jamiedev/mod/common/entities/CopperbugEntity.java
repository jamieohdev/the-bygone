package com.jamiedev.mod.common.entities;

import com.google.common.collect.Lists;
import com.jamiedev.mod.common.blocks.entity.CopperbugNestBlockEntity;
import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import com.jamiedev.mod.fabric.init.JamiesModPOI;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.NoWaterTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CopperbugEntity extends AnimalEntity implements Angerable
{
    private static final TrackedData<Byte> COPPERBUG_FLAGS;

    private static final TrackedData<Boolean> WARNING;
    private static final float field_30352 = 6.0F;
    private float lastWarningAnimationProgress;
    private float warningAnimationProgress;
    private int warningSoundCooldown;
    private static final UniformIntProvider ANGER_TIME_RANGE;
    private int angerTime;
    @Nullable
    private UUID angryAt;

    CopperbugEntity ref;

    private static final int HAS_OXIDIZATION_FLAG = 8;
    private static final int COPPER_NAVIGATION_START_TICKS = 2400;
    private static final int SCRAPING_FAIL_TICKS = 3600;
    private static final int MAX_SCRAPED_COPPER = 8;
    private static final int MIN_MAX_RETURN_DISTANCE = 16;

    public static final String COPPERS_UPDATED_SINCE_SCRAPING_KEY = "CoppersUpdatedSinceScraping";
    public static final String CANNOT_ENTER_NEST_TICKS_KEY = "CannotEnterNestTicks";
    public static final String TICKS_SINCE_SCRAPING_KEY = "TicksSinceScraping";

    public static final String HAS_OXIDIZATION_KEY = "HasOxidization";
    public static final String COPPER_POS_KEY = "copper_pos";
    public static final String NEST_POS_KEY = "nest_pos";

    int ticksSinceScraping;
    private int cannotEnterNestTicks;
    private int copperUpdatedSinceScraping;
    private static final int field_30274 = 200;
    int ticksLeftToFindNest;
    private static final int field_30275 = 200;
    int ticksUntilCanScraping;
    @Nullable
    BlockPos copperPos;
    @Nullable
    BlockPos nestPos;
    ScrapeGoal scrapeGoal;
    MoveToNestGoal moveToNestGoal;

    MoveToCopperGoal moveToCopperGoal;

    public CopperbugEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.ticksUntilCanScraping = MathHelper.nextInt(this.random, 20, 60);
    }

    public static DefaultAttributeContainer.Builder createCopperbugAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EnterNestGoal());
        this.goalSelector.add(1, new CopperbugEntity.AttackGoal());
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0, (polarBear) -> {
            return polarBear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES;
        }));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25));
        this.scrapeGoal = new ScrapeGoal();
        this.goalSelector.add(4, this.scrapeGoal);
        this.goalSelector.add(5, new WanderAroundGoal(this, 1.0));
        this.goalSelector.add(5, new FindNestGoal());
        this.moveToNestGoal = new MoveToNestGoal();
        this.goalSelector.add(5, this.moveToNestGoal);
        this.moveToCopperGoal = new MoveToCopperGoal();
        this.goalSelector.add(6, this.moveToCopperGoal);
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.goalSelector.add(7, new OxidizeCopperGoal());
        this.targetSelector.add(1, new CopperbugEntity.CopperbugRevengeGoal());
        this.targetSelector.add(2, new CopperbugEntity.IsCopperOrVerdigrisGoal());
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, SlimeEntity.class, 10, true, true, null));
        this.targetSelector.add(5, new UniversalAngerGoal<>(this, false));
    }

    @Debug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.nestPos = (BlockPos)NbtHelper.toBlockPos(nbt, "nest_pos").orElse((BlockPos) null);
        this.copperPos = (BlockPos)NbtHelper.toBlockPos(nbt, "copper_pos").orElse((BlockPos) null);
        super.readCustomDataFromNbt(nbt);
        this.setHasOxidization(nbt.getBoolean("HasOxidization"));
        this.ticksSinceScraping = nbt.getInt("TicksSinceScraping");
        this.cannotEnterNestTicks = nbt.getInt("CannotEnterNestTicks");
        this.copperUpdatedSinceScraping = nbt.getInt("CopperUpdatedSinceScraping");

        this.readAngerFromNbt(this.getWorld(), nbt);
    }



    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (this.hasNest())
        {
            nbt.put("nest_pos", NbtHelper.fromBlockPos(this.getNestPos()));
        }

        if (this.hasCopperBlock())
        {
            nbt.put("copper_pos", NbtHelper.fromBlockPos(this.getCopperBlockPos()));
        }

        nbt.putBoolean("HasOxidization", this.hasOxidization());
        nbt.putInt("TicksSinceScraping", this.ticksSinceScraping);
        nbt.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
        nbt.putInt("CopperUpdatedSinceScraping", this.copperUpdatedSinceScraping);

        this.writeAngerToNbt(nbt);
    }



    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COPPERBUG_FLAGS, (byte)0);
        builder.add(WARNING, false);
    }

    public void tick() {
        super.tick();

        if (this.hasOxidization() && this.getCopperOxidizedSinceScraping() < 10 && this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.addParticle(this.getWorld(), this.getX() - 0.30000001192092896, this.getX() + 0.30000001192092896, this.getZ() - 0.30000001192092896, this.getZ() + 0.30000001192092896, this.getBodyY(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }

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

    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient) {
            if (this.cannotEnterNestTicks > 0) {
                --this.cannotEnterNestTicks;
            }

            if (this.ticksLeftToFindNest > 0) {
                --this.ticksLeftToFindNest;
            }

            if (this.ticksUntilCanScraping > 0) {
                --this.ticksUntilCanScraping;
            }

            if (this.age % 20 == 0 && !this.isNestValid()) {
                this.nestPos = null;
            }
        }

    }

    private void addParticle(World world, double lastX, double x, double lastZ, double z, double y, ParticleEffect effect) {
        world.addParticle(effect, MathHelper.lerp(world.random.nextDouble(), lastX, x), y, MathHelper.lerp(world.random.nextDouble(), lastZ, z), 0.0, 0.0, 0.0);
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


    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
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

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundCooldown <= 0) {
            this.playSound(SoundEvents.ENTITY_ENDERMITE_HURT);
            this.warningSoundCooldown = 40;
        }

    }

    protected void tickWaterBreathingAir(int air) {
        this.setAir(300);

    }

    public void baseTick() {
        int i = this.getAir();
        super.baseTick();
        this.tickWaterBreathingAir(i);
    }

    protected void mobTick() {

        if (!this.hasOxidization()) {
            ++this.ticksSinceScraping;
        }

        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld)this.getWorld(), false);
        }

    }

    public boolean isPushedByFluids() {
        return false;
    }

    public boolean canBeLeashed() {
        return false;
    }

    public  boolean isTriggerItem()
    {
        if (this.isSpectator()) {
            return false;
        } else {
            boolean bl;
            bl = this.getEquippedStack(EquipmentSlot.MAINHAND).isIn(JamiesModTag.COPPER_BLOCKS)
                    && this.getEquippedStack(EquipmentSlot.MAINHAND).isIn(JamiesModTag.VERDAGRIS_ITEMS)
                    && this.getEquippedStack(EquipmentSlot.OFFHAND).isIn(JamiesModTag.COPPER_BLOCKS)
                    && this.getEquippedStack(EquipmentSlot.OFFHAND).isIn(JamiesModTag.VERDAGRIS_ITEMS);
            return bl;
        }
    }

    public static boolean canSpawn(EntityType<CopperbugEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isOf(Blocks.WATER) || world.getBlockState(pos.down()).isOf(Blocks.MOSS_BLOCK) || world.getBlockState(pos.down()).isOf(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
    }

    static {
        COPPERBUG_FLAGS = DataTracker.registerData(CopperbugEntity.class, TrackedDataHandlerRegistry.BYTE);
        WARNING = DataTracker.registerData(CopperbugEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    }
    

    boolean isCopperBlock(BlockPos pos) {
        return this.getWorld().canSetBlock(pos) && this.getWorld().getBlockState(pos).isIn(JamiesModTag.COPPER_BLOCKS_1);
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

    public List<BlockPos> getPossibleNests() {
        return this.moveToNestGoal.possibleNests;
    }

    private boolean failedScrapingTooLong() {
        return this.ticksSinceScraping > 3600;
    }

    public void resetScrapingTicks() {
        this.ticksSinceScraping = 0;
    }

    public void setNestPos(BlockPos pos) {
        this.nestPos = pos;
    }

    private @Nullable BlockPos getNestPos() {
        return this.nestPos;
    }

    private boolean hasNest() {
        return this.nestPos != null;
    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), (double)distance);
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


    boolean isNestValid() {
        if (!this.hasNest()) {
            return false;
        } else if (this.isTooFar(this.nestPos)) {
            return false;
        } else {
            BlockEntity blockEntity = this.getWorld().getBlockEntity(this.nestPos);
            return blockEntity != null && blockEntity.getType() == JamiesModBlockEntities.COPPERBUGNEST;
        }
    }

    private boolean doesNestHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.getWorld().getBlockEntity(pos);
        if (blockEntity instanceof CopperbugNestBlockEntity) {
            return !((CopperbugNestBlockEntity)blockEntity).isFullOfCopperbugs();
        } else {
            return false;
        }
    }


    boolean canEnterNest() {
        if (this.cannotEnterNestTicks <= 0 && !this.scrapeGoal.isRunning() && !this.hasAngerTime() &&
                this.getTarget() == null) {
            boolean bl = this.failedScrapingTooLong() || this.getWorld().isRaining() || this.getWorld().isNight() || this.hasOxidization();
            return bl && !this.isNestNearFire();
        } else {
            return false;
        }
    }

    private boolean isNestNearFire() {
        if (this.nestPos == null) {
            return false;
        } else {
            BlockEntity blockEntity = this.getWorld().getBlockEntity(this.nestPos);
            return blockEntity instanceof CopperbugNestBlockEntity && ((CopperbugNestBlockEntity)blockEntity).isNearFire();
        }
    }

    public void setCannotEnterNestTicks(int cannotEnterNestTicks) {
        this.cannotEnterNestTicks = cannotEnterNestTicks;
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
            this.dataTracker.set(COPPERBUG_FLAGS, (byte)((Byte)this.dataTracker.get(COPPERBUG_FLAGS) | bit));
        } else {
            this.dataTracker.set(COPPERBUG_FLAGS, (byte)((Byte)this.dataTracker.get(COPPERBUG_FLAGS) & ~bit));
        }

    }

    private boolean getCopperbugFlag(int location) {
        return ((Byte)this.dataTracker.get(COPPERBUG_FLAGS) & location) != 0;
    }


    private class AttackGoal extends MeleeAttackGoal {
        public AttackGoal() {
            super(CopperbugEntity.this, 1.25, true);
        }

        protected void attack(LivingEntity target) {
            if (this.canAttack(target)) {
                this.resetCooldown();
                this.mob.tryAttack(target);
                CopperbugEntity.this.setWarning(false);
            } else if (this.mob.squaredDistanceTo(target) < (double)((target.getWidth() + 3.0F) * (target.getWidth() + 3.0F))) {
                if (this.isCooledDown()) {
                    CopperbugEntity.this.setWarning(false);
                    this.resetCooldown();
                }

                if (this.getCooldown() <= 10) {
                    CopperbugEntity.this.setWarning(true);
                    CopperbugEntity.this.playWarningSound();
                }
            } else {
                this.resetCooldown();
                CopperbugEntity.this.setWarning(false);
            }

        }

        public void stop() {
            CopperbugEntity.this.setWarning(false);
            super.stop();
        }
    }

    class IsCopperOrVerdigrisGoal extends ActiveTargetGoal<PlayerEntity> {
        public IsCopperOrVerdigrisGoal() {
            super(CopperbugEntity.this, PlayerEntity.class, 20, true, true, null);
        }

        public boolean canStart() {
            if (!CopperbugEntity.this.isBaby()) {
                if (super.canStart()) {
                    List<CopperbugEntity> list = CopperbugEntity.this.getWorld().getNonSpectatingEntities(CopperbugEntity.class, CopperbugEntity.this.getBoundingBox().expand(8.0, 4.0, 8.0));

                    for (CopperbugEntity CopperbugEntity : list) {
                        if (CopperbugEntity.isTriggerItem()) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }


        protected double getFollowRange() {
            return super.getFollowRange() * 0.5;
        }
    }

    class CopperbugRevengeGoal extends RevengeGoal {
        public CopperbugRevengeGoal() {
            super(CopperbugEntity.this, new Class[0]);
        }

        public void start() {
            super.start();
            if (CopperbugEntity.this.isTriggerItem()) {
                this.callSameTypeForRevenge();
                this.stop();
            }

        }

        protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
            if (mob instanceof CopperbugEntity || !((CopperbugEntity) mob).isTriggerItem()) {
                super.setMobEntityTarget(mob, target);
            }

        }
    }
    
    class ScrapeGoal extends Goal {
        private static final int field_30300 = 400;
        private static final int field_30301 = 20;
        private static final int field_30302 = 60;
        private final Predicate<BlockState> copperPredicate = (state) -> {
            if (state.contains(Properties.WATERLOGGED) && (Boolean)state.get(Properties.WATERLOGGED)) {
                return false;
            } else return state.isIn(JamiesModTag.COPPER_BLOCKS_1);
        };
        private static final double field_30303 = 0.1;
        private static final int field_30304 = 25;
        private static final float field_30305 = 0.35F;
        private static final float field_30306 = 0.6F;
        private static final float field_30307 = 0.33333334F;
        private int pollinationTicks;
        private int lastPollinationTick;
        private boolean running;
        @Nullable
        private Vec3d nextTarget;
        private int ticks;
        private static final int field_30308 = 600;

        ScrapeGoal() {
            super();
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            if (CopperbugEntity.this.ticksUntilCanScraping > 0) {
                return false;
            } else if (CopperbugEntity.this.hasOxidization()) {
                return false;
            } else {
                Optional<BlockPos> optional = this.getFlower();
                if (optional.isPresent()) {
                    CopperbugEntity.this.copperPos = (BlockPos)optional.get();
                    CopperbugEntity.this.navigation.startMovingTo((double)CopperbugEntity.this.copperPos.getX() + 0.5, (double)CopperbugEntity.this.copperPos.getY() + 0.5, (double)CopperbugEntity.this.copperPos.getZ() + 0.5, 1.2000000476837158);
                    return true;
                } else {
                    CopperbugEntity.this.ticksUntilCanScraping = MathHelper.nextInt(CopperbugEntity.this.random, 20, 60);
                    return false;
                }
            }
        }

        public boolean shouldContinue() {
            if (!this.running) {
                return false;
            } else if (!CopperbugEntity.this.hasCopperBlock()) {
                return false;
            } else if (CopperbugEntity.this.getWorld().isRaining()) {
                return false;
            } else if (this.completedPollination()) {
                return CopperbugEntity.this.random.nextFloat() < 0.2F;
            } else if (CopperbugEntity.this.age % 20 == 0 && !CopperbugEntity.this.isCopperBlock(CopperbugEntity.this.copperPos)) {
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

        public void start() {
            this.pollinationTicks = 0;
            this.ticks = 0;
            this.lastPollinationTick = 0;
            this.running = true;
            CopperbugEntity.this.resetScrapingTicks();
        }

        public void stop() {
            if (this.completedPollination()) {
                CopperbugEntity.this.setHasOxidization(true);
            }

            this.running = false;
            CopperbugEntity.this.navigation.stop();
            CopperbugEntity.this.ticksUntilCanScraping = 200;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            ++this.ticks;
            if (this.ticks > 600) {
                CopperbugEntity.this.copperPos = null;
            } else {
                assert CopperbugEntity.this.copperPos != null;
                Vec3d vec3d = Vec3d.ofBottomCenter(CopperbugEntity.this.copperPos).add(0.0, 0.6000000238418579, 0.0);
                if (vec3d.distanceTo(CopperbugEntity.this.getPos()) > 1.0) {
                    this.nextTarget = vec3d;
                    this.moveToNextTarget();
                } else {
                    if (this.nextTarget == null) {
                        this.nextTarget = vec3d;
                    }

                    boolean bl = CopperbugEntity.this.getPos().distanceTo(this.nextTarget) <= 0.1;
                    boolean bl2 = true;
                    if (!bl && this.ticks > 600) {
                        CopperbugEntity.this.copperPos = null;
                    } else {
                        if (bl) {
                            boolean bl3 = CopperbugEntity.this.random.nextInt(25) == 0;
                            if (bl3) {
                                this.nextTarget = new Vec3d(vec3d.getX() + (double)this.getRandomOffset(), vec3d.getY(), vec3d.getZ() + (double)this.getRandomOffset());
                                CopperbugEntity.this.navigation.stop();
                            } else {
                                bl2 = false;
                            }

                            CopperbugEntity.this.getLookControl().lookAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());
                        }

                        if (bl2) {
                            this.moveToNextTarget();
                        }

                        ++this.pollinationTicks;
                        if (CopperbugEntity.this.random.nextFloat() < 0.05F && this.pollinationTicks > this.lastPollinationTick + 60) {
                            this.lastPollinationTick = this.pollinationTicks;
                            CopperbugEntity.this.playSound(SoundEvents.ITEM_AXE_SCRAPE, 1.0F, 1.0F);
                        }

                    }
                }
            }
        }

        private void moveToNextTarget() {
            CopperbugEntity.this.getMoveControl().moveTo(this.nextTarget.getX(), this.nextTarget.getY(), this.nextTarget.getZ(), 0.3499999940395355);
        }

        private float getRandomOffset() {
            return (CopperbugEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> getFlower() {
            return this.findFlower(this.copperPredicate, 5.0);
        }

        private Optional<BlockPos> findFlower(Predicate<BlockState> predicate, double searchDistance) {
            BlockPos blockPos = CopperbugEntity.this.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for(int i = 0; (double)i <= searchDistance; i = i > 0 ? -i : 1 - i) {
                for(int j = 0; (double)j < searchDistance; ++j) {
                    for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutable.set(blockPos, k, i - 1, l);
                            if (blockPos.isWithinDistance(mutable, searchDistance) && predicate.test(CopperbugEntity.this.getWorld().getBlockState(mutable))) {
                                return Optional.of(mutable);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }
    }
    
    class EnterNestGoal extends Goal {
        EnterNestGoal() {
            super();
        }

        public boolean canStart() {
            if (CopperbugEntity.this.hasNest() && CopperbugEntity.this.canEnterNest()) {
                assert CopperbugEntity.this.nestPos != null;
                if (CopperbugEntity.this.nestPos.isWithinDistance(CopperbugEntity.this.getPos(), 2.0)) {
                    BlockEntity blockEntity = CopperbugEntity.this.getWorld().getBlockEntity(CopperbugEntity.this.nestPos);
                    if (blockEntity instanceof CopperbugNestBlockEntity beenestBlockEntity) {
                        if (!beenestBlockEntity.isFullOfCopperbugs()) {
                            return true;
                        }

                        CopperbugEntity.this.nestPos = null;
                    }
                }
            }

            return false;
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            BlockEntity blockEntity = CopperbugEntity.this.getWorld().getBlockEntity(CopperbugEntity.this.nestPos);
            if (blockEntity instanceof CopperbugNestBlockEntity beenestBlockEntity) {
                beenestBlockEntity.tryEnterNest(CopperbugEntity.this);
            }

        }
    }

    private static void cleanOxidationAround(World world, BlockPos pos, BlockPos.Mutable mutablePos, int count) {
        mutablePos.set(pos);

        for(int i = 0; i < count; ++i) {
            Optional<BlockPos> optional = cleanOxidationAround(world, mutablePos);
            if (optional.isEmpty()) {
                break;
            }

            mutablePos.set((Vec3i)optional.get());
        }

    }

    private static Optional<BlockPos> cleanOxidationAround(World world, BlockPos pos) {
        Iterator var2 = BlockPos.iterateRandomly(world.random, 10, pos, 1).iterator();

        BlockPos blockPos;
        BlockState blockState;
        do {
            if (!var2.hasNext()) {
                return Optional.empty();
            }

            blockPos = (BlockPos)var2.next();
            blockState = world.getBlockState(blockPos);
        } while(!(blockState.getBlock() instanceof Oxidizable));

        BlockPos finalBlockPos = blockPos;
        Oxidizable.getDecreasedOxidationState(blockState).ifPresent((state) -> {
            world.setBlockState(finalBlockPos, state);
        });
        world.syncWorldEvent(3002, blockPos, -1);
        return Optional.of(blockPos);
    }
    
    class FindNestGoal extends Goal
    {

        FindNestGoal() {
            super();
        }

        public boolean canStart() {
            return CopperbugEntity.this.ticksLeftToFindNest == 0 && !CopperbugEntity.this.hasNest() && CopperbugEntity.this.canEnterNest();
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            CopperbugEntity.this.ticksLeftToFindNest = 200;
            List<BlockPos> list = this.getNearbyFreeNests();
            if (!list.isEmpty()) {
                Iterator var2 = list.iterator();

                BlockPos blockPos;
                do {
                    if (!var2.hasNext()) {
                        CopperbugEntity.this.moveToNestGoal.clearPossibleNests();
                        CopperbugEntity.this.nestPos = (BlockPos)list.get(0);
                        return;
                    }

                    blockPos = (BlockPos)var2.next();
                } while(CopperbugEntity.this.moveToNestGoal.isPossibleNest(blockPos));

                CopperbugEntity.this.nestPos = blockPos;
            }
        }

        private List getNearbyFreeNests() {
            BlockPos blockPos = CopperbugEntity.this.getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = ((ServerWorld)CopperbugEntity.this.getWorld()).getPointOfInterestStorage();
            Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle((poiType) -> {
                return poiType.isIn(JamiesModPOI.COPPERBUG_HOME);
            }, blockPos, 20, PointOfInterestStorage.OccupationStatus.ANY);
            return stream.map(PointOfInterest::getPos).filter(CopperbugEntity.this::doesNestHaveSpace).sorted(Comparator.comparingDouble((blockPos2) -> {
                return blockPos2.getSquaredDistance(blockPos);
            })).collect(Collectors.toList());
        }
    }
    
    class MoveToNestGoal extends Goal
    {
        public static final int field_30295 = 600;
        int ticks;
        private static final int field_30296 = 3;
        final List<BlockPos> possibleNests;
        @Nullable
        private Path path;
        private static final int field_30297 = 60;
        private int ticksUntilLost;

        MoveToNestGoal() {
            super();
            this.ticks = CopperbugEntity.this.getWorld().random.nextInt(10);
            this.possibleNests = Lists.newArrayList();
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return CopperbugEntity.this.nestPos != null && !CopperbugEntity.this.hasPositionTarget() && CopperbugEntity.this.canEnterNest() && !this.isCloseEnough(CopperbugEntity.this.nestPos) && CopperbugEntity.this.getWorld().getBlockState(CopperbugEntity.this.nestPos).isIn(JamiesModTag.COPPERBUGNESTS);
        }

        public boolean shouldContinue() {
            return this.canStart();
        }

        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            CopperbugEntity.this.navigation.stop();
            CopperbugEntity.this.navigation.resetRangeMultiplier();
        }

        public void tick() {
            if (CopperbugEntity.this.nestPos != null) {
                ++this.ticks;
                if (this.ticks > this.getTickCount(600)) {
                    this.makeChosenNestPossibleNest();
                } else if (!CopperbugEntity.this.navigation.isFollowingPath()) {
                    if (!CopperbugEntity.this.isWithinDistance(CopperbugEntity.this.nestPos, 16)) {
                        if (CopperbugEntity.this.isTooFar(CopperbugEntity.this.nestPos)) {
                            this.setLost();
                        } else {
                            CopperbugEntity.this.startMovingTo(CopperbugEntity.this.nestPos);
                        }
                    } else {
                        boolean bl = this.startMovingToFar(CopperbugEntity.this.nestPos);
                        if (!bl) {
                            this.makeChosenNestPossibleNest();
                        } else if (this.path != null && CopperbugEntity.this.navigation.getCurrentPath().equalsPath(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = CopperbugEntity.this.navigation.getCurrentPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            CopperbugEntity.this.navigation.setRangeMultiplier(10.0F);
            CopperbugEntity.this.navigation.startMovingTo((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 2, 1.0);
            return CopperbugEntity.this.navigation.getCurrentPath() != null && CopperbugEntity.this.navigation.getCurrentPath().reachesTarget();
        }

        boolean isPossibleNest(BlockPos pos) {
            return this.possibleNests.contains(pos);
        }

        private void addPossibleNest(BlockPos pos) {
            this.possibleNests.add(pos);

            while(this.possibleNests.size() > 3) {
                this.possibleNests.remove(0);
            }

        }

        void clearPossibleNests() {
            this.possibleNests.clear();
        }

        private void makeChosenNestPossibleNest() {
            if (CopperbugEntity.this.nestPos != null) {
                this.addPossibleNest(CopperbugEntity.this.nestPos);
            }

            this.setLost();
        }

        private void setLost() {
            CopperbugEntity.this.nestPos = null;
            CopperbugEntity.this.ticksLeftToFindNest = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            if (CopperbugEntity.this.isWithinDistance(pos, 2)) {
                return true;
            } else {
                Path path = CopperbugEntity.this.navigation.getCurrentPath();
                return path != null && path.getTarget().equals(pos) && path.reachesTarget() && path.isFinished();
            }
        }
    }

    private void startMovingTo(BlockPos pos) {
        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
        int i = 0;
        BlockPos blockPos = this.getBlockPos();
        int j = (int)vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.getManhattanDistance(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3d vec3d2 = NoWaterTargeting.find(this, k, l, i, vec3d, 0.3141592741012573);
        if (vec3d2 != null) {
            this.navigation.setRangeMultiplier(0.5F);
            this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
        }
    }

    class MoveToCopperGoal extends Goal
    {
        private static final int MAX_FLOWER_NAVIGATION_TICKS = 600;
        int ticks;

        MoveToCopperGoal() {
            super();
            this.ticks = CopperbugEntity.this.getWorld().random.nextInt(10);
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return CopperbugEntity.this.copperPos != null && !CopperbugEntity.this.hasPositionTarget() && this.shouldMoveToFlower() && CopperbugEntity.this.isCopperBlock(CopperbugEntity.this.copperPos) && !CopperbugEntity.this.isWithinDistance(CopperbugEntity.this.copperPos, 2);
        }

        public boolean shouldContinue() {
            return this.canStart();
        }

        public void start() {
            this.ticks = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            CopperbugEntity.this.navigation.stop();
            CopperbugEntity.this.navigation.resetRangeMultiplier();
        }

        public void tick() {
            if (CopperbugEntity.this.copperPos != null) {
                ++this.ticks;
                if (this.ticks > this.getTickCount(600)) {
                    CopperbugEntity.this.copperPos = null;
                } else if (!CopperbugEntity.this.navigation.isFollowingPath()) {
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
    
    class OxidizeCopperGoal extends Goal
    {
        static final int field_30299 = 30;

        OxidizeCopperGoal() {
            super();
        }

        public boolean canStart() {
            if (CopperbugEntity.this.getCopperOxidizedSinceScraping() >= 10) {
                return false;
            } else if (CopperbugEntity.this.random.nextFloat() < 0.3F) {
                return false;
            } else {
                return CopperbugEntity.this.hasOxidization() && CopperbugEntity.this.isNestValid();
            }
        }

        public boolean shouldContinue() {
            return this.canStart();
        }

        public void tick() {
        //    @Nullable PlayerEntity player = CopperbugEntity.this.attackingPlayer;

            if (CopperbugEntity.this.random.nextInt(this.getTickCount(30)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = CopperbugEntity.this.getBlockPos().down(i);
                    BlockState blockState = CopperbugEntity.this.getWorld().getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    BlockState blockState2 = null;
                    if (blockState.isIn(JamiesModTag.COPPER_BLOCKS_1)) {
                        Optional<BlockState> optional2 = Oxidizable.getDecreasedOxidationState(blockState);

                        if (block instanceof OxidizableBlock copperBlock) {
                            if (optional2.isPresent()) {
                                CopperbugEntity.this.getWorld().playSound((PlayerEntity)null, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                CopperbugEntity.this.getWorld().syncWorldEvent((PlayerEntity)null, 3005, blockPos, 0);
                               // optional2;
                            }

                            if (!(copperBlock.getDegradationLevel() == Oxidizable.OxidationLevel.OXIDIZED))
                            {
                                 copperBlock.getDegradationLevel();

                            }
                        }

                        if (block instanceof CropBlock) {
                            CropBlock cropBlock = (CropBlock)block;
                            if (!cropBlock.isMature(blockState)) {
                                blockState2 = cropBlock.withAge(cropBlock.getAge(blockState) + 1);
                            }
                        } else {
                            int j;
                            if (block instanceof StemBlock) {
                                j = (Integer)blockState.get(StemBlock.AGE);
                                if (j < 7) {
                                    blockState2 = (BlockState)blockState.with(StemBlock.AGE, j + 1);
                                }
                            } else if (blockState.isOf(Blocks.SWEET_BERRY_BUSH)) {
                                j = (Integer)blockState.get(SweetBerryBushBlock.AGE);
                                if (j < 3) {
                                    blockState2 = (BlockState)blockState.with(SweetBerryBushBlock.AGE, j + 1);
                                }
                            } else if (blockState.isOf(Blocks.CAVE_VINES) || blockState.isOf(Blocks.CAVE_VINES_PLANT)) {
                                ((Fertilizable)blockState.getBlock()).grow((ServerWorld)CopperbugEntity.this.getWorld(), CopperbugEntity.this.random, blockPos, blockState);
                            }
                        }

                        if (blockState2 != null) {
                            CopperbugEntity.this.getWorld().syncWorldEvent(5011, blockPos, 15);
                            CopperbugEntity.this.getWorld().setBlockState(blockPos, blockState2);
                            CopperbugEntity.this.addCopperCounter();
                        }
                    }
                }

            }
        }
    }
}
