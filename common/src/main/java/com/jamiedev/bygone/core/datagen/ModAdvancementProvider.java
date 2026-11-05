package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.criteria.SurviveHauntingsTrigger;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGCriteria;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementSubProvider {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
        var dimension = ResourceKey.create(Registries.DIMENSION, Bygone.id("bygone"));
        var biomes = registries.lookupOrThrow(Registries.BIOME);
        var structures = registries.lookupOrThrow(Registries.STRUCTURE);

        AdvancementHolder root = Advancement.Builder.recipeAdvancement()
                .display(BGItems.ARCANE_CORE.get(), Component.translatable("advancements.bygone.bygone.title"),
                        Component.translatable("advancements.bygone.bygone.desc"), Bygone.id("textures/block/bystone.png"),
                        AdvancementType.TASK, false, false, false)
                .addCriterion("bygone", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(dimension))
                .sendsTelemetryEvent()
                .save(output, "bygone:bygone");

        AdvancementHolder entered = advancement("enter_bygone", BGItems.ARCANE_CORE.get(), AdvancementType.TASK, false)
                .parent(root)
                .addCriterion("enter_bygone", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(dimension))
                .rewards(AdvancementRewards.Builder.experience(100))
                .sendsTelemetryEvent()
                .save(output, "bygone:enter_bygone");

        Advancement.Builder exploration = advancement("explore_bygone", BGItems.VERDIGRIS_SPURS.get(), AdvancementType.CHALLENGE, false)
                .parent(entered)
                .rewards(AdvancementRewards.Builder.experience(500))
                .sendsTelemetryEvent();
        for (String biome : List.of("alphahang", "amber_desert", "bygone_barrens", "megalith_field", "primordial_shores",
                "primordial_ocean", "sable_forest", "shelfhollow", "underhang")) {
            exploration.addCriterion("bygone:" + biome, PlayerTrigger.TriggerInstance.located(
                    LocationPredicate.Builder.inBiome(biomes.getOrThrow(ResourceKey.create(Registries.BIOME, Bygone.id(biome))))));
        }
        exploration.save(output, "bygone:explore_bygone");

        AdvancementHolder sewer = advancement("find_sewer", Blocks.OXIDIZED_COPPER, AdvancementType.TASK, false)
                .parent(entered)
                .addCriterion("sewer", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(
                        structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, Bygone.id("sewer"))))))
                .sendsTelemetryEvent()
                .save(output, "bygone:find_sewer");

        advancement("find_great_trail_ruins", BGBlocks.ALPHA_MOSSY_CLAYSTONE_PATH.get(), AdvancementType.TASK, false)
                .parent(entered)
                .addCriterion("great_trail_ruins", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(
                        structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, Bygone.id("great_trail_ruins"))))))
                .sendsTelemetryEvent()
                .save(output, "bygone:find_great_trail_ruins");

        advancement("find_village_underhang", BGBlocks.CLAYSTONE_BRICKS.get(), AdvancementType.TASK, false)
                .parent(entered)
                .addCriterion("village", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(
                        structures.getOrThrow(ResourceKey.create(Registries.STRUCTURE, Bygone.id("village_underhang"))))))
                .sendsTelemetryEvent()
                .save(output, "bygone:find_village");

        advancement("cowabummer", Blocks.OXIDIZED_COPPER_GRATE, AdvancementType.CHALLENGE, true)
                .parent(sewer)
                .addCriterion("cowabummer", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(BGEntityTypes.COPPERBUG.get())))
                .sendsTelemetryEvent()
                .save(output, "bygone:cowabummer");

        advancement("kill_spectral_mob", Items.IRON_AXE, AdvancementType.TASK, false)
                .parent(entered)
                .addCriterion("kill_spectral_mob", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(JamiesModTag.SPECTRAL)))
                .save(output, "bygone:kill_spectral_mob");

        advancement("ride_big_beak", BGItems.EXOTIC_PLUMAGE.get(), AdvancementType.TASK, false)
                .parent(entered)
                .addCriterion("ride_big_beak", StartRidingTrigger.TriggerInstance.playerStartsRiding(EntityPredicate.Builder.entity()
                        .vehicle(EntityPredicate.Builder.entity().of(BGEntityTypes.BIG_BEAK.get()))))
                .sendsTelemetryEvent()
                .save(output, "bygone:ride_big_beak");

        advancement("survive_hauntings", BGBlocks.LITHINE_LAMP.get(), AdvancementType.GOAL, false)
                .parent(entered)
                .addCriterion("survive_hauntings", new Criterion<>(BGCriteria.SURVIVE_HAUNTINGS_CRITERION,
                        new SurviveHauntingsTrigger.SurviveHauntingsInstance()))
                .save(output, "bygone:survive_hauntings");
    }

    private static Advancement.Builder advancement(String name, ItemLike icon, AdvancementType type, boolean hidden) {
        return Advancement.Builder.recipeAdvancement().display(icon,
                Component.translatable("advancements.bygone." + name + ".title"),
                Component.translatable("advancements.bygone." + name + ".desc"), null, type, true, true, hidden);
    }
}
