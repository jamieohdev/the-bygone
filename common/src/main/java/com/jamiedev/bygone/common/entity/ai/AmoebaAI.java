package com.jamiedev.bygone.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.jamiedev.bygone.common.entity.AmoebaEntity;
import com.jamiedev.bygone.common.entity.SabeastEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.frog.TadpoleAi;
import net.minecraft.world.entity.schedule.Activity;

public class AmoebaAI 
{
    private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
    private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;

    public AmoebaAI() {
    }

    public static Brain<?> makeBrain(AmoebaEntity amoeba, Brain<AmoebaEntity> brain) {
        initCoreActivity(amoeba, brain);
        initIdleActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }


    private static void initCoreActivity(AmoebaEntity amoeba, Brain<AmoebaEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.<BehaviorControl<? super AmoebaEntity>>of(
                        new AnimalPanic<>(2.0F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
    }

    private static void initIdleActivity(Brain<AmoebaEntity> brain) {
        brain.addActivity(Activity.IDLE, ImmutableList.of(Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
                Pair.of(1, new FollowTemptation((p_218740_) -> 1.25F)), 
                Pair.of(2, new GateBehavior(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
                        ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, 
                        GateBehavior.RunningPolicy.TRY_ALL, 
                        ImmutableList.of(Pair.of(RandomStroll.swim(0.5F), 2), 
                                Pair.of(SetWalkTargetFromLookTarget.create(0.5F, 3), 3),
                                Pair.of(BehaviorBuilder.triggerIf(Entity::isInWaterOrBubble), 5))))));
    }

    public static void updateActivity(AmoebaEntity amoeba) {
        amoeba.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }
}
