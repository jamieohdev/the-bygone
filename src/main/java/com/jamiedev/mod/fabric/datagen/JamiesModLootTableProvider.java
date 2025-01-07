package com.jamiedev.mod.fabric.datagen;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyNameLootFunction;
import net.minecraft.loot.function.LimitCountLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
public class JamiesModLootTableProvider  extends FabricBlockLootTableProvider {


    public JamiesModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(JamiesModBlocks.CLAYSTONE_BRICKS);
        addDrop(JamiesModBlocks.CLAYSTONE_BRICKS_WALL);
        addDrop(JamiesModBlocks.CLAYSTONE_BRICKS_STAIRS);
        addDrop(JamiesModBlocks.CLAYSTONE_BRICKS_SLAB);;
    }

    public LootTable.Builder mushroomBlockDrops(Block withSilkTouch, ItemConvertible withoutSilkTouch) {
        return this.dropsWithSilkTouch(withSilkTouch, this.applyExplosionDecay(withSilkTouch,
                ItemEntry.builder(withoutSilkTouch)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(-6.0F, 2.0F)))
                        .apply(LimitCountLootFunction.builder(BoundedIntUnaryOperator.createMin(0)))));
    }

    public LootTable.Builder nameableContainerDrops(Block drop) {
        return LootTable.builder().pool(this.addSurvivesExplosionCondition(drop, LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(drop).apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY)))));
    }
}