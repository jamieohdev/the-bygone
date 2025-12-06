package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import java.util.Set;
import java.util.function.Supplier;

public class BygoneBlockLootSubProvider extends BlockLootSubProvider {


    protected BygoneBlockLootSubProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    public void generate() {
        dropSelf(BGBlocks.GUMBO_POT.get());
        dropSelf(BGBlocks.MEGALITH_BLOCK.get());
        dropWhenSilkTouch(BGBlocks.MEGALITH_FACE.get());
        dropWhenSilkTouch(BGBlocks.MEGALITH_LANTERN.get());

        dropSelf(BGBlocks.AMPHORA.get());
        dropSelf(BGBlocks.BLACK_AMPHORA.get());
        dropSelf(BGBlocks.BLUE_AMPHORA.get());
        dropSelf(BGBlocks.BROWN_AMPHORA.get());
        dropSelf(BGBlocks.GILDED_AMPHORA.get());
        dropSelf(BGBlocks.CYAN_AMPHORA.get());
        dropSelf(BGBlocks.GRAY_AMPHORA.get());
        dropSelf(BGBlocks.GREEN_AMPHORA.get());
        dropSelf(BGBlocks.LIGHT_BLUE_AMPHORA.get());
        dropSelf(BGBlocks.LIGHT_GRAY_AMPHORA.get());
        dropSelf(BGBlocks.LIME_AMPHORA.get());
        dropSelf(BGBlocks.MAGENTA_AMPHORA.get());
        dropSelf(BGBlocks.ORANGE_AMPHORA.get());
        dropSelf(BGBlocks.PINK_AMPHORA.get());
        dropSelf(BGBlocks.PURPLE_AMPHORA.get());
        dropSelf(BGBlocks.RED_AMPHORA.get());
        dropSelf(BGBlocks.WHITE_AMPHORA.get());
        dropSelf(BGBlocks.YELLOW_AMPHORA.get());

        dropSelf(BGBlocks.BROKEN_VERDIGRIS_COG.get());
        dropSelf(BGBlocks.RAMSHACKLED_VERDIGRIS_COG.get());
        dropSelf(BGBlocks.TARNISHED_VERDIGRIS_COG.get());
        dropSelf(BGBlocks.PRISTINE_VERDIGRIS_COG.get());


        dropSelf(BGBlocks.WHITE_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.ORANGE_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.MAGENTA_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.LIGHT_BLUE_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.YELLOW_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.LIME_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.PINK_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.GRAY_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.LIGHT_GRAY_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.CYAN_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.PURPLE_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.BLUE_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.BROWN_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.GREEN_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.RED_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.BLACK_GLOW_CONCRETE.get());
        dropSelf(BGBlocks.WHITE_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.ORANGE_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.MAGENTA_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.LIGHT_BLUE_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.YELLOW_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.LIME_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.PINK_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.GRAY_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.LIGHT_GRAY_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.CYAN_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.PURPLE_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.BLUE_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.BROWN_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.GREEN_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.RED_GLOW_CONCRETE_POWDER.get());
        dropSelf(BGBlocks.BLACK_GLOW_CONCRETE_POWDER.get());
    }


}