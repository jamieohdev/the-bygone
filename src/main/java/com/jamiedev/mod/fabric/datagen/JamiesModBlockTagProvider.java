package com.jamiedev.mod.fabric.datagen;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class JamiesModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public JamiesModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_STAIRS)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_WALL)
                .add(JamiesModBlocks.CLAYSTONE_BRICKS_SLAB);
        
    }
}
