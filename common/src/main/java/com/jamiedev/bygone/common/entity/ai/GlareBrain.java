package com.jamiedev.bygone.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jamiedev.bygone.common.entity.GlareEntity;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.StayCloseToTarget;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import java.util.List;
import java.util.Optional;

public class GlareBrain
{

        public static final List<MemoryModuleType<?>> MEMORY_MODULES;
        public static final List<SensorType<? extends Sensor<? super GlareEntity>>> SENSORS;

        public GlareBrain() {
            // empty, like their brains
        }

        public static Brain<?> create(Dynamic<?> dynamic) {
            Brain.Provider<GlareEntity> profile = Brain.provider(MEMORY_MODULES, SENSORS);
            Brain<GlareEntity> brain = profile.makeBrain(dynamic);

            addCoreActivities(brain);
            addAvoidActivities(brain);
            addIdleActivities(brain);

            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            brain.setDefaultActivity(Activity.IDLE);
            brain.useDefaultActivity();

            return brain;
        }

        private static void addCoreActivities(Brain<GlareEntity> brain) {
            brain.addActivity(Activity.CORE,
                    0,
                    ImmutableList.of(
                            new Swim(0.8f),
                            new LookAtTargetSink(45, 90),
                            new MoveToTargetSink(),
                            new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                            new CountDownCooldownTicks(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)
                    )
            );
        }



        private static void addAvoidActivities(Brain<GlareEntity> brain) {
            brain.addActivityWithConditions(
                    Activity.AVOID,
                    ImmutableList.of(
                            Pair.of(0, SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.25F, 16, false))
                    ),
                    ImmutableSet.of(
                            Pair.of(MemoryModuleType.AVOID_TARGET, MemoryStatus.VALUE_PRESENT)
                    )
            );
        }



    private static void addIdleActivities(Brain<GlareEntity> brain) {
            brain.addActivityWithConditions(
                    Activity.IDLE,
                    ImmutableList.of(
                            Pair.of(0, new FollowTemptation(glare -> 1.25f)),
                            Pair.of(1, new AnimalMakeLove(BGEntityTypes.GLARE.get())),
                            Pair.of(2, BabyFollowAdult.create(UniformInt.of(5, 16), 1.25f)),
                            Pair.of(3, StayCloseToTarget.create(glare -> getOwner((GlareEntity) glare), (glare) -> true, 3, 8, 2.0f)),
                            Pair.of(4, SetEntityLookTargetSometimes.create(6.0f, UniformInt.of(30, 60))),
                            Pair.of(5, new RunOne(
                                    ImmutableList.of(
                                            Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 2),
                                            Pair.of(new GlareFloatTask(), 2),
                                            Pair.of(new DoNothing(30, 60), 1)
                                    )
                            ))
                    ),
                    ImmutableSet.of(
                            Pair.of(MemoryModuleType.AVOID_TARGET, MemoryStatus.VALUE_ABSENT)
                    )
            );
        }

        private static Optional<PositionTracker> getOwner(GlareEntity glare) {
            if (
                    !glare.hasExactlyOnePlayerPassenger()
                            || !glare.isInWaterRainOrBubble()
            ) {
                return Optional.empty();
            }

            return Optional.of(new EntityTracker(glare.getTarget(), true));
        }

        public static void updateActivities(GlareEntity glare) {
            glare.getBrain().setActiveActivityToFirstValid(
                    ImmutableList.of(

                            Activity.AVOID,
                            Activity.IDLE
                    )
            );
        }

static {
        SENSORS = List.of(

                SensorType.NEAREST_ITEMS,
                SensorType.NEAREST_ADULT,
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS

        );
            MEMORY_MODULES = List.of(
                    MemoryModuleType.BREED_TARGET,
                    MemoryModuleType.AVOID_TARGET,
                    MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                    MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.PATH,
                    MemoryModuleType.TEMPTING_PLAYER,
                    MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
                    MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
                    MemoryModuleType.IS_PANICKING,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.ATE_RECENTLY);
        }
}
