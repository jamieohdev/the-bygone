package com.jamiedev.mod.init;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.blocks.entity.*;
import com.jamiedev.mod.worldgen.structure.AncientRootStructure;
import com.jamiedev.mod.worldgen.structure.TestRootStructure;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Locale;

import static com.jamiedev.mod.init.JamiesModBlocks.*;

public class JamiesModBlockEntities <T extends BlockEntity>
{
    BlockEntityType ref;

    public static BlockEntityType<PrimordialVentEntity> PRIMORDIAL_VENT;

    public static BlockEntityType<CasterBlockEntity> CASTER;

    public static BlockEntityType<PrimordialUrchinEntity> PRIMORDIAL_URCHIN;

    public static BlockEntityType<ModSignBlockEntity> MOD_SIGN_BLOCK_ENTITY;

    public static BlockEntityType<ModHangingSignBlockEntity> MOD_HANGING_SIGN_BLOCK_ENTITY;

    public static BlockEntityType<BlemishCatalystBlockEntity> BLEMISH_CATALYST;

    public static BlockEntityType<BygoneBrushableBlockEntity> BRUSHABLE_BLOCK;

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, JamiesMod.getModId(name), type);
    }

    public static void init()
    {
        BLEMISH_CATALYST = register("blemish_catalyst",
                BlockEntityType.Builder.create(BlemishCatalystBlockEntity::new, JamiesModBlocks.BLEMISH_CATALYST)
                        .build());
        CASTER = register("caster",
                BlockEntityType.Builder.create(CasterBlockEntity::new, JamiesModBlocks.CASTER)
                        .build());
        PRIMORDIAL_VENT = register("primordial_vent",
                BlockEntityType.Builder.create(PrimordialVentEntity::new, JamiesModBlocks.PRIMORDIAL_VENT)
                        .build());
        PRIMORDIAL_URCHIN = register("primordial_urchin",
                BlockEntityType.Builder.create(PrimordialUrchinEntity::new, JamiesModBlocks.PRIMORDIAL_URCHIN)
                        .build());
        MOD_SIGN_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                JamiesMod.getModId("mod_sign_entity"),
                FabricBlockEntityTypeBuilder.create(ModSignBlockEntity::new, ANCIENT_SIGN, ANCIENT_WALL_SIGN).build()
        );
        MOD_HANGING_SIGN_BLOCK_ENTITY = register("mod_hanging_sign_entity",
                BlockEntityType.Builder.create(ModHangingSignBlockEntity::new, JamiesModBlocks.ANCIENT_HANGING_SIGN, JamiesModBlocks.ANCIENT_WALL_HANGING_SIGN).build()
        );
        BRUSHABLE_BLOCK = register("brushable_block",
                BlockEntityType.Builder.create(BygoneBrushableBlockEntity::new, SUSPICIOUS_UMBER)
                        .build());

    }
}
