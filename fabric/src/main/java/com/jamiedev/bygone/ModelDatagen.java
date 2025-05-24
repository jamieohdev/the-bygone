package com.jamiedev.bygone;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;


public class ModelDatagen extends FabricModelProvider
{
    public ModelDatagen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        BlockModelGenerators.BlockFamilyProvider bystone_polished = blockStateModelGenerator.family(BGBlocks.AMBER_SANDSTONE.get());


        bystone_polished.stairs(BGBlocks.AMBER_SANDSTONE_STAIRS.get());
        bystone_polished.slab(BGBlocks.AMBER_SANDSTONE_SLAB.get());
        bystone_polished.wall(BGBlocks.AMBER_SANDSTONE_WALL.get());


    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }


}
