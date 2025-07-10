package com.jamiedev.bygone.common.entity;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.IntFunction;

public class WraithEntity extends Monster implements RangedAttackMob
{
    private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID;
    protected int spellCastingTickCount;
    private WraithEntity.WraithSpell currentSpell;

    protected WraithEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
     //   this.currentSpell = WraithSpell.NONE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.FOLLOW_RANGE, 18.0)
                .add(Attributes.MAX_HEALTH, 32.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WraithEntity.SpellcasterCastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6, 1.0));

        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WraithEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
  }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SPELL_CASTING_ID, (byte)0);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.spellCastingTickCount = compound.getInt("SpellTicks");
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("SpellTicks", this.spellCastingTickCount);
    }

    public boolean isCastingSpell() {
        return this.level().isClientSide ? (Byte)this.entityData.get(DATA_SPELL_CASTING_ID) > 0 : this.spellCastingTickCount > 0;
    }

    public void setIsCastingSpell(WraithEntity.WraithSpell currentSpell) {
        this.currentSpell = currentSpell;
        this.entityData.set(DATA_SPELL_CASTING_ID, (byte)currentSpell.id);
    }

    protected WraithEntity.WraithSpell getCurrentSpell() {
        return !this.level().isClientSide ? this.currentSpell : WraithEntity.WraithSpell.byId((Byte)this.entityData.get(DATA_SPELL_CASTING_ID));
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellCastingTickCount > 0) {
            --this.spellCastingTickCount;
        }

    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isCastingSpell()) {
            WraithEntity.WraithSpell spell = this.getCurrentSpell();
            float f = (float)spell.spellColor[0];
            float f1 = (float)spell.spellColor[1];
            float f2 = (float)spell.spellColor[2];
            float f3 = this.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float f4 = Mth.cos(f3);
            float f5 = Mth.sin(f3);
            double d0 = 0.6 * (double)this.getScale();
            double d1 = 1.8 * (double)this.getScale();
            this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, f1, f2), this.getX() + (double)f4 * d0, this.getY() + d1, this.getZ() + (double)f5 * d0, (double)0.0F, (double)0.0F, (double)0.0F);
            this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, f1, f2), this.getX() - (double)f4 * d0, this.getY() + d1, this.getZ() - (double)f5 * d0, (double)0.0F, (double)0.0F, (double)0.0F);
        }

    }

    protected int getSpellCastingTime() {
        return this.spellCastingTickCount;
    }

    protected SoundEvent getCastingSoundEvent()
    {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    static {
        DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(WraithEntity.class, EntityDataSerializers.BYTE);
    }

    protected static enum WraithSpell {
        NONE(0, (double)0.0F, (double)0.0F, (double)0.0F),
        TELEPORT(1, 0.7, 0.7, 0.8),
        FIRE(2, 0.4, 0.3, 0.35),
        NOVELTY(3, 0.7, (double)0.5F, 0.2),
        DISAPPEAR(4, 0.3, 0.3, 0.8),
        PUKE(5, 0.1, 0.1, 0.2);

        private static final IntFunction<WraithEntity.WraithSpell> BY_ID = ByIdMap.continuous((p_263091_) -> p_263091_.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        final int id;
        final double[] spellColor;

        private WraithSpell(int id, double red, double green, double blue) {
            this.id = id;
            this.spellColor = new double[]{red, green, blue};
        }

        public static WraithEntity.WraithSpell byId(int id) {
            return (WraithEntity.WraithSpell)BY_ID.apply(id);
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
                WraithEntity.this.getLookControl().setLookAt(WraithEntity.this.getTarget(), (float)WraithEntity.this.getMaxHeadYRot(), (float)WraithEntity.this.getMaxHeadXRot());
            }

        }
    }

    protected abstract class SpellcasterUseSpellGoal extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        protected SpellcasterUseSpellGoal() {
        }

        public boolean canUse() {
            LivingEntity livingentity = WraithEntity.this.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
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
            --this.attackWarmupDelay;
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
}
