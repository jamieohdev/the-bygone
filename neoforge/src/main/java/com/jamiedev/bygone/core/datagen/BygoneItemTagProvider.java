package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
public class BygoneItemTagProvider extends ItemTagsProvider {


    public BygoneItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags,@Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Bygone.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        signs();
        slabs();
        stairs();
        walls();
   }

   private void signs() {

   }

   private void slabs() {
       this.tag(ItemTags.SLABS)
               .add(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get().asItem())
               .add(BGBlocks.POLISHED_BYSTONE_SLAB.get().asItem())
               .add(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get().asItem())
               .add(BGBlocks.POLISHED_BYSLATE_SLAB.get().asItem());
   }

    private void stairs() {
        this.tag(ItemTags.STAIRS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get().asItem())
                .add(BGBlocks.POLISHED_BYSTONE_STAIRS.get().asItem())
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get().asItem())
                .add(BGBlocks.POLISHED_BYSLATE_STAIRS.get().asItem());
    }

    private void walls() {
        this.tag(ItemTags.WALLS)
                .add(BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get().asItem())
                .add(BGBlocks.POLISHED_BYSTONE_WALL.get().asItem())
                .add(BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get().asItem())
                .add(BGBlocks.POLISHED_BYSLATE_WALL.get().asItem());
    }
}
