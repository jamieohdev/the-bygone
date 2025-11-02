package com.jamiedev.bygone.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jamiedev.bygone.common.entity.HydropusEntity;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

public class HydropusBrain {

    public static Brain<?> create(Dynamic<?> dynamic,
                                  ImmutableList<MemoryModuleType<?>> memory,
                                  ImmutableList<SensorType<? extends Sensor<? super HydropusEntity>>> sensors,
                                  HydropusEntity hydropus
    ) {
        Brain.Provider<HydropusEntity> profile = Brain.provider(memory, sensors);
        Brain<HydropusEntity> brain = profile.makeBrain(dynamic);

        initCoreActivity(hydropus, brain);
        initFightActivity(hydropus, brain);
        initIdleActivity(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initFightActivity(HydropusEntity hydropus, Brain<HydropusEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                0,
                ImmutableList.<net.minecraft.world.entity.ai.behavior.BehaviorControl<? super HydropusEntity>>of(
                        StopAttackingIfTargetInvalid.create(p_34981_ -> !hydropus.canAttack(p_34981_) && !hydropus.isBaby()),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        //new LaunchMeleeAttack(),
                        MeleeAttack.create(20)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void initCoreActivity(HydropusEntity hydropus, Brain<HydropusEntity> brain) {
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

    private static void initIdleActivity(Brain<HydropusEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(

                )
        );
    }

}
