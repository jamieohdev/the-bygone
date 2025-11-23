package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.goal.FollowAquifawnLeaderGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AquifawnEntity extends WaterAnimal implements NeutralMob, ItemSteerable, PlayerRideableJumping, Saddleable {

    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    private final ItemBasedSteering steering;

    @Nullable
    private AquifawnEntity leader;
    private int schoolSize = 1;
    private int playerAirBubbleAmount;

    private float clientSideTailAnimation;
    private float clientSideTailAnimationO;
    private float clientSideTailAnimationSpeed;

    private static final UniformInt PERSISTENT_ANGER_TIME;
    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;

    public AquifawnEntity(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 8;
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
    }

    public int getMaxHeadXRot() {
        return 1;
    }

    public int getMaxHeadYRot() {
        return 1;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return  Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)10.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)1.2F)
                .add(Attributes.ATTACK_DAMAGE, (double)3.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(2, new AquifawnAvoidGoal(this, 8.0F, 1.0, 1.0));
        this.goalSelector.addGoal(3, new FollowAquifawnLeaderGoal(this));
        this.goalSelector.addGoal(4, new FollowBoatGoal(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, (p_336182_) -> p_336182_.is(BGItems.AMOEBA_GEL_ON_A_STICK.get()), false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, (p_335406_) -> p_335406_.is(JamiesModTag.AQUIFAWN_FOOD), false));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AmoebaEntity.class, false));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[]{ScuttleEntity.class})).setAlertOthers(new Class[0]));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BOOST_TIME.equals(key) && this.level().isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(key);
    }


    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BOOST_TIME, 0);
        builder.define(DATA_SADDLE_ID, false);
    }

    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    public float getTailAnimation(float partialTick) {
        return Mth.lerp(partialTick, this.clientSideTailAnimationO, this.clientSideTailAnimation);
    }

    protected boolean closeToNextPos() {
        BlockPos blockpos = this.getNavigation().getTargetPos();
        return blockpos != null && blockpos.closerToCenterThan(this.position(), (double) 12.0F);
    }


    public void aiStep() {

        if (this.isAlive()) {
            if (this.level().isClientSide) {
                this.clientSideTailAnimationO = this.clientSideTailAnimation;

                if (!this.isInWater()) {
                    this.clientSideTailAnimationSpeed = 2.0F;
                }

                if (this.clientSideTailAnimationSpeed < 0.5F) {
                    this.clientSideTailAnimationSpeed = 4.0F;
                } else {
                    this.clientSideTailAnimationSpeed += (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
                }
                this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
            }
        }
        if (this.isInWater() && this.isVehicle()) {
            // this.setNoGravity(true);
        }

        super.aiStep();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        }
        else if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            this.setYRot(player.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(player.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            if (this.isInWater()) {
                float speed = this.getRiddenSpeed(player);

                if (travelVector.lengthSqr() > 1.0E-5) {
                    this.moveRelative(speed, travelVector);
                }

                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.97));

            } else {
                super.travel(travelVector);
            }
        }
        else {
            super.travel(travelVector);
        }
    }

    public void tick()
    {
        super.tick();

        if (this.hasFollowers() && this.level().random.nextInt(200) == 1) {
            List<? extends AquifawnEntity> list = this.level().getEntitiesOfClass(this.getClass(),
                    this.getBoundingBox().inflate((double)8.0F, (double)8.0F, (double)8.0F));
            if (list.size() <= 1) {
                this.schoolSize = 1;
            }
        }

        if (!this.level().isClientSide)
        {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean flag = this.isFood(player.getItemInHand(hand));
        if (!flag && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                player.startRiding(this);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        else {
            InteractionResult interactionresult = super.mobInteract(player, hand);
            this.playerAirBubbleAmount = player.getAirSupply();

            if (!interactionresult.consumesAction()) {
                ItemStack itemstack = player.getItemInHand(hand);
                return itemstack.is(Items.SADDLE) ? itemstack.interactLivingEntity(player, this, hand) : InteractionResult.PASS;
            } else {
                return interactionresult;
            }
        }

    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @javax.annotation.Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Object var10000;
        if (this.isSaddled()) {
            Entity var2 = this.getFirstPassenger();
            if (var2 instanceof Player) {
                Player player = (Player)var2;
                if (player.isHolding(BGItems.AMOEBA_GEL_ON_A_STICK.get())) {
                    var10000 = player;
                    return (LivingEntity)var10000;
                }
            }
        }

        var10000 = super.getControllingPassenger();
        return (LivingEntity)var10000;
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        player.setAirSupply(this.playerAirBubbleAmount);
        this.steering.tickBoost();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;
        double yInput = 0.0;

        if (f1 > 0.0F) {
            yInput = player.getLookAngle().y * 2.0;
        }

        if (f1 <= 0.0F) {
            f1 *= 0.5F;
        }

        return new Vec3((double)f, yInput, (double)f1);
    }

    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.getXRot() * 0.5F, entity.getYRot());
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        if (this.isInWater()){
            return 0.05F;
            //return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
        }
        return 0.01F;
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() && this.isVehicle();
    }

    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }

    protected boolean canRandomSwim() {
        return !this.isFollower();
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    public AquifawnEntity startFollowing(AquifawnEntity leader) {
        this.leader = leader;
        leader.addFollower();
        return leader;
    }

    public void stopFollowing() {
        assert this.leader != null;
        this.leader.removeFollower();
        this.leader = null;
    }

    private void addFollower() {
        ++this.schoolSize;
    }

    private void removeFollower() {
        --this.schoolSize;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
    }

    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }

    public boolean inRangeOfLeader() {
        assert this.leader != null;
        return this.distanceToSqr(this.leader) <= (double)121.0F;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            assert this.leader != null;
            this.getNavigation().moveTo(this.leader, (double)1.0F);
        }

    }

    public void addFollowers(Stream<? extends AquifawnEntity> followers) {
        followers.limit((long)(this.getMaxSchoolSize() - this.schoolSize)).filter((p_27538_) -> p_27538_ != this).forEach((p_27536_) -> p_27536_.startFollowing(this));
    }

    public boolean canBeLeashed() {
        return true;
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
        this.steering.readAdditionalSaveData(compound);
        this.playerAirBubbleAmount = compound.getInt("PlayerAirBubbleAmount");
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
        this.steering.addAdditionalSaveData(compound);
        compound.putInt("PlayerAirBubbleAmount", this.playerAirBubbleAmount);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @javax.annotation.Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(JamiesModTag.AQUIFAWN_FOOD);
    }

    public AquifawnEntity getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        return BGEntityTypes.AQUIFAWN.get().create(level);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        if (spawnGroupData == null) {
            spawnGroupData = new AquifawnSpawnGroupData(this);
        } else {
            this.startFollowing(((AquifawnSpawnGroupData)spawnGroupData).leader);
        }

        return spawnGroupData;
    }

    @Override
    public void onPlayerJump(int jumpPower) {

    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public void handleStartJump(int jumpPower) {

    }

    @Override
    public void handleStopJump() {

    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public void equipSaddle(ItemStack stack, @Nullable SoundSource soundSource) {
        this.steering.setSaddle(true);
        if (soundSource != null) {
            this.level().playSound((Player)null, this, SoundEvents.PIG_SADDLE, soundSource, 0.5F, 1.0F);
        }

    }

    public static class AquifawnSpawnGroupData implements SpawnGroupData {
        public final AquifawnEntity leader;

        public AquifawnSpawnGroupData(AquifawnEntity leader) {
            this.leader = leader;
        }
    }

    public static class AquifawnAvoidGoal extends AvoidEntityGoal {
        protected final PathfinderMob mob;
        private final double walkSpeedModifier;
        private final double sprintSpeedModifier;
        protected final float maxDist;
        @Nullable
        protected Path path;
        protected final PathNavigation pathNav;
        /**
         * Class of entity this behavior seeks to avoid
         */
        private final TargetingConditions avoidEntityTargeting;

        public AquifawnAvoidGoal(PathfinderMob mob, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
            super(mob, LivingEntity.class, maxDistance, walkSpeedModifier, sprintSpeedModifier);
            this.mob = mob;
            this.maxDist = maxDistance;
            this.walkSpeedModifier = walkSpeedModifier;
            this.sprintSpeedModifier = sprintSpeedModifier;
            this.pathNav = mob.getNavigation();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.avoidEntityTargeting = TargetingConditions.forCombat().range((double)maxDistance).selector(predicateOnAvoidEntity.and(avoidPredicate));
        }

        @Override
        public boolean canUse() {
            this.toAvoid = this.mob
                    .level()
                    .getNearestEntity(
                            this.mob
                                    .level()
                                    .getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate((double)this.maxDist, 3.0, (double)this.maxDist),
                                            livingEntity -> livingEntity.isAlive() &&
                                                    !(livingEntity instanceof AquifawnEntity) &&
                                                    !(livingEntity instanceof AmoebaEntity)),
                            this.avoidEntityTargeting,
                            this.mob,
                            this.mob.getX(),
                            this.mob.getY(),
                            this.mob.getZ()
                    );
            if (this.toAvoid == null) {
                return false;
            } else {
                Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
                if (vec3 == null) {
                    return false;
                } else if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.mob)) {
                    return false;
                } else {
                    this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                    return this.path != null;
                }
            }
        }
    }

    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(AquifawnEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(AquifawnEntity.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }
}