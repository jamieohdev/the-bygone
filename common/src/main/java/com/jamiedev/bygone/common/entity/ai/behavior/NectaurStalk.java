package com.jamiedev.bygone.common.entity.ai.behavior;

import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.function.Function;

public class NectaurStalk {
    public static BehaviorControl<Mob> create(float speedModifier, int closeEnoughDist, int tooFarDist) {
        return create(p_147908_ -> speedModifier, closeEnoughDist, tooFarDist);
    }

    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier, int closeDist, int farDist) {
        return BehaviorBuilder.create(
                p_258687_ -> p_258687_.group(
                                p_258687_.registered(MemoryModuleType.WALK_TARGET),
                                p_258687_.registered(MemoryModuleType.LOOK_TARGET),
                                p_258687_.present(MemoryModuleType.ATTACK_TARGET),
                                p_258687_.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES),
                                p_258687_.registered(BGMemoryModuleTypes.IS_STALKING)
                        )
                        .apply(p_258687_, (walk_target,
                                           look_target,
                                           attack_target,
                                           nearest_visible_living_entities,
                                           is_stalking) ->
                                (p_258694_,
                                 thisEntity,
                                 p_258696_) -> {
                            LivingEntity livingentity = p_258687_.get(attack_target);
                            if (!thisEntity.getBrain().getMemory(BGMemoryModuleTypes.IS_STALKING).get() &&
                                    thisEntity.getBrain().getMemory(BGMemoryModuleTypes.IS_IN_GROUP).get()) {
                                thisEntity.getBrain().setMemory(BGMemoryModuleTypes.IS_STALKING, true);
                                thisEntity.getNavigation().stop();
                            }
                            else {
                                if (thisEntity.getBrain().getMemory(BGMemoryModuleTypes.IS_STALKING).get()) {
                                    thisEntity.getNavigation().stop();
                                }
                            }


                            return true;
                        })
        );
    }
}
