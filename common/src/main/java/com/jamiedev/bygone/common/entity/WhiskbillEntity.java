package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.block.gourds.GourdLanternBlock;
import com.jamiedev.bygone.common.block.gourds.GourdVineBlock;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WhiskbillEntity extends Animal
{

    Frog ref;

    int moreCarrotTicks;

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;

    public WhiskbillEntity(EntityType<? extends WhiskbillEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MoreGourdTicks", this.moreCarrotTicks);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.moreCarrotTicks = compound.getInt("MoreGourdTicks");
    }

    public void customServerAiStep() {
        if (this.moreCarrotTicks > 0) {
            this.moreCarrotTicks -= this.random.nextInt(3);
            if (this.moreCarrotTicks < 0) {
                this.moreCarrotTicks = 0;
            }
        }

    }

    public boolean isClimbing() {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b0 = (Byte)this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.35F).add(Attributes.MAX_HEALTH, (double)14.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.0F, (p_335873_) -> p_335873_.is(ItemTags.RABBIT_FOOD), false));
        this.goalSelector.addGoal(5, new RaidGardenGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }


    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(JamiesModTag.BIGBEAK_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    boolean wantsMoreFood() {
        return true;
    }

    static class RaidGardenGoal extends MoveToBlockGoal {
        private final WhiskbillEntity rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;

        public RaidGardenGoal(WhiskbillEntity rabbit) {
            super(rabbit, (double)0.7F, 64);
            this.rabbit = rabbit;
        }

        public boolean canUse() {
            if (this.nextStartTick <= 0) {
                if (!this.rabbit.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    return false;
                }

                this.canRaid = false;
                this.wantsToRaid = this.rabbit.wantsMoreFood();
            }

            return super.canUse();
        }

        public boolean canContinueToUse() {
            return this.canRaid && super.canContinueToUse();
        }

        public void tick() {
            super.tick();
            this.rabbit.getLookControl().setLookAt((double)this.blockPos.getX() + (double)0.5F, (double)(this.blockPos.getY() + 1),
                    (double)this.blockPos.getZ() + (double)0.5F, 10.0F, (float)this.rabbit.getMaxHeadXRot());
            if (this.isReachedTarget()) {
                Level level = this.rabbit.level();
                BlockPos blockpos = this.blockPos.above();
                BlockState blockstate = level.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (this.canRaid && block instanceof GourdLanternBlock) {
                  //  player.displayClientMessage(Component.translatable("SUCCESS "), true);
                    int i = (Integer)blockstate.getValue(CarrotBlock.AGE);
                    if (i == 0) {
                        level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                        level.destroyBlock(blockpos, true, this.rabbit);
                    } else {
                        level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                        level.destroyBlock(blockpos, true, this.rabbit);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(this.rabbit));
                        level.levelEvent(2001, blockpos, Block.getId(blockstate));
                      //  player.displayClientMessage(Component.translatable("GOURD DESTROYED AT " + blockpos), true);
                    }

                    this.rabbit.moreCarrotTicks = 40;
                }

                this.canRaid = false;
                this.nextStartTick = 10;
            }
            else
            {

               // player.displayClientMessage(Component.translatable("FAIL? "), true);
            }

        }

        BedBlock ref445;
        Player player;
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            BlockState blockstate = level.getBlockState(pos);
            BlockState blockstate1 = level.getBlockState(pos);
            if ((blockstate.is(Blocks.MOSS_BLOCK) || blockstate.is(BGBlocks.GOURD_VINE.get()) || blockstate.is(BGBlocks.GOURD_LANTERN_BEIGE.get())
                    || blockstate.is(BGBlocks.GOURD_LANTERN_MUAVE.get())|| blockstate.is(BGBlocks.GOURD_LANTERN_VERDANT.get())
                    && this.wantsToRaid && !this.canRaid)) {

                //player.displayClientMessage(Component.translatable("LOCATED AT" + pos), true);

                blockstate = level.getBlockState(pos.below());
                blockstate1 = level.getBlockState(pos.above());
                if (blockstate.getBlock() instanceof GourdLanternBlock || blockstate1.getBlock() instanceof GourdLanternBlock) {
                   /// player.displayClientMessage(Component.translatable("GOURD LOCATED AT" + blockstate.getBlock()), true);
                    this.canRaid = true;
                    return true;
                }

                if (blockstate.getBlock() instanceof GourdVineBlock) {
                    //player.displayClientMessage(Component.translatable("VINE LOCATED AT" + blockstate.getBlock()), true);
                    this.canRaid = true;
                    return true;
                }
            }

            if (blockstate.is(BGBlocks.GOURD_LANTERN_BEIGE.get()) && this.wantsToRaid && !this.canRaid) {
                blockstate = level.getBlockState(pos.below());
                if (blockstate.getBlock() instanceof GourdLanternBlock) {
                    this.canRaid = true;
                    return true;
                }
            }

            return false;
        }
    }

    abstract class WhiskbillGoal extends Goal
    {
        public WhiskbillGoal()
        {
            
        }

        public abstract boolean canWhiskbillUse();

        public abstract boolean canWhiskbillContinueToUse();

        public boolean canUse() {
            return this.canWhiskbillUse();
        }

        public boolean canContinueToUse() {
            return this.canWhiskbillContinueToUse();
        }
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(WhiskbillEntity.class, EntityDataSerializers.BYTE);
    }
}
