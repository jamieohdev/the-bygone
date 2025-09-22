package com.jamiedev.bygone.common.entity.ai.behavior;

import com.jamiedev.bygone.common.entity.SabeastEntity;
import com.jamiedev.bygone.core.init.JamiesModTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;

public class SabeastMeleeAttack {
    public static OneShot<SabeastEntity> create(int cooldownBetweenAttacks) {
        return BehaviorBuilder.create(
                sabeast -> sabeast.group(
                                sabeast.registered(MemoryModuleType.LOOK_TARGET),
                                sabeast.present(MemoryModuleType.ATTACK_TARGET),
                                sabeast.absent(MemoryModuleType.ATTACK_COOLING_DOWN),
                                sabeast.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                        )
                        .apply(
                                sabeast,
                                (p_258529_, attack_target, p_258531_, p_258532_) -> (p_258539_, p_258540_, p_258541_) -> {
                                    LivingEntity target = sabeast.get(attack_target);
                                    if (!isHoldingUsableProjectileWeapon(p_258540_)
                                            && p_258540_.isWithinMeleeAttackRange(target)
                                            && sabeast.<NearestVisibleLivingEntities>get(p_258532_).contains(target)
                                            && !target.isHolding(heldItemStack -> heldItemStack.is(JamiesModTag.SABEAST_AFRAID_PLAYER_HOLD))) {
                                        p_258529_.set(new EntityTracker(target, true));
                                        p_258540_.swing(InteractionHand.MAIN_HAND);
                                        p_258540_.doHurtTarget(target);
                                        p_258531_.setWithExpiry(true, (long)cooldownBetweenAttacks);
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                        )
        );
    }

    private static boolean isHoldingUsableProjectileWeapon(Mob mob) {
        return mob.isHolding(p_147697_ -> {
            Item item = p_147697_.getItem();

            return item instanceof ProjectileWeaponItem && mob.canFireProjectileWeapon((ProjectileWeaponItem)item);
        });
    }
}
