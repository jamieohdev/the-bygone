package com.jamiedev.mod.init;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.worldgen.structure.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.NetherFossilGenerator;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Locale;

public class JamiesModStructures
{
    public static StructureType<AncientRootStructure> ANCIENT_ROOTS;
    public static StructurePieceType ANCIENT_ROOTS_PIECES = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("ancient_roots"),  AncientRootGenerator.Piece::new);

    public static StructureType<RuinStructure> RUIN;
    public static StructurePieceType RUIN_PIECES = Registry.register(Registries.STRUCTURE_PIECE,
            JamiesMod.getModId("ruin"),  RuinGenerator.Piece::new);

   // public static StructureType<BlemishRuinStructure> BLEMISH_RUIN;
    //public static StructurePieceType BLEMISH_RUIN_PIECES = Registry.register(Registries.STRUCTURE_PIECE,
   ////        JamiesMod.getModId("blemish_ruin"),  BlemishRuinStructurePieces.BlemishRuinPiece::new);
    public static StructureType<TestRootStructure> TEST_ROOTS;

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
        TEST_ROOTS = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID, "test_roots"), () -> TestRootStructure.CODEC);
        RUIN = Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(JamiesMod.MOD_ID, "ruin"), () -> RuinStructure.CODEC);
    }
}
