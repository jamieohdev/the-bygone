package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class BGBlockEntitiesFabric
{
    public static void register() {
        FabricBlockEntityType fabricBrushableBlock = (FabricBlockEntityType) BlockEntityType.BRUSHABLE_BLOCK;
        fabricBrushableBlock.addSupportedBlock(BGBlocks.SUSPICIOUS_SHELLSAND.get());
        fabricBrushableBlock.addSupportedBlock(BGBlocks.SUSPICIOUS_CLAYSTONE.get());
    }

    @NotNull
    private static <T extends BlockEntity> BlockEntityType<T> register(@NotNull String path, @NotNull BlockEntityType.BlockEntitySupplier<T> factory, @NotNull Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bygone.id(path),
                BlockEntityType.Builder.of(factory, blocks).build(null));
    }
}
