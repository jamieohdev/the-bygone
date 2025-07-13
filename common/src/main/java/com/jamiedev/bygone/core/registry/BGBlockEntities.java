package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.entity.*;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;

import java.util.function.Supplier;

public class BGBlockEntities {
    public static Supplier<BlockEntityType<CopperbugNestBlockEntity>> COPPERBUGNEST;
    public static Supplier<BlockEntityType<BygonePortalBlockEntity>> BYGONE_PORTAL;
    public static Supplier<BlockEntityType<PrimordialVentEntity>> PRIMORDIAL_VENT;

    public static Supplier<BlockEntityType<SprinklerEntity>> SPRINKLER;

    public static Supplier<BlockEntityType<CasterBlockEntity>> CASTER;

    public static Supplier<BlockEntityType<PrimordialUrchinEntity>> PRIMORDIAL_URCHIN;


    public static Supplier<BlockEntityType<BlemishCatalystBlockEntity>> BLEMISH_CATALYST;
    public static Supplier<BlockEntityType<AmphoraBlockEntity>> AMPHORA;

    public static Supplier<BlockEntityType<BrushableBlockEntity>> BRUSHABLE_BLOCK;

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> type) {
        return JinxedRegistryHelper.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bygone.MOD_ID, name, type);
    }

    public static void init()
    {
        BLEMISH_CATALYST = register("blemish_catalyst", () ->
                BlockEntityType.Builder.of(BlemishCatalystBlockEntity::new, BGBlocks.BLEMISH_CATALYST.get())
                        .build(null));
        CASTER = register("caster", () ->
                BlockEntityType.Builder.of(CasterBlockEntity::new, BGBlocks.CASTER.get())
                        .build(null));
      //  COPPERBUGNEST = register("copperbug_nest",
       //         BlockEntityType.Builder.create(CopperbugNestBlockEntity::new, JamiesModBlocks.COPPERBUG_NEST)
       //                 .build());
        SPRINKLER = register("ancient_sprinkler", () ->
                BlockEntityType.Builder.of(SprinklerEntity::new, BGBlocks.SPRINKER.get())
                        .build(null));
        PRIMORDIAL_VENT = register("primordial_vent", () ->
                BlockEntityType.Builder.of(PrimordialVentEntity::new, BGBlocks.PRIMORDIAL_VENT.get())
                        .build(null));
        PRIMORDIAL_URCHIN = register("primordial_urchin", () ->
                BlockEntityType.Builder.of(PrimordialUrchinEntity::new, BGBlocks.PRIMORDIAL_URCHIN.get())
                        .build(null));

        AMPHORA = register("amphora", () ->
                BlockEntityType.Builder.of(AmphoraBlockEntity::new, BGBlocks.ORANGE_AMPHORA.get())
                        .build(null));

        BYGONE_PORTAL =  register(
                "bygone_portal", () ->
                BlockEntityType.Builder.of(BygonePortalBlockEntity::new, BGBlocks.BYGONE_PORTAL.get()).build(null));

    }
}
