package com.jamiedev.bygone;

import com.google.common.collect.ImmutableMap;
import com.jamiedev.bygone.entities.*;
import com.jamiedev.bygone.init.*;
import com.jamiedev.bygone.util.Consumer4;
import com.mojang.datafixers.util.Function6;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang3.function.Consumers;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

}