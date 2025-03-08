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
        BlockModelGenerators.BlockFamilyProvider bystone_polished = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSTONE);
        BlockModelGenerators.BlockFamilyProvider bystone_bricks = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSTONE_BRICK);

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSTONE);
        bystone_polished.stairs(BGBlocks.POLISHED_BYSTONE_STAIRS);
        bystone_polished.slab(BGBlocks.POLISHED_BYSTONE_SLAB);
        bystone_polished.wall(BGBlocks.POLISHED_BYSTONE_WALL);

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSTONE_BRICK);
        bystone_bricks.stairs(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS);
        bystone_bricks.slab(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB);
        bystone_bricks.wall(BGBlocks.POLISHED_BYSTONE_BRICK_WALL);
        
        blockStateModelGenerator.createGenericCube(BGBlocks.CHISELED_POLISHED_BYSTONE);
        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSTONE_SHINGLES);

        BlockModelGenerators.BlockFamilyProvider byslate_polished = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSLATE);
        BlockModelGenerators.BlockFamilyProvider byslate_bricks = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSLATE_BRICK);

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSLATE);
        byslate_polished.stairs(BGBlocks.POLISHED_BYSLATE_STAIRS);
        byslate_polished.slab(BGBlocks.POLISHED_BYSLATE_SLAB);
        byslate_polished.wall(BGBlocks.POLISHED_BYSLATE_WALL);

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSLATE_BRICK);
        byslate_bricks.stairs(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS);
        byslate_bricks.slab(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB);
        byslate_bricks.wall(BGBlocks.POLISHED_BYSLATE_BRICK_WALL);

        blockStateModelGenerator.createGenericCube(BGBlocks.CHISELED_POLISHED_BYSLATE);
        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSLATE_SHINGLES);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }


}
