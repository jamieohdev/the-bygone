package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.jamiedev.bygone.core.registry.BGItems;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class BygoneAdvancementProvider extends AdvancementProvider {
    public BygoneAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper) {
        super(output, registries, helper, List.of(new BygoneAdvancements()));
    }

    public static class BygoneAdvancements implements AdvancementGenerator {

        @SuppressWarnings("unused")
        @Override
        public void generate(HolderLookup.@NotNull Provider provider, @NotNull Consumer<AdvancementHolder> consumer, @NotNull ExistingFileHelper existingFileHelper) {

            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(BGItems.ARCANE_CORE.get(),
                            Component.translatable("advancement.bygone.bygone"),
                            Component.translatable("advancement.bygone.bygone.desc"),
                            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "textures/block/bygone.png"),
                            AdvancementType.TASK, false, false, false)
                    .addCriterion("bygone", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(BGDimensions.BYGONE_LEVEL_KEY))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "bygone"), existingFileHelper);

            AdvancementHolder enterBygone = Advancement.Builder.advancement()
                    .parent(root)
                    .display(BGItems.ARCANE_CORE.get(),
                            Component.translatable("advancement.bygone.enter_bygone"),
                            Component.translatable("advancement.bygone.enter_bygone.desc"),
                            null,
                            AdvancementType.TASK, true, true, false)
                    .addCriterion("enter_bygone", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(BGDimensions.BYGONE_LEVEL_KEY))
                    .rewards(new AdvancementRewards(100, List.of(), List.of(), Optional.empty()))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "enter_bygone"), existingFileHelper);
            Bygone ref;
        }
    }

    private static ItemUsedOnLocationTrigger.TriggerInstance itemUsedOnLocationCheckAbove(LocationPredicate.Builder location, LocationPredicate.Builder above, ItemPredicate.Builder item) {
        ContextAwarePredicate contextawarepredicate = ContextAwarePredicate.create(LocationCheck.checkLocation(location).build(), LocationCheck.checkLocation(above, BlockPos.ZERO.above()).build(), MatchTool.toolMatches(item).build());
        return new ItemUsedOnLocationTrigger.TriggerInstance(Optional.empty(), Optional.of(contextawarepredicate));
    }

    public static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> itemUsedOnBlockCheckAbove(LocationPredicate.Builder location, LocationPredicate.Builder above, ItemPredicate.Builder item) {
        return CriteriaTriggers.ITEM_USED_ON_BLOCK.createCriterion(itemUsedOnLocationCheckAbove(location, above, item));
    }
}
