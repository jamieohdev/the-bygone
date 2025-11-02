package com.jamiedev.bygone.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jamiedev.bygone.common.entity.SabeastEntity;
import com.jamiedev.bygone.common.entity.ai.behavior.SabeastSetWalkTargetFromAttackTargetIfTargetOutOfReach;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Predicate;

public class SabeastAI {

    public static Brain<?> makeBrain(SabeastEntity sabeast, Brain<SabeastEntity> brain) {
        initCoreActivity(sabeast, brain);
        initIdleActivity(brain);
        initFightActivity(sabeast, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initFightActivity(SabeastEntity sabeast, Brain<SabeastEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                0,
                ImmutableList.<BehaviorControl<? super SabeastEntity>>of(
                        SabeastSetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.6f)
                        //SabeastMeleeAttack.create(30)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void initCoreActivity(SabeastEntity sabeast, Brain<SabeastEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        StopBeingAngryIfTargetDead.create()
                )
        );
    }

    private static void initIdleActivity(Brain<SabeastEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create((sabeast) -> sabeast.isAlive(),
                                SabeastAI::findNearestValidAttackTarget),
                        createIdleMovementBehaviors()
                )
        );
    }

    private static boolean canSetWalkTargetFromLookTarget(LivingEntity entity) {
        Level level = entity.level();
        Optional<PositionTracker> optional = entity.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
        if (optional.isPresent()) {
            BlockPos blockpos = optional.get().currentBlockPosition();
            return level.isWaterAt(blockpos) == entity.isInWaterOrBubble();
        } else {
            return false;
        }
    }

    private static RunOne<SabeastEntity> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(RandomStroll.stroll(0.6F), 2),
                        Pair.of(new DoNothing(30, 60), 1)
                )
        );
    }

    public static void updateActivity(SabeastEntity sabeast) {
        Brain<SabeastEntity> brain = sabeast.getBrain();
        brain.setActiveActivityToFirstValid(
                ImmutableList.of(Activity.FIGHT, Activity.IDLE)
        );
        sabeast.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }


    private static boolean isNearestValidAttackTarget(SabeastEntity sabeast, LivingEntity target) {
        return findNearestValidAttackTarget(sabeast).filter(p_34887_ -> p_34887_ == target).isPresent();
    }

    private static float getSpeedModifierChasing(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 0.6F : 0.15F;
    }

    private static float getSpeedModifierFollowingAdult(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 0.6F : 0.15F;
    }

    private static float getSpeedModifier(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 0.5F : 0.15F;
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(SabeastEntity sabeast) {
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(sabeast, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(sabeast, optional.get())) {
            return optional;
        }

        Optional<LivingEntity> nearestValidMob = sabeast.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .flatMap(list -> list.findClosest(entity ->
                        (entity instanceof net.minecraft.world.entity.player.Player) &&
                                Sensor.isEntityAttackableIgnoringLineOfSight(sabeast, entity))
                );

        return nearestValidMob;
    }


    private static Optional<? extends LivingEntity> getTargetIfWithinRange(SabeastEntity sabeast, MemoryModuleType<? extends LivingEntity> memoryType) {
        return sabeast.getBrain().getMemory(memoryType).filter(p_35108_ -> p_35108_.closerThan(sabeast, 12.0));
    }

    public static Predicate<ItemStack> getTemptations() {
        return p_335657_ -> p_335657_.is(ItemTags.AXOLOTL_FOOD);
    }

}
