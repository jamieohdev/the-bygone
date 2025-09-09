package com.jamiedev.bygone.common.entity;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.block.AmaranthCropBlock;
import com.jamiedev.bygone.common.block.PlagaCropBlock;
import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.common.entity.ai.EatCropGoal;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.init.JamiesModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

public class PestEntity extends Animal
{
    Spider ref1;

    protected static final ImmutableList<SensorType<? extends Sensor<? super PestEntity>>> SENSOR_TYPES;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;
    private int moreCropTicks;
    private int eatAnimationTick;
    private EatCropGoal eatBlockGoal;

    Rabbit ref;

    public PestEntity(EntityType<? extends PestEntity> entityType, Level level) {
        super(BGEntityTypes.PEST.get(), level);
        this.setSpeedModifier((double)0.1F);;
    }

    public void setSpeedModifier(double speedModifier) {
        this.getNavigation().setSpeedModifier(speedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speedModifier);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)6.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F).add(Attributes.ATTACK_DAMAGE, (double)5.0F).add(Attributes.STEP_HEIGHT, (double)3.0F);
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
        this.eatBlockGoal = new EatCropGoal(this);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));

        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 2.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.PEST_REPELLENTS);
        }));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 8, 1.2, 2.1, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            if (state.is(BGBlocks.PLAGA_CROP.get())){
                return state.getValue(PlagaCropBlock.AGE) > 5;
            } else return false;
        }));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 4, 1.2, 2.1, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            if (state.is(BGBlocks.PLAGA_CROP.get())){
                return state.getValue(PlagaCropBlock.AGE) < 5;
            } else return false;
        }));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, (double)1.0F, 1.2));

        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.0F, (p_335873_) -> p_335873_.is(ItemTags.ARMOR_ENCHANTABLE), false));
        this.goalSelector.addGoal(4, new PestEntity.PestAvoidEntityGoal<>(this, NectaurEntity.class, 4.0F, 1.1, 1.5));
        this.goalSelector.addGoal(4, new PestEntity.PestAvoidEntityGoal<>(this, Player.class, 8.0F, 1.2, 2.3));
        this.goalSelector.addGoal(4, new PestEntity.PestAvoidEntityGoal<>(this, BigBeakEntity.class, 16.0F, 0.8, 1.12));
        this.goalSelector.addGoal(5, this.eatBlockGoal);
        this.goalSelector.addGoal(5, new RaidGardenGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0, 10));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    protected void customServerAiStep() {
        this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();

        if (this.moreCropTicks > 0) {
            this.moreCropTicks -= this.random.nextInt(3);
            if (this.moreCropTicks < 0) {
                this.moreCropTicks = 0;

            }
        }
    }

    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }

        super.aiStep();
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
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS);
    }

    @Override
    @Nullable
    public PestEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return BGEntityTypes.PEST.get().create(level);
    }


    static class PestAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

        public PestAvoidEntityGoal(PestEntity pest, Class<T> entityClassToAvoid, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
            super(pest, entityClassToAvoid, maxDist, walkSpeedModifier, sprintSpeedModifier);
        }

        public boolean canUse() {
            return super.canUse();
        }
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }
    }

    static class RaidGardenGoal extends MoveToBlockGoal {
        private final PestEntity pest;
        private boolean wantsToRaid;
        private boolean canRaid;

        public RaidGardenGoal(PestEntity pest) {
            super(pest, (double)0.7F, 64);
            this.pest = pest;
        }

        public boolean canUse() {
            if (this.nextStartTick <= 0) {
                this.canRaid = true;
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
                if (this.canRaid && block instanceof AmaranthCropBlock) {
                    int i = (Integer)blockstate.getValue(AmaranthCropBlock.AGE);
                    if (i == 0) {
                        level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                        level.destroyBlock(blockpos, true, this.pest);
                    } else {
                        level.setBlock(blockpos, (BlockState)blockstate.setValue(AmaranthCropBlock.AGE, i - 1), 2);
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
            if (blockstate.is(Blocks.FARMLAND) && this.wantsToRaid && !this.canRaid) {
                blockstate = level.getBlockState(pos.above());
                if (blockstate.getBlock() instanceof CropBlock || blockstate.getBlock() instanceof AmaranthCropBlock && ((AmaranthCropBlock)blockstate.getBlock()).isMaxAge(blockstate)) {
                    this.canRaid = true;
                    return true;
                }
            }

            return false;
        }
    }

    boolean wantsMoreFood() {
        return true;
    }

    public void ate() {
        super.ate();
        if (this.isBaby()) {
            this.ageUp(60);
        }

    }

    static {
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS,
                SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);
        MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.ATE_RECENTLY,
                MemoryModuleType.NEAREST_REPELLENT);

    }
}