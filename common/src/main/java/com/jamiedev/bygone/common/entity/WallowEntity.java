package com.jamiedev.bygone.common.entity;

 import com.jamiedev.bygone.common.entity.ai.goal.WallowDriftGoal;
import com.jamiedev.bygone.common.entity.ai.goal.WallowSeekTargetGoal;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class WallowEntity extends Monster {
    public WallowEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.xpReward = 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new WallowSeekTargetGoal(this, 0.18D, 1.2D, e -> true));
        this.goalSelector.addGoal(5, new WallowDriftGoal(this));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.noPhysics = true;
        this.setNoGravity(true);

        if (this.level().isClientSide) {
            return;
        }
        freezeOnTouchLiving(this);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.WALLOW_CRY_ADDITIONS_EVENT;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 60;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.FALL)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    private static void freezeOnTouchLiving(WallowEntity wallow) {
        AABB touchBox = wallow.getBoundingBox().inflate(0.05D);
        List<LivingEntity> living = wallow.level().getEntitiesOfClass(
                LivingEntity.class,
                touchBox,
                entity -> entity.isAlive()
                        && entity != wallow
                        && (!(entity instanceof Player p) || !p.isSpectator())
        );
        for (LivingEntity entity : living) {
            int addPerTick = 7;
            int newFrozen = Math.min(entity.getTicksRequiredToFreeze(), entity.getTicksFrozen() + addPerTick);
            entity.setTicksFrozen(newFrozen);
        }
    }

}