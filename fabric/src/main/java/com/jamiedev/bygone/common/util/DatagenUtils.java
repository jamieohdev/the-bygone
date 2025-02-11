package com.jamiedev.bygone.common.util;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.blocks.CasterBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.Optional;

public class DatagenUtils {

    public static final ModelTemplate ORIENTABLE_VERTICAL_WITH_BOTTOM = block("orientable_vertical", "_vertical", TextureSlot.FRONT, TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.DOWN);
    
    public static final ResourceLocation CASTER = Bygone.getModId("caster");
    
    public static void registerCasterModel(Block block, BlockModelGenerators blockStateModelGenerator) {
        TextureMapping textureMap = new TextureMapping()
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"))
                .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom"));

        TextureMapping textureMap2 = new TextureMapping()
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"))
                .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom"));

        TextureMapping textureMap3 = new TextureMapping()
                .put(TextureSlot.TOP, getCasterSubID("blaze_", "_top"))
                .put(TextureSlot.SIDE, getCasterSubID("blaze_", "_side"))
                .put(TextureSlot.FRONT, getCasterSubID("blaze_", "_front"))
                .put(TextureSlot.DOWN, getCasterSubID("blaze_", "_bottom"))
                .put(TextureSlot.BOTTOM, getCasterSubID("blaze_", "_bottom"));

        TextureMapping textureMap4 = new TextureMapping()
                .put(TextureSlot.SIDE, getCasterSubID("blaze_", "_side"))
                .put(TextureSlot.FRONT, getCasterSubID("blaze_", "_front"))
                .put(TextureSlot.DOWN, getCasterSubID("blaze_", "_bottom"))
                .put(TextureSlot.BOTTOM, getCasterSubID("blaze_", "_bottom"));

        TextureMapping textureMap5 = new TextureMapping()
                .put(TextureSlot.TOP, getCasterSubID("breeze_", "_top"))
                .put(TextureSlot.SIDE, getCasterSubID("breeze_", "_side"))
                .put(TextureSlot.FRONT, getCasterSubID("breeze_", "_front"))
                .put(TextureSlot.DOWN, getCasterSubID("breeze_", "_bottom"))
                .put(TextureSlot.BOTTOM, getCasterSubID("breeze_", "_bottom"));

        TextureMapping textureMap6 = new TextureMapping()
                .put(TextureSlot.SIDE, getCasterSubID("breeze_", "_side"))
                .put(TextureSlot.FRONT, getCasterSubID("breeze_", "_front"))
                .put(TextureSlot.DOWN, getCasterSubID("breeze_", "_bottom"))
                .put(TextureSlot.BOTTOM, getCasterSubID("breeze_", "_bottom"));

        TextureMapping textureMap7 = new TextureMapping()
                .put(TextureSlot.TOP, getCasterSubID("spike_", "_top"))
                .put(TextureSlot.SIDE, getCasterSubID("spike_", "_side"))
                .put(TextureSlot.FRONT, getCasterSubID("spike_", "_front"))
                .put(TextureSlot.DOWN, getCasterSubID("spike_", "_bottom"))
                .put(TextureSlot.BOTTOM, getCasterSubID("spike_", "_bottom"));

        TextureMapping textureMap8 = new TextureMapping()
                .put(TextureSlot.SIDE, getCasterSubID("spike_", "_side"))
                .put(TextureSlot.FRONT, getCasterSubID("spike_", "_front"))
                .put(TextureSlot.DOWN, getCasterSubID("spike_", "_bottom"))
                .put(TextureSlot.BOTTOM, getCasterSubID("spike_", "_bottom"));

        ResourceLocation identifier = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(block, textureMap, blockStateModelGenerator.modelOutput);
        ResourceLocation identifier2 = ORIENTABLE_VERTICAL_WITH_BOTTOM.create(block, textureMap2, blockStateModelGenerator.modelOutput);
        ResourceLocation identifier3 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(CASTER.withPath((path) -> {
            return "block/" + "blaze_" + path;
        }), textureMap3, blockStateModelGenerator.modelOutput);
        ResourceLocation identifier4 = ORIENTABLE_VERTICAL_WITH_BOTTOM.create(CASTER.withPath((path) -> {
            return "block/" + "blaze_" + path + "_vertical";
        }), textureMap4, blockStateModelGenerator.modelOutput);
        ResourceLocation identifier5 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(CASTER.withPath((path) -> {
            return "block/" + "breeze_" + path;
        }), textureMap5, blockStateModelGenerator.modelOutput);
        ResourceLocation identifier6 = ORIENTABLE_VERTICAL_WITH_BOTTOM.create(CASTER.withPath((path) -> {
            return "block/" + "breeze_" + path + "_vertical";
        }), textureMap6, blockStateModelGenerator.modelOutput);
        ResourceLocation identifier7 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(CASTER.withPath((path) -> {
            return "block/" + "guardian_" + path;
        }), textureMap7, blockStateModelGenerator.modelOutput);
        ResourceLocation identifier8 = ORIENTABLE_VERTICAL_WITH_BOTTOM.create(CASTER.withPath((path) -> {
            return "block/" + "guardian_" + path + "_vertical";
        }), textureMap8, blockStateModelGenerator.modelOutput);

        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(
                PropertyDispatch.properties(BlockStateProperties.FACING, CasterBlock.TYPE)
                    .select(Direction.DOWN, CasterBlock.CasterType.NONE, Variant.variant().with(VariantProperties.MODEL, identifier2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                    .select(Direction.UP, CasterBlock.CasterType.NONE, Variant.variant().with(VariantProperties.MODEL, identifier2))
                    .select(Direction.NORTH, CasterBlock.CasterType.NONE, Variant.variant().with(VariantProperties.MODEL, identifier))
                    .select(Direction.EAST, CasterBlock.CasterType.NONE, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                    .select(Direction.SOUTH, CasterBlock.CasterType.NONE, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                    .select(Direction.WEST, CasterBlock.CasterType.NONE, Variant.variant().with(VariantProperties.MODEL, identifier).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.DOWN, CasterBlock.CasterType.BLAZE, Variant.variant().with(VariantProperties.MODEL, identifier4).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.UP, CasterBlock.CasterType.BLAZE, Variant.variant().with(VariantProperties.MODEL, identifier4))
                        .select(Direction.NORTH, CasterBlock.CasterType.BLAZE, Variant.variant().with(VariantProperties.MODEL, identifier3))
                        .select(Direction.EAST, CasterBlock.CasterType.BLAZE, Variant.variant().with(VariantProperties.MODEL, identifier3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.SOUTH, CasterBlock.CasterType.BLAZE, Variant.variant().with(VariantProperties.MODEL, identifier3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.WEST, CasterBlock.CasterType.BLAZE, Variant.variant().with(VariantProperties.MODEL, identifier3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))

                        .select(Direction.DOWN, CasterBlock.CasterType.BREEZE, Variant.variant().with(VariantProperties.MODEL, identifier6).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.UP, CasterBlock.CasterType.BREEZE, Variant.variant().with(VariantProperties.MODEL, identifier6))
                        .select(Direction.NORTH, CasterBlock.CasterType.BREEZE, Variant.variant().with(VariantProperties.MODEL, identifier5))
                        .select(Direction.EAST, CasterBlock.CasterType.BREEZE, Variant.variant().with(VariantProperties.MODEL, identifier5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.SOUTH, CasterBlock.CasterType.BREEZE, Variant.variant().with(VariantProperties.MODEL, identifier5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.WEST, CasterBlock.CasterType.BREEZE, Variant.variant().with(VariantProperties.MODEL, identifier5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))

                        .select(Direction.DOWN, CasterBlock.CasterType.GUARDIAN, Variant.variant().with(VariantProperties.MODEL, identifier8).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.UP, CasterBlock.CasterType.GUARDIAN, Variant.variant().with(VariantProperties.MODEL, identifier8))
                        .select(Direction.NORTH, CasterBlock.CasterType.GUARDIAN, Variant.variant().with(VariantProperties.MODEL, identifier7))
                        .select(Direction.EAST, CasterBlock.CasterType.GUARDIAN, Variant.variant().with(VariantProperties.MODEL, identifier7).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.SOUTH, CasterBlock.CasterType.GUARDIAN, Variant.variant().with(VariantProperties.MODEL, identifier7).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.WEST, CasterBlock.CasterType.GUARDIAN, Variant.variant().with(VariantProperties.MODEL, identifier7).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                )
        );
    }

    public static ResourceLocation getCasterSubID(String prefix, String suffix) {
        return CASTER.withPath((path) -> {
            return "block/" + prefix + path + suffix;
        });
    }

    private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }
}
