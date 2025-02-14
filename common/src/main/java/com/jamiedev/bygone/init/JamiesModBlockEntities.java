package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.block.entity.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static com.jamiedev.bygone.init.JamiesModBlocks.*;

public class JamiesModBlockEntities {
    public static BlockEntityType<CopperbugNestBlockEntity> COPPERBUGNEST;
    public static BlockEntityType<BygonePortalBlockEntity> BYGONE_PORTAL;
    public static BlockEntityType<PrimordialVentEntity> PRIMORDIAL_VENT;

    public static BlockEntityType<SprinklerEntity> SPRINKLER;

    public static BlockEntityType<CasterBlockEntity> CASTER;

    public static BlockEntityType<PrimordialUrchinEntity> PRIMORDIAL_URCHIN;


    public static BlockEntityType<BlemishCatalystBlockEntity> BLEMISH_CATALYST;

    public static BlockEntityType<BygoneBrushableBlockEntity> BRUSHABLE_BLOCK;

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bygone.id(name), type);
    }

    public static void init()
    {
        BLEMISH_CATALYST = register("blemish_catalyst",
                BlockEntityType.Builder.of(BlemishCatalystBlockEntity::new, JamiesModBlocks.BLEMISH_CATALYST)
                        .build(null));
        CASTER = register("caster",
                BlockEntityType.Builder.of(CasterBlockEntity::new, JamiesModBlocks.CASTER)
                        .build(null));
      //  COPPERBUGNEST = register("copperbug_nest",
       //         BlockEntityType.Builder.create(CopperbugNestBlockEntity::new, JamiesModBlocks.COPPERBUG_NEST)
       //                 .build());
        SPRINKLER = register("ancient_sprinkler",
                BlockEntityType.Builder.of(SprinklerEntity::new, JamiesModBlocks.SPRINKER)
                        .build(null));
        PRIMORDIAL_VENT = register("primordial_vent",
                BlockEntityType.Builder.of(PrimordialVentEntity::new, JamiesModBlocks.PRIMORDIAL_VENT)
                        .build(null));
        PRIMORDIAL_URCHIN = register("primordial_urchin",
                BlockEntityType.Builder.of(PrimordialUrchinEntity::new, JamiesModBlocks.PRIMORDIAL_URCHIN)
                        .build(null));

       // BRUSHABLE_BLOCK = register("brushable_block",
        //        BlockEntityType.Builder.create(BygoneBrushableBlockEntity::new, SUSPICIOUS_UMBER)
        //                .build());
        BYGONE_PORTAL =  Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Bygone.id("bygone_portal"),
                BlockEntityType.Builder.of(BygonePortalBlockEntity::new, JamiesModBlocks.BYGONE_PORTAL).build(null));

    }
}
