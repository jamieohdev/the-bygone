package com.jamiedev.mod.util;

import com.jamiedev.mod.init.JamiesModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import java.util.Optional;

public class DatagenUtils {

    public static final Model ORIENTABLE_VERTICAL_WITH_BOTTOM = block("orientable_vertical", "_vertical", TextureKey.FRONT, TextureKey.SIDE, TextureKey.BOTTOM, TextureKey.DOWN);

    //
    public static void registerCasterModel(Block block, BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"))
                .put(TextureKey.DOWN, TextureMap.getSubId(block, "_bottom"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(JamiesModBlocks.CASTER, "_bottom"));

        TextureMap textureMap2 = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"))
                .put(TextureKey.DOWN, TextureMap.getSubId(JamiesModBlocks.CASTER, "_bottom"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(JamiesModBlocks.CASTER, "_bottom"));

        Identifier identifier = Models.ORIENTABLE_WITH_BOTTOM.upload(block, textureMap, blockStateModelGenerator.modelCollector);
        Identifier identifier2 = ORIENTABLE_VERTICAL_WITH_BOTTOM.upload(block, textureMap2, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(
                BlockStateVariantMap.create(Properties.FACING)
                    .register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier2).put(VariantSettings.X, VariantSettings.Rotation.R180))
                    .register(Direction.UP, BlockStateVariant.create().put(VariantSettings.MODEL, identifier2))
                    .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier))
                    .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                    .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                    .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                )
        );
    }

    private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.ofVanilla("block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }
}
