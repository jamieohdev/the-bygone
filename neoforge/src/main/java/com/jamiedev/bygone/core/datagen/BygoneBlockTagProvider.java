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


    }

    private void swordEfficient() {

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
