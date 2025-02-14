package com.jamiedev.bygone.entities;

import com.jamiedev.bygone.entities.projectile.ScuttleSpikeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ScuttleEntity extends WaterAnimal implements RangedAttackMob
{
    Guardian ref;
    @Nullable
    private LivingEntity cachedTarget;
    private static final EntityDataAccessor<Integer> TARGET_ID;
    int attackCooldown = 0;
    int attackAnimTick;
    private float tailAngle;
    private float prevTailAngle;
    private boolean flopping;

    public ScuttleEntity(EntityType<? extends ScuttleEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 15;
        this.moveControl = new ScuttleMoveControl(this);
        this.tailAngle = this.random.nextFloat();
        this.prevTailAngle = this.tailAngle;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 0.8F));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 0.5D, 20, 10.0F));
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0D, 1)
        );
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Squid.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Animal.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET_ID, 0);
    }


    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (TARGET_ID.equals(data)) {
           // this.cachedTarget = null;
        }
    }
    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        this.lookAt(this, 100, 100);
        this.yBodyRot = yBodyRotO;
        ScuttleSpikeEntity glass = new ScuttleSpikeEntity(this.level(), this, new ItemStack(Items.TRIDENT));
        double xDistance = target.getX() - this.getX();
        double yDistance = target.getY(0.3333333333333333D) - glass.getY();
        double zDistance = target.getZ() - this.getZ();
        double yMath = Math.sqrt((float) ((xDistance * xDistance) + (zDistance * zDistance)));
        glass.shoot(xDistance, yDistance + yMath * 0.10000000298023224D, zDistance, 1.6F, 11.0F);
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(glass);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        this.attackAnimTick = 10;
        this.level().broadcastEntityEvent(this, (byte)4);
        //boolean bl = super.tryAttack(target);
        float f = this.getAttackDamage();
        float f1 = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        boolean flag = target.hurt(damageSources().noAggroMobAttack(this), f1);

        if (flag)
        {
            Level var7 = this.level();
            ServerLevel serverWorld2 = (ServerLevel)var7;
            target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 0.4F, 0.0D));
            DamageSource damageSource = this.damageSources().mobAttack(this);
            EnchantmentHelper.doPostAttackEffects(serverWorld2, target, damageSource);

        }

        return flag;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public int getAttackAnimationTick() {
        return this.attackAnimTick;
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == 4) {
            this.attackAnimTick = 10;
        }
        super.handleEntityEvent(status);

    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new WaterBoundPathNavigation(this, world);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.attackCooldown > 0)
        {
            this.attackCooldown--;
        }

    }

    @Override
    public void travel(Vec3 movementInput) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, movementInput);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }
    void setProjTarget(int entityId) {
        this.entityData.set(TARGET_ID, entityId);
    }
    public boolean hasProjTarget() {
        return this.entityData.get(TARGET_ID) != 0;
    }

    @Nullable
    public LivingEntity getProjTarget() {
        if (!this.hasProjTarget()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.cachedTarget != null) {
                return this.cachedTarget;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(TARGET_ID));
                if (entity instanceof LivingEntity) {
                    this.cachedTarget = (LivingEntity)entity;
                    return this.cachedTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    @Override
    public void aiStep() {
        if (this.isAlive()) {
            if (this.level().isClientSide) {
                this.prevTailAngle = this.tailAngle;
                Vec3 vec3d;
                if (!this.isInWater()) {
                    vec3d = this.getDeltaMovement();
                    if (vec3d.y > 0.0 && this.flopping && !this.isSilent()) {
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), this.getFlopSound(), this.getSoundSource(), 1.0F, 1.0F, false);
                    }

                    this.flopping = vec3d.y < 0.0 && this.level().loadedAndEntityCanStandOn(this.blockPosition().below(), this);
                }

                this.tailAngle += 2.0F;


                if (this.isInWater()) {
                    vec3d = this.getViewVector(0.0F);

                    for(int i = 0; i < 2; ++i) {
                        this.level().addParticle(ParticleTypes.BUBBLE, this.getRandomX(0.5) - vec3d.x * 1.5, this.getRandomY() - vec3d.y * 1.5, this.getRandomZ(0.5) - vec3d.z * 1.5, 0.0, 0.0, 0.0);
                    }
                }

                if (this.hasProjTarget()) {


                    LivingEntity livingEntity = this.getProjTarget();
                    if (livingEntity != null) {
                        this.getLookControl().setLookAt(livingEntity, 90.0F, 90.0F);
                        this.getLookControl().tick();
                        double e = livingEntity.getX() - this.getX();
                        double f = livingEntity.getY(0.5) - this.getEyeY();
                        double g = livingEntity.getZ() - this.getZ();
                        double h = Math.sqrt(e * e + f * f + g * g);
                        e /= h;
                        f /= h;
                        g /= h;
                        double j = this.random.nextDouble();

                        while(j < h) {
                            this.level().addParticle(ParticleTypes.BUBBLE, this.getX() + e * j, this.getEyeY() + f * j, this.getZ() + g * j, 0.0, 0.0, 0.0);
                        }
                    }
                }
            }

            if (this.isInWaterOrBubble()) {
                this.setAirSupply(300);
            } else if (this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F, 0.5, (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F));
                this.setYRot(this.random.nextFloat() * 360.0F);
                this.setOnGround(false);
                this.hasImpulse = true;
            }

            if (this.hasProjTarget()) {
                this.setYRot(this.yHeadRot);
            }
        }

        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4000000059604645, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
            this.setOnGround(false);
            this.hasImpulse = true;
           // this.playSound(this.getFlopSound());
        }

        super.aiStep();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 260;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_AMBIENT : SoundEvents.GUARDIAN_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_HURT : SoundEvents.GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWaterOrBubble() ? SoundEvents.GUARDIAN_DEATH : SoundEvents.GUARDIAN_DEATH_LAND;
    }

    public float getTailAngle(float tickDelta) {
        return Mth.lerp(tickDelta, this.prevTailAngle, this.tailAngle);
    }


    public static boolean checkSurfaceWaterAnimalSpawnRule(EntityType<? extends WaterAnimal> entityType,
                                   LevelAccessor iServerWorld,
                                   MobSpawnType reason,
                                   BlockPos pos,
                                   RandomSource random) {
        return iServerWorld.getBlockState(pos).getFluidState().is(FluidTags.WATER)
                && iServerWorld.getBlockState(pos.above()).is(Blocks.WATER)
                && isLightLevelOk(pos, iServerWorld);
    }

    private static boolean isLightLevelOk(BlockPos pos, LevelAccessor iServerWorld) {
        int light = iServerWorld.getMaxLocalRawBrightness(pos);
        return light <= 4;
    }

    static class LookAtTargetGoal extends Goal {
        private final ScuttleEntity Spitter;

        public LookAtTargetGoal(ScuttleEntity Spitter) {
            this.Spitter = Spitter;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.Spitter.getTarget() == null) {
                Vec3 vec3d = this.Spitter.getDeltaMovement();
                this.Spitter.setYRot(-((float) Mth.atan2(vec3d.x, vec3d.z)) * 57.295776F);
                this.Spitter.yBodyRot = this.Spitter.getYRot();
            } else {
                LivingEntity livingEntity = this.Spitter.getTarget();
                double d = 64.0;
                if (livingEntity.distanceToSqr(this.Spitter) < 4096.0) {
                    double e = livingEntity.getX() - this.Spitter.getX();
                    double f = livingEntity.getZ() - this.Spitter.getZ();
                    this.Spitter.setYRot(-((float)Mth.atan2(e, f)) * 57.295776F);
                    this.Spitter.yBodyRot = this.Spitter.getYRot();
                }
            }

        }
    }

    protected boolean hasSelfControl() {
        return true;
    }

    static class ScuttleMoveControl extends MoveControl {
        private final ScuttleEntity guardian;

        public ScuttleMoveControl(ScuttleEntity guardian) {
            super(guardian);
            this.guardian = guardian;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO && !this.guardian.getNavigation().isDone()) {
                Vec3 vec3d = new Vec3(this.wantedX - this.guardian.getX(), this.wantedY - this.guardian.getY(), this.wantedZ - this.guardian.getZ());
                double d = vec3d.length();
                double e = vec3d.x / d;
                double f = vec3d.y / d;
                double g = vec3d.z / d;
                float h = (float)(Mth.atan2(vec3d.z, vec3d.x) * 57.2957763671875) - 90.0F;
                this.guardian.setYRot(this.rotlerp(this.guardian.getYRot(), h, 90.0F));
                this.guardian.yBodyRot = this.guardian.getYRot();
                float i = (float)(this.speedModifier * this.guardian.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float j = Mth.lerp(0.125F, this.guardian.getSpeed(), i);
                this.guardian.setSpeed(j);
                double k = Math.sin((double)(this.guardian.tickCount + this.guardian.getId()) * 0.5) * 0.05;
                double l = Math.cos(this.guardian.getYRot() * 0.017453292F);
                double m = Math.sin(this.guardian.getYRot() * 0.017453292F);
                double n = Math.sin((double)(this.guardian.tickCount + this.guardian.getId()) * 0.75) * 0.05;
                this.guardian.setDeltaMovement(this.guardian.getDeltaMovement().add(k * l, n * (m + l) * 0.25 + (double)j * f * 0.1, k * m));
                LookControl lookControl = this.guardian.getLookControl();
                double o = this.guardian.getX() + e * 2.0;
                double p = this.guardian.getEyeY() + f / d;
                double q = this.guardian.getZ() + g * 2.0;
                double r = lookControl.getWantedX();
                double s = lookControl.getWantedY();
                double t = lookControl.getWantedZ();
                if (!lookControl.isLookingAtTarget()) {
                    r = o;
                    s = p;
                    t = q;
                }

                this.guardian.getLookControl().setLookAt(Mth.lerp(0.125, r, o), Mth.lerp(0.125, s, p), Mth.lerp(0.125, t, q), 10.0F, 40.0F);

            } else {
                this.guardian.setSpeed(0.0F);

            }
        }
    }



    static {
        TARGET_ID = SynchedEntityData.defineId(ScuttleEntity.class, EntityDataSerializers.INT);
    }
}
