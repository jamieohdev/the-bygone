package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.blocks.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static com.jamiedev.mod.fabric.init.JamiesModBlocks.*;

public class JamiesModBlockEntities <T extends BlockEntity>
{
    public static BlockEntityType<CopperbugNestBlockEntity> COPPERBUGNEST;
    BlockEntityType ref;
    public static BlockEntityType<BygonePortalBlockEntity> BYGONE_PORTAL;
    public static BlockEntityType<PrimordialVentEntity> PRIMORDIAL_VENT;

    public static BlockEntityType<SprinklerEntity> SPRINKLER;

    public static BlockEntityType<CasterBlockEntity> CASTER;

    public static BlockEntityType<PrimordialUrchinEntity> PRIMORDIAL_URCHIN;

    public static BlockEntityType<ModSignBlockEntity> MOD_SIGN_BLOCK_ENTITY;

    public static BlockEntityType<ModHangingSignBlockEntity> MOD_HANGING_SIGN_BLOCK_ENTITY;

    public static BlockEntityType<BlemishCatalystBlockEntity> BLEMISH_CATALYST;

    public static BlockEntityType<BygoneBrushableBlockEntity> BRUSHABLE_BLOCK;

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, JamiesModFabric.getModId(name), type);
    }

    public static void init()
    {
        BLEMISH_CATALYST = register("blemish_catalyst",
                BlockEntityType.Builder.of(BlemishCatalystBlockEntity::new, JamiesModBlocks.BLEMISH_CATALYST)
                        .build());
        CASTER = register("caster",
                BlockEntityType.Builder.of(CasterBlockEntity::new, JamiesModBlocks.CASTER)
                        .build());
      //  COPPERBUGNEST = register("copperbug_nest",
       //         BlockEntityType.Builder.create(CopperbugNestBlockEntity::new, JamiesModBlocks.COPPERBUG_NEST)
       //                 .build());
        SPRINKLER = register("ancient_sprinkler",
                BlockEntityType.Builder.of(SprinklerEntity::new, JamiesModBlocks.SPRINKER)
                        .build());
        PRIMORDIAL_VENT = register("primordial_vent",
                BlockEntityType.Builder.of(PrimordialVentEntity::new, JamiesModBlocks.PRIMORDIAL_VENT)
                        .build());
        PRIMORDIAL_URCHIN = register("primordial_urchin",
                BlockEntityType.Builder.of(PrimordialUrchinEntity::new, JamiesModBlocks.PRIMORDIAL_URCHIN)
                        .build());
        MOD_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                JamiesModFabric.getModId("ancient_sign_entity"),
                FabricBlockEntityTypeBuilder.create(ModSignBlockEntity::new, ANCIENT_SIGN, ANCIENT_WALL_SIGN).build()
        );
        MOD_HANGING_SIGN_BLOCK_ENTITY = register("ancient_hanging_sign_entity",
                BlockEntityType.Builder.of(ModHangingSignBlockEntity::new, JamiesModBlocks.ANCIENT_HANGING_SIGN, JamiesModBlocks.ANCIENT_WALL_HANGING_SIGN).build()
        );
       // BRUSHABLE_BLOCK = register("brushable_block",
        //        BlockEntityType.Builder.create(BygoneBrushableBlockEntity::new, SUSPICIOUS_UMBER)
        //                .build());
        BYGONE_PORTAL =  Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                JamiesModFabric.getModId("bygone_portal"),
                FabricBlockEntityTypeBuilder.create(BygonePortalBlockEntity::new, JamiesModBlocks.BYGONE_PORTAL).build());

    }
}
