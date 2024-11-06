package com.jamiedev.mod.datagen;

import com.jamiedev.mod.init.JamiesModBlocks;
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
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(JamiesModBlocks.COBBLED_BYSLATE_SLAB)
                .add(JamiesModBlocks.COBBLED_BYSLATE_STAIRS)
                .add(JamiesModBlocks.COBBLED_BYSLATE_WALL)
                .add(JamiesModBlocks.COBBLED_BYSTONE_SLAB)
                .add(JamiesModBlocks.COBBLED_BYSTONE_STAIRS)
                .add(JamiesModBlocks.COBBLED_BYSTONE_WALL);
        
    }
}
