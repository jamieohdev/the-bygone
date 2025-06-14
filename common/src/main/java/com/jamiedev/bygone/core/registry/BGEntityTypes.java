package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.*;
import com.jamiedev.bygone.common.entity.projectile.ExoticArrowEntity;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.common.entity.projectile.ScuttleSpikeEntity;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

import java.util.function.Supplier;

public class BGEntityTypes {

    public class CustomSpawnGroups
    {
        public static MobCategory GLARES, BIG_BEAKS;

        public CustomSpawnGroups()
        {

        }

        public static  MobCategory getGlares() {
            return GLARES;
        }

        public static MobCategory getBigBeaks() {
            return BIG_BEAKS;
        }
    }



    public static final Supplier<EntityType<HookEntity>> HOOK = registerEntityType("hook", () ->
            EntityType.Builder.<HookEntity>of(HookEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(5)
                    .build("")
    );

    public static final Supplier<EntityType<BigBeakEntity>> BIG_BEAK = registerEntityType("big_beak", () ->
            EntityType.Builder.of(BigBeakEntity::new, MobCategory.CREATURE)
                    .sized(1.0F, 2.0F).build(""));

    public static final Supplier<EntityType<CoelacanthEntity>> COELACANTH = registerEntityType("coelacanth", () ->
            EntityType.Builder.of(CoelacanthEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8F, 0.6F).build(""));

    public static final Supplier<EntityType<DuckEntity>> DUCK = registerEntityType( "duck", () ->
            EntityType.Builder.of(DuckEntity::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.7F).build(""));

    public static final Supplier<EntityType<FungalParentEntity>> FUNGAL_PARENT = registerEntityType("fungal_parent", () ->
            EntityType.Builder.of(FungalParentEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.1F)
                    .clientTrackingRange(8)

                    .build(""));
// .dimensions(0.6F, 2.9F).eyeHeight(2.55F).passengerAttachments(2.80625F).maxTrackingRange(8));


    public static final Supplier<EntityType<ExoticArrowEntity>> EXOTIC_ARROW = registerEntityType( "exotic_arrow", () ->
            EntityType.Builder.<ExoticArrowEntity>of(ExoticArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)

                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("")
    );

    public static final Supplier<EntityType<GlareEntity>> GLARE = registerEntityType("glare", () ->
            EntityType.Builder.of(GlareEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.8F).build(""));

    public static final Supplier<EntityType<MoobooEntity>> MOOBOO = registerEntityType("mooboo", () ->
            EntityType.Builder.of(MoobooEntity::new, MobCategory.CREATURE)
                .sized(0.9F, 1.4F)
                    .eyeHeight(1.3f)
                        .build(""));


    public static final Supplier<EntityType<ScuttleEntity>> SCUTTLE = registerEntityType("scuttle", () ->
            EntityType.Builder.of(ScuttleEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8F, 0.4F).build(""));

    public static final Supplier<EntityType<ScuttleSpikeEntity>> SCUTTLE_SPIKE = registerEntityType( "scuttle_spike", () ->
            EntityType.Builder.<ScuttleSpikeEntity>of(ScuttleSpikeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).eyeHeight(0.13F)
                    .clientTrackingRange(4)

                    .build("")
    );

    public static final Supplier<EntityType<TrilobiteEntity>> TRILOBITE = registerEntityType( "trilobite", () ->
            EntityType.Builder.of(TrilobiteEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.4F, 0.3F).build(""));



    public static final  Supplier<EntityType<RisingBlockEntity>> RISING_BLOCK = registerEntityType("rising_block", () ->
            EntityType.Builder.of(RisingBlockEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)

                    .build(""));

    public static final Supplier<EntityType<CopperbugEntity>> COPPERBUG = registerEntityType("copperbug", () ->
            EntityType.Builder.of(CopperbugEntity::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.3F).eyeHeight(.13f)
                    .clientTrackingRange(8)

                    .build(""));

    public static final Supplier<EntityType<CopperbugEntity>> COPPERFLY = registerEntityType("copperfly", () ->
            EntityType.Builder.of(CopperbugEntity::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.3F).eyeHeight(.13f)
                    .clientTrackingRange(8)

                    .build(""));

    public static final Supplier<EntityType<PeskyEntity>> PESKY = registerEntityType("pesky", () ->
            EntityType.Builder.of(PeskyEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.5F).eyeHeight(.13f)
                    .clientTrackingRange(8)
                    .build(""));

    public static final Supplier<EntityType<PestEntity>> PEST = registerEntityType("pest", () ->
            EntityType.Builder.of(PestEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.5F).eyeHeight(.13f)
                    .clientTrackingRange(8)

                    .build(""));

    public static final Supplier<EntityType<WhiskbillEntity>> WHISKBILL = registerEntityType("whiskbill", () ->
            EntityType.Builder.of(WhiskbillEntity::new, MobCategory.CREATURE)
                    .sized(1.9F, 1.25F).eyeHeight(1.04f)
                    .clientTrackingRange(10)

                    .nameTagOffset(2.04F)
                    .passengerAttachments(2.09F)
                    .build(""));

    public static final Supplier<EntityType<NectaurEntity>> NECTAUR = registerEntityType("nectaur", () ->
            EntityType.Builder.of(NectaurEntity::new, MobCategory.CREATURE)
                    .sized(1.0F, 2.0F).build(""));

    public static void postInit() {
        initSpawnRestrictions();
    }

    public static void initSpawnRestrictions() {


    }

    private static <T extends EntityType<?>> Supplier<T> registerEntityType(String name, Supplier<T> entityTypeSupplier) {
        return JinxedRegistryHelper.register(BuiltInRegistries.ENTITY_TYPE, Bygone.MOD_ID, name, entityTypeSupplier);
    }
}
