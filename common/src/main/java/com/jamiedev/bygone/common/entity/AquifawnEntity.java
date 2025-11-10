package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.goal.FollowAquifawnLeaderGoal;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class AquifawnEntity extends WaterAnimal implements NeutralMob {

    @Nullable
    private AquifawnEntity leader;
    private int schoolSize = 1;

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
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, (double)1.0F, 10));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new FollowAquifawnLeaderGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, (double)1.2F, true));
        this.goalSelector.addGoal(4, new FollowBoatGoal(this));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, ScuttleEntity.class, 8.0F, (double)1.0F, (double)1.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AmoebaEntity.class, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{ScuttleEntity.class})).setAlertOthers(new Class[0]));
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

        super.aiStep();
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add((double)0.0F, -0.005, (double)0.0F));
            }
        } else {
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
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
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

    public boolean isFood(ItemStack itemStack) {
        return false;
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

    public static class AquifawnSpawnGroupData implements SpawnGroupData {
        public final AquifawnEntity leader;

        public AquifawnSpawnGroupData(AquifawnEntity leader) {
            this.leader = leader;
        }
    }

    static {
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }
}
