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
        BlockModelGenerators.BlockFamilyProvider bystone_polished = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSTONE.get());
        BlockModelGenerators.BlockFamilyProvider bystone_bricks = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSTONE_BRICK.get());

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSTONE.get());
        bystone_polished.stairs(BGBlocks.POLISHED_BYSTONE_STAIRS.get());
        bystone_polished.slab(BGBlocks.POLISHED_BYSTONE_SLAB.get());
        bystone_polished.wall(BGBlocks.POLISHED_BYSTONE_WALL.get());

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSTONE_BRICK.get());
        bystone_bricks.stairs(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get());
        bystone_bricks.slab(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get());
        bystone_bricks.wall(BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get());
        
        blockStateModelGenerator.createGenericCube(BGBlocks.CHISELED_POLISHED_BYSTONE.get());
        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSTONE_SHINGLES.get());

        BlockModelGenerators.BlockFamilyProvider byslate_polished = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSLATE.get());
        BlockModelGenerators.BlockFamilyProvider byslate_bricks = blockStateModelGenerator.family(BGBlocks.POLISHED_BYSLATE_BRICK.get());

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSLATE.get());
        byslate_polished.stairs(BGBlocks.POLISHED_BYSLATE_STAIRS.get());
        byslate_polished.slab(BGBlocks.POLISHED_BYSLATE_SLAB.get());
        byslate_polished.wall(BGBlocks.POLISHED_BYSLATE_WALL.get());

        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSLATE_BRICK.get());
        byslate_bricks.stairs(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get());
        byslate_bricks.slab(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get());
        byslate_bricks.wall(BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get());

        blockStateModelGenerator.createGenericCube(BGBlocks.CHISELED_POLISHED_BYSLATE.get());
        blockStateModelGenerator.createGenericCube(BGBlocks.POLISHED_BYSLATE_SHINGLES.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }


}
