package com.jamiedev.bygone.common.entity.ai.behavior;

import com.jamiedev.bygone.core.init.JamiesModTag;
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

public class SabeastSetWalkTargetFromAttackTargetIfTargetOutOfReach {

    private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;

    public static BehaviorControl<Mob> create(float speedModifier) {
        return create(p_147908_ -> speedModifier);
    }

    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier) {
        return BehaviorBuilder.create(
                p_258687_ -> p_258687_.group(
                                p_258687_.registered(MemoryModuleType.WALK_TARGET),
                                p_258687_.registered(MemoryModuleType.LOOK_TARGET),
                                p_258687_.present(MemoryModuleType.ATTACK_TARGET),
                                p_258687_.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                        )
                        .apply(p_258687_, (walk_target, look_target, attack_target, p_258702_) -> (p_258694_, p_258695_, p_258696_) -> {
                            LivingEntity target = p_258687_.get(attack_target);
                            Optional<NearestVisibleLivingEntities> optional = p_258687_.tryGet(p_258702_);
                            if ((optional.isPresent() && optional.get().contains(target) && BehaviorUtils.isWithinAttackRange(p_258695_, target, 1))
                                    || target.isHolding(heldItemStack -> heldItemStack.is(JamiesModTag.SABEAST_AFRAID_PLAYER_HOLD))) {
                                walk_target.erase();
                            } else {
                                look_target.set(new EntityTracker(target, true));
                                walk_target.set(new WalkTarget(new EntityTracker(target, false), speedModifier.apply(p_258695_), 0));
                            }

                            return true;
                        })
        );
    }

}
