package com.jamiedev.bygone.common.criteria;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class SurviveHauntingsTrigger extends SimpleCriterionTrigger<SurviveHauntingsTrigger.SurviveHauntingsInstance> {
    @Override public Codec<SurviveHauntingsInstance> codec() { return SurviveHauntingsInstance.CODEC; }
    public void trigger(ServerPlayer player) { this.trigger(player, instance -> true); }

    public record SurviveHauntingsInstance() implements SimpleInstance {
        public static final Codec<SurviveHauntingsInstance> CODEC = Codec.unit(new SurviveHauntingsInstance());
        @Override public void validate(CriterionValidator validator) { SimpleInstance.super.validate(validator); }
        @Override public Optional<ContextAwarePredicate> player() { return Optional.empty(); }
    }
}
