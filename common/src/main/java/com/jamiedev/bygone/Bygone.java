package com.jamiedev.bygone;

import com.google.common.collect.ImmutableMap;
import com.jamiedev.bygone.entities.*;
import com.jamiedev.bygone.init.*;
import com.jamiedev.bygone.util.Consumer4;
import com.mojang.datafixers.util.Function6;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang3.function.Consumers;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class Bygone {


    public static final String MOD_ID = "bygone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {


    }

    public static void registerStrippables() {

        if (AxeItem.STRIPPABLES instanceof ImmutableMap) {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        }

        AxeItem.STRIPPABLES.put(JamiesModBlocks.ANCIENT_LOG, JamiesModBlocks.STRIPPED_ANCIENT_LOG);
        AxeItem.STRIPPABLES.put(JamiesModBlocks.ANCIENT_WOOD, JamiesModBlocks.STRIPPED_ANCIENT_WOOD);
    }

    public static ResourceLocation id(String id){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static void registerBuiltIn() {
        JamiesModBlocks.init();
        JamiesModBlockEntities.init();
        JamiesModItems.init();
        JamiesModEntityTypes.postInit();
        JamiesModBiomes.init();
        JamiesModItemGroup.registerItemgroups();
        JamiesModFeatures.init();
        JamiesModStructures.init();
        JamiesModParticleTypes.init();
        JamiesModSoundEvents.init();
    }

    public static void initAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> consumer) {
        consumer.accept(JamiesModEntityTypes.DUCK, DuckEntity.createDuckAttributes().build());
        consumer.accept(JamiesModEntityTypes.BIG_BEAK, BigBeakEntity.createBigBeakAttributes().build());
        consumer.accept(JamiesModEntityTypes.GLARE, GlareEntity.createGlareAttributes().build());
        consumer.accept(JamiesModEntityTypes.SCUTTLE, ScuttleEntity.createAttributes().build());
        consumer.accept(JamiesModEntityTypes.COELACANTH, CoelacanthEntity.createAttributes().build());
        consumer.accept(JamiesModEntityTypes.TRILOBITE, TrilobiteEntity.createAttributes().build());
        consumer.accept(JamiesModEntityTypes.MOOBOO, MoobooEntity.createAttributes().build());
        consumer.accept(JamiesModEntityTypes.COPPERBUG, CopperbugEntity.createCopperbugAttributes().build());
        consumer.accept(JamiesModEntityTypes.FUNGAL_PARENT, FungalParentEntity.createFungieAttributes().build());
    }

    public static<T extends Mob> void registerSpawnPlacements(Consumer4<EntityType<T>,SpawnPlacementType,Heightmap.Types,SpawnPlacements.SpawnPredicate<T>> consumer) {
        consumer.accept((EntityType<T>) JamiesModEntityTypes.SCUTTLE, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, iServerWorld, reason1, pos1, random1) -> ScuttleEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<? extends WaterAnimal>) entityType, iServerWorld, reason1, pos1, random1));
        consumer.accept((EntityType<T>) JamiesModEntityTypes.GLARE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GlareEntity::canSpawn);
        consumer.accept((EntityType<T>) JamiesModEntityTypes.BIG_BEAK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, BigBeakEntity::canSpawn);
        consumer.accept((EntityType<T>) JamiesModEntityTypes.TRILOBITE, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type1, world1, reason1, pos1, random1) -> TrilobiteEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<? extends WaterAnimal>) type1, world1, reason1, pos1, random1));
        consumer.accept((EntityType<T>) JamiesModEntityTypes.COPPERBUG, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, (type1, world1, spawnReason, pos1, random1) -> CopperbugEntity.canSpawn((EntityType<CopperbugEntity>) type1, world1, spawnReason, pos1, random1));
        //SpawnRestriction.register(COPPERBUG, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CopperbugEntity::canSpawn);

        consumer.accept((EntityType<T>) JamiesModEntityTypes.COELACANTH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, world, reason, pos, random) -> CoelacanthEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<CoelacanthEntity>) type, world, reason, pos, random));
    }

    public static void addValidBlocks(BiConsumer<BlockEntityType<?>, Block> consumer) {
        consumer.accept(BlockEntityType.SIGN,JamiesModBlocks.ANCIENT_SIGN);
        consumer.accept(BlockEntityType.SIGN,JamiesModBlocks.ANCIENT_WALL_SIGN);

        consumer.accept(BlockEntityType.HANGING_SIGN,JamiesModBlocks.ANCIENT_HANGING_SIGN);
        consumer.accept(BlockEntityType.HANGING_SIGN,JamiesModBlocks.ANCIENT_WALL_HANGING_SIGN);
    }

    public static void addFurnaceFuels(BiConsumer<ItemLike,Integer> consumer) {
        consumer.accept(JamiesModBlocks.ANCIENT_SAPLING, 300);
        consumer.accept(JamiesModBlocks.CAVE_VINES_PLANT, 200);
        consumer.accept(JamiesModBlocks.CAVE_VINES, 200);
        consumer.accept(JamiesModBlocks.CHARNIA, 50);
        consumer.accept(JamiesModBlocks.MONTSECHIA, 50);
        consumer.accept(JamiesModBlocks.RAFFLESIA, 500);
        consumer.accept(JamiesModBlocks.SAGARIA, 50);
        consumer.accept(JamiesModBlocks.SHORT_GRASS, 300);
        consumer.accept(JamiesModBlocks.TALL_GRASS, 200);
        consumer.accept(JamiesModBlocks.ANCIENT_ROOTS, 50);
        consumer.accept(JamiesModBlocks.ALPHA_MOSS_CARPET, 30);
        consumer.accept(JamiesModBlocks.ALPHA_MOSS_BLOCK, 60);
        consumer.accept(JamiesModBlocks.BELLADONNA, 50);
        consumer.accept(JamiesModBlocks.COLEUS, 50);

        consumer.accept(JamiesModBlocks.ANCIENT_WOOD, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_LOG, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_PLANKS, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_SLAB, 150);
        consumer.accept(JamiesModBlocks.ANCIENT_STAIRS, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_PRESSURE_PLATE, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_BUTTON, 100);
        consumer.accept(JamiesModBlocks.ANCIENT_TRAPDOOR, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_FENCE_GATE, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_FENCE, 300);
        consumer.accept(JamiesModBlocks.ANCIENT_DOOR, 200);

    }

    public static void addCompostables(BiConsumer<ItemLike,Float> consumer) {
        consumer.accept(JamiesModBlocks.ANCIENT_SAPLING, 0.5F);
        consumer.accept(JamiesModBlocks.CAVE_VINES_PLANT, 0.3F);
        consumer.accept(JamiesModBlocks.CAVE_VINES, 0.3F);
        consumer.accept(JamiesModBlocks.CHARNIA, 0.2F);
        consumer.accept(JamiesModBlocks.MONTSECHIA, 0.2F);
        consumer.accept(JamiesModBlocks.RAFFLESIA, 0.8F);
        consumer.accept(JamiesModBlocks.SAGARIA, 0.2F);
        consumer.accept(JamiesModBlocks.SHORT_GRASS, 0.2F);
        consumer.accept(JamiesModBlocks.TALL_GRASS, 0.4F);
        consumer.accept(JamiesModBlocks.ANCIENT_ROOTS, 0.3F);
        consumer.accept(JamiesModBlocks.ANCIENT_VINE, 0.3F);
        consumer.accept(JamiesModBlocks.ANCIENT_LEAVES, 0.3F);

        consumer.accept(JamiesModBlocks.ALPHA_MOSS_CARPET, 0.3F);
        consumer.accept(JamiesModBlocks.ALPHA_MOSS_BLOCK, 0.3F);

        consumer.accept(JamiesModBlocks.CREOSOTE_SPROUTS, 0.2F);
        consumer.accept(JamiesModBlocks.SHELF_MOLD, 0.2F);
        consumer.accept(JamiesModBlocks.SHELF_ROOTS, 0.2F);
        consumer.accept(JamiesModBlocks.SHELF_FUNGUS, 0.2F);
        consumer.accept(JamiesModBlocks.SHELF_SPROUTS, 0.2F);

        consumer.accept(JamiesModBlocks.ORANGE_FUNGI_VINES, 0.1F);
        consumer.accept(JamiesModBlocks.PINK_FUNGI_VINES, 0.1F);
        consumer.accept(JamiesModBlocks.PURPLE_FUNGI_VINES, 0.1F);
        consumer.accept(JamiesModBlocks.BELLADONNA,0.1F);
        consumer.accept(JamiesModBlocks.COLEUS, 0.1F);
    }

        public static void addFlammable() {
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_WOOD, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_LOG, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_PLANKS, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_SLAB, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_STAIRS, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_PRESSURE_PLATE, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_BUTTON, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_TRAPDOOR, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_FENCE_GATE, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_FENCE, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(JamiesModBlocks.ANCIENT_DOOR, 5, 20);
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