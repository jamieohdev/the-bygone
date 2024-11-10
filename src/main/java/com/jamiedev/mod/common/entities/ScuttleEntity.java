package com.jamiedev.mod.common.entities;

import com.jamiedev.mod.common.entities.projectile.ScuttleSpikeEntity;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ScuttleEntity extends WaterCreatureEntity implements RangedAttackMob
{
    GuardianEntity ref;
    @Nullable
    private LivingEntity cachedTarget;
    private static final TrackedData<Integer> TARGET_ID;
    int attackCooldown = 0;
    int attackAnimTick;
    private float tailAngle;
    private float prevTailAngle;
    private boolean flopping;

    public ScuttleEntity(EntityType<? extends ScuttleEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 15;
        this.moveControl = new ScuttleMoveControl(this);
        this.tailAngle = this.random.nextFloat();
        this.prevTailAngle = this.tailAngle;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new MoveIntoWaterGoal(this));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 0.8F));
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 0.5D, 20, 10.0F));
        this.goalSelector.add(3, new SwimAroundGoal(this, 1.0D, 1)
        );
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, SquidEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, AnimalEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));

    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0D);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(TARGET_ID, 0);
    }


    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (TARGET_ID.equals(data)) {
           // this.cachedTarget = null;
        }
    }
    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        this.lookAtEntity(this, 100, 100);
        this.bodyYaw = prevBodyYaw;
        ScuttleSpikeEntity glass = new ScuttleSpikeEntity(this.getWorld(), this, new ItemStack(Items.TRIDENT));
        double xDistance = target.getX() - this.getX();
        double yDistance = target.getBodyY(0.3333333333333333D) - glass.getY();
        double zDistance = target.getZ() - this.getZ();
        double yMath = Math.sqrt((float) ((xDistance * xDistance) + (zDistance * zDistance)));
        glass.setVelocity(xDistance, yDistance + yMath * 0.10000000298023224D, zDistance, 1.6F, 11.0F);
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(glass);
    }

    @Override
    public boolean tryAttack(Entity target) {
        this.attackAnimTick = 10;
        this.getWorld().sendEntityStatus(this, (byte)4);
        //boolean bl = super.tryAttack(target);
        float f = this.getAttackDamage();
        float f1 = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        boolean flag = target.damage(getDamageSources().mobAttackNoAggro(this), f1);

        if (flag)
        {
            World var7 = this.getWorld();
            ServerWorld serverWorld2 = (ServerWorld)var7;
            target.setVelocity(target.getVelocity().add(0.0D, (double)0.4F, 0.0D));
            DamageSource damageSource = this.getDamageSources().mobAttack(this);
            EnchantmentHelper.onTargetDamaged(serverWorld2, target, damageSource);

        }

        return flag;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    public int getAttackAnimationTick() {
        return this.attackAnimTick;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 4) {
            this.attackAnimTick = 10;
        }
        super.handleStatus(status);

    }

    protected EntityNavigation createNavigation(World world) {
        return new SwimNavigation(this, world);
    }

    public void tick()
    {
        super.tick();

        if (this.attackCooldown > 0)
        {
            this.attackCooldown--;
        }

    }

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(0.01F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }

    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }
    void setProjTarget(int entityId) {
        this.dataTracker.set(TARGET_ID, entityId);
    }
    public boolean hasProjTarget() {
        return (Integer)this.dataTracker.get(TARGET_ID) != 0;
    }

    @Nullable
    public LivingEntity getProjTarget() {
        if (!this.hasProjTarget()) {
            return null;
        } else if (this.getWorld().isClient) {
            if (this.cachedTarget != null) {
                return this.cachedTarget;
            } else {
                Entity entity = this.getWorld().getEntityById((Integer)this.dataTracker.get(TARGET_ID));
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

    public void tickMovement() {
        if (this.isAlive()) {
            if (this.getWorld().isClient) {
                this.prevTailAngle = this.tailAngle;
                Vec3d vec3d;
                if (!this.isTouchingWater()) {
                    vec3d = this.getVelocity();
                    if (vec3d.y > 0.0 && this.flopping && !this.isSilent()) {
                        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), this.getFlopSound(), this.getSoundCategory(), 1.0F, 1.0F, false);
                    }

                    this.flopping = vec3d.y < 0.0 && this.getWorld().isTopSolid(this.getBlockPos().down(), this);
                }

                this.tailAngle += 2.0F;


                if (this.isTouchingWater()) {
                    vec3d = this.getRotationVec(0.0F);

                    for(int i = 0; i < 2; ++i) {
                        this.getWorld().addParticle(ParticleTypes.BUBBLE, this.getParticleX(0.5) - vec3d.x * 1.5, this.getRandomBodyY() - vec3d.y * 1.5, this.getParticleZ(0.5) - vec3d.z * 1.5, 0.0, 0.0, 0.0);
                    }
                }

                if (this.hasProjTarget()) {


                    LivingEntity livingEntity = this.getProjTarget();
                    if (livingEntity != null) {
                        this.getLookControl().lookAt(livingEntity, 90.0F, 90.0F);
                        this.getLookControl().tick();
                        double e = livingEntity.getX() - this.getX();
                        double f = livingEntity.getBodyY(0.5) - this.getEyeY();
                        double g = livingEntity.getZ() - this.getZ();
                        double h = Math.sqrt(e * e + f * f + g * g);
                        e /= h;
                        f /= h;
                        g /= h;
                        double j = this.random.nextDouble();

                        while(j < h) {
                            this.getWorld().addParticle(ParticleTypes.BUBBLE, this.getX() + e * j, this.getEyeY() + f * j, this.getZ() + g * j, 0.0, 0.0, 0.0);
                        }
                    }
                }
            }

            if (this.isInsideWaterOrBubbleColumn()) {
                this.setAir(300);
            } else if (this.isOnGround()) {
                this.setVelocity(this.getVelocity().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F), 0.5, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F)));
                this.setYaw(this.random.nextFloat() * 360.0F);
                this.setOnGround(false);
                this.velocityDirty = true;
            }

            if (this.hasProjTarget()) {
                this.setYaw(this.headYaw);
            }
        }

        if (!this.isTouchingWater() && this.isOnGround() && this.verticalCollision) {
            this.setVelocity(this.getVelocity().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.setOnGround(false);
            this.velocityDirty = true;
           // this.playSound(this.getFlopSound());
        }

        super.tickMovement();
    }

    public int getMinAmbientSoundDelay() {
        return 260;
    }

    protected SoundEvent getAmbientSound() {
        return this.isInsideWaterOrBubbleColumn() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isInsideWaterOrBubbleColumn() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound() {
        return this.isInsideWaterOrBubbleColumn() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
    }

    public float getTailAngle(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevTailAngle, this.tailAngle);
    }


    public static boolean canSpawn(EntityType<? extends WaterCreatureEntity> entityType,
                                   WorldAccess iServerWorld,
                                   SpawnReason reason,
                                   BlockPos pos,
                                   Random random) {
        return iServerWorld.getBlockState(pos).getFluidState().isIn(FluidTags.WATER)
                && iServerWorld.getBlockState(pos.up()).isOf(Blocks.WATER)
                && isLightLevelOk(pos, iServerWorld);
    }

    private static boolean isLightLevelOk(BlockPos pos, WorldAccess iServerWorld) {
        int light = iServerWorld.getLightLevel(pos);
        return light <= 4;
    }

    static class LookAtTargetGoal extends Goal {
        private final ScuttleEntity Spitter;

        public LookAtTargetGoal(ScuttleEntity Spitter) {
            this.Spitter = Spitter;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            return true;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            if (this.Spitter.getTarget() == null) {
                Vec3d vec3d = this.Spitter.getVelocity();
                this.Spitter.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776F);
                this.Spitter.bodyYaw = this.Spitter.getYaw();
            } else {
                LivingEntity livingEntity = this.Spitter.getTarget();
                double d = 64.0;
                if (livingEntity.squaredDistanceTo(this.Spitter) < 4096.0) {
                    double e = livingEntity.getX() - this.Spitter.getX();
                    double f = livingEntity.getZ() - this.Spitter.getZ();
                    this.Spitter.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    this.Spitter.bodyYaw = this.Spitter.getYaw();
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

        public void tick() {
            if (this.state == State.MOVE_TO && !this.guardian.getNavigation().isIdle()) {
                Vec3d vec3d = new Vec3d(this.targetX - this.guardian.getX(), this.targetY - this.guardian.getY(), this.targetZ - this.guardian.getZ());
                double d = vec3d.length();
                double e = vec3d.x / d;
                double f = vec3d.y / d;
                double g = vec3d.z / d;
                float h = (float)(MathHelper.atan2(vec3d.z, vec3d.x) * 57.2957763671875) - 90.0F;
                this.guardian.setYaw(this.wrapDegrees(this.guardian.getYaw(), h, 90.0F));
                this.guardian.bodyYaw = this.guardian.getYaw();
                float i = (float)(this.speed * this.guardian.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125F, this.guardian.getMovementSpeed(), i);
                this.guardian.setMovementSpeed(j);
                double k = Math.sin((double)(this.guardian.age + this.guardian.getId()) * 0.5) * 0.05;
                double l = Math.cos((double)(this.guardian.getYaw() * 0.017453292F));
                double m = Math.sin((double)(this.guardian.getYaw() * 0.017453292F));
                double n = Math.sin((double)(this.guardian.age + this.guardian.getId()) * 0.75) * 0.05;
                this.guardian.setVelocity(this.guardian.getVelocity().add(k * l, n * (m + l) * 0.25 + (double)j * f * 0.1, k * m));
                LookControl lookControl = this.guardian.getLookControl();
                double o = this.guardian.getX() + e * 2.0;
                double p = this.guardian.getEyeY() + f / d;
                double q = this.guardian.getZ() + g * 2.0;
                double r = lookControl.getLookX();
                double s = lookControl.getLookY();
                double t = lookControl.getLookZ();
                if (!lookControl.isLookingAtSpecificPosition()) {
                    r = o;
                    s = p;
                    t = q;
                }

                this.guardian.getLookControl().lookAt(MathHelper.lerp(0.125, r, o), MathHelper.lerp(0.125, s, p), MathHelper.lerp(0.125, t, q), 10.0F, 40.0F);

            } else {
                this.guardian.setMovementSpeed(0.0F);

            }
        }
    }



    static {
        TARGET_ID = DataTracker.registerData(ScuttleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
