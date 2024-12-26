package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.entities.*;
import com.jamiedev.mod.common.entities.projectile.BygoneItemEntity;
import com.jamiedev.mod.common.entities.projectile.ExoticArrowEntity;
import com.jamiedev.mod.common.entities.projectile.HookEntity;
import com.jamiedev.mod.common.entities.projectile.ScuttleSpikeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import javax.imageio.spi.RegisterableService;

public class JamiesModEntityTypes {

    public class CustomSpawnGroups
    {
        public static SpawnGroup GLARES, BIG_BEAKS;

        public CustomSpawnGroups()
        {

        }

        public static  SpawnGroup getGlares() {
            return GLARES;
        }

        public static SpawnGroup getBigBeaks() {
            return BIG_BEAKS;
        }
    }

    public static Identifier id(String name){
        return Identifier.of(JamiesModFabric.MOD_ID, name);
    }

    public static final EntityType<HookEntity> HOOK = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "hook"),
            FabricEntityTypeBuilder.<HookEntity>create(SpawnGroup.MISC, HookEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeChunks(4)
                    .trackedUpdateRate(5)
                    .build()
    );

    public static final EntityType<BigBeakEntity> BIG_BEAK = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "big_beak"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BigBeakEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0F, 2.0F)).build());

    public static final EntityType<CoelacanthEntity> COELACANTH = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "coelacanth"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, CoelacanthEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 0.6F)).build());

    public static final EntityType<DuckEntity> DUCK = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "duck"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DuckEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4F, 0.7F)).build());

    public static final EntityType<BrungleEntity> BRUNGLE = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "brungle"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrungleEntity::new)
                    .dimensions(EntityDimensions.fixed(1.5F, 1.0F)).build());

    public static final EntityType<ExoticArrowEntity> EXOTIC_ARROW = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "exotic_arrow"),
            FabricEntityTypeBuilder.<ExoticArrowEntity>create(SpawnGroup.MISC, ExoticArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))

                    .trackRangeChunks(4)
                    .trackedUpdateRate(20)
                    .build()
    );

    public static final EntityType<GlareEntity> GLARE = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "glare"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, GlareEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 0.8F)).build());

    public static final EntityType<JawsEntity> JAWS = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "jaws"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, JawsEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 0.4F)).build());

    public static final  EntityType<MoobooEntity> MOOBOO = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId("mooboo"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MoobooEntity::new)
                .dimensions(EntityDimensions.fixed(0.9F, 1.4F).withEyeHeight(1.3F))
                        .trackedUpdateRate(10).build());


    public static final EntityType<ScuttleEntity> SCUTTLE = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "scuttle"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, ScuttleEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 0.4F)).build());

    public static final EntityType<ScuttleSpikeEntity> SCUTTLE_SPIKE = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "scuttle_spike"),
            FabricEntityTypeBuilder.<ScuttleSpikeEntity>create(SpawnGroup.MISC, ScuttleSpikeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F).withEyeHeight(0.13F))
                    .trackRangeChunks(4)
                    .trackedUpdateRate(20)
                    .build()
    );

    public static final EntityType<TrilobiteEntity> TRILOBITE = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "trilobite"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, TrilobiteEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4F, 0.3F)).build());


    public static final  EntityType<BygoneItemEntity> BYGONE_ITEM = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId( "bygone_item"),
            FabricEntityTypeBuilder.<BygoneItemEntity>create(SpawnGroup.MISC, BygoneItemEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeChunks(4)
                    .trackedUpdateRate(4)
                    .build());

    public static final  EntityType<RisingBlockEntity> RISING_BLOCK = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId("rising_block"),
            FabricEntityTypeBuilder.<RisingBlockEntity>create(SpawnGroup.MISC, RisingBlockEntity::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.98F))
                    .trackRangeChunks(10)
                    .trackedUpdateRate(20)
                    .build());

    public static final  EntityType<CopperbugEntity> COPPERBUG = Registry.register(Registries.ENTITY_TYPE,
            JamiesModFabric.getModId("copperbug"),
            FabricEntityTypeBuilder.<CopperbugEntity>create(SpawnGroup.CREATURE, CopperbugEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4F, 0.3F).withEyeHeight(0.13F)).trackRangeChunks(8)
                    .trackedUpdateRate(8)
                    .build());



    EntityType ref;

    public static void init()
    {
        FabricDefaultAttributeRegistry.register(DUCK, DuckEntity.createDuckAttributes());
        FabricDefaultAttributeRegistry.register(BIG_BEAK, BigBeakEntity.createBigBeakAttributes());
        FabricDefaultAttributeRegistry.register(GLARE, GlareEntity.createGlareAttributes());
        FabricDefaultAttributeRegistry.register(SCUTTLE, ScuttleEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(JAWS, JawsEntity.createJawsAttributes());
        FabricDefaultAttributeRegistry.register(COELACANTH, CoelacanthEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TRILOBITE, TrilobiteEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MOOBOO, MoobooEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(COPPERBUG, CopperbugEntity.createCopperbugAttributes());
    }

    public static void postInit() {
        initSpawnRestrictions();
    }

    public static void initSpawnRestrictions() {


    }
}
