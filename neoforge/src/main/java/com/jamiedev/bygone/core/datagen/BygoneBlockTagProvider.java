package com.jamiedev.bygone.core.datagen;

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
        .add(BGBlocks.POLISHED_BYSTONE.get())
        .add(BGBlocks.POLISHED_BYSTONE_SLAB.get())
        .add(BGBlocks.POLISHED_BYSTONE_STAIRS.get())
        .add(BGBlocks.POLISHED_BYSTONE_WALL.get())

        .add(BGBlocks.POLISHED_BYSTONE_BRICK.get())
        .add(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get())
        .add(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get())
        .add(BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get())

        .add(BGBlocks.CHISELED_POLISHED_BYSTONE.get())
        .add(BGBlocks.POLISHED_BYSTONE_SHINGLES.get())

        .add(BGBlocks.POLISHED_BYSLATE.get())
        .add(BGBlocks.POLISHED_BYSLATE_SLAB.get())
        .add(BGBlocks.POLISHED_BYSLATE_STAIRS.get())
        .add(BGBlocks.POLISHED_BYSLATE_WALL.get())

        .add(BGBlocks.POLISHED_BYSLATE_BRICK.get())
        .add(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get())
        .add(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get())
        .add(BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get())

        .add(BGBlocks.CHISELED_POLISHED_BYSLATE.get())
        .add(BGBlocks.POLISHED_BYSLATE_SHINGLES.get());
        
    }

    private void swordEfficient() {
        tag(BlockTags.SLABS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get())
                .add(BGBlocks.POLISHED_BYSTONE_SLAB.get())
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get())
                .add(BGBlocks.POLISHED_BYSLATE_SLAB.get());
        tag(BlockTags.STAIRS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get())
                .add(BGBlocks.POLISHED_BYSTONE_STAIRS.get())
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get())
                .add(BGBlocks.POLISHED_BYSLATE_STAIRS.get());
        tag(BlockTags.SLABS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get())
                .add(BGBlocks.POLISHED_BYSTONE_WALL.get())
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get())
                .add(BGBlocks.POLISHED_BYSLATE_WALL.get());
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
