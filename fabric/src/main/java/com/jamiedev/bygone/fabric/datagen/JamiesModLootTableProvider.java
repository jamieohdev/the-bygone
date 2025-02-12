package com.jamiedev.bygone.fabric.datagen;

import com.jamiedev.bygone.init.JamiesModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.concurrent.CompletableFuture;
public class JamiesModLootTableProvider  extends FabricBlockLootTableProvider {


    public JamiesModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS);
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS_WALL);
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS_STAIRS);
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS_SLAB);
    }

    @Override
    public LootTable.Builder createMushroomBlockDrop(Block withSilkTouch, ItemLike withoutSilkTouch) {
        return this.createSilkTouchDispatchTable(withSilkTouch, this.applyExplosionDecay(withSilkTouch,
                LootItem.lootTableItem(withoutSilkTouch)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(-6.0F, 2.0F)))
                        .apply(LimitCount.limitCount(IntRange.lowerBound(0)))));
    }

    @Override
    public LootTable.Builder createNameableBlockEntityTable(Block drop) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(drop, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(drop).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
    }
}