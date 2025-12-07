package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.IntFunction;

public class WraithEntity extends Monster implements RangedAttackMob, FlyingAnimal {
    public static final int SPELL_PARTICLES_PER_HAND_PER_TICK = 1;
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID;
    private static final EntityDataAccessor<Boolean> DATA_PREPARE_TELEPORT;

    static {
        DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(WraithEntity.class, EntityDataSerializers.BYTE);
        DATA_PREPARE_TELEPORT = SynchedEntityData.defineId(WraithEntity.class, EntityDataSerializers.BOOLEAN);
    }

    public AnimationState floatAnimationState = new AnimationState();
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState meleeAnimationState = new AnimationState();
    public AnimationState spellAnimationState = new AnimationState();
    protected int withinRangeToTeleportTick = 0;
    protected int spellCastingTickCount;
    protected BlockPos targetSavedPos = BlockPos.ZERO;
    private WraithEntity.WraithSpell currentSpell;

    public WraithEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.moveControl = new FlyingMoveControl(this, 35, false);
        this.setNoGravity(true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
        this.currentSpell = WraithSpell.NONE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.9)
                .add(Attributes.FOLLOW_RANGE, 18.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MAX_HEALTH, 32.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpellcasterCastingSpellGoal());
        this.goalSelector.addGoal(2, new WraithFireSquareSpellGoal());
        this.goalSelector.addGoal(2, new WraithEntity.WraithTeleportSpellGoal());
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.1, true));

        this.goalSelector.addGoal(8, new WraithEntity.WraithWanderGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WraithEntity.class).setAlertOthers());
        this.targetSelector.addGoal(
                2,
                new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300)
        );
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {
            @Override
            public void tick() {
                super.tick();
            }

            @Override
            public boolean isStableDestination(@NotNull BlockPos pos) {
                return this.level.getBlockState(pos).isAir();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SPELL_CASTING_ID, (byte) 0);
        builder.define(DATA_PREPARE_TELEPORT, false);
    }

    public void tick() {
        this.setNoGravity(true);
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

        // Creates the particles for casting the spell.
        if (this.level().isClientSide && this.isCastingSpell()) {
            WraithEntity.WraithSpell spell = this.getCurrentSpell();
            float r = (float) spell.spellColor[0];
            float g = (float) spell.spellColor[1];
            float b = (float) spell.spellColor[2];
            float swayAngle = this.yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
            float swayX = Mth.cos(swayAngle);
            float swayZ = Mth.sin(swayAngle);
            double radius = 0.6 * (double) this.getScale();
            double height = 1.8 * (double) this.getScale();

            for (int i = 0; i < SPELL_PARTICLES_PER_HAND_PER_TICK; i++) {
                this.level().addParticle(
                        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, r, g, b),
                        this.getX() + (double) swayX * radius,
                        this.getY() + height,
                        this.getZ() + (double) swayZ * radius,
                        0.0F,
                        0.0F,
                        0.0F
                );
            }
            for (int i = 0; i < SPELL_PARTICLES_PER_HAND_PER_TICK; i++) {
                this.level().addParticle(
                        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, r, g, b),
                        this.getX() - (double) swayX * radius,
                        this.getY() + height,
                        this.getZ() - (double) swayZ * radius,
                        0.0F,
                        0.0F,
                        0.0F
                );
            }

        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.WRAITH_AMBIENT_ADDITIONS_EVENT;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("SpellTicks", this.spellCastingTickCount);
        if (this.targetSavedPos != BlockPos.ZERO) {
            compound.putInt("TargetPosX", this.targetSavedPos.getX());
            compound.putInt("TargetPosY", this.targetSavedPos.getY());
            compound.putInt("TargetPosZ", this.targetSavedPos.getZ());
        }
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.spellCastingTickCount = compound.getInt("SpellTicks");

        if (compound.contains("TargetPosX") && compound.contains("TargetPosY") && compound.contains("TargetPosZ")) {
            this.targetSavedPos = new BlockPos(
                    compound.getInt("TargetPosX"),
                    compound.getInt("TargetPosY"),
                    compound.getInt("TargetPosZ")
            );

        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellCastingTickCount > 0) {
            --this.spellCastingTickCount;
            if (this.spellCastingTickCount == 0) {
                this.setIsCastingSpell(WraithSpell.NONE);
            }
        }
    }

    @Override
    public void playAttackSound() {
        this.playSound(BGSoundEvents.WRAITH_ATTACK_ADDITIONS_EVENT, 1.0F, 1.0F);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity livingEntity, float v) {

    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return BGSoundEvents.WRAITH_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return BGSoundEvents.WRAITH_DEATH_ADDITIONS_EVENT;
    }

    public boolean isCastingSpell() {
        return this.level().isClientSide ? this.entityData.get(DATA_SPELL_CASTING_ID) > 0 : this.spellCastingTickCount > 0;
    }

    public void setIsCastingSpell(WraithEntity.WraithSpell currentSpell) {
        this.currentSpell = currentSpell;
        this.entityData.set(DATA_SPELL_CASTING_ID, (byte) currentSpell.id);
    }

    protected WraithEntity.WraithSpell getCurrentSpell() {
        return !this.level().isClientSide ? this.currentSpell : WraithEntity.WraithSpell.byId(this.entityData.get(
                DATA_SPELL_CASTING_ID));
    }

    private void setupAnimationStates() {

        this.idleAnimationState.startIfStopped(this.tickCount);
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F) {
            this.floatAnimationState.startIfStopped(this.tickCount);
        } else {
            this.floatAnimationState.stop();
        }

        if (this.isCastingSpell()) {
            this.spellAnimationState.startIfStopped(this.tickCount);
        } else {
            if (this.attackAnim > 0) {
                this.meleeAnimationState.start(this.tickCount);
            } else if (this.attackAnim == 0) {
                this.meleeAnimationState.stop();
            }
        }
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isFreezing() {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {

        if (source.is(DamageTypeTags.IS_FREEZING)) {
            return super.hurt(source, 0);
        }

        if (source.isDirect()) {
            this.withinRangeToTeleportTick = Math.max(this.withinRangeToTeleportTick - 10, 0);
        }

        if (source.is(DamageTypes.IN_FIRE)) {
            return super.hurt(source, 0);
        }

        if (source.is(DamageTypes.ON_FIRE)) {
            return super.hurt(source, 0);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(strength, x, z);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            super.travel(travelVector);
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    protected int getSpellCastingTime() {
        return this.spellCastingTickCount;
    }


    protected SoundEvent getCastingSoundEvent() {
        return BGSoundEvents.WRAITH_ATTACK_ADDITIONS_EVENT;
    }

    public enum WraithSpell {
        NONE(0, 0.0F, 0.0F, 0.0F),
        TELEPORT(1, 0.7, 0.7, 0.8),
        FIRE(2, 0.4, 0.3, 0.35),
        NOVELTY(3, 0.7, 0.5F, 0.2),
        DISAPPEAR(4, 0.3, 0.3, 0.8),
        PUKE(5, 0.1, 0.1, 0.2);

        private static final IntFunction<WraithEntity.WraithSpell> BY_ID = ByIdMap.continuous(
                (spell) -> spell.id,
                values(),
                ByIdMap.OutOfBoundsStrategy.ZERO
        );
        final int id;
        final double[] spellColor;

        WraithSpell(int id, double red, double green, double blue) {
            this.id = id;
            this.spellColor = new double[]{red, green, blue};
        }

        public static WraithEntity.WraithSpell byId(int id) {
            return BY_ID.apply(id);
        }
    }

    public static class WraithWanderGoal extends WaterAvoidingRandomFlyingGoal {

        public static final int DESTINATIONS_TO_CHECK = 10;

        public WraithWanderGoal(PathfinderMob mob, double speed) {
            super(mob, speed);
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            RandomSource random = this.mob.getRandom();
            Level level = this.mob.level();
            BlockPos mobPos = this.mob.blockPosition();

            for (int i = 0; i < DESTINATIONS_TO_CHECK; i++) {
                int dx = Mth.nextInt(random, -10, 10);
                int dy = Mth.nextInt(random, -12, 13);
                int dz = Mth.nextInt(random, -10, 10);

                BlockPos candidate = mobPos.offset(dx, dy, dz);
                BlockPos ground = candidate.below();

                BlockState state = level.getBlockState(ground);
                for (int checkGround = 1; checkGround <= 2; checkGround++) {
                    BlockState checkState = level.getBlockState(new BlockPos(
                            ground.getX(),
                            ground.getY() + checkGround,
                            ground.getZ()
                    ));
                    if (!checkState.isAir()) {
                        break;
                    }
                }
                if (state.isFaceSturdy(
                        level,
                        ground,
                        Direction.DOWN
                ) && !state.isAir() && level.isEmptyBlock(candidate) && level.isEmptyBlock(candidate.above())) {
                    return Vec3.atCenterOf(candidate);
                }
            }

            return null; // No position found
        }

    }

    protected class SpellcasterCastingSpellGoal extends Goal {
        public SpellcasterCastingSpellGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return WraithEntity.this.getSpellCastingTime() > 0;
        }

        public void start() {
            super.start();
            WraithEntity.this.navigation.stop();
        }

        public void stop() {
            super.stop();
            WraithEntity.this.setIsCastingSpell(WraithEntity.WraithSpell.NONE);
        }

        public void tick() {
            if (WraithEntity.this.getTarget() != null) {
                WraithEntity.this.getLookControl().setLookAt(
                        WraithEntity.this.getTarget(),
                        (float) WraithEntity.this.getMaxHeadYRot(),
                        (float) WraithEntity.this.getMaxHeadXRot()
                );
            }

        }
    }

    protected abstract class SpellcasterUseSpellGoal extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        protected SpellcasterUseSpellGoal() {
        }

        public boolean canUse() {
            LivingEntity target = WraithEntity.this.getTarget();
            if (target != null && target.isAlive()) {
                return !WraithEntity.this.isCastingSpell() && WraithEntity.this.tickCount >= this.nextAttackTickCount;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = WraithEntity.this.getTarget();
            return livingentity != null && livingentity.isAlive() && this.attackWarmupDelay > 0;
        }

        public void start() {
            this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
            WraithEntity.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = WraithEntity.this.tickCount + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                WraithEntity.this.playSound(soundevent, 1.0F, 1.0F);
            }

            WraithEntity.this.setIsCastingSpell(this.getSpell());
        }

        public void tick() {
            this.attackWarmupDelay--;
            if (this.attackWarmupDelay == 0) {
                this.performSpellCasting();
                WraithEntity.this.playSound(WraithEntity.this.getCastingSoundEvent(), 1.0F, 1.0F);
            }

        }

        protected abstract void performSpellCasting();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract WraithEntity.WraithSpell getSpell();
    }

    public class WraithTeleportSpellGoal extends SpellcasterUseSpellGoal {

        public boolean canUse() {
            return super.canUse() && ((WraithEntity.this.getTarget() != null) && (WraithEntity.this.distanceTo(
                    WraithEntity.this.getTarget()) < 6 && !WraithEntity.this.entityData.get(DATA_PREPARE_TELEPORT)));
        }

        @Override
        public void start() {
            super.start();
            WraithEntity.this.entityData.set(DATA_PREPARE_TELEPORT, true);
            WraithEntity.this.withinRangeToTeleportTick = 0;
        }

        @Override
        protected void performSpellCasting() {

            LivingEntity target = Objects.requireNonNull(WraithEntity.this.getTarget());

            BlockPos targetOnPos = target.getOnPos();
            RandomSource random = WraithEntity.this.random;
            Level level = WraithEntity.this.level();

            for (int check = 0; check < 10; check++) {
                int x = targetOnPos.getX();
                if (random.nextBoolean()) {
                    x -= random.nextInt(7, 11);
                } else {
                    x += random.nextInt(7, 11);
                }
                int z = targetOnPos.getZ();
                if (random.nextBoolean()) {
                    z -= random.nextInt(7, 11);
                } else {
                    z += random.nextInt(7, 11);
                }

                int y = targetOnPos.getY();
                BlockPos groundPos = new BlockPos(x, y, z);

                if (level
                        .getBlockState(groundPos)
                        .isFaceSturdy(level, groundPos, Direction.DOWN) && level
                        .getBlockState(groundPos.above())
                        .isAir() && level.getBlockState(groundPos.above().above()).isAir()) {
                    WraithEntity.this.teleportTo(x, y + 1, z);
                    break;
                } else {
                    boolean teleported = false;
                    for (int checkY = -4; checkY <= 4; checkY++) {
                        BlockPos newGroundPos = groundPos.offset(0, checkY, 0);
                        if (level
                                .getBlockState(newGroundPos)
                                .isFaceSturdy(level, newGroundPos, Direction.DOWN) && level
                                .getBlockState(newGroundPos.above())
                                .isAir() && level.getBlockState(newGroundPos.above().above()).isAir()) {
                            target.teleportTo(x, y + 1, z);
                            level.playSound(
                                    null,
                                    WraithEntity.this.xo,
                                    WraithEntity.this.yo,
                                    WraithEntity.this.zo,
                                    BGSoundEvents.WRAITH_TELEPORT_ADDITIONS_EVENT,
                                    WraithEntity.this.getSoundSource(),
                                    1.0F,
                                    1.0F
                            );
                            WraithEntity.this.playSound(BGSoundEvents.WRAITH_TELEPORT_ADDITIONS_EVENT, 1.0F, 1.0F);
                            teleported = true;
                            break;
                        }
                    }
                    if (teleported) {
                        break;
                    }
                }
            }

        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 120;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return null;
        }

        @Override
        protected WraithSpell getSpell() {
            return WraithSpell.TELEPORT;
        }

        @Override
        public void stop() {
            super.stop();
            WraithEntity.this.entityData.set(DATA_PREPARE_TELEPORT, false);
        }
    }

    public class WraithFireSquareSpellGoal extends SpellcasterUseSpellGoal {
        public static final int FLAME_HORIZONTAL_RADIUS = 1;
        public static final int FLAME_VERTICAL_RADIUS = 2;

        @Override
        public void start() {
            super.start();
            WraithEntity.this.targetSavedPos = Objects.requireNonNull(WraithEntity.this.getTarget()).getOnPos();
        }

        @Override
        protected void performSpellCasting() {
            Level level = WraithEntity.this.level();
            if (WraithEntity.this.targetSavedPos != BlockPos.ZERO) {
                BlockPos targetPos = WraithEntity.this.targetSavedPos.above();

                for (int checkZ = -FLAME_HORIZONTAL_RADIUS; checkZ <= FLAME_HORIZONTAL_RADIUS; checkZ++) {
                    for (int checkX = -FLAME_HORIZONTAL_RADIUS; checkX <= FLAME_HORIZONTAL_RADIUS; checkX++) {
                        for (int checkY = FLAME_VERTICAL_RADIUS; checkY >= -FLAME_VERTICAL_RADIUS; checkY--) {
                            BlockPos newYPos = targetPos.offset(checkX, checkY, checkZ);
                            if (level.getBlockState(newYPos).isAir() && level.getBlockState(newYPos.below())
                                    .isFaceSturdy(level, newYPos.below(), Direction.UP)) {
                                WraithEntity.this.level()
                                        .setBlockAndUpdate(newYPos, BGBlocks.ICE_BOUQUET.get().defaultBlockState());
                                break;
                            }
                        }
                    }
                }
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return null;
        }

        @Override
        protected WraithSpell getSpell() {
            return WraithSpell.FIRE;
        }

        @Override
        public void stop() {
            super.stop();
            // Reset the saved target position to empty.
            WraithEntity.this.targetSavedPos = BlockPos.ZERO;

        }
    }

}
