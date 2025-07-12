package com.jamiedev.bygone;

import com.jamiedev.bygone.common.entity.*;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.*;
import com.jamiedev.bygone.core.mixin.AxeItemAccess;
import com.jamiedev.bygone.common.util.Consumer4;
import com.jamiedev.bygone.core.util.HeightGetter;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
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
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Bygone {
    public static final Supplier<DataComponentType<Integer>> MAP_HEIGHT = JinxedRegistryHelper.register(BuiltInRegistries.DATA_COMPONENT_TYPE, "bygone", "map_height", () -> HeightGetter.MAP_HEIGHT);

    public static final String MOD_ID = "bygone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        BGBlocks.init();
        BGBlockEntities.init();
        BGItems.init();
        BGEntityTypes.postInit();
        BGBiomes.init();
        BGItemGroups.register();
    }

    public static void registerStrippables() {

        Bygone.LOGGER.debug("Bygone: Registering strippable Blocks...");

        Map<Block, Block> stripables = new IdentityHashMap<>(AxeItemAccess.getStripables());

        stripables.put(BGBlocks.ANCIENT_LOG.get(), BGBlocks.STRIPPED_ANCIENT_LOG.get());
        stripables.put(BGBlocks.ANCIENT_WOOD.get(), BGBlocks.STRIPPED_ANCIENT_WOOD.get());


        AxeItemAccess.setStripables(stripables);
    }

    public static ResourceLocation id(String id){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static void registerBuiltIn() {
        BGFeatures.init();
        BGStructures.init();
        BGParticleTypes.init();
        BGSoundEvents.init();
    }

    public static void initAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> consumer) {
        consumer.accept(BGEntityTypes.DUCK.get(), DuckEntity.createDuckAttributes().build());
        consumer.accept(BGEntityTypes.BIG_BEAK.get(), BigBeakEntity.createBigBeakAttributes().build());
        consumer.accept(BGEntityTypes.GLARE.get(), GlareEntity.createGlareAttributes().build());
        consumer.accept(BGEntityTypes.SCUTTLE.get(), ScuttleEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.COELACANTH.get(), CoelacanthEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.TRILOBITE.get(), TrilobiteEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.MOOBOO.get(), MoobooEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.COPPERBUG.get(), CopperbugEntity.createCopperbugAttributes().build());
        consumer.accept(BGEntityTypes.FUNGAL_PARENT.get(), FungalParentEntity.createFungieAttributes().build());
        consumer.accept(BGEntityTypes.PESKY.get(), PeskyEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.PEST.get(), PestEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.WHISKBILL.get(), WhiskbillEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.NECTAUR.get(), NectaurEntity.createAttributes().build());
        consumer.accept(BGEntityTypes.LITHY.get(), LithyEntity.createAttributes().build());
    }



    @SuppressWarnings("unchecked")
    public static<T extends Mob> void registerSpawnPlacements(Consumer4<EntityType<T>,SpawnPlacementType,Heightmap.Types,SpawnPlacements.SpawnPredicate<T>> consumer) {
        consumer.accept((EntityType<T>) BGEntityTypes.SCUTTLE.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, iServerWorld, reason1, pos1, random1) -> ScuttleEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<? extends WaterAnimal>) entityType, iServerWorld, reason1, pos1, random1));
        consumer.accept((EntityType<T>) BGEntityTypes.GLARE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GlareEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.BIG_BEAK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, BigBeakEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.TRILOBITE.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type1, world1, reason1, pos1, random1) -> TrilobiteEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<? extends WaterAnimal>) type1, world1, reason1, pos1, random1));
        consumer.accept((EntityType<T>) BGEntityTypes.COPPERBUG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, (type1, world1, spawnReason, pos1, random1) -> CopperbugEntity.canSpawn((EntityType<CopperbugEntity>) type1, world1, spawnReason, pos1, random1));
        consumer.accept((EntityType<T>) BGEntityTypes.COELACANTH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, world, reason, pos, random) -> CoelacanthEntity.checkSurfaceWaterAnimalSpawnRule((EntityType<CoelacanthEntity>) type, world, reason, pos, random));
        consumer.accept((EntityType<T>) BGEntityTypes.PESKY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GlareEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.PEST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GlareEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.NECTAUR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, BigBeakEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.WHISKBILL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, BigBeakEntity::canSpawn);
        consumer.accept((EntityType<T>) BGEntityTypes.LITHY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, LithyEntity::canSpawn);
    }

    public static void addValidBlocks(BiConsumer<BlockEntityType<?>, Block> consumer) {
        consumer.accept(BlockEntityType.SIGN, BGBlocks.ANCIENT_SIGN.get());
        consumer.accept(BlockEntityType.SIGN, BGBlocks.ANCIENT_WALL_SIGN.get());

        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.ANCIENT_HANGING_SIGN.get());
        consumer.accept(BlockEntityType.HANGING_SIGN, BGBlocks.ANCIENT_WALL_HANGING_SIGN.get());
    }


    public static void addFlammable() {
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_WOOD.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_LOG.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_PLANKS.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_SLAB.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_STAIRS.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_PRESSURE_PLATE.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_BUTTON.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_TRAPDOOR.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_FENCE_GATE.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_FENCE.get(), 5, 20);
        ((FireBlock)Blocks.FIRE).setFlammable(BGBlocks.ANCIENT_DOOR.get(), 5, 20);
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

    private static int timeInBygone;
    private static Level level;

    public static Level level() {
        return level;
    }

    public static void tickCow(Cow cow) {

        if (canCowZombify(cow)) {
            ++timeInBygone;
            if (timeInBygone > 300) {

                zombify(cow);
            }
        } else {
            timeInBygone= 0;
        }


    }

    public static boolean canCowZombify(Cow cow) {
        return cow.level().dimension() == BGDimensions.BYGONE_LEVEL_KEY && !cow.isNoAi();
    }

    protected static void zombify(Cow cow) {
        MoobooEntity moobooEntity = cow.convertTo(BGEntityTypes.MOOBOO.get(), true);
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