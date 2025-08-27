package com.jamiedev.bygone;

import com.jamiedev.bygone.core.registry.BGDimensions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BGAdvancementDatagen extends FabricAdvancementProvider
{

    protected BGAdvancementDatagen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        String id = "advancements." + Bygone.MOD_ID + ".";
        HolderLookup.RegistryLookup<Biome> biomeHolderGetter = registryLookup.lookupOrThrow(Registries.BIOME);
        HolderLookup.RegistryLookup<Structure> structureHolderGetter = registryLookup.lookupOrThrow(Registries.STRUCTURE);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        Blocks.SUSPICIOUS_GRAVEL,
                        Component.translatable(id + "root.title"),
                        Component.translatable(id + "root.description"),
                        Bygone.id("textures/gui/advancements/root.png"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                ).addCriterion("trail_ruins", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureHolderGetter.getOrThrow(BuiltinStructures.TRAIL_RUINS))))
                .save(consumer, path("root"));

        AdvancementHolder enterBygone = Advancement.Builder.advancement().parent(root)
                .display(
                        Blocks.MOSS_BLOCK,
                        Component.translatable(id + "enter_bygone.title"),
                        Component.translatable(id + "enter_bygone.description"),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                ).addCriterion("bygone", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(BGDimensions.BYGONE_LEVEL_KEY))
                .save(consumer, path("bygone"));
    }

    private String path(String name) {
        return Bygone.id("main/" + name).toString();
    }
}
