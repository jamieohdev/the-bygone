package com.jamiedev.mod.common.init;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.entities.*;
import com.jamiedev.mod.common.entities.projectile.ExoticArrowEntity;
import com.jamiedev.mod.common.entities.projectile.HookEntity;
import com.jamiedev.mod.common.entities.projectile.ScuttleSpikeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

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
        return Identifier.of(JamiesMod.MOD_ID, name);
    }

    public static final EntityType<HookEntity> HOOK = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "hook"),
            FabricEntityTypeBuilder.<HookEntity>create(SpawnGroup.MISC, HookEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeChunks(4)
                    .trackedUpdateRate(5)
                    .build()
    );

    public static final EntityType<BigBeakEntity> BIG_BEAK = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "big_beak"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BigBeakEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0F, 2.0F)).build());

    public static final EntityType<CoelacanthEntity> COELACANTH = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "coelacanth"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, CoelacanthEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 0.6F)).build());

    public static final EntityType<DuckEntity> DUCK = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "duck"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DuckEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4F, 0.7F)).build());

    public static final EntityType<BrungleEntity> BRUNGLE = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "brungle"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrungleEntity::new)
                    .dimensions(EntityDimensions.fixed(1.5F, 1.0F)).build());

    public static final EntityType<ExoticArrowEntity> EXOTIC_ARROW = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "exotic_arrow"),
            FabricEntityTypeBuilder.<ExoticArrowEntity>create(SpawnGroup.MISC, ExoticArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))

                    .trackRangeChunks(4)
                    .trackedUpdateRate(20)
                    .build()
    );

    public static final EntityType<GlareEntity> GLARE = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "glare"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, GlareEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 0.8F)).build());

    public static final EntityType<JawsEntity> JAWS = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "jaws"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, JawsEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 0.4F)).build());

    public static final  EntityType<MoobooEntity> MOOBOO = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId("mooboo"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MoobooEntity::new)
                .dimensions(EntityDimensions.fixed(0.9F, 1.4F).withEyeHeight(1.3F))
                        .trackedUpdateRate(10).build());


    public static final EntityType<ScuttleEntity> SCUTTLE = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "scuttle"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, ScuttleEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 0.4F)).build());

    public static final EntityType<ScuttleSpikeEntity> SCUTTLE_SPIKE = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "scuttle_spike"),
            FabricEntityTypeBuilder.<ScuttleSpikeEntity>create(SpawnGroup.MISC, ScuttleSpikeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F).withEyeHeight(0.13F))
                    .trackRangeChunks(4)
                    .trackedUpdateRate(20)
                    .build()
    );

    public static final EntityType<TrilobiteEntity> TRILOBITE = Registry.register(Registries.ENTITY_TYPE,
            JamiesMod.getModId( "trilobite"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, TrilobiteEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4F, 0.3F)).build());


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
    }

    public static void postInit() {
        initSpawnRestrictions();
    }

    public static void initSpawnRestrictions() {


    }
}
