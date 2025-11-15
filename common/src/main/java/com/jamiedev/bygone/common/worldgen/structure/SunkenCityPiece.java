package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGStructures;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SunkenCityPiece
{
    private static final ResourceLocation[] SUNKEN_NBT = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/big_shellbrick_1"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/big_shellbrick_2"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/big_shellbrick_3"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/long_shellhouse"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/sea_ruins_1"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/sea_storehouse"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/sea_temple"),
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "sunken_ruins/shellhouses")
    };

    public SunkenCityPiece() {}

    public static void addPieces(StructureTemplateManager manager, StructurePieceAccessor holder, @NotNull RandomSource random, BlockPos pos) {
        Rotation blockRotation = Rotation.getRandom(random);
        holder.addPiece(new SunkenCityPiece.Piece(manager, Util.getRandom(SUNKEN_NBT, random), pos, blockRotation));
    }

    public static class Piece extends TemplateStructurePiece
    {
        public Piece(StructureTemplateManager manager, ResourceLocation resourceLocation, BlockPos pos, Rotation rotation) {
            super(BGStructures.SUNKEN_CITY_PIECES, 0, manager, resourceLocation, resourceLocation.toString(), makeSettings(rotation), pos);
        }

        public Piece(StructureTemplateManager manager, CompoundTag tag) {
            super(BGStructures.SUNKEN_CITY_PIECES, tag, manager,
                    (x) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
        }


        public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
            super(BGStructures.SUNKEN_CITY_PIECES, tag, context.structureTemplateManager(),
                    (x) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setKnownShape(false);
        }

        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.putString("Rot", this.placeSettings.getRotation().name());
        }

        public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, @NotNull RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            chunkBox.encapsulate(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
            super.postProcess(world, structureAccessor, chunkGenerator, random, chunkBox, chunkPos, pivot);
        }


        protected void handleDataMarker(String string, BlockPos pos, ServerLevelAccessor accessor, RandomSource random, BoundingBox box) {
        }
    }
}
