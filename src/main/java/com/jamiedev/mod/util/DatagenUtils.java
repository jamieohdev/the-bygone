package com.jamiedev.mod.util;

import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.data.client.BlockStateModelGenerator.createSingletonBlockState;

public class DatagenUtils {
    //
    public static void registerCasterModel(Block block, BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"))
                .put(TextureKey.DOWN, TextureMap.getSubId(block, "_bottom"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom"));

        TextureMap textureMap2 = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(Blocks.FURNACE, "_top"))
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front_vertical"));

        Identifier identifier = Models.ORIENTABLE_WITH_BOTTOM.upload(block, textureMap, blockStateModelGenerator.modelCollector);
        Identifier identifier2 = Models.ORIENTABLE_VERTICAL.upload(block, textureMap2, blockStateModelGenerator.modelCollector);

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
}
