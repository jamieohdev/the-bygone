package com.jamiedev.bygone.common.entity.ai.behavior;

import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class NectaurStalk {
    public static BehaviorControl<Mob> create(float speedModifier, int closeEnoughDist, int tooFarDist) {
        return create(p_147908_ -> speedModifier, closeEnoughDist, tooFarDist);
    }

    private static final int STALK_DURATION_TICKS = 60;

    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier, int closeDist, int farDist) {
        return BehaviorBuilder.create(
                inst -> inst.group(
                                inst.absent(MemoryModuleType.SONIC_BOOM_COOLDOWN),
                                inst.registered(MemoryModuleType.TOUCH_COOLDOWN),
                                inst.registered(MemoryModuleType.WALK_TARGET),
                                inst.registered(MemoryModuleType.LOOK_TARGET),
                                inst.present(MemoryModuleType.ATTACK_TARGET),
                                inst.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES),
                                inst.registered(BGMemoryModuleTypes.IS_IN_GROUP),
                                inst.registered(BGMemoryModuleTypes.GROUP_LEADER),
                                inst.registered(BGMemoryModuleTypes.IS_STALKING)
                        )
                        .apply(inst, (stalk_cooldown, touch, walk_target, look_target, attack_target, nearest_visible_living_entities, is_in_group, group_leader, is_stalking) ->
                                (level, self, gameTime) -> {
                                    LivingEntity target = inst.get(attack_target);
                                    UUID leaderUUID = self.getBrain().getMemory(BGMemoryModuleTypes.GROUP_LEADER).orElse(null);

                                    if (leaderUUID == null || !self.getUUID().equals(leaderUUID))
                                        return true;

                                    double distanceSq = self.distanceToSqr(target);
                                    boolean isAlreadyStalking = self.getBrain().getMemory(BGMemoryModuleTypes.IS_STALKING).orElse(false);

                                    if (!isAlreadyStalking) {
                                        self.getBrain().setMemory(BGMemoryModuleTypes.IS_STALKING, true);
                                        self.getBrain().setMemoryWithExpiry(MemoryModuleType.WALK_TARGET, null, 1);
                                        self.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);

                                        lookAtTarget(self, target);
                                        self.getBrain().setMemoryWithExpiry(MemoryModuleType.TOUCH_COOLDOWN, Unit.INSTANCE, (long)STALK_DURATION_TICKS);

                                        signalNearbyAllies(level, self, BGMemoryModuleTypes.IS_STALKING, true, 8);
                                    }

                                    if (isAlreadyStalking) {
                                        lookAtTarget(self, target);
                                        self.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);


                                        if (distanceSq < closeDist * closeDist) {
                                            signalNearbyToAttack(level, self, target, 10);
                                            return true;
                                        }

                                        if (!self.getBrain().checkMemory(MemoryModuleType.TOUCH_COOLDOWN, MemoryStatus.VALUE_ABSENT)) {
                                            if (self.getRandom().nextIntBetweenInclusive(0, 45) == 1) {
                                                signalNearbyToAttack(level, self, target, 10);
                                                return true;
                                            }
                                        }
                                    }

                                    return true;
                                }))
                ;
    }

    private static void lookAtTarget(Mob mob, LivingEntity target) {
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    private static void signalNearbyAllies(ServerLevel level, Mob leader, MemoryModuleType<Boolean> flag, boolean value, int radius) {
        level.getEntitiesOfClass(leader.getClass(), leader.getBoundingBox().inflate(radius), e -> e != leader)
                .forEach(entity -> {
                    if (entity instanceof Mob mob) {
                        mob.getBrain().setMemory(flag, value);
                        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                        lookAtTarget(mob, Objects.requireNonNull(leader.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null)));

                        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(Objects.requireNonNull(leader.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null)), true));
                    }
                });
    }

    private static void signalNearbyToAttack(ServerLevel level, Mob leader, LivingEntity target, int radius) {
        setCooldown(leader, 2400);
        level.getEntitiesOfClass(leader.getClass(), leader.getBoundingBox().inflate(radius), e -> e != leader)
                .forEach(entity -> {
                    if (entity instanceof Mob mob) {
                        mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
                        mob.getBrain().setMemory(BGMemoryModuleTypes.IS_STALKING, false);
                    }
                });

        leader.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        leader.getBrain().setMemory(BGMemoryModuleTypes.IS_STALKING, false);
    }

    public static void setCooldown(LivingEntity entity, int cooldown) {
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_COOLDOWN, Unit.INSTANCE, (long)cooldown);
    }
}
