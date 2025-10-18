package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.entity.*;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;

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

    public  static Supplier<BlockEntityType<DoguEntity>> DOGU;

    public static Supplier<BlockEntityType<MegalithTotemEntity>> MEGALITH_TOTEM;

    public static Supplier<BlockEntityType<GumboPotBlockEntity>> GUMBO_POT;

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
                BlockEntityType.Builder.of(AmphoraBlockEntity::new,
                                BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(),
                                BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GILDED_AMPHORA.get(),
                                BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(),
                                BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(),
                                BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(),
                                BGBlocks.RED_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get())
                        .build(null));

        BYGONE_PORTAL =  register(
                "bygone_portal", () ->
                BlockEntityType.Builder.of(BygonePortalBlockEntity::new, BGBlocks.BYGONE_PORTAL.get()).build(null));

        MEGALITH_TOTEM = register("megalith_totem", () ->
                BlockEntityType.Builder.of(MegalithTotemEntity::new, BGBlocks.MEGALITH_TOTEM.get()).build(null));

        GUMBO_POT = register("gumbo_pot", () ->
                BlockEntityType.Builder.of(GumboPotBlockEntity::new, BGBlocks.GUMBO_POT.get()).build(null));

         DOGU = register("dogu", () ->
                BlockEntityType.Builder.of(DoguEntity::new,
                                BGBlocks.DOGU.get())
                        .build(null));
    }
}
