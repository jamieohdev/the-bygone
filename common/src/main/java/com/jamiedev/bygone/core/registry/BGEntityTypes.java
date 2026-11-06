package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.*;
import com.jamiedev.bygone.common.entity.projectile.*;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;

import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BGEntityTypes {
    public static final Supplier<EntityType<BygonePortalEntity>> BYGONE_PORTAL = registerEntityType(
            "bygone_portal", () ->
                    EntityType.Builder.<BygonePortalEntity>of(BygonePortalEntity::new, MobCategory.MISC)
                            .sized(1.5F, 1.5F)
                            .clientTrackingRange(5)
                            .build("")
    );

    public static final Supplier<EntityType<HookEntity>> HOOK = registerEntityType(
            "hook", () ->
                    EntityType.Builder.<HookEntity>of(HookEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(5)
                            .build("")
    );

    public static final Supplier<EntityType<BigBeakEntity>> BIG_BEAK = registerEntityType(
            "big_beak",
            () -> EntityType.Builder.of(BigBeakEntity::new, MobCategory.CREATURE).sized(1.0F, 2.0F).build("")
    );

    public static final Supplier<EntityType<CoelacanthEntity>> COELACANTH = registerEntityType(
            "coelacanth",
            () -> EntityType.Builder.of(CoelacanthEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.6F).build("")
    );

    public static final Supplier<EntityType<DuckEntity>> DUCK = registerEntityType(
            "duck",
            () -> EntityType.Builder.of(DuckEntity::new, MobCategory.CREATURE).sized(0.4F, 0.7F).build("")
    );

    public static final Supplier<EntityType<FungalParentEntity>> FUNGAL_PARENT = registerEntityType(
            "fungal_parent",
            () -> EntityType.Builder.of(FungalParentEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.1F)
                    .clientTrackingRange(8)
                    .build("")
    );

    public static final Supplier<EntityType<ExoticArrowEntity>> EXOTIC_ARROW = registerEntityType(
            "exotic_arrow",
            () -> EntityType.Builder.<ExoticArrowEntity>of(ExoticArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("")
    );

    public static final Supplier<EntityType<LithoArrowEntity>> LITHOPLASM_ARROW = registerEntityType(
            "lithoplasm_arrow",
            () -> EntityType.Builder.<LithoArrowEntity>of(LithoArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("")
    );

    public static final Supplier<EntityType<NectaurPetalEntity>> NECTAUR_PETAL = registerEntityType(
            "nectaur_petal",
            () -> EntityType.Builder.<NectaurPetalEntity>of(NectaurPetalEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("")
    );

    public static final Supplier<EntityType<AmoebaEntity>> AMOEBA = registerEntityType(
            "amoeba", () ->
                    EntityType.Builder.of(AmoebaEntity::new, MobCategory.CREATURE)
                            .sized(0.8F, 0.2F).build("")
    );

    public static final Supplier<EntityType<SkipperEntity>> SKIPPER = registerEntityType(
            "skipper", () ->
                    EntityType.Builder.of(SkipperEntity::new, MobCategory.CREATURE)
                            .sized(1.8F, 1.3F)
                            .eyeHeight(0.85F)
                            .passengerAttachments(new Vec3(0.0, 11.5 / 16.0 + 0.001, 3.0 / 16.0))
                            .clientTrackingRange(10)
                            .build("")
    );

    public static final Supplier<EntityType<AquifawnEntity>> AQUIFAWN = registerEntityType(
            "aquifawn", () ->
                    EntityType.Builder.of(AquifawnEntity::new, MobCategory.CREATURE)
                            .sized(1.3964844F, 1.6F)
                            .eyeHeight(1.52F)
                            .passengerAttachments(new Vec3(0.0, 1.0, -0.25))
                            .build("")
    );

    public static final Supplier<EntityType<MurklingEntity>> MURKLING = registerEntityType(
            "murkling", () ->
                    EntityType.Builder.of(MurklingEntity::new, MobCategory.MONSTER)
                            .sized(0.85F, 1.0F).eyeHeight(0.9F).build("")
    );

    public static final Supplier<EntityType<MoobooEntity>> MOOBOO = registerEntityType(
            "mooboo", () ->
                    EntityType.Builder.of(MoobooEntity::new, MobCategory.CREATURE)
                            .sized(0.9F, 1.4F)
                            .eyeHeight(1.3f)
                            .fireImmune()
                            .build("")
    );

    public static final Supplier<EntityType<ScuttleEntity>> SCUTTLE = registerEntityType(
            "scuttle",
            () -> EntityType.Builder.of(ScuttleEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.4F).build("")
    );

    public static final Supplier<EntityType<ScuttleSpikeEntity>> SCUTTLE_SPIKE = registerEntityType(
            "scuttle_spike",
            () -> EntityType.Builder.<ScuttleSpikeEntity>of(ScuttleSpikeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .build("")
    );

    public static final Supplier<EntityType<TrilobiteEntity>> TRILOBITE = registerEntityType(
            "trilobite",
            () -> EntityType.Builder.of(TrilobiteEntity::new, MobCategory.WATER_CREATURE).sized(0.4F, 0.3F).build("")
    );

    public static final Supplier<EntityType<RisingBlockEntity>> RISING_BLOCK = registerEntityType(
            "rising_block",
            () -> EntityType.Builder.of(RisingBlockEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .build("")
    );

    public static final Supplier<EntityType<CopperbugEntity>> COPPERBUG = registerEntityType(
            "copperbug",
            () -> EntityType.Builder.of(CopperbugEntity::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.3F)
                    .eyeHeight(.13f)
                    .clientTrackingRange(8)
                    .build("")
    );

    public static final Supplier<EntityType<PeskyEntity>> PESKY = registerEntityType(
            "pesky",
            () -> EntityType.Builder.of(PeskyEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(.13f)
                    .clientTrackingRange(8)
                    .build("")
    );

    public static final Supplier<EntityType<WhiskbillEntity>> WHISKBILL = registerEntityType(
            "whiskbill",
            () -> EntityType.Builder.of(WhiskbillEntity::new, MobCategory.CREATURE)
                    .sized(1.9F, 1.25F)
                    .eyeHeight(1.04f)
                    .clientTrackingRange(10)
                    .nameTagOffset(2.04F)
                    .passengerAttachments(2.09F)
                    .build("")
    );

    public static final Supplier<EntityType<NectaurEntity>> NECTAUR = registerEntityType(
            "nectaur",
            () -> EntityType.Builder.of(NectaurEntity::new, MobCategory.CREATURE).sized(1.0F, 2.0F).build("")
    );

    public static final Supplier<EntityType<LithyEntity>> LITHY = registerEntityType(
            "lithy",
            () -> EntityType.Builder.of(LithyEntity::new, MobCategory.CREATURE).sized(0.8F, 2F).build("")
    );

    public static final Supplier<EntityType<ReaverEntity>> REAVER = registerEntityType(
            "reaver",
            () -> EntityType.Builder.of(ReaverEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .sized(1.5F, 1.9F)
                    .eyeHeight(0.72F)
                    .ridingOffset(0.04F)
                    .clientTrackingRange(8)
                    .updateInterval(2)
                    .build("")
    );

    public static final Supplier<EntityType<WallowEntity>> WALLOW = registerEntityType(
            "wallow",
            () -> EntityType.Builder.of(WallowEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .sized(0.9F, 1.4F)
                    .eyeHeight(1.15F)
                    .ridingOffset(0.04F)
                    .clientTrackingRange(8)
                    .updateInterval(2)
                    .build("")
    );

    public static final Supplier<EntityType<WispEntity>> WISP = registerEntityType(
            "wisp",
            () -> EntityType.Builder.of(WispEntity::new, MobCategory.CREATURE)
                    .sized(0.35F, 0.35F)
                    .eyeHeight(0.36F)
                    .ridingOffset(0.04F)
                    .clientTrackingRange(8)
                    .updateInterval(2)
                    .build("")
    );

    public static final Supplier<EntityType<HauntEntity>> HAUNT = registerEntityType(
            "haunt",
            () -> EntityType.Builder.of(HauntEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .sized(0.35F, 0.35F)
                    .eyeHeight(0.36F)
                    .ridingOffset(0.04F)
                    .fireImmune()
                    .clientTrackingRange(8)
                    .updateInterval(2)
                    .build("")
    );

    public static final Supplier<EntityType<GeistEntity>> GEIST = registerEntityType(
            "geist",
            () -> EntityType.Builder.of(GeistEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .immuneTo(BGBlocks.ICE_BOUQUET.get())
                    .fireImmune()
                    .sized(1.0F, 2.0F)
                    .build("")
    );

    public static final Supplier<EntityType<WraithEntity>> WRAITH = registerEntityType(
            "wraith",
            () -> EntityType.Builder.of(WraithEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .immuneTo(BGBlocks.ICE_BOUQUET.get())
                    .fireImmune()
                    .sized(1.0F, 2.0F)
                    .build("")
    );

    public static final Supplier<EntityType<MournEntity>> MOURN = registerEntityType(
            "mourn",
            () -> EntityType.Builder.of(MournEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .immuneTo(BGBlocks.ICE_BOUQUET.get())
                    .fireImmune()
                    .sized(1.2F, 2.6F)
                    .eyeHeight(2.0F)
                    .clientTrackingRange(8)
                    .build("")
    );

    public static final Supplier<EntityType<WhisperEntity>> WHISPER = registerEntityType(
            "whisper",
            () -> EntityType.Builder.of(WhisperEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .immuneTo(BGBlocks.ICE_BOUQUET.get())
                    .fireImmune()
                    .sized(0.5F, 0.75F)
                    .eyeHeight(0.55F)
                    .clientTrackingRange(8)
                    .build("")
    );

    public static final Supplier<EntityType<ScareEntity>> SCARE = registerEntityType(
            "scare",
            () -> EntityType.Builder.of(ScareEntity::new, HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                    .immuneTo(BGBlocks.ICE_BOUQUET.get())
                    .fireImmune()
                    .sized(0.9F, 0.8F)
                    .eyeHeight(0.65F)
                    .clientTrackingRange(8)
                    .build("")
    );

    public static final Supplier<EntityType<ScareBoltEntity>> SCARE_BOLT = registerEntityType(
            "scare_bolt",
            () -> EntityType.Builder.<ScareBoltEntity>of(ScareBoltEntity::new, MobCategory.MISC)
                    .sized(0.4F, 0.4F)
                    .clientTrackingRange(6)
                    .updateInterval(10)
                    .build("")
    );

    public static final Supplier<EntityType<SabeastEntity>> SABEAST = registerEntityType(
            "sabeast",
            () -> EntityType.Builder.of(SabeastEntity::new, MobCategory.CREATURE).sized(1.0F, 2.0F).build("")
    );

    public static final Supplier<EntityType<PrimordialFishEntity>> PRIMORDIAL_FISH = registerEntityType(
            "primordial_fish",
            () -> EntityType.Builder.of(PrimordialFishEntity::new, MobCategory.WATER_AMBIENT)
                    .sized(0.5F, 0.4F)
                    .eyeHeight(0.26F)
                    .clientTrackingRange(4)
                    .build("")
    );

    public static final Supplier<EntityType<GlareEntity>> GLARE = registerEntityType(
            "glare",
            () -> EntityType.Builder.of(GlareEntity::new, MobCategory.CREATURE).sized(0.6F, 0.8F).build("")
    );

    public static final Supplier<EntityType<PestEntity>> PEST = registerEntityType(
            "pest",
            () -> EntityType.Builder.of(PestEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(.13f)
                    .clientTrackingRange(8)

                    .build("")
    );

    private static <T extends EntityType<?>> Supplier<T> registerEntityType(String name, Supplier<T> entityTypeSupplier) {
        return JinxedRegistryHelper.register(BuiltInRegistries.ENTITY_TYPE, Bygone.MOD_ID, name, entityTypeSupplier);
    }



    public static void init(){}

    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>,AttributeSupplier> consumer){
        consumer.accept(AMOEBA.get(), AmoebaEntity.createAttributes().build());
        consumer.accept(AQUIFAWN.get(), AquifawnEntity.createAttributes().build());
        consumer.accept(BIG_BEAK.get(), BigBeakEntity.createBigBeakAttributes().build());
        consumer.accept(COELACANTH.get(), CoelacanthEntity.createAttributes().build());
        consumer.accept(COPPERBUG.get(), CopperbugEntity.createCopperbugAttributes().build());
        consumer.accept(DUCK.get(), DuckEntity.createDuckAttributes().build());
        consumer.accept(FUNGAL_PARENT.get(), FungalParentEntity.createFungieAttributes().build());
        consumer.accept(GEIST.get(), GeistEntity.createAttributes().build());
        consumer.accept(GLARE.get(), GlareEntity.createGlareAttributes().build());
        consumer.accept(HAUNT.get(), HauntEntity.createAttributes().build());
        consumer.accept(LITHY.get(), LithyEntity.createAttributes().build());
        consumer.accept(MOOBOO.get(), MoobooEntity.createAttributes().build());
        consumer.accept(MOURN.get(), MournEntity.createAttributes().build());
        consumer.accept(MURKLING.get(), MurklingEntity.createAttributes().build());
        consumer.accept(NECTAUR.get(), NectaurEntity.createAttributes().build());
        consumer.accept(PESKY.get(), PeskyEntity.createAttributes().build());
        consumer.accept(PEST.get(), PestEntity.createAttributes().build());
        consumer.accept(PRIMORDIAL_FISH.get(), PrimordialFishEntity.createAttributes().build());
        consumer.accept(REAVER.get(), ReaverEntity.createAttributes().build());
        consumer.accept(SABEAST.get(), SabeastEntity.createAttributes().build());
        consumer.accept(SCARE.get(), ScareEntity.createAttributes().build());
        consumer.accept(SCUTTLE.get(), ScuttleEntity.createAttributes().build());
        consumer.accept(SKIPPER.get(), SkipperEntity.createAttributes().build());
        consumer.accept(TRILOBITE.get(), TrilobiteEntity.createAttributes().build());
        consumer.accept(WALLOW.get(), WallowEntity.createAttributes().build());
        consumer.accept(WHISKBILL.get(), WhiskbillEntity.createAttributes().build());
        consumer.accept(WHISPER.get(), WhisperEntity.createAttributes().build());
        consumer.accept(WISP.get(), WispEntity.createAttributes().build());
        consumer.accept(WRAITH.get(), WraithEntity.createAttributes().build());
    }

    public static void registerSpawnPlacements(PlacementReceiver receiver){
        receiver.accept(AMOEBA.get(),          SpawnPlacementTypes.IN_WATER,   Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TrilobiteEntity::checkSurfaceWaterAnimalSpawnRule);
        receiver.accept(AQUIFAWN.get(),        SpawnPlacementTypes.IN_WATER,   Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScuttleEntity::checkSurfaceWaterAnimalSpawnRule);
        receiver.accept(BIG_BEAK.get(),        SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           BigBeakEntity::canSpawn);
        receiver.accept(COELACANTH.get(),      SpawnPlacementTypes.IN_WATER,   Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoelacanthEntity::checkSurfaceWaterAnimalSpawnRule);
        receiver.accept(COPPERBUG.get(),       SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           CopperbugEntity::canSpawn);
        receiver.accept(FUNGAL_PARENT.get(),   SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           FungalParentEntity::canSpawn);
        receiver.accept(GEIST.get(),           SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           GeistEntity::canSpawn);
        receiver.accept(GLARE.get(),           SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           GlareEntity::canSpawn);
        receiver.accept(HAUNT.get(),           SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           HauntEntity::canSpawn);
        receiver.accept(LITHY.get(),           SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           LithyEntity::canSpawn);
        receiver.accept(MOOBOO.get(),          SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           MoobooEntity::canSpawn);
        receiver.accept(MOURN.get(),           SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           MournEntity::canSpawn);
        receiver.accept(NECTAUR.get(),         SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           BigBeakEntity::canSpawn);
        receiver.accept(PESKY.get(),           SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           GlareEntity::canSpawn);
        receiver.accept(PEST.get(),            SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           GlareEntity::canSpawn);
        receiver.accept(PRIMORDIAL_FISH.get(), SpawnPlacementTypes.IN_WATER,   Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PrimordialFishEntity::checkPrimordialFishEntitySpawnRules);
        receiver.accept(REAVER.get(),          SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           ReaverEntity::canSpawn);
        receiver.accept(SABEAST.get(),         SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           MoobooEntity::canSpawn);
        receiver.accept(SCARE.get(),           SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           ScareEntity::canSpawn);
        receiver.accept(SCUTTLE.get(),         SpawnPlacementTypes.IN_WATER,   Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScuttleEntity::checkSurfaceWaterAnimalSpawnRule);
        receiver.accept(SKIPPER.get(),         SkipperEntity.ON_WATER_SURFACE, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkipperEntity::canSpawn);
        receiver.accept(TRILOBITE.get(),       SpawnPlacementTypes.IN_WATER,   Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TrilobiteEntity::checkSurfaceWaterAnimalSpawnRule);
        receiver.accept(WALLOW.get(),          SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           WraithEntity::canSpawn);
        receiver.accept(WHISKBILL.get(),       SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           BigBeakEntity::canSpawn);
        receiver.accept(WHISPER.get(),         SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           WhisperEntity::canSpawn);
        receiver.accept(WISP.get(),            SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           WispEntity::canSpawn);
        receiver.accept(WRAITH.get(),          SpawnPlacementTypes.ON_GROUND,  Heightmap.Types.MOTION_BLOCKING,           WraithEntity::canSpawn);
    }
    public static interface PlacementReceiver{
        <T extends Mob> void accept(EntityType<T> type, SpawnPlacementType placement, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicates);
    }
}
