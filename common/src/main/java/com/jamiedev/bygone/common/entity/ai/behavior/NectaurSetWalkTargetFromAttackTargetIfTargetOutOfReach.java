package com.jamiedev.bygone.common.entity.ai.behavior;

import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Optional;
import java.util.function.Function;

public class NectaurSetWalkTargetFromAttackTargetIfTargetOutOfReach {

    private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;

    public static BehaviorControl<Mob> create(float speedModifier, int closeEnoughDist) {
        return create(p_147908_ -> speedModifier, closeEnoughDist);
    }

    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier, int dist) {
        return BehaviorBuilder.create(
                p_258687_ -> p_258687_.group(
                                p_258687_.registered(MemoryModuleType.WALK_TARGET),
                                p_258687_.registered(MemoryModuleType.LOOK_TARGET),
                                p_258687_.present(MemoryModuleType.ATTACK_TARGET),
                                p_258687_.present(BGMemoryModuleTypes.GROUP_LEADER),
                                p_258687_.registered(BGMemoryModuleTypes.IS_STALKING),
                                p_258687_.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                        )
                        .apply(p_258687_, (walk_target, look_target, attack_target, group_leader, is_stalking, nearest_visible_living_entities) -> (p_258694_, mob, p_258696_) -> {
                            LivingEntity livingentity = p_258687_.get(attack_target);
                            Optional<NearestVisibleLivingEntities> optional = p_258687_.tryGet(nearest_visible_living_entities);
                            if (mob.getBrain().getMemory(BGMemoryModuleTypes.IS_STALKING).orElse(false)) return false;
                            if (optional.isPresent() && optional.get().contains(livingentity) && BehaviorUtils.isWithinAttackRange(mob, livingentity, 1)) {
                                walk_target.erase();
                            } else {
                                look_target.set(new EntityTracker(livingentity, true));
                                walk_target.set(new WalkTarget(new EntityTracker(livingentity, false), speedModifier.apply(mob), dist));
                            }

                            return true;
                        })
        );
    }
}
