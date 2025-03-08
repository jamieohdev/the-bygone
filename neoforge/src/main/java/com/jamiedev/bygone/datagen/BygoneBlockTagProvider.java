package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BygoneBlockTagProvider extends BlockTagsProvider {


    public BygoneBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Bygone.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        swordEfficient();
        flowers();
        insideStepSoundBlocks();
        //mineableWithHoe();
        endermanHoldable();
        goatSpawnableOn();
        bambooPlantable();
        sculkReplaceable();
       tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(BGBlocks.POLISHED_BYSTONE)
        .add(BGBlocks.POLISHED_BYSTONE_SLAB)
        .add(BGBlocks.POLISHED_BYSTONE_STAIRS)
        .add(BGBlocks.POLISHED_BYSTONE_WALL)

        .add(BGBlocks.POLISHED_BYSTONE_BRICK)
        .add(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB)
        .add(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS)
        .add(BGBlocks.POLISHED_BYSTONE_BRICK_WALL)

        .add(BGBlocks.CHISELED_POLISHED_BYSTONE)
        .add(BGBlocks.POLISHED_BYSTONE_SHINGLES)

        .add(BGBlocks.POLISHED_BYSLATE)
        .add(BGBlocks.POLISHED_BYSLATE_SLAB)
        .add(BGBlocks.POLISHED_BYSLATE_STAIRS)
        .add(BGBlocks.POLISHED_BYSLATE_WALL)

        .add(BGBlocks.POLISHED_BYSLATE_BRICK)
        .add(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB)
        .add(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS)
        .add(BGBlocks.POLISHED_BYSLATE_BRICK_WALL)

        .add(BGBlocks.CHISELED_POLISHED_BYSLATE)
        .add(BGBlocks.POLISHED_BYSLATE_SHINGLES);
        
    }

    private void swordEfficient() {
        tag(BlockTags.SLABS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB)
                .add(BGBlocks.POLISHED_BYSTONE_SLAB)
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB)
                .add(BGBlocks.POLISHED_BYSLATE_SLAB);
        tag(BlockTags.STAIRS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS)
                .add(BGBlocks.POLISHED_BYSTONE_STAIRS)
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS)
                .add(BGBlocks.POLISHED_BYSLATE_STAIRS);
        tag(BlockTags.SLABS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_WALL)
                .add(BGBlocks.POLISHED_BYSTONE_WALL)
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_WALL)
                .add(BGBlocks.POLISHED_BYSLATE_WALL);
    }

    private void flowers() {

    }

    private void insideStepSoundBlocks() {

    }

    private void mineableWithHoe() {

    }

    private void endermanHoldable() {

    }

    private void bambooPlantable() {

    }

    private void goatSpawnableOn() {

    }

    private void sculkReplaceable() {

    }
}
