package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.client.particles.LithoParticleOptions;
import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class HauntEntity extends Allay {
    protected static final int ATTACK_TIME = 80;
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState floatAnimationState = new AnimationState();

    @Nullable
    private LivingEntity clientSideCachedAttackTarget;
    private int clientSideAttackTime;
    @Nullable
    protected RandomStrollGoal randomStrollGoal;

    public HauntEntity(EntityType<? extends HauntEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Allay.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }
    
    public void registerGoals()
    {
        super.registerGoals();
        this.randomStrollGoal = new RandomStrollGoal(this, (double)1.0F, 80);
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        }));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(BGBlocks.LITHOPLASMIC_POWDER.get());
        }));
        this.goalSelector.addGoal(4, new HauntEntityAttackGoal(this));
        this.goalSelector.addGoal(5, new HauntGotoTotemGoal(this, 1.0, 16));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, new HauntEntityAttackSelector(this)));
        this.randomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity living && !(living instanceof Player) && !(living instanceof SabeastEntity)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    private void setupAnimationStates() {

        this.idleAnimationState.startIfStopped(this.tickCount);
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F) {
            this.floatAnimationState.startIfStopped(this.tickCount);
        } else {
            this.floatAnimationState.stop();
        }
    }

    private boolean collidingHurtSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        });
    }

    private boolean collidingSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.SPECTRAL_BLOCKS);
        });
    }


    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

        if  (collidingHurtSpectralBlocks())
        {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
 
        }

        noPhysics = !collidingSpectralBlocks();

    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffect) {
        return !(potioneffect.is(MobEffects.POISON) || potioneffect.is(MobEffects.HARM)|| potioneffect.is(MobEffects.WITHER)) && super.canBeAffected(potioneffect);
    }

    public void aiStep()
    {
        super.aiStep();

        if (this.isAlive()) {
            if (this.level().isClientSide) {
                if (this.hasActiveAttackTarget()) {
                    if (this.clientSideAttackTime < this.getAttackDuration()) {
                        ++this.clientSideAttackTime;
                    }

                    LivingEntity livingentity = this.getActiveAttackTarget();
                    if (livingentity != null) {
                        this.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                        this.getLookControl().tick();
                        double d5 = (double)this.getAttackAnimationScale(0.0F);
                        double d0 = livingentity.getX() - this.getX();
                        double d1 = livingentity.getY((double)0.5F) - this.getEyeY();
                        double d2 = livingentity.getZ() - this.getZ();
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        double d4 = this.random.nextDouble();

                        while(d4 < d3) {
                            d4 += 1.8 - d5 + this.random.nextDouble() * (1.7 - d5);
                            this.level().addParticle(LithoParticleOptions.REDSTONE, this.getX() + d0 * d4, this.getEyeY() + d1 * d4, this.getZ() + d2 * d4, (double)0.0F, (double)0.0F, (double)0.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public static boolean canSpawn(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason, BlockPos blockPos, RandomSource random) {
        return level.getBlockState(blockPos.below()).is(JamiesModTag.HAUNT_SPAWNABLE_ON);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_ATTACK_TARGET, 0);
    }

    public int getAttackDuration() {
        return 80;
    }

    void setActiveAttackTarget(int activeAttackTargetId) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, activeAttackTargetId);
    }

    public boolean hasActiveAttackTarget() {
        return (Integer)this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level().getEntity((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity)entity;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_ID_ATTACK_TARGET.equals(key)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }

    }

    public float getAttackAnimationScale(float partialTick) {
        return ((float)this.clientSideAttackTime + partialTick) / (float)this.getAttackDuration();
    }

    public float getClientSideAttackTime() {
        return (float)this.clientSideAttackTime;
    }

    static {
        DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(HauntEntity.class,
                EntityDataSerializers.INT);
    }

    static class HauntEntityAttackGoal extends Goal {
        protected static final int ORBIT_PERIOD = 100;
        protected static final double ORBIT_RADIUS = 3.5;
        protected static final double ORBIT_HEIGHT = 1.5;
        protected static final double ORBIT_SPEED = 1.2;

        private final HauntEntity haunt;
        private int attackTime;

        public HauntEntityAttackGoal(HauntEntity haunt) {
            this.haunt = haunt;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.haunt.getTarget();
            return livingentity != null && livingentity.isAlive();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.haunt.getTarget() != null && this.haunt.distanceToSqr(this.haunt.getTarget()) > (double)9.0F);
        }

        public void start() {
            this.attackTime = -10;
            this.haunt.getNavigation().stop();
            LivingEntity livingentity = this.haunt.getTarget();
            if (livingentity != null) {
                this.haunt.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
            }

            this.haunt.hasImpulse = true;
        }

        public void stop() {
            this.haunt.setActiveAttackTarget(0);
            this.haunt.setTarget((LivingEntity)null);
            assert this.haunt.randomStrollGoal != null;
            this.haunt.randomStrollGoal.trigger();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.haunt.getTarget();
            if (livingentity != null) {
                this.haunt.getNavigation().stop();
                float orbitAngle = (this.haunt.tickCount % ORBIT_PERIOD) / (float) ORBIT_PERIOD * ((float) Math.PI * 2F);
                this.haunt.getMoveControl().setWantedPosition(
                        livingentity.getX() + Mth.cos(orbitAngle) * ORBIT_RADIUS,
                        livingentity.getY() + ORBIT_HEIGHT,
                        livingentity.getZ() + Mth.sin(orbitAngle) * ORBIT_RADIUS,
                        ORBIT_SPEED
                );
                this.haunt.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                if (!this.haunt.hasLineOfSight(livingentity)) {
                    this.haunt.setTarget((LivingEntity)null);
                } else {
                    ++this.attackTime;
                    if (this.attackTime == 0) {
                        this.haunt.setActiveAttackTarget(livingentity.getId());
                        if (!this.haunt.isSilent()) {
                            this.haunt.level().broadcastEntityEvent(this.haunt, (byte)121);
                        }
                    } else if (this.attackTime >= this.haunt.getAttackDuration()) {
                        float f = 1.0F;
                        if (this.haunt.level().getDifficulty() == Difficulty.HARD) {
                            f += 2.0F;
                        }
                        
                        livingentity.hurt(this.haunt.damageSources().indirectMagic(this.haunt, this.haunt), f);
                        haunt.heal(f);
                        this.haunt.doHurtTarget(livingentity);
                        this.haunt.setTarget((LivingEntity)null);
                    }

                    super.tick();
                }
            }

        }
    }

    static class HauntGotoTotemGoal extends MoveToBlockGoal {

        public HauntGotoTotemGoal(HauntEntity haunt, double speedModifier, int searchRange) {
            super(haunt, speedModifier, searchRange);
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return (state.is(BGBlocks.MEGALITH_TOTEM.get())) && level.isEmptyBlock(pos.above());
        }
    }

    static class HauntEntityAttackSelector implements Predicate<LivingEntity> {
        private final HauntEntity haunt;

        public HauntEntityAttackSelector(HauntEntity haunt) {
            this.haunt = haunt;
        }

        public boolean test(@Nullable LivingEntity entity) {
            return entity != null && !(entity instanceof HauntEntity)
                    && (entity.getType().is(JamiesModTag.SPECTRAL) || entity instanceof Vex)
                    && entity.distanceToSqr(this.haunt) > (double)9.0F;
        }
    }
}
