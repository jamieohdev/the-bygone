package com.jamiedev.bygone.entities;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.entities.ai.AvoidBlockGoal;
import com.jamiedev.bygone.init.JamiesModBlocks;
import com.jamiedev.bygone.init.JamiesModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PestEntity extends Animal implements NeutralMob
{
    IronGolem ref2;
    Rabbit ref;
    Piglin ref3;
    Hoglin ref4;

    protected static final ImmutableList<SensorType<? extends Sensor<? super PestEntity>>> SENSOR_TYPES;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    private int moreCropTicks;

    public PestEntity(EntityType<? extends PestEntity> entityType, Level level) {
        super(JamiesModEntityTypes.PEST, level);
        this.setSpeedModifier((double)0.1F);
    }

    public void setSpeedModifier(double speedModifier) {
        this.getNavigation().setSpeedModifier(speedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speedModifier);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double)6.0F).add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F).add(Attributes.ATTACK_DAMAGE, (double)5.0F).add(Attributes.STEP_HEIGHT, (double)1.0F);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MoreCropTicks", this.moreCropTicks);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.moreCropTicks = compound.getInt("MoreCropTicks");
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(2, new AvoidBlockGoal<>(this, JamiesModBlocks.AMBER, 24.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.0F, (p_335873_) -> p_335873_.is(ItemTags.RABBIT_FOOD), false));
        this.goalSelector.addGoal(5, new RaidGardenGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    protected Brain.Provider<PestEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected void doPush(Entity entity) {
        if (entity instanceof Enemy && !(entity instanceof Creeper) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)entity);
        }

        super.doPush(entity);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {

    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    @Override
    public @Nullable LivingEntity getLastHurtByMob() {
        return null;
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingEntity) {

    }

    @Override
    public void setLastHurtByPlayer(@Nullable Player player) {

    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {

    }

    @Override
    public boolean canAttack(LivingEntity livingEntity) {
        return false;
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return null;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    static class RaidGardenGoal extends MoveToBlockGoal {
        private final PestEntity pest;
        private boolean wantsToRaid;
        private boolean canRaid;

        public RaidGardenGoal(PestEntity pest) {
            super(pest, (double)0.7F, 16);
            this.pest = pest;
        }

        public boolean canUse() {
            if (this.nextStartTick <= 0) {
                if (!this.pest.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    return false;
                }

                this.canRaid = false;
                this.wantsToRaid = this.pest.wantsMoreFood();
            }

            return super.canUse();
        }

        public boolean canContinueToUse() {
            return this.canRaid && super.canContinueToUse();
        }

        public void tick() {
            super.tick();
            this.pest.getLookControl().setLookAt((double)this.blockPos.getX() + (double)0.5F, (double)(this.blockPos.getY() + 1), (double)this.blockPos.getZ() + (double)0.5F, 10.0F, (float)this.pest.getMaxHeadXRot());
            if (this.isReachedTarget()) {
                Level level = this.pest.level();
                BlockPos blockpos = this.blockPos.above();
                BlockState blockstate = level.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (this.canRaid && block instanceof CropBlock) {
                    int i = (Integer)blockstate.getValue(CropBlock.AGE);
                    if (i == 0) {
                        level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                        level.destroyBlock(blockpos, true, this.pest);
                    } else {
                        level.setBlock(blockpos, (BlockState)blockstate.setValue(CropBlock.AGE, i - 1), 2);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(this.pest));
                        level.levelEvent(2001, blockpos, Block.getId(blockstate));
                    }

                    this.pest.moreCropTicks = 40;
                }

                this.canRaid = false;
                this.nextStartTick = 10;
            }

        }

        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            BlockState blockstate = level.getBlockState(pos);
            if (blockstate.is(JamiesModBlocks.CLAYSTONE_FARMLAND) && this.wantsToRaid && !this.canRaid) {
                blockstate = level.getBlockState(pos.above());
                if (blockstate.getBlock() instanceof CropBlock && ((CropBlock)blockstate.getBlock()).isMaxAge(blockstate)) {
                    this.canRaid = true;
                    return true;
                }
            }

            return false;
        }
    }

    boolean wantsMoreFood() {
        return this.moreCropTicks <= 0;
    }

    static {
         SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS,
                 SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);
         MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES,
                 MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.ATE_RECENTLY,
                 MemoryModuleType.NEAREST_REPELLENT);

    }
}
