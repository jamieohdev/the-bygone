package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.init.JamiesModBlocks;
import com.jamiedev.bygone.init.JamiesModItems;
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
   }

   private void signs() {
        this.tag(ItemTags.SIGNS)
                .add(JamiesModBlocks.ANCIENT_SIGN.asItem());
       this.tag(ItemTags.HANGING_SIGNS)
               .add(JamiesModItems.ANCIENT_HANGING_SIGN);
   }
}
