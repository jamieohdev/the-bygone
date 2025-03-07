package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.init.JamiesModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
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
        .add(JamiesModBlocks.POLISHED_BYSTONE)
        .add(JamiesModBlocks.POLISHED_BYSTONE_SLAB)
        .add(JamiesModBlocks.POLISHED_BYSTONE_STAIRS)
        .add(JamiesModBlocks.POLISHED_BYSTONE_WALL)

        .add(JamiesModBlocks.POLISHED_BYSTONE_BRICK)
        .add(JamiesModBlocks.POLISHED_BYSTONE_BRICK_SLAB)
        .add(JamiesModBlocks.POLISHED_BYSTONE_BRICK_STAIRS)
        .add(JamiesModBlocks.POLISHED_BYSTONE_BRICK_WALL)

        .add(JamiesModBlocks.CHISELED_POLISHED_BYSTONE)
        .add(JamiesModBlocks.POLISHED_BYSTONE_SHINGLES)

        .add(JamiesModBlocks.POLISHED_BYSLATE)
        .add(JamiesModBlocks.POLISHED_BYSLATE_SLAB)
        .add(JamiesModBlocks.POLISHED_BYSLATE_STAIRS)
        .add(JamiesModBlocks.POLISHED_BYSLATE_WALL)

        .add(JamiesModBlocks.POLISHED_BYSLATE_BRICK)
        .add(JamiesModBlocks.POLISHED_BYSLATE_BRICK_SLAB)
        .add(JamiesModBlocks.POLISHED_BYSLATE_BRICK_STAIRS)
        .add(JamiesModBlocks.POLISHED_BYSLATE_BRICK_WALL)

        .add(JamiesModBlocks.CHISELED_POLISHED_BYSLATE)
        .add(JamiesModBlocks.POLISHED_BYSLATE_SHINGLES);
        
    }

    private void swordEfficient() {
        tag(BlockTags.SLABS)
                .add(JamiesModBlocks.POLISHED_BYSTONE_BRICK_SLAB)
                .add(JamiesModBlocks.POLISHED_BYSTONE_SLAB)
                .add(JamiesModBlocks.POLISHED_BYSLATE_BRICK_SLAB)
                .add(JamiesModBlocks.POLISHED_BYSLATE_SLAB);
        tag(BlockTags.STAIRS)
                .add(JamiesModBlocks.POLISHED_BYSTONE_BRICK_STAIRS)
                .add(JamiesModBlocks.POLISHED_BYSTONE_STAIRS)
                .add(JamiesModBlocks.POLISHED_BYSLATE_BRICK_STAIRS)
                .add(JamiesModBlocks.POLISHED_BYSLATE_STAIRS);
        tag(BlockTags.SLABS)
                .add(JamiesModBlocks.POLISHED_BYSTONE_BRICK_WALL)
                .add(JamiesModBlocks.POLISHED_BYSTONE_WALL)
                .add(JamiesModBlocks.POLISHED_BYSLATE_BRICK_WALL)
                .add(JamiesModBlocks.POLISHED_BYSLATE_WALL);
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
