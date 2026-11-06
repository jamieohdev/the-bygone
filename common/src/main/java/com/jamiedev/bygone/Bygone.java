package com.jamiedev.bygone;

import com.jamiedev.bygone.common.entity.*;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.mixin.AxeItemAccess;
import com.jamiedev.bygone.core.registry.*;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bygone {
    public static final String MOD_ID = "bygone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        BGFluids.init();
        BGBlocks.init();
        BGBlockEntities.init();
        BGAttributes.init();
        BGDataComponentTypes.init();
        BGInterAttachmentTypes.init();
        BGItems.init();
        BGMobEffects.init();
        BGEntityTypes.init();
        BGBiomes.init();
        BGBlockProperties.init();
        BGItemGroups.register();
        BGFishingTables.init();
        BGDecoratedPotPatterns.init();
    }

    public static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static void registerBuiltIn() {
        BGFeatures.init();
        BGStructures.init();
        BGParticleTypes.init();
        BGSoundEvents.init();
    }

    public static void addValidBlocks(BiConsumer<BlockEntityType<?>, Block> consumer) {
        consumer.accept(BlockEntityType.SIGN, BGBlocks.ANCIENT_SIGN.get());
        consumer.accept(BlockEntityType.SIGN, BGBlocks.ANCIENT_WALL_SIGN.get());
        consumer.accept(BlockEntityType.SIGN, BGBlocks.SABLE_SIGN.get());
        consumer.accept(BlockEntityType.SIGN, BGBlocks.SABLE_WALL_SIGN.get());

        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.ANCIENT_HANGING_SIGN.get());
        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.ANCIENT_WALL_HANGING_SIGN.get());
        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.SABLE_HANGING_SIGN.get());
        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.SABLE_WALL_HANGING_SIGN.get());

        consumer.accept(BlockEntityType.BRUSHABLE_BLOCK, BGBlocks.SUSPICIOUS_SHELLSAND.get());
        consumer.accept(BlockEntityType.BRUSHABLE_BLOCK, BGBlocks.SUSPICIOUS_CLAYSTONE.get());
    }

    public static void addFlammable() {
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_WOOD.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_LOG.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_PLANKS.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_SLAB.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_STAIRS.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_PRESSURE_PLATE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_BUTTON.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_TRAPDOOR.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_FENCE_GATE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_FENCE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_DOOR.get(), 5, 20);

        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_WOOD.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_LOG.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_PLANKS.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_SLAB.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_STAIRS.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_PRESSURE_PLATE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_BUTTON.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_TRAPDOOR.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_FENCE_GATE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_FENCE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(BGBlocks.SABLE_DOOR.get(), 5, 20);
    }

    public static boolean isSprinklerNearby(LevelReader world, BlockPos pos) {
        Iterable<BlockPos> var2 = BlockPos.betweenClosed(pos.offset(-15, 0, -15), pos.offset(15, 1, 15));

        for (BlockPos pos1 : var2) {
            if (world.getBlockState(pos1).is(JamiesModTag.SPRINKLERS)) {
                return true;
            }
        }

        return false;
    }

    public static Stream<Block> getKnownBlocks() {
        return getKnown(BuiltInRegistries.BLOCK);
    }

    public static Stream<Item> getKnownItems() {
        return getKnown(BuiltInRegistries.ITEM);
    }

    public static <V> Stream<V> getKnown(Registry<V> registry) {
        return registry.stream().filter(o -> registry.getKey(o).getNamespace().equals(MOD_ID));
    }
}
