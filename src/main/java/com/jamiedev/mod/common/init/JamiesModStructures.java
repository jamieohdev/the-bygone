package com.jamiedev.mod.common.init;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.worldgen.structure.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Locale;

public class JamiesModStructures
{
    public static StructureType<AncientRootStructure> ANCIENT_ROOTS;
    public static StructurePieceType ANCIENT_ROOTS_PIECES = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("ancient_roots"),  AncientRootGenerator.Piece::new);

    public static StructureType<RuinStructure> BLEMISH_RUINS;
    public static StructureType<AmberRuinsStructure> AMBER_RUINS;

    public static StructureType<BygoneMineshaftStructure> BYGONE_MINESHAFT;
    public static StructurePieceType BYGONE_MINESHAFT_CORRIDOR = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("corr"), BygoneMineshaftGenerator.BygoneMineshaftCorridor::new);
    public static StructurePieceType BYGONE_MINESHAFT_CROSSING = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("cros"), BygoneMineshaftGenerator.BygoneMineshaftCrossing::new);

    public static  StructurePieceType BYGONE_MINESHAFT_ROOM = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("room"), BygoneMineshaftGenerator.BygoneMineshaftRoom::new);
    public static StructurePieceType BYGONE_MINESHAFT_STAIRS = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("star"), BygoneMineshaftGenerator.BygoneMineshaftStairs::new);

    public static StructureType<BygoneFossilStructure> BYGONE_FOSSIL;
    public static StructurePieceType FOSSIL_PIECES = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("bygone_fossil"), BygoneFossilGenerator.Piece::new);
    public static StructurePieceType AMBER_RUIN_PIECES = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("ruin"),  RuinGenerator.Piece::new);
    public static StructurePieceType RUIN_PIECES = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("amber_ruins"),  AmberRuinsGenerator.Piece::new);

    public static StructureType<TestRootStructure> TEST_ROOTS;

    public static StructureType<BygonePortalStructure> BYGONE_PORTAL;
    public static StructurePieceType PORTAL_PIECE = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("bygone_portal"), BygonePortalGenerator.Piece::new);

    private static StructurePieceType register(StructurePieceType type, String id) {
        return (StructurePieceType)Registry.register(Registries.STRUCTURE_PIECE, id.toLowerCase(Locale.ROOT), type);
    }

    private static StructurePieceType register(StructurePieceType.Simple type, String id) {
        return register((StructurePieceType)type, id);
    }

    private static StructurePieceType register(StructurePieceType.ManagerAware type, String id) {
        return register((StructurePieceType)type, id);
    }

    public static void init()
    {
        ANCIENT_ROOTS = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID, "ancient_roots"), () -> AncientRootStructure.CODEC);
        BYGONE_FOSSIL = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID , "bygone_fossil"), () -> BygoneFossilStructure.CODEC);
        BYGONE_PORTAL = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID , "bygone_portal"), () -> BygonePortalStructure.CODEC);
        TEST_ROOTS = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID, "test_roots"), () -> TestRootStructure.CODEC);
        BLEMISH_RUINS = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID, "ruin"), () -> RuinStructure.CODEC);
        AMBER_RUINS = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID, "amber_ruins"), () -> AmberRuinsStructure.CODEC);
        BYGONE_MINESHAFT = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID, "bygone_mineshaft"), () -> BygoneMineshaftStructure.CODEC);
    }
}
