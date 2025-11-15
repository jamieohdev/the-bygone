package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;

import java.util.Locale;

import com.jamiedev.bygone.common.worldgen.structure.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;

public class BGStructures
{
    public static StructureType<AncientRootStructure> ANCIENT_ROOTS;
    public static StructurePieceType ANCIENT_ROOTS_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("ancient_roots"),  AncientRootGenerator.Piece::new);
    public static StructureType<AncientRootStructure> ABANDONED_FARM;
    public static StructurePieceType ABANDONED_FARM_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("abandoned_farm"),  AbandonedFarmGenerator.Piece::new);

    public static StructureType<RuinStructure> BLEMISH_RUINS;
    public static StructureType<AmberRuinsStructure> AMBER_RUINS;
    public static StructureType<AmberPyramidStructure> AMBER_PYRAMID;
    public static StructureType<MegalithRuinsStructure> MEGALITH_RUINS;
    public static StructureType<MinilithStructure> MINILITHS;

    public static StructureType<SunkenCityStructure> SUNKEN_CITY;
    public static StructurePieceType SUNKEN_CITY_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("sunken_ruins"), SunkenCityPiece.Piece::new);

    public static StructureType<BygoneMineshaftStructure> BYGONE_MINESHAFT;
    public static StructurePieceType BYGONE_MINESHAFT_CORRIDOR = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("corr"), BygoneMineshaftGenerator.BygoneMineshaftCorridor::new);
    public static StructurePieceType BYGONE_MINESHAFT_CROSSING = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("cros"), BygoneMineshaftGenerator.BygoneMineshaftCrossing::new);

    public static  StructurePieceType BYGONE_MINESHAFT_ROOM = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("room"), BygoneMineshaftGenerator.BygoneMineshaftRoom::new);
    public static StructurePieceType BYGONE_MINESHAFT_STAIRS = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("star"), BygoneMineshaftGenerator.BygoneMineshaftStairs::new);

    public static StructureType<BygoneFossilStructure> BYGONE_FOSSIL;
    public static StructurePieceType FOSSIL_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("bygone_fossil"), BygoneFossilGenerator.Piece::new);
    public static StructurePieceType AMBER_RUIN_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("ruin"),  RuinGenerator.Piece::new);
    public static StructurePieceType RUIN_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("amber_ruins"),  AmberRuinsGenerator.Piece::new);
    public static StructurePieceType PYRAMID_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("amber_pyramid"), AmberRuinsGenerator.Piece::new);

    public static StructurePieceType MEGALITH_RUINS_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("megalith_ruins"), AmberRuinsGenerator.Piece::new);
    public static StructurePieceType MINILITH_PIECES = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("miniliths"),  MinilithGenerator.Piece::new);

    public static StructureType<TestRootStructure> TEST_ROOTS;

    public static StructureType<BygonePortalStructure> BYGONE_PORTAL;
    public static StructurePieceType PORTAL_PIECE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
            Bygone.id("bygone_portal"), BygonePortalGenerator.Piece::new);


    private static StructurePieceType register(StructurePieceType type, String id) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, id.toLowerCase(Locale.ROOT), type);
    }

    private static StructurePieceType register(StructurePieceType.ContextlessType type, String id) {
        return register((StructurePieceType)type, id);
    }

    private static StructurePieceType register(StructurePieceType.StructureTemplateType type, String id) {
        return register((StructurePieceType)type, id);
    }


    public static void init()
    {
        ABANDONED_FARM = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "abandoned_farm"),
                () -> AncientRootStructure.CODEC);
        ANCIENT_ROOTS = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "ancient_roots"), () -> AncientRootStructure.CODEC);
        BYGONE_FOSSIL = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID , "bygone_fossil"), () -> BygoneFossilStructure.CODEC);
        BYGONE_PORTAL = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID , "bygone_portal"), () -> BygonePortalStructure.CODEC);
        TEST_ROOTS = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "test_roots"), () -> TestRootStructure.CODEC);
        BLEMISH_RUINS = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "ruin"), () -> RuinStructure.CODEC);
        AMBER_RUINS = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "amber_ruins"), () -> AmberRuinsStructure.CODEC);
        AMBER_PYRAMID = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "amber_pyramid"), () -> AmberPyramidStructure.CODEC);
        BYGONE_MINESHAFT = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "bygone_mineshaft"), () -> BygoneMineshaftStructure.CODEC);

        MEGALITH_RUINS = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "megalith_ruins"), () -> MegalithRuinsStructure.CODEC);
        MINILITHS = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "miniliths"), () -> MinilithStructure.CODEC);
        SUNKEN_CITY = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins"), () -> SunkenCityStructure.CODEC);

    }
}
