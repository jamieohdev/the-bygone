package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.block.gourds.GourdLanternBlock;
import com.jamiedev.bygone.common.block.gourds.GourdVineBlock;
import com.jamiedev.bygone.common.entity.ai.EatGourdGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class WhiskbillEntity extends Animal
{

Guardian ref;


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

    boolean isLookingAtMe(Player player) {
        ItemStack itemstack = (ItemStack)player.getInventory().armor.get(3);
        if (itemstack.is(Blocks.CARVED_PUMPKIN.asItem())) {
            return false;
        } else {
            Vec3 vec3 = player.getViewVector(1.0F).normalize();
            Vec3 vec31 = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            double d0 = vec31.length();
            vec31 = vec31.normalize();
            double d1 = vec3.dot(vec31);
            return d1 > (double) 1.0F - 0.025 / d0 && player.hasLineOfSight(this);
        }
    }
    
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SNIFFER_STEP, 0.15F, 1.0F);
    }

    public SoundEvent getEatingSound(ItemStack stack) {
        return SoundEvents.SNIFFER_EAT;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNIFFER_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SNIFFER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SNIFFER_DEATH;
    }

    public boolean isClimbing() {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (source.is(DamageTypeTags.IS_PLAYER_ATTACK)) {
                Entity var4 = source.getDirectEntity();
                if (var4 instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity)var4;
                    livingentity.hurt(this.damageSources().thorns(this), 2.0F);
                    livingentity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * 1, 0), this);
                }
            }

            return super.hurt(source, amount);
        }
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
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.20F).add(Attributes.MAX_HEALTH, (double)10.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(1, new WhiskbillEntityFreezeWhenLookedAt(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.0F, (p_335873_) -> p_335873_.is(JamiesModTag.BIGBEAK_FOOD), false));

        this.goalSelector.addGoal(4, new WhiskbillEntity.EatBeigeGourdGoal(this, 1.0, 3));
        this.goalSelector.addGoal(4, new WhiskbillEntity.EatMuaveGourdGoal(this, 1.0, 6));
        this.goalSelector.addGoal(4, new WhiskbillEntity.EatVerdantGourdGoal(this, 1.0, 12));


        //this.goalSelector.addGoal(5, new RaidGardenGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }


    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(JamiesModTag.BIGBEAK_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return BGEntityTypes.WHISKBILL.get().create(serverLevel);
    }

    boolean wantsMoreFood() {
        return random.nextInt(700) == 1;
    }

    public boolean isSteppingCarefully() {
        return this.isCrouching() || super.isSteppingCarefully();
    }

    static class WhiskbillEntityFreezeWhenLookedAt extends Goal {
        private final WhiskbillEntity WhiskbillEntity;
        @javax.annotation.Nullable
        private LivingEntity target;

        public WhiskbillEntityFreezeWhenLookedAt(WhiskbillEntity WhiskbillEntity) {
            this.WhiskbillEntity = WhiskbillEntity;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            this.target = this.WhiskbillEntity.getTarget();
            if (!(this.target instanceof Player)) {
                return false;
            } else {
                double d0 = this.target.distanceToSqr(this.WhiskbillEntity);
                return !(d0 > (double) 256.0F) && this.WhiskbillEntity.isLookingAtMe((Player) this.target);
            }
        }

        public void start() {
            this.WhiskbillEntity.getNavigation().stop();
        }

        public void tick() {
            this.WhiskbillEntity.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        }
    }

    class EatBeigeGourdGoal extends EatGourdGoal {
        EatBeigeGourdGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
            super(BGBlocks.GOURD_LANTERN_BEIGE.get(), mob, speedModifier, verticalSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + WhiskbillEntity.this.random.nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 1.14;
        }
    }

    class EatMuaveGourdGoal extends EatGourdGoal {
        EatMuaveGourdGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
            super(BGBlocks.GOURD_LANTERN_MUAVE.get(), mob, speedModifier, verticalSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + WhiskbillEntity.this.random.nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 1.14;
        }
    }


    class EatVerdantGourdGoal extends EatGourdGoal {
        EatVerdantGourdGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
            super(BGBlocks.GOURD_LANTERN_VERDANT.get(), mob, speedModifier, verticalSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + WhiskbillEntity.this.random.nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 1.14;
        }
    }

    class EatGoal extends RemoveBlockGoal {
        EatGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
            super(BGBlocks.AMBER.get(), mob, speedModifier, verticalSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + WhiskbillEntity.this.random.nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 1.14;
        }
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(WhiskbillEntity.class, EntityDataSerializers.BYTE);
    }
}
