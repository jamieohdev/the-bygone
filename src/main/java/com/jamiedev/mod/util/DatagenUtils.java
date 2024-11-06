package com.jamiedev.mod.util;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.blocks.CasterBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class DatagenUtils {

    public static final Model ORIENTABLE_VERTICAL_WITH_BOTTOM = block("orientable_vertical", "_vertical", TextureKey.FRONT, TextureKey.SIDE, TextureKey.BOTTOM, TextureKey.DOWN);
    
    public static final Identifier CASTER = JamiesMod.getModId("caster");
    
    public static void registerCasterModel(Block block, BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"))
                .put(TextureKey.DOWN, TextureMap.getSubId(block, "_bottom"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom"));

        TextureMap textureMap2 = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"))
                .put(TextureKey.DOWN, TextureMap.getSubId(block, "_bottom"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom"));

        TextureMap textureMap3 = new TextureMap()
                .put(TextureKey.TOP, getCasterSubID("blaze_", "_top"))
                .put(TextureKey.SIDE, getCasterSubID("blaze_", "_side"))
                .put(TextureKey.FRONT, getCasterSubID("blaze_", "_front"))
                .put(TextureKey.DOWN, getCasterSubID("blaze_", "_bottom"))
                .put(TextureKey.BOTTOM, getCasterSubID("blaze_", "_bottom"));

        TextureMap textureMap4 = new TextureMap()
                .put(TextureKey.SIDE, getCasterSubID("blaze_", "_side"))
                .put(TextureKey.FRONT, getCasterSubID("blaze_", "_front"))
                .put(TextureKey.DOWN, getCasterSubID("blaze_", "_bottom"))
                .put(TextureKey.BOTTOM, getCasterSubID("blaze_", "_bottom"));

        TextureMap textureMap5 = new TextureMap()
                .put(TextureKey.TOP, getCasterSubID("breeze_", "_top"))
                .put(TextureKey.SIDE, getCasterSubID("breeze_", "_side"))
                .put(TextureKey.FRONT, getCasterSubID("breeze_", "_front"))
                .put(TextureKey.DOWN, getCasterSubID("breeze_", "_bottom"))
                .put(TextureKey.BOTTOM, getCasterSubID("breeze_", "_bottom"));

        TextureMap textureMap6 = new TextureMap()
                .put(TextureKey.SIDE, getCasterSubID("breeze_", "_side"))
                .put(TextureKey.FRONT, getCasterSubID("breeze_", "_front"))
                .put(TextureKey.DOWN, getCasterSubID("breeze_", "_bottom"))
                .put(TextureKey.BOTTOM, getCasterSubID("breeze_", "_bottom"));

        TextureMap textureMap7 = new TextureMap()
                .put(TextureKey.TOP, getCasterSubID("spike_", "_top"))
                .put(TextureKey.SIDE, getCasterSubID("spike_", "_side"))
                .put(TextureKey.FRONT, getCasterSubID("spike_", "_front"))
                .put(TextureKey.DOWN, getCasterSubID("spike_", "_bottom"))
                .put(TextureKey.BOTTOM, getCasterSubID("spike_", "_bottom"));

        TextureMap textureMap8 = new TextureMap()
                .put(TextureKey.SIDE, getCasterSubID("spike_", "_side"))
                .put(TextureKey.FRONT, getCasterSubID("spike_", "_front"))
                .put(TextureKey.DOWN, getCasterSubID("spike_", "_bottom"))
                .put(TextureKey.BOTTOM, getCasterSubID("spike_", "_bottom"));

        Identifier identifier = Models.ORIENTABLE_WITH_BOTTOM.upload(block, textureMap, blockStateModelGenerator.modelCollector);
        Identifier identifier2 = ORIENTABLE_VERTICAL_WITH_BOTTOM.upload(block, textureMap2, blockStateModelGenerator.modelCollector);
        Identifier identifier3 = Models.ORIENTABLE_WITH_BOTTOM.upload(CASTER.withPath((path) -> {
            return "block/" + "blaze_" + path;
        }), textureMap3, blockStateModelGenerator.modelCollector);
        Identifier identifier4 = ORIENTABLE_VERTICAL_WITH_BOTTOM.upload(CASTER.withPath((path) -> {
            return "block/" + "blaze_" + path + "_vertical";
        }), textureMap4, blockStateModelGenerator.modelCollector);
        Identifier identifier5 = Models.ORIENTABLE_WITH_BOTTOM.upload(CASTER.withPath((path) -> {
            return "block/" + "breeze_" + path;
        }), textureMap5, blockStateModelGenerator.modelCollector);
        Identifier identifier6 = ORIENTABLE_VERTICAL_WITH_BOTTOM.upload(CASTER.withPath((path) -> {
            return "block/" + "breeze_" + path + "_vertical";
        }), textureMap6, blockStateModelGenerator.modelCollector);
        Identifier identifier7 = Models.ORIENTABLE_WITH_BOTTOM.upload(CASTER.withPath((path) -> {
            return "block/" + "guardian_" + path;
        }), textureMap7, blockStateModelGenerator.modelCollector);
        Identifier identifier8 = ORIENTABLE_VERTICAL_WITH_BOTTOM.upload(CASTER.withPath((path) -> {
            return "block/" + "guardian_" + path + "_vertical";
        }), textureMap8, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(
                BlockStateVariantMap.create(Properties.FACING, CasterBlock.TYPE)
                    .register(Direction.DOWN, CasterBlock.CasterType.NONE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier2).put(VariantSettings.X, VariantSettings.Rotation.R180))
                    .register(Direction.UP, CasterBlock.CasterType.NONE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier2))
                    .register(Direction.NORTH, CasterBlock.CasterType.NONE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier))
                    .register(Direction.EAST, CasterBlock.CasterType.NONE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                    .register(Direction.SOUTH, CasterBlock.CasterType.NONE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                    .register(Direction.WEST, CasterBlock.CasterType.NONE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                        .register(Direction.DOWN, CasterBlock.CasterType.BLAZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier4).put(VariantSettings.X, VariantSettings.Rotation.R180))
                        .register(Direction.UP, CasterBlock.CasterType.BLAZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier4))
                        .register(Direction.NORTH, CasterBlock.CasterType.BLAZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier3))
                        .register(Direction.EAST, CasterBlock.CasterType.BLAZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier3).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.SOUTH, CasterBlock.CasterType.BLAZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier3).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.WEST, CasterBlock.CasterType.BLAZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier3).put(VariantSettings.Y, VariantSettings.Rotation.R270))

                        .register(Direction.DOWN, CasterBlock.CasterType.BREEZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier6).put(VariantSettings.X, VariantSettings.Rotation.R180))
                        .register(Direction.UP, CasterBlock.CasterType.BREEZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier6))
                        .register(Direction.NORTH, CasterBlock.CasterType.BREEZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier5))
                        .register(Direction.EAST, CasterBlock.CasterType.BREEZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier5).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.SOUTH, CasterBlock.CasterType.BREEZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier5).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.WEST, CasterBlock.CasterType.BREEZE, BlockStateVariant.create().put(VariantSettings.MODEL, identifier5).put(VariantSettings.Y, VariantSettings.Rotation.R270))

                        .register(Direction.DOWN, CasterBlock.CasterType.GUARDIAN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier8).put(VariantSettings.X, VariantSettings.Rotation.R180))
                        .register(Direction.UP, CasterBlock.CasterType.GUARDIAN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier8))
                        .register(Direction.NORTH, CasterBlock.CasterType.GUARDIAN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier7))
                        .register(Direction.EAST, CasterBlock.CasterType.GUARDIAN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier7).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.SOUTH, CasterBlock.CasterType.GUARDIAN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier7).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.WEST, CasterBlock.CasterType.GUARDIAN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier7).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                )
        );
    }

    public static Identifier getCasterSubID(String prefix, String suffix) {
        return CASTER.withPath((path) -> {
            return "block/" + prefix + path + suffix;
        });
    }

    private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.ofVanilla("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }
}
