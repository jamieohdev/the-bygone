package com.jamiedev.bygone;

import com.jamiedev.bygone.common.entity.*;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.*;
import com.jamiedev.bygone.core.mixin.AxeItemAccess;
import com.jamiedev.bygone.core.platform.Services;
import com.jamiedev.bygone.common.util.Consumer4;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Bygone {


    public static final String MOD_ID = "bygone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {


    }

    public static void registerStrippables() {

        Bygone.LOGGER.debug("Bygone: Registering strippable Blocks...");

        Map<Block, Block> stripables = new IdentityHashMap<>(AxeItemAccess.getStripables());

        stripables.put(BGBlocks.ANCIENT_LOG, BGBlocks.STRIPPED_ANCIENT_LOG);
        stripables.put(BGBlocks.ANCIENT_WOOD, BGBlocks.STRIPPED_ANCIENT_WOOD);


        AxeItemAccess.setStripables(stripables);
        Bygone.LOGGER.info("Bygone: Strippables registered!");
    }

    public static ResourceLocation id(String id){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static void registerBuiltIn() {
        BGBlocks.init();
        BGBlockEntities.init();
        BGItems.init();
        BGEntityTypes.postInit();
        BGBiomes.init();
        BGItemGroups.registerItemgroups();
        BGFeatures.init();
        BGStructures.init();
        BGParticleTypes.init();
        BGSoundEvents.init();
    }

    public static void initAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> consumer) {
        consumer.accept(BGEntityTypes.DUCK, DuckEntity.createDuckAttributes().build());
        consumer.accept(BGEntityTypes.BIG_BEAK, BigBeakEntity.createBigBeakAttributes().build());
        consumer.accept(BGEntityTypes.GLARE, GlareEntity.createGlareAttributes().build());
        consumer.accept(BGEntityTypes.SCUTTLE, ScuttleEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.COELACANTH, CoelacanthEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.TRILOBITE, TrilobiteEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.MOOBOO, MoobooEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.COPPERBUG, CopperbugEntity.createCopperbugAttributes().build());
        consumer.accept(BGEntityTypes.FUNGAL_PARENT, FungalParentEntity.createFungieAttributes().build());
        consumer.accept(BGEntityTypes.PEST, PestEntity.createAttributes().build());
    }



    @SuppressWarnings("unchecked")
    public static<T extends Mob> void registerSpawnPlacements(Consumer4<EntityType<T>,SpawnPlacementType,Heightmap.Types,SpawnPlacements.SpawnPredicate<T>> consumer) {
        consumer.accept((EntityType<T>) BGEntityTypes.SCUTTLE, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, iServerWorld, reason1, pos1, random1) -> ScuttleEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<? extends WaterAnimal>) entityType, iServerWorld, reason1, pos1, random1));
        consumer.accept((EntityType<T>) BGEntityTypes.GLARE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GlareEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.BIG_BEAK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, BigBeakEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.TRILOBITE, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type1, world1, reason1, pos1, random1) -> TrilobiteEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<? extends WaterAnimal>) type1, world1, reason1, pos1, random1));
        consumer.accept((EntityType<T>) BGEntityTypes.COPPERBUG, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, (type1, world1, spawnReason, pos1, random1) -> CopperbugEntity.canSpawn((EntityType<CopperbugEntity>) type1, world1, spawnReason, pos1, random1));
        consumer.accept((EntityType<T>) BGEntityTypes.COELACANTH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, world, reason, pos, random) -> CoelacanthEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<CoelacanthEntity>) type, world, reason, pos, random));
        consumer.accept((EntityType<T>) BGEntityTypes.PEST, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GlareEntity::canSpawn);
    }

    public static void addValidBlocks(BiConsumer<BlockEntityType<?>, Block> consumer) {
        consumer.accept(BlockEntityType.SIGN, BGBlocks.ANCIENT_SIGN);
        consumer.accept(BlockEntityType.SIGN, BGBlocks.ANCIENT_WALL_SIGN);

        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.ANCIENT_HANGING_SIGN);
        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.ANCIENT_WALL_HANGING_SIGN);
    }

    public static void addFurnaceFuels(BiConsumer<ItemLike,Integer> consumer) {
        consumer.accept(BGBlocks.ANCIENT_SAPLING, 300);
        consumer.accept(BGBlocks.CAVE_VINES_PLANT, 200);
        consumer.accept(BGBlocks.CAVE_VINES, 200);
        consumer.accept(BGBlocks.CHARNIA, 50);
        consumer.accept(BGBlocks.MONTSECHIA, 50);
        consumer.accept(BGBlocks.RAFFLESIA, 500);
        consumer.accept(BGBlocks.SAGARIA, 50);
        consumer.accept(BGBlocks.SHORT_GRASS, 300);
        consumer.accept(BGBlocks.TALL_GRASS, 200);
        consumer.accept(BGBlocks.ANCIENT_ROOTS, 50);
        consumer.accept(BGBlocks.ALPHA_MOSS_CARPET, 30);
        consumer.accept(BGBlocks.ALPHA_MOSS_BLOCK, 60);
        consumer.accept(BGBlocks.BELLADONNA, 50);
        consumer.accept(BGBlocks.COLEUS, 50);

        consumer.accept(BGBlocks.ANCIENT_WOOD, 300);
        consumer.accept(BGBlocks.ANCIENT_LOG, 300);
        consumer.accept(BGBlocks.ANCIENT_PLANKS, 300);
        consumer.accept(BGBlocks.ANCIENT_SLAB, 150);
        consumer.accept(BGBlocks.ANCIENT_STAIRS, 300);
        consumer.accept(BGBlocks.ANCIENT_PRESSURE_PLATE, 300);
        consumer.accept(BGBlocks.ANCIENT_BUTTON, 100);
        consumer.accept(BGBlocks.ANCIENT_TRAPDOOR, 300);
        consumer.accept(BGBlocks.ANCIENT_FENCE_GATE, 300);
        consumer.accept(BGBlocks.ANCIENT_FENCE, 300);
        consumer.accept(BGBlocks.ANCIENT_DOOR, 200);
        consumer.accept(BGBlocks.WHIRLIWEED, 75);
        consumer.accept(BGBlocks.BIG_WHIRLIWEED, 125);

    }

    public static void addCompostables(BiConsumer<ItemLike,Float> consumer) {
        consumer.accept(BGBlocks.ANCIENT_SAPLING, 0.5F);
        consumer.accept(BGBlocks.CAVE_VINES_PLANT, 0.3F);
        consumer.accept(BGBlocks.CAVE_VINES, 0.3F);
        consumer.accept(BGBlocks.CHARNIA, 0.2F);
        consumer.accept(BGBlocks.MONTSECHIA, 0.2F);
        consumer.accept(BGBlocks.RAFFLESIA, 0.8F);
        consumer.accept(BGBlocks.SAGARIA, 0.2F);
        consumer.accept(BGBlocks.SHORT_GRASS, 0.2F);
        consumer.accept(BGBlocks.TALL_GRASS, 0.4F);
        consumer.accept(BGBlocks.ANCIENT_ROOTS, 0.3F);
        consumer.accept(BGBlocks.ANCIENT_VINE, 0.5F);
        consumer.accept(BGBlocks.ANCIENT_LEAVES, 0.3F);

        consumer.accept(BGBlocks.ALPHA_MOSS_CARPET, 0.3F);
        consumer.accept(BGBlocks.ALPHA_MOSS_BLOCK, 0.3F);

        consumer.accept(BGBlocks.CREOSOTE_SPROUTS, 0.2F);
        consumer.accept(BGBlocks.SHELF_MOLD, 0.2F);
        consumer.accept(BGBlocks.SHELF_ROOTS, 0.2F);
        consumer.accept(BGBlocks.SHELF_FUNGUS, 0.2F);
        consumer.accept(BGBlocks.SHELF_SPROUTS, 0.2F);

        consumer.accept(BGBlocks.ORANGE_FUNGI_VINES, 0.5F);
        consumer.accept(BGBlocks.PINK_FUNGI_VINES, 0.5F);
        consumer.accept(BGBlocks.PURPLE_FUNGI_VINES, 0.5F);
        consumer.accept(BGBlocks.BELLADONNA,0.1F);
        consumer.accept(BGBlocks.COLEUS, 0.1F);
        consumer.accept(BGBlocks.WHIRLIWEED, 0.3F);
        consumer.accept(BGBlocks.BIG_WHIRLIWEED, 0.4F);
    }

    public static void addWoodTypes() {

    }

    public static void addFlammable() {
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_WOOD, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_LOG, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_PLANKS, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_SLAB, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_STAIRS, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_PRESSURE_PLATE, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_BUTTON, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_TRAPDOOR, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_FENCE_GATE, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_FENCE, 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_DOOR, 5, 20);
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

    public static void tickCow(Cow cow) {

        if (canCowZombify(cow)) {
            int timeInBygone = Services.PLATFORM.getTimeInBygone(cow);

            if (timeInBygone > 300) {
                zombify(cow);
            }
            Services.PLATFORM.setTimeInBygone(cow,++timeInBygone);

        } else {
            Services.PLATFORM.setTimeInBygone(cow,0);
        }

    }

    public static boolean canCowZombify(Cow cow) {
        return cow.level().dimension() == BGDimensions.BYGONE_LEVEL_KEY && !cow.isNoAi();
    }

    protected static void zombify(Cow cow) {
        MoobooEntity moobooEntity = cow.convertTo(BGEntityTypes.MOOBOO, true);
        if (moobooEntity != null) {
            moobooEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
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