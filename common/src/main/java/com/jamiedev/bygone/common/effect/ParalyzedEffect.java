package com.jamiedev.bygone.common.effect;

import com.jamiedev.bygone.Bygone;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ParalyzedEffect extends MobEffect {

    public ParalyzedEffect() {
        super(MobEffectCategory.HARMFUL, 0x1B1B2F);
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                Bygone.id("effect.paralyzed_movement"),
                -1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.JUMP_STRENGTH,
                Bygone.id("effect.paralyzed_jump"),
                -1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
