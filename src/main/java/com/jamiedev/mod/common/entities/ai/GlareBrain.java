package com.jamiedev.mod.common.entities.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jamiedev.mod.common.entities.GlareEntity;
import com.jamiedev.mod.common.init.JamiesModEntityTypes;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;

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
            Brain.Profile<GlareEntity> profile = Brain.createProfile(MEMORY_MODULES, SENSORS);
            Brain<GlareEntity> brain = profile.deserialize(dynamic);

            addCoreActivities(brain);
            addAvoidActivities(brain);
            addIdleActivities(brain);

            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            brain.setDefaultActivity(Activity.IDLE);
            brain.resetPossibleActivities();

            return brain;
        }

        private static void addCoreActivities(Brain<GlareEntity> brain) {
            brain.setTaskList(Activity.CORE,
                    0,
                    ImmutableList.of(
                            new StayAboveWaterTask(0.8f),
                            new LookAroundTask(45, 90),
                            new MoveToTargetTask(),
                            new TemptationCooldownTask(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                            new TemptationCooldownTask(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)
                    )
            );
        }



        private static void addAvoidActivities(Brain<GlareEntity> brain) {
            brain.setTaskList(
                    Activity.AVOID,
                    ImmutableList.of(
                            Pair.of(0, GoToRememberedPositionTask.createEntityBased(MemoryModuleType.AVOID_TARGET, 1.25F, 16, false))
                    ),
                    ImmutableSet.of(
                            Pair.of(MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_PRESENT)
                    )
            );
        }



    private static void addIdleActivities(Brain<GlareEntity> brain) {
            brain.setTaskList(
                    Activity.IDLE,
                    ImmutableList.of(
                            Pair.of(0, new TemptTask(glare -> 1.25f)),
                            Pair.of(1, new BreedTask(JamiesModEntityTypes.GLARE)),
                            Pair.of(2, WalkTowardClosestAdultTask.create(UniformIntProvider.create(5, 16), 1.25f)),
                            Pair.of(3, WalkTowardsLookTargetTask.create(glare -> getOwner((GlareEntity) glare), (glare) -> true, 3, 8, 2.0f)),
                            Pair.of(4, LookAtMobWithIntervalTask.follow(6.0f, UniformIntProvider.create(30, 60))),
                            Pair.of(5, new RandomTask(
                                    ImmutableList.of(
                                            Pair.of(GoTowardsLookTargetTask.create(1.0F, 3), 2),
                                            Pair.of(new GlareFloatTask(), 2),
                                            Pair.of(new WaitTask(30, 60), 1)
                                    )
                            ))
                    ),
                    ImmutableSet.of(
                            Pair.of(MemoryModuleType.AVOID_TARGET, MemoryModuleState.VALUE_ABSENT)
                    )
            );
        }

        private static Optional<LookTarget> getOwner(GlareEntity glare) {
            if (
                    !glare.hasPlayerRider()
                            || !glare.isWet()
            ) {
                return Optional.empty();
            }

            return Optional.of(new EntityLookTarget(glare.getTarget(), true));
        }

        public static void updateActivities(GlareEntity glare) {
            glare.getBrain().resetPossibleActivities(
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
            MemoryModuleType.VISIBLE_MOBS,
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
