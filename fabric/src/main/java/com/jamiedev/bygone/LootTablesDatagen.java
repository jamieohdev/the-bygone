package com.jamiedev.bygone;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LootTablesDatagen  extends FabricBlockLootTableProvider
{
    // i hate you so much datagen
    protected LootTablesDatagen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(BGBlocks.MEGALITH_BLOCK.get());
        dropWhenSilkTouch(BGBlocks.MEGALITH_FACE.get());
        dropWhenSilkTouch(BGBlocks.MEGALITH_LANTERN.get());

        dropSelf(BGBlocks.AMPHORA.get());
        dropSelf(BGBlocks.BLACK_AMPHORA.get());
        dropSelf(BGBlocks.BLUE_AMPHORA.get());
        dropSelf(BGBlocks.BROWN_AMPHORA.get());
        dropSelf(BGBlocks.GILDED_AMPHORA.get());
        dropSelf(BGBlocks.CYAN_AMPHORA.get());
        dropSelf(BGBlocks.GRAY_AMPHORA.get());
        dropSelf(BGBlocks.GREEN_AMPHORA.get());
        dropSelf(BGBlocks.LIGHT_BLUE_AMPHORA.get());
        dropSelf(BGBlocks.LIGHT_GRAY_AMPHORA.get());
        dropSelf(BGBlocks.LIME_AMPHORA.get());
        dropSelf(BGBlocks.MAGENTA_AMPHORA.get());
        dropSelf(BGBlocks.ORANGE_AMPHORA.get());
        dropSelf(BGBlocks.PINK_AMPHORA.get());
        dropSelf(BGBlocks.PURPLE_AMPHORA.get());
        dropSelf(BGBlocks.RED_AMPHORA.get());
        dropSelf(BGBlocks.WHITE_AMPHORA.get());
        dropSelf(BGBlocks.YELLOW_AMPHORA.get());

        dropSelf(BGBlocks.BROKEN_VERDIGRIS_COG.get());
        dropSelf(BGBlocks.RAMSHACKLED_VERDIGRIS_COG.get());
        dropSelf(BGBlocks.TARNISHED_VERDIGRIS_COG.get());
        dropSelf(BGBlocks.PRISTINE_VERDIGRIS_COG.get());
    }
}
