package com.jamiedev.bygone.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Items;

import java.util.Map;

public class NectaurRangeAttack<E extends Mob & RangedAttackMob, T extends LivingEntity> extends Behavior<E> {

    private static final int COOLDOWN_TICKS = 80;
    private static final int DURATION = Mth.ceil(60.0F);

    public NectaurRangeAttack(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
        super(entryCondition);
    }

    public NectaurRangeAttack() {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                BGMemoryModuleTypes.GROUP_LEADER, MemoryStatus.VALUE_PRESENT,
                BGMemoryModuleTypes.NECTAUR_RANGED_COOLDOWN, MemoryStatus.VALUE_ABSENT),
                DURATION);
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        LivingEntity livingentity = getAttackTarget(owner);
        return BehaviorUtils.canSee(owner, livingentity)
                && owner.distanceTo(livingentity) < 5.0F;
    }

    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(level, entity);
    }

    protected void tick(ServerLevel level, E owner, long gameTime) {
        LivingEntity livingentity = getAttackTarget(owner);
        this.lookAtTarget(owner, livingentity);
        if (owner.distanceTo(livingentity) < 4.0F) {
            owner.getNavigation().stop();
        }
        if (owner.getBrain().checkMemory(BGMemoryModuleTypes.NECTAUR_RANGED_COOLDOWN, MemoryStatus.VALUE_ABSENT)) {
            owner.performRangedAttack(livingentity, 1.0F);
            owner.playSound(SoundEvents.LINGERING_POTION_THROW, 1.0F, 1.0F);
        }
        owner.getBrain().setMemoryWithExpiry(BGMemoryModuleTypes.NECTAUR_RANGED_COOLDOWN, Unit.INSTANCE, (long)COOLDOWN_TICKS);
    }

    private void lookAtTarget(Mob shooter, LivingEntity target) {
        shooter.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity shooter) {
        return shooter.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        setCooldown(entity, COOLDOWN_TICKS);
        super.stop(level, entity, gameTime);
    }

    public static void setCooldown(LivingEntity entity, int cooldown) {
        entity.getBrain().setMemoryWithExpiry(BGMemoryModuleTypes.NECTAUR_RANGED_COOLDOWN, Unit.INSTANCE, (long)cooldown);
    }
}
