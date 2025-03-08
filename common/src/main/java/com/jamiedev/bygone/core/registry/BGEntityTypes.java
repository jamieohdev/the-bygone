package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.*;
import com.jamiedev.bygone.common.entity.projectile.ExoticArrowEntity;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.common.entity.projectile.ScuttleSpikeEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

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



    public static final EntityType<HookEntity> HOOK = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "hook"),
            EntityType.Builder.<HookEntity>of(HookEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(5)
                    .build("")
    );

    public static final EntityType<BigBeakEntity> BIG_BEAK = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "big_beak"),
            EntityType.Builder.of(BigBeakEntity::new, MobCategory.CREATURE)
                    .sized(1.0F, 2.0F).build(""));

    public static final EntityType<CoelacanthEntity> COELACANTH = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "coelacanth"),
            EntityType.Builder.of(CoelacanthEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8F, 0.6F).build(""));

    public static final EntityType<DuckEntity> DUCK = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "duck"),
            EntityType.Builder.of(DuckEntity::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.7F).build(""));

    public static final EntityType<FungalParentEntity> FUNGAL_PARENT = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "fungal_parent"),
            EntityType.Builder.of(FungalParentEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.1F)
                    .clientTrackingRange(8)
                    .updateInterval(8)
                    .build(""));
// .dimensions(0.6F, 2.9F).eyeHeight(2.55F).passengerAttachments(2.80625F).maxTrackingRange(8));


    public static final EntityType<ExoticArrowEntity> EXOTIC_ARROW = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "exotic_arrow"),
            EntityType.Builder.<ExoticArrowEntity>of(ExoticArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)

                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("")
    );

    public static final EntityType<GlareEntity> GLARE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "glare"),
            EntityType.Builder.of(GlareEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.8F).build(""));

    public static final  EntityType<MoobooEntity> MOOBOO = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id("mooboo"),
            EntityType.Builder.of(MoobooEntity::new, MobCategory.CREATURE)
                .sized(0.9F, 1.4F)
                    .eyeHeight(1.3f)
                        .updateInterval(10).build(""));


    public static final EntityType<ScuttleEntity> SCUTTLE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "scuttle"),
            EntityType.Builder.of(ScuttleEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.8F, 0.4F).build(""));

    public static final EntityType<ScuttleSpikeEntity> SCUTTLE_SPIKE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "scuttle_spike"),
            EntityType.Builder.<ScuttleSpikeEntity>of(ScuttleSpikeEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("")
    );

    public static final EntityType<TrilobiteEntity> TRILOBITE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id( "trilobite"),
            EntityType.Builder.of(TrilobiteEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.4F, 0.3F).build(""));

    public static final  EntityType<RisingBlockEntity> RISING_BLOCK = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id("rising_block"),
            EntityType.Builder.of(RisingBlockEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build(""));

    public static final  EntityType<CopperbugEntity> COPPERBUG = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id("copperbug"),
            EntityType.Builder.of(CopperbugEntity::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.3F).eyeHeight(.13f)
                    .clientTrackingRange(8)
                    .updateInterval(8)
                    .build(""));

    public static final  EntityType<PestEntity> PEST = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Bygone.id("pest"),
            EntityType.Builder.of(PestEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.5F).eyeHeight(.13f)
                    .clientTrackingRange(8)
                    .updateInterval(8)
                    .build(""));


    public static void postInit() {
        initSpawnRestrictions();
    }

    public static void initSpawnRestrictions() {


    }
}
