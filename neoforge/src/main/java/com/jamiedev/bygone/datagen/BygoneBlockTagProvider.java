package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.init.JamiesModBlocks;
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
       /* tag(BlockTags.MINEABLE_WITH_AXE)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_STAIRS)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_WALL)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_SLAB);*///todo
        
    }

    private void swordEfficient() {
        this.tag(BlockTags.SWORD_EFFICIENT)
                .add(JamiesModBlocks.BELLADONNA)
                .add(JamiesModBlocks.COLEUS);
    }

    private void flowers() {
        this.tag(BlockTags.FLOWERS)
                .add(JamiesModBlocks.BELLADONNA)
                .add(JamiesModBlocks.COLEUS);
    }

    private void insideStepSoundBlocks() {
        this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(JamiesModBlocks.BELLADONNA)
                .add(JamiesModBlocks.COLEUS);
    }

    private void mineableWithHoe() {
        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(JamiesModBlocks.BELLADONNA)
                .add(JamiesModBlocks.COLEUS);
    }

    private void endermanHoldable() {
        this.tag(BlockTags.ENDERMAN_HOLDABLE)
                .add(JamiesModBlocks.GLOW_GRAVEL);
    }

    private void bambooPlantable() {
        this.tag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(JamiesModBlocks.GLOW_GRAVEL);
    }

    private void goatSpawnableOn() {
        this.tag(BlockTags.GOATS_SPAWNABLE_ON)
                .add(JamiesModBlocks.GLOW_GRAVEL);
    }

    private void sculkReplaceable() {
        this.tag(BlockTags.SCULK_REPLACEABLE)
                .add(JamiesModBlocks.GLOW_GRAVEL);
    }
}
