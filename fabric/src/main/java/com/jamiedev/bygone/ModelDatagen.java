package com.jamiedev.bygone;

import com.jamiedev.bygone.init.JamiesModBlocks;
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
        BlockModelGenerators.BlockFamilyProvider bystone_polished = blockStateModelGenerator.family(JamiesModBlocks.POLISHED_BYSTONE);
        BlockModelGenerators.BlockFamilyProvider bystone_bricks = blockStateModelGenerator.family(JamiesModBlocks.POLISHED_BYSTONE_BRICK);

        blockStateModelGenerator.createGenericCube(JamiesModBlocks.POLISHED_BYSTONE);
        bystone_polished.stairs(JamiesModBlocks.POLISHED_BYSTONE_STAIRS);
        bystone_polished.slab(JamiesModBlocks.POLISHED_BYSTONE_SLAB);
        bystone_polished.wall(JamiesModBlocks.POLISHED_BYSTONE_WALL);

        blockStateModelGenerator.createGenericCube(JamiesModBlocks.POLISHED_BYSTONE_BRICK);
        bystone_bricks.stairs(JamiesModBlocks.POLISHED_BYSTONE_BRICK_STAIRS);
        bystone_bricks.slab(JamiesModBlocks.POLISHED_BYSTONE_BRICK_SLAB);
        bystone_bricks.wall(JamiesModBlocks.POLISHED_BYSTONE_BRICK_WALL);
        
        blockStateModelGenerator.createGenericCube(JamiesModBlocks.CHISELED_POLISHED_BYSTONE);
        blockStateModelGenerator.createGenericCube(JamiesModBlocks.POLISHED_BYSTONE_SHINGLES);

        BlockModelGenerators.BlockFamilyProvider byslate_polished = blockStateModelGenerator.family(JamiesModBlocks.POLISHED_BYSLATE);
        BlockModelGenerators.BlockFamilyProvider byslate_bricks = blockStateModelGenerator.family(JamiesModBlocks.POLISHED_BYSLATE_BRICK);

        blockStateModelGenerator.createGenericCube(JamiesModBlocks.POLISHED_BYSLATE);
        byslate_polished.stairs(JamiesModBlocks.POLISHED_BYSLATE_STAIRS);
        byslate_polished.slab(JamiesModBlocks.POLISHED_BYSLATE_SLAB);
        byslate_polished.wall(JamiesModBlocks.POLISHED_BYSLATE_WALL);

        blockStateModelGenerator.createGenericCube(JamiesModBlocks.POLISHED_BYSLATE_BRICK);
        byslate_bricks.stairs(JamiesModBlocks.POLISHED_BYSLATE_BRICK_STAIRS);
        byslate_bricks.slab(JamiesModBlocks.POLISHED_BYSLATE_BRICK_SLAB);
        byslate_bricks.wall(JamiesModBlocks.POLISHED_BYSLATE_BRICK_WALL);

        blockStateModelGenerator.createGenericCube(JamiesModBlocks.CHISELED_POLISHED_BYSLATE);
        blockStateModelGenerator.createGenericCube(JamiesModBlocks.POLISHED_BYSLATE_SHINGLES);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }


}
