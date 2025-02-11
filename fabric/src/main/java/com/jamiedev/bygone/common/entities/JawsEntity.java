package com.jamiedev.bygone.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class JawsEntity extends Monster
{
JawsEntity ref;
    private boolean flopping;
    @Nullable
    protected RandomStrollGoal wanderGoal;
    
    public static final float field_30460 = 0.03F;
    boolean targetingUnderwater;


    public JawsEntity(EntityType<? extends JawsEntity> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new JawsEntity.JawsMoveControl(this);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, world);
        this.landNavigation = new GroundPathNavigation(this, world);
    }


    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation landNavigation;


    public static AttributeSupplier.Builder createJawsAttributes() {
        return Zombie.createAttributes().add(Attributes.STEP_HEIGHT, 1.0);
    }

    protected void initCustomGoals() {
        this.goalSelector.addGoal(1, new JawsEntity.WanderAroundOnSurfaceGoal(this, 1.0));
        //this.goalSelector.add(2, new JawsEntity.TridentAttackGoal(this, 1.0, 40, 10.0F));
        this.goalSelector.addGoal(2, new JawsEntity.JawsAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new JawsEntity.LeaveWaterGoal(this, 1.0));
        this.goalSelector.addGoal(6, new JawsEntity.TargetAboveWaterGoal(this, 1.0, this.level().getSeaLevel()));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, JawsEntity.class)).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::canDrownedAttackTarget));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Axolotl.class, true, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    private boolean canDrownedAttackTarget(LivingEntity target) {
        if (target != null) {
            return !this.level().isDay() || target.isInWater();
        } else {
            return false;
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        if (this.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && world.getRandom().nextFloat() < 0.03F) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.NAUTILUS_SHELL));
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        }

        return entityData;
    }



    public static boolean canSpawn(EntityType<JawsEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        if (!world.getFluidState(pos.below()).is(FluidTags.WATER) && !MobSpawnType.isSpawner(spawnReason)) {
            return false;
        } else {
            Holder<Biome> registryEntry = world.getBiome(pos);
            boolean bl = world.getDifficulty() != Difficulty.PEACEFUL && (MobSpawnType.ignoresLightRequirements(spawnReason) || isDarkEnoughToSpawn(world, pos, random)) && (MobSpawnType.isSpawner(spawnReason) || world.getFluidState(pos).is(FluidTags.WATER));
            if (bl && MobSpawnType.isSpawner(spawnReason)) {
                return true;
            } else if (registryEntry.is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
                return random.nextInt(15) == 0 && bl;
            } else {
                return random.nextInt(40) == 0 && isValidSpawnDepth(world, pos) && bl;
            }
        }
    }

    private static boolean isValidSpawnDepth(LevelAccessor world, BlockPos pos) {
        return pos.getY() < world.getSeaLevel() - 5;
    }

    protected boolean shouldBreakDoors() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.DROWNED_AMBIENT_WATER : SoundEvents.DROWNED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isInWater() ? SoundEvents.DROWNED_HURT_WATER : SoundEvents.DROWNED_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.DROWNED_DEATH_WATER : SoundEvents.DROWNED_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.DROWNED_STEP;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.DROWNED_SWIM;
    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance localDifficulty) {
        if ((double)random.nextFloat() > 0.9) {
            int i = random.nextInt(16);
            if (i < 10) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
        }

    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack newStack, ItemStack oldStack) {
        if (oldStack.is(Items.NAUTILUS_SHELL)) {
            return false;
        } else if (oldStack.is(Items.TRIDENT)) {
            if (newStack.is(Items.TRIDENT)) {
                return newStack.getDamageValue() < oldStack.getDamageValue();
            } else {
                return false;
            }
        } else {
            return newStack.is(Items.TRIDENT) || super.canReplaceCurrentItem(newStack, oldStack);
        }
    }

    protected boolean canConvertInWater() {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    public boolean canJawsAttackTarget(@Nullable LivingEntity target) {
        if (target != null) {
            return !this.level().isDay() || target.isInWater();
        } else {
            return false;
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    boolean isTargetingUnderwater() {
        if (this.targetingUnderwater) {
            return true;
        } else {
            LivingEntity livingEntity = this.getTarget();
            return livingEntity != null && livingEntity.isInWater();
        }
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isControlledByLocalInstance() && this.isInWater() && this.isTargetingUnderwater()) {
            this.moveRelative(0.01F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(movementInput);
        }

    }

    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isInWater() && this.isTargetingUnderwater()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.landNavigation;
                this.setSwimming(false);
            }
        }

    }

    @Override
    public boolean isVisuallySwimming() {
        return this.isSwimming();
    }

    protected boolean hasFinishedCurrentPath() {
        Path path = this.getNavigation().getPath();
        if (path != null) {
            BlockPos blockPos = path.getTarget();
            if (blockPos != null) {
                double d = this.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                return d < 4.0;
            }
        }

        return false;
    }

    public void shootAt(LivingEntity target, float pullProgress) {
        ThrownTrident tridentEntity = new ThrownTrident(this.level(), this, new ItemStack(Items.TRIDENT));
        double d = target.getX() - this.getX();
        double e = target.getY(0.3333333333333333) - tridentEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        tridentEntity.shoot(d, e + g * 0.20000000298023224, f, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(tridentEntity);
    }

    public void setTargetingUnderwater(boolean targetingUnderwater) {
        this.targetingUnderwater = targetingUnderwater;
    }

    private static class JawsMoveControl extends MoveControl {
        private final JawsEntity drowned;

        public JawsMoveControl(JawsEntity drowned) {
            super(drowned);
            this.drowned = drowned;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.drowned.getTarget();
            if (this.drowned.isTargetingUnderwater() && this.drowned.isInWater()) {
                if (livingEntity != null && livingEntity.getY() > this.drowned.getY() || this.drowned.targetingUnderwater) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, 0.002, 0.0));
                }

                if (this.operation != Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
                    this.drowned.setSpeed(0.0F);
                    return;
                }

                double d = this.wantedX - this.drowned.getX();
                double e = this.wantedY - this.drowned.getY();
                double f = this.wantedZ - this.drowned.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float)(Mth.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.drowned.setYRot(this.rotlerp(this.drowned.getYRot(), h, 90.0F));
                this.drowned.yBodyRot = this.drowned.getYRot();
                float i = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float j = Mth.lerp(0.125F, this.drowned.getSpeed(), i);
                this.drowned.setSpeed(j);
                this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add((double)j * d * 0.005, (double)j * e * 0.1, (double)j * f * 0.005));
            } else {
                if (!this.drowned.onGround()) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, -0.008, 0.0));
                }

                super.tick();
            }

        }
    }

    private static class WanderAroundOnSurfaceGoal extends Goal {
        private final PathfinderMob mob;
        private double x;
        private double y;
        private double z;
        private final double speed;
        private final Level world;

        public WanderAroundOnSurfaceGoal(PathfinderMob mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.world = mob.level();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.world.isDay()) {
                return false;
            } else if (this.mob.isInWater()) {
                return false;
            } else {
                Vec3 vec3d = this.getWanderTarget();
                if (vec3d == null) {
                    return false;
                } else {
                    this.x = vec3d.x;
                    this.y = vec3d.y;
                    this.z = vec3d.z;
                    return true;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.mob.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
        }

        @Nullable
        private Vec3 getWanderTarget() {
            RandomSource random = this.mob.getRandom();
            BlockPos blockPos = this.mob.blockPosition();

            for(int i = 0; i < 10; ++i) {
                BlockPos blockPos2 = blockPos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (this.world.getBlockState(blockPos2).is(Blocks.WATER)) {
                    return Vec3.atBottomCenterOf(blockPos2);
                }
            }

            return null;
        }
    }

    static class TridentAttackGoal extends RangedAttackGoal {
        private final JawsEntity drowned;

        public TridentAttackGoal(RangedAttackMob rangedAttackMob, double d, int i, float f) {
            super(rangedAttackMob, d, i, f);
            this.drowned = (JawsEntity)rangedAttackMob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT);
        }

        @Override
        public void start() {
            super.start();
            this.drowned.setAggressive(true);
            this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
        }

        @Override
        public void stop() {
            super.stop();
            this.drowned.stopUsingItem();
            this.drowned.setAggressive(false);
        }
    }

    private static class JawsAttackGoal extends MeleeAttackGoal {
        private final JawsEntity drowned;

        public JawsAttackGoal(JawsEntity drowned, double speed, boolean pauseWhenMobIdle) {
            super(drowned, speed, pauseWhenMobIdle);
            this.drowned = drowned;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.drowned.canJawsAttackTarget(this.drowned.getTarget());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.drowned.canJawsAttackTarget(this.drowned.getTarget());
        }
    }

    private static class LeaveWaterGoal extends MoveToBlockGoal {
        private final JawsEntity drowned;

        public LeaveWaterGoal(JawsEntity drowned, double speed) {
            super(drowned, speed, 8, 2);
            this.drowned = drowned;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.drowned.level().isDay() && this.drowned.isInWater() && this.drowned.getY() >= (double)(this.drowned.level().getSeaLevel() - 3);
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader world, BlockPos pos) {
            BlockPos blockPos = pos.above();
            return world.isEmptyBlock(blockPos) && world.isEmptyBlock(blockPos.above()) && world.getBlockState(pos).entityCanStandOn(world, pos, this.drowned);
        }

        @Override
        public void start() {
            this.drowned.setTargetingUnderwater(false);
            this.drowned.navigation = this.drowned.landNavigation;
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    private static class TargetAboveWaterGoal extends Goal {
        private final JawsEntity drowned;
        private final double speed;
        private final int minY;
        private boolean foundTarget;

        public TargetAboveWaterGoal(JawsEntity drowned, double speed, int minY) {
            this.drowned = drowned;
            this.speed = speed;
            this.minY = minY;
        }

        @Override
        public boolean canUse() {
            return !this.drowned.level().isDay() && this.drowned.isInWater() && this.drowned.getY() < (double)(this.minY - 2);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() && !this.foundTarget;
        }

        @Override
        public void tick() {
            if (this.drowned.getY() < (double)(this.minY - 1) && (this.drowned.getNavigation().isDone() || this.drowned.hasFinishedCurrentPath())) {
                Vec3 vec3d = DefaultRandomPos.getPosTowards(this.drowned, 4, 8, new Vec3(this.drowned.getX(), this.minY - 1, this.drowned.getZ()), 1.5707963705062866);
                if (vec3d == null) {
                    this.foundTarget = true;
                    return;
                }

                this.drowned.getNavigation().moveTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }

        }

        @Override
        public void start() {
            this.drowned.setTargetingUnderwater(true);
            this.foundTarget = false;
        }

        @Override
        public void stop() {
            this.drowned.setTargetingUnderwater(false);
        }
    }
}
