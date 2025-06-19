package com.jamiedev.bygone.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jamiedev.bygone.common.entity.NectaurEntity;
import com.jamiedev.bygone.common.entity.ai.behavior.*;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

public class NectaurBrain {

    public static Brain<?> create(NectaurEntity nectaur, Brain<NectaurEntity> brain
    ) {
        initCoreActivity(nectaur, brain);
        initFightActivity(nectaur, brain);
        initIdleActivity(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initFightActivity(NectaurEntity nectaur, Brain<NectaurEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                0,
                ImmutableList.<net.minecraft.world.entity.ai.behavior.BehaviorControl<? super NectaurEntity>>of(
                        //NectaurStalk.create(0.75F, 12, 18),
                        StopAttackingIfTargetInvalid.<NectaurEntity>create(target -> !isNearestValidAttackTarget(nectaur, target) && !nectaur.isBaby()),
                        BehaviorBuilder.triggerIf(entity -> entity.getBrain().checkMemory(BGMemoryModuleTypes.GROUP_LEADER, MemoryStatus.VALUE_ABSENT), BackUpIfTooClose.create(12, 1.3F)),
                        // This behavior should be when it is not in a group
                        BackUpIfTooClose.create(4, 1.4F),
                        NectaurSetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F, 4),
                        new NectaurRangeAttack<>()
                        // This should be for when it is in a group
                        //BehaviorBuilder.triggerIf(entity -> entity.getBrain().checkMemory(BGMemoryModuleTypes.GROUP_LEADER, MemoryStatus.VALUE_PRESENT) && entity.getBrain().checkMemory(BGMemoryModuleTypes.NECTAUR_RANGED_COOLDOWN, MemoryStatus.VALUE_PRESENT), MeleeAttack.create(3))
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static boolean isNearestValidAttackTarget(NectaurEntity nectaur, LivingEntity target) {
        return findNearestValidAttackTarget(nectaur).filter(p_34887_ -> p_34887_ == target).isPresent();
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(NectaurEntity nectaur) {
        Brain<NectaurEntity> brain = nectaur.getBrain();
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(nectaur, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(nectaur, optional.get())) {
            return optional;
        } else {

            Optional<Mob> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            if (optional3.isPresent()) {
                return optional3;
            } else {
                Optional<Player> optional2 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                return optional2.isPresent() && Sensor.isEntityAttackable(nectaur, optional2.get()) ? optional2 : Optional.empty();
            }
        }
    }

    private static void initCoreActivity(NectaurEntity nectaur, Brain<NectaurEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        StopBeingAngryIfTargetDead.create()
                )
        );
    }

    private static void initIdleActivity(Brain<NectaurEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        new NectaurFollowLeader(),
                        StartAttacking.<NectaurEntity>create(NectaurEntity::isAlive, NectaurBrain::findNearestValidAttackTarget),
                        new NectaurJoinGroup(),
                        createIdleLookBehaviors(),
                        createIdleMovementBehaviors()

                )
        );
    }

    public static void updateActivity(NectaurEntity nectaur) {
        Brain<NectaurEntity> brain = nectaur.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);

        nectaur.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    private static RunOne<NectaurEntity> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(RandomStroll.stroll(0.6F), 2),
                        Pair.of(new DoNothing(30, 60), 1)
                )
        );
    }

    private static ImmutableList<Pair<OneShot<LivingEntity>, Integer>> createLookBehaviors() {
        return ImmutableList.of(
                Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 1),
                Pair.of(SetEntityLookTarget.create(BGEntityTypes.NECTAUR.get(), 8.0F), 1),
                Pair.of(SetEntityLookTarget.create(8.0F), 1)
        );
    }

    private static RunOne<LivingEntity> createIdleLookBehaviors() {
        return new RunOne<>(
                ImmutableList.<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>builder()
                        .addAll(createLookBehaviors())
                        .add(Pair.of(new DoNothing(30, 60), 1))
                        .build()
        );
    }

}
