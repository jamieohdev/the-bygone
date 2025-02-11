package com.jamiedev.bygone.fabric.datagen;

import com.jamiedev.bygone.fabric.init.JamiesModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import java.util.concurrent.CompletableFuture;

public class JamiesModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public JamiesModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
       /* tag(BlockTags.MINEABLE_WITH_AXE)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_STAIRS)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_WALL)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_SLAB);*///todo
        
    }
}
