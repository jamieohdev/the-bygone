package com.jamiedev.bygone.common.worldgen.structure;

import com.google.common.collect.Lists;
import com.jamiedev.bygone.common.block.gourds.GourdDangoWallBlock;
import com.jamiedev.bygone.core.init.JamiesModLootTables;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGStructures;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BygoneMineshaftGenerator {
    public static final int field_34729 = 50;
    static final Logger LOGGER = LogUtils.getLogger();
    private static final int field_31551 = 3;
    private static final int field_31552 = 3;
    private static final int field_31553 = 5;
    private static final int field_31554 = 20;
    private static final int field_31555 = 50;
    private static final int field_31556 = 8;


    public BygoneMineshaftGenerator() {
    }

    private static BygoneMineshaftGenerator.BygoneMineshaftPart pickPiece(StructurePieceAccessor holder, @NotNull RandomSource random, int x, int y, int z, @Nullable Direction orientation, int chainLength, BygoneMineshaftStructure.Type type) {
        int i = random.nextInt(100);
        BoundingBox blockBox;
        if (i >= 80) {
            blockBox = BygoneMineshaftGenerator.BygoneMineshaftCrossing.getBoundingBox(holder, random, x, y, z, orientation);
            if (blockBox != null) {
                return new BygoneMineshaftGenerator.BygoneMineshaftCrossing(chainLength, blockBox, orientation, type);
            }
        } else if (i >= 70) {
            assert orientation != null;
            blockBox = BygoneMineshaftGenerator.BygoneMineshaftStairs.getBoundingBox(holder, random, x, y, z, orientation);
            if (blockBox != null) {
                return new BygoneMineshaftGenerator.BygoneMineshaftStairs(chainLength, blockBox, orientation, type);
            }
        } else {
            blockBox = BygoneMineshaftGenerator.BygoneMineshaftCorridor.getBoundingBox(holder, random, x, y, z, orientation);
            if (blockBox != null) {
                return new BygoneMineshaftGenerator.BygoneMineshaftCorridor(chainLength, random, blockBox, orientation, type);
            }
        }

        return null;
    }

    static BygoneMineshaftGenerator.BygoneMineshaftPart pieceGenerator(StructurePiece start, StructurePieceAccessor holder, @NotNull RandomSource random, int x, int y, int z, Direction orientation, int chainLength) {
        if (chainLength > 8) {
            return null;
        } else if (Math.abs(x - start.getBoundingBox().minX()) <= 80 && Math.abs(z - start.getBoundingBox().minZ()) <= 80) {
            BygoneMineshaftStructure.Type type = ((BygoneMineshaftGenerator.BygoneMineshaftPart) start).mineshaftType;
            BygoneMineshaftGenerator.BygoneMineshaftPart mineshaftPart = pickPiece(holder, random, x, y, z, orientation, chainLength + 1, type);
            if (mineshaftPart != null) {
                holder.addPiece(mineshaftPart);
                mineshaftPart.addChildren(start, holder, random);
            }

            return mineshaftPart;
        } else {
            return null;
        }
    }

    public static class BygoneMineshaftCrossing extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        private final Direction direction;


        public BygoneMineshaftCrossing(StructurePieceSerializationContext context, CompoundTag nbt) {
            super(BGStructures.BYGONE_MINESHAFT_CROSSING, nbt);
            this.direction = Direction.from2DDataValue(nbt.getInt("D"));
        }

        public BygoneMineshaftCrossing(int chainLength, BoundingBox boundingBox, @Nullable Direction orientation, BygoneMineshaftStructure.Type type) {
            super(BGStructures.BYGONE_MINESHAFT_CROSSING, chainLength, type, boundingBox);
            this.direction = orientation;
        }

        @Nullable
        public static BoundingBox getBoundingBox(StructurePieceAccessor holder, @NotNull RandomSource random, int x, int y, int z, Direction orientation) {
            byte i;
            if (random.nextInt(4) == 0) {
                i = 6;
            } else {
                i = 2;
            }

            BoundingBox blockBox;
            switch (orientation) {
                case NORTH:
                default:
                    blockBox = new BoundingBox(-1, 0, -4, 3, i, 0);
                    break;
                case SOUTH:
                    blockBox = new BoundingBox(-1, 0, 0, 3, i, 4);
                    break;
                case WEST:
                    blockBox = new BoundingBox(-4, 0, -1, 0, i, 3);
                    break;
                case EAST:
                    blockBox = new BoundingBox(0, 0, -1, 4, i, 3);
            }

            blockBox.move(x, y, z);
            return holder.findCollisionPiece(blockBox) != null ? null : blockBox;
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
            super.addAdditionalSaveData(context, nbt);
            nbt.putInt("D", this.direction.get2DDataValue());
        }

        @Override
        public void addChildren(StructurePiece start, StructurePieceAccessor holder, @NotNull RandomSource random) {
            int i = this.getGenDepth();
            switch (this.direction) {
                case NORTH:
                default:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
                    break;
                case SOUTH:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
                    break;
                case WEST:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.WEST, i);
                    break;
                case EAST:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, Direction.EAST, i);
            }


        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, @NotNull RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {
                BlockState blockState = this.mineshaftType.getPlanks();
                this.generateBox(world, chunkBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
                this.generateBox(world, chunkBox, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);


                this.generateCrossingPillar(world, chunkBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
                this.generateCrossingPillar(world, chunkBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
                this.generateCrossingPillar(world, chunkBox, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
                this.generateCrossingPillar(world, chunkBox, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
                int i = this.boundingBox.minY() - 1;

                for (int j = this.boundingBox.minX(); j <= this.boundingBox.maxX(); ++j) {
                    for (int k = this.boundingBox.minZ(); k <= this.boundingBox.maxZ(); ++k) {
                        this.tryPlaceFloor(world, chunkBox, blockState, j, i, k);
                    }
                }

            }
        }

        private void generateCrossingPillar(WorldGenLevel world, BoundingBox boundingBox, int x, int minY, int z, int maxY) {
            if (!this.getBlock(world, x, maxY + 1, z, boundingBox).isAir()) {
                this.generateBox(world, boundingBox, x, minY, z, x, maxY, z, this.mineshaftType.getPlanks(), CAVE_AIR, false);
            }

        }
    }

    public static class BygoneMineshaftStairs extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        public BygoneMineshaftStairs(int chainLength, BoundingBox boundingBox, Direction orientation, BygoneMineshaftStructure.Type type) {
            super(BGStructures.BYGONE_MINESHAFT_STAIRS, chainLength, type, boundingBox);
            this.setOrientation(orientation);
        }

        public BygoneMineshaftStairs(StructurePieceSerializationContext context, CompoundTag nbt) {
            super(BGStructures.BYGONE_MINESHAFT_STAIRS, nbt);
        }

        @Nullable
        public static BoundingBox getBoundingBox(StructurePieceAccessor holder, @NotNull RandomSource random, int x, int y, int z, Direction orientation) {
            BoundingBox blockBox;
            switch (orientation) {
                case NORTH:
                default:
                    blockBox = new BoundingBox(0, -5, -8, 2, 2, 0);
                    break;
                case SOUTH:
                    blockBox = new BoundingBox(0, -5, 0, 2, 2, 8);
                    break;
                case WEST:
                    blockBox = new BoundingBox(-8, -5, 0, 0, 2, 2);
                    break;
                case EAST:
                    blockBox = new BoundingBox(0, -5, 0, 8, 2, 2);
            }

            blockBox.move(x, y, z);
            return holder.findCollisionPiece(blockBox) != null ? null : blockBox;
        }

        @Override
        public void addChildren(StructurePiece start, StructurePieceAccessor holder, @NotNull RandomSource random) {
            int i = this.getGenDepth();
            Direction direction = this.getOrientation();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                        break;
                    case SOUTH:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                        break;
                    case WEST:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.WEST, i);
                        break;
                    case EAST:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.EAST, i);
                }
            }

        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, @NotNull RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {
                this.generateBox(world, chunkBox, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
                this.generateBox(world, chunkBox, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);

                for (int i = 0; i < 5; ++i) {
                    this.generateBox(world, chunkBox, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, CAVE_AIR, CAVE_AIR, false);
                }

            }
        }
    }

    public static class BygoneMineshaftCorridor extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        private final boolean hasRails;
        private final boolean hasCobwebs;
        private final int length;
        private boolean hasSpawner;

        public BygoneMineshaftCorridor(CompoundTag nbt) {
            super(BGStructures.BYGONE_MINESHAFT_CORRIDOR, nbt);
            this.hasRails = nbt.getBoolean("hr");
            this.hasCobwebs = nbt.getBoolean("sc");
            this.hasSpawner = nbt.getBoolean("hps");
            this.length = nbt.getInt("Num");
        }

        public BygoneMineshaftCorridor(StructurePieceSerializationContext structureContext, CompoundTag nbt) {
            super(BGStructures.BYGONE_MINESHAFT_CORRIDOR, nbt);
            this.hasRails = nbt.getBoolean("hr");
            this.hasCobwebs = nbt.getBoolean("sc");
            this.hasSpawner = nbt.getBoolean("hps");
            this.length = nbt.getInt("Num");
        }

        public BygoneMineshaftCorridor(int chainLength, @NotNull RandomSource random, BoundingBox boundingBox, Direction orientation, BygoneMineshaftStructure.Type type) {
            super(BGStructures.BYGONE_MINESHAFT_CORRIDOR, chainLength, type, boundingBox);
            this.setOrientation(orientation);
            this.hasRails = random.nextInt(3) == 0;
            this.hasCobwebs = !this.hasRails && random.nextInt(23) == 0;
            if (Objects.requireNonNull(this.getOrientation()).getAxis() == Direction.Axis.Z) {
                this.length = boundingBox.getZSpan() / 5;
            } else {
                this.length = boundingBox.getXSpan() / 5;
            }

        }

        @Nullable
        public static BoundingBox getBoundingBox(StructurePieceAccessor holder, @NotNull RandomSource random, int x, int y, int z, Direction orientation) {
            for (int i = random.nextInt(3) + 2; i > 0; --i) {
                int j = i * 5;
                BoundingBox blockBox;
                switch (orientation) {
                    case NORTH:
                    default:
                        blockBox = new BoundingBox(0, 0, -(j - 1), 2, 2, 0);
                        break;
                    case SOUTH:
                        blockBox = new BoundingBox(0, 0, 0, 2, 2, j - 1);
                        break;
                    case WEST:
                        blockBox = new BoundingBox(-(j - 1), 0, 0, 0, 2, 2);
                        break;
                    case EAST:
                        blockBox = new BoundingBox(0, 0, 0, j - 1, 2, 2);
                }

                blockBox.move(x, y, z);
                if (holder.findCollisionPiece(blockBox) == null) {
                    return blockBox;
                }
            }

            return null;
        }

        private static void fillColumn(WorldGenLevel world, BlockState state, BlockPos.MutableBlockPos pos, int startY, int endY) {
            for (int i = startY; i < endY; ++i) {
                world.setBlock(pos.setY(i), state, 2);
            }

        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
            super.addAdditionalSaveData(context, nbt);
            nbt.putBoolean("hr", this.hasRails);
            nbt.putBoolean("sc", this.hasCobwebs);
            nbt.putBoolean("hps", this.hasSpawner);
            nbt.putInt("Num", this.length);
        }

        @Override
        public void addChildren(StructurePiece start, StructurePieceAccessor holder, @NotNull RandomSource random) {
            int i = this.getGenDepth();
            int j = random.nextInt(4);
            Direction direction = this.getOrientation();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.WEST, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), Direction.EAST, i);
                        }
                        break;
                    case SOUTH:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.WEST, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() - 3, Direction.EAST, i);
                        }
                        break;
                    case WEST:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX(), this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                        }
                        break;
                    case EAST:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ(), direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + random.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                        }
                }
            }

            if (i < 8) {
                int k;
                int l;
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    for (k = this.boundingBox.minX() + 3; k + 3 <= this.boundingBox.maxX(); k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, k, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i + 1);
                        } else if (l == 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, k, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (k = this.boundingBox.minZ() + 3; k + 3 <= this.boundingBox.maxZ(); k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), k, Direction.WEST, i + 1);
                        } else if (l == 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), k, Direction.EAST, i + 1);
                        }
                    }
                }
            }

        }

        @Override
        protected boolean createChest(WorldGenLevel world, BoundingBox boundingBox, @NotNull RandomSource random, int x, int y, int z, ResourceKey<LootTable> lootTable) {
            BlockPos blockPos = this.getWorldPos(x, y, z);
            if (boundingBox.isInside(blockPos) && world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos.below()).isAir()) {
                BlockState blockState = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                this.placeBlock(world, blockState, x, y, z, boundingBox);
                MinecartChest chestMinecartEntity = new MinecartChest(world.getLevel(), (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5);
                chestMinecartEntity.setLootTable(lootTable, random.nextLong());
                world.addFreshEntity(chestMinecartEntity);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, @NotNull RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {

                int m = this.length * 5 - 1;
                BlockState blockState = this.mineshaftType.getPlanks();
                this.generateBox(world, chunkBox, 0, 0, 0, 2, 1, m, CAVE_AIR, CAVE_AIR, false);
                this.generateMaybeBox(world, chunkBox, random, 0.8F, 0, 2, 0, 2, 2, m, CAVE_AIR, CAVE_AIR, false, false);
                if (this.hasCobwebs) {
                    this.generateMaybeBox(world, chunkBox, random, 0.6F, 0, 0, 0, 2, 1, m, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
                }

                int n;
                int o;
                for (n = 0; n < this.length; ++n) {
                    o = 2 + n * 5;
                    this.generateSupports(world, chunkBox, 0, 0, o, 2, 2, random);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.1F, 0, 2, o - 1);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.1F, 2, 2, o - 1);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.1F, 0, 2, o + 1);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.1F, 2, 2, o + 1);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.05F, 0, 2, o - 2);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.05F, 2, 2, o - 2);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.05F, 0, 2, o + 2);
                    this.addCobwebsUnderground(world, chunkBox, random, 0.05F, 2, 2, o + 2);
                    if (random.nextInt(100) == 0) {
                        this.createChest(world, chunkBox, random, 2, 0, o - 1, JamiesModLootTables.ABANDONED_MINESHAFT_CHEST);
                    }

                    if (random.nextInt(100) == 0) {
                        this.createChest(world, chunkBox, random, 0, 0, o + 1, JamiesModLootTables.ABANDONED_MINESHAFT_CHEST);
                    }

                    if (this.hasCobwebs && !this.hasSpawner) {

                        int q = o - 1 + random.nextInt(3);
                        BlockPos blockPos = this.getWorldPos(1, 0, q);
                        if (chunkBox.isInside(blockPos) && this.isInterior(world, 1, 0, q, chunkBox)) {
                            this.hasSpawner = true;
                            world.setBlock(blockPos, Blocks.SPAWNER.defaultBlockState(), 2);
                            BlockEntity blockEntity = world.getBlockEntity(blockPos);
                            if (blockEntity instanceof SpawnerBlockEntity mobSpawnerBlockEntity) {
                                mobSpawnerBlockEntity.setEntityId(BGEntityTypes.PESKY.get(), random);
                            }
                        }
                    }
                }

                for (n = 0; n <= 2; ++n) {
                    for (o = 0; o <= m; ++o) {
                        this.tryPlaceFloor(world, chunkBox, blockState, n, -1, o);
                    }
                }


                this.fillSupportBeam(world, chunkBox, 0, -1, 2);
                if (this.length > 1) {
                    o = m - 2;
                    this.fillSupportBeam(world, chunkBox, 0, -1, o);
                }

                if (this.hasRails) {
                    BlockState blockState2 = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH);

                    for (int p = 0; p <= m; ++p) {
                        BlockState blockState3 = this.getBlock(world, 1, -1, p, chunkBox);
                        if (!blockState3.isAir() && blockState3.isSolidRender(world, this.getWorldPos(1, -1, p))) {
                            float f = this.isInterior(world, 1, 0, p, chunkBox) ? 0.7F : 0.9F;
                            this.maybeGenerateBlock(world, chunkBox, random, f, 1, 0, p, blockState2);
                        }
                    }
                }

            }
        }

        private void fillSupportBeam(WorldGenLevel world, BoundingBox box, int x, int y, int z) {
            BlockState blockState = this.mineshaftType.getLog();
            BlockState blockState2 = this.mineshaftType.getPlanks();
            if (this.getBlock(world, x, y, z, box).is(blockState2.getBlock())) {
                this.fillSupportBeam(world, blockState, x, y, z, box);
            }

            if (this.getBlock(world, x + 2, y, z, box).is(blockState2.getBlock())) {
                this.fillSupportBeam(world, blockState, x + 2, y, z, box);
            }

        }

        @Override
        protected void fillColumnDown(WorldGenLevel world, BlockState state, int x, int y, int z, BoundingBox box) {
            BlockPos.MutableBlockPos mutable = this.getWorldPos(x, y, z);
            if (box.isInside(mutable)) {
                int i = mutable.getY();

                while (this.isReplaceableByStructures(world.getBlockState(mutable)) && mutable.getY() > world.getMinBuildHeight() + 1) {
                    mutable.move(Direction.DOWN);
                }

                if (this.isUpsideSolidFullSquare(world, mutable, world.getBlockState(mutable))) {
                    while (mutable.getY() < i) {
                        mutable.move(Direction.UP);
                        world.setBlock(mutable, state, 2);
                    }

                }
            }
        }

        protected void fillSupportBeam(WorldGenLevel world, BlockState state, int x, int y, int z, BoundingBox box) {
            BlockPos.MutableBlockPos mutable = this.getWorldPos(x, y, z);
            if (box.isInside(mutable)) {
                int i = mutable.getY();
                int j = 1;
                boolean bl = true;

                for (boolean bl2 = true; bl || bl2; ++j) {
                    BlockState blockState;
                    boolean bl3;
                    if (bl) {
                        mutable.setY(i - j);
                        blockState = world.getBlockState(mutable);
                        bl3 = this.isReplaceableByStructures(blockState) && !blockState.is(Blocks.LAVA);
                        if (!bl3 && this.isUpsideSolidFullSquare(world, mutable, blockState)) {
                            fillColumn(world, state, mutable, i - j + 1, i);
                            return;
                        }

                        bl = j <= 20 && bl3 && mutable.getY() > world.getMinBuildHeight() + 1;
                    }

                    if (bl2) {
                        mutable.setY(i + j);
                        blockState = world.getBlockState(mutable);
                        bl3 = this.isReplaceableByStructures(blockState);
                        if (!bl3 && this.sideCoversSmallSquare(world, mutable, blockState)) {
                            world.setBlock(mutable.setY(i + 1), this.mineshaftType.getFence(), 2);
                            fillColumn(world, Blocks.CHAIN.defaultBlockState(), mutable, i + 2, i + j);
                            return;
                        }

                        bl2 = j <= 50 && bl3 && mutable.getY() < world.getMaxBuildHeight() - 1;
                    }
                }

            }
        }

        private boolean isUpsideSolidFullSquare(LevelReader world, BlockPos pos, BlockState state) {
            return state.isFaceSturdy(world, pos, Direction.UP);
        }

        private boolean sideCoversSmallSquare(LevelReader world, BlockPos pos, BlockState state) {
            return Block.canSupportCenter(world, pos, Direction.DOWN) && !(state.getBlock() instanceof FallingBlock);
        }

        private void generateSupports(WorldGenLevel world, BoundingBox boundingBox, int minX, int minY, int z, int maxY, int maxX, @NotNull RandomSource random) {
            if (this.isSolidCeiling(world, boundingBox, minX, maxX, maxY, z)) {
                BlockState blockState = this.mineshaftType.getPlanks();
                BlockState blockState2 = this.mineshaftType.getFence();
                this.generateBox(world, boundingBox, minX, minY, z, minX, maxY - 1, z, blockState2.setValue(FenceBlock.WEST, true), CAVE_AIR, false);
                this.generateBox(world, boundingBox, maxX, minY, z, maxX, maxY - 1, z, blockState2.setValue(FenceBlock.EAST, true), CAVE_AIR, false);
                if (random.nextInt(4) == 0) {
                    this.generateBox(world, boundingBox, minX, maxY, z, minX, maxY, z, blockState, CAVE_AIR, false);
                    this.generateBox(world, boundingBox, maxX, maxY, z, maxX, maxY, z, blockState, CAVE_AIR, false);
                } else {
                    this.generateBox(world, boundingBox, minX, maxY, z, maxX, maxY, z, blockState, CAVE_AIR, false);
                    this.maybeGenerateBlock(world, boundingBox, random, 0.05F, minX + 1, maxY, z - 1, BGBlocks.GOURD_DANGO_WALL.get().defaultBlockState().setValue(GourdDangoWallBlock.FACING, Direction.SOUTH));
                    this.maybeGenerateBlock(world, boundingBox, random, 0.05F, minX + 1, maxY, z + 1, BGBlocks.GOURD_DANGO_WALL.get().defaultBlockState().setValue(GourdDangoWallBlock.FACING, Direction.NORTH));
                }

            }
        }

        private void addCobwebsUnderground(WorldGenLevel world, BoundingBox box, @NotNull RandomSource random, float threshold, int x, int y, int z) {
            if (this.isInterior(world, x, y, z, box) && random.nextFloat() < threshold && this.hasSolidNeighborBlocks(world, box, x, y, z, 2)) {
                this.placeBlock(world, Blocks.COBWEB.defaultBlockState(), x, y, z, box);
            }

        }

        private boolean hasSolidNeighborBlocks(WorldGenLevel world, BoundingBox box, int x, int y, int z, int count) {
            BlockPos.MutableBlockPos mutable = this.getWorldPos(x, y, z);
            int i = 0;
            Direction[] var9 = Direction.values();
            int var10 = var9.length;

            for (int var11 = 0; var11 < var10; ++var11) {
                Direction direction = var9[var11];
                mutable.move(direction);
                if (box.isInside(mutable) && world.getBlockState(mutable).isFaceSturdy(world, mutable, direction.getOpposite())) {
                    ++i;
                    if (i >= count) {
                        return true;
                    }
                }

                mutable.move(direction.getOpposite());
            }

            return false;
        }
    }

    private abstract static class BygoneMineshaftPart extends StructurePiece {
        protected BygoneMineshaftStructure.Type mineshaftType;

        public BygoneMineshaftPart(StructurePieceType structurePieceType, int chainLength, BygoneMineshaftStructure.Type type, BoundingBox box) {
            super(structurePieceType, chainLength, box);
            this.mineshaftType = type;
        }

        public BygoneMineshaftPart(StructurePieceType structurePieceType, CompoundTag nbtCompound) {
            super(structurePieceType, nbtCompound);
            this.mineshaftType = BygoneMineshaftStructure.Type.byId(nbtCompound.getInt("MST"));
        }

        @Override
        protected boolean canBeReplaced(LevelReader world, int x, int y, int z, BoundingBox box) {
            BlockState blockState = this.getBlock(world, x, y, z, box);
            return !blockState.is(this.mineshaftType.getPlanks().getBlock()) && !blockState.is(this.mineshaftType.getLog().getBlock()) && !blockState.is(this.mineshaftType.getFence().getBlock()) && !blockState.is(Blocks.CHAIN);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
            nbt.putInt("MST", this.mineshaftType.ordinal());
        }

        protected boolean isSolidCeiling(BlockGetter world, BoundingBox boundingBox, int minX, int maxX, int y, int z) {
            for (int i = minX; i <= maxX; ++i) {
                if (this.getBlock(world, i, y + 1, z, boundingBox).isAir()) {
                    return false;
                }
            }

            return true;
        }

        protected boolean cannotGenerate(LevelAccessor world, BoundingBox box) {
            int i = Math.max(this.boundingBox.minX() - 1, box.minX());
            int j = Math.max(this.boundingBox.minY() - 1, box.minY());
            int k = Math.max(this.boundingBox.minZ() - 1, box.minZ());
            int l = Math.min(this.boundingBox.maxX() + 1, box.maxX());
            int m = Math.min(this.boundingBox.maxY() + 1, box.maxY());
            int n = Math.min(this.boundingBox.maxZ() + 1, box.maxZ());
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos((i + l) / 2, (j + m) / 2, (k + n) / 2);
            if (world.getBiome(mutable).is(BiomeTags.MINESHAFT_BLOCKING)) {
                return true;
            } else {
                int o;
                int p;
                for (o = i; o <= l; ++o) {
                    for (p = k; p <= n; ++p) {
                        if (world.getBlockState(mutable.set(o, j, p)).liquid()) {
                            return true;
                        }

                        if (world.getBlockState(mutable.set(o, m, p)).liquid()) {
                            return true;
                        }
                    }
                }

                for (o = i; o <= l; ++o) {
                    for (p = j; p <= m; ++p) {
                        if (world.getBlockState(mutable.set(o, p, k)).liquid()) {
                            return true;
                        }

                        if (world.getBlockState(mutable.set(o, p, n)).liquid()) {
                            return true;
                        }
                    }
                }

                for (o = k; o <= n; ++o) {
                    for (p = j; p <= m; ++p) {
                        if (world.getBlockState(mutable.set(i, p, o)).liquid()) {
                            return true;
                        }

                        if (world.getBlockState(mutable.set(l, p, o)).liquid()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected void tryPlaceFloor(WorldGenLevel world, BoundingBox box, BlockState state, int x, int y, int z) {
            if (this.isInterior(world, x, y, z, box)) {
                BlockPos blockPos = this.getWorldPos(x, y, z);
                BlockState blockState = world.getBlockState(blockPos);
                if (!blockState.isFaceSturdy(world, blockPos, Direction.UP)) {
                    world.setBlock(blockPos, state, 2);
                }

            }
        }
    }

    public static class BygoneMineshaftRoom extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        private final List<BoundingBox> entrances = Lists.newLinkedList();

        public BygoneMineshaftRoom(int chainLength, @NotNull RandomSource random, int x, int z, BygoneMineshaftStructure.Type type) {
            super(BGStructures.BYGONE_MINESHAFT_ROOM, chainLength, type, new BoundingBox(x, 100, z, x + 7 + random.nextInt(6), 200, z + 7 + random.nextInt(6)));
            this.mineshaftType = type;
        }

        public BygoneMineshaftRoom(StructurePieceSerializationContext context, CompoundTag nbt) {
            super(BGStructures.BYGONE_MINESHAFT_ROOM, nbt);
            DataResult<List<BoundingBox>> var10000 = BoundingBox.CODEC.listOf().parse(NbtOps.INSTANCE, nbt.getList("Entrances", 11));
            Logger var10001 = BygoneMineshaftGenerator.LOGGER;
            Objects.requireNonNull(var10001);
            Optional<List<BoundingBox>> var2 = var10000.resultOrPartial(var10001::error);
            List<BoundingBox> var3 = this.entrances;
            Objects.requireNonNull(var3);
            var2.ifPresent(var3::addAll);
        }

        @Override
        public void addChildren(StructurePiece start, StructurePieceAccessor holder, @NotNull RandomSource random) {
            int i = this.getGenDepth();
            int j = this.boundingBox.getYSpan() - 3 - 1;
            if (j <= 0) {
                j = 1;
            }

            int k;
            BygoneMineshaftGenerator.BygoneMineshaftPart structurePiece;
            BoundingBox blockBox;
            for (k = 0; k < this.boundingBox.getXSpan(); k += 4) {
                k += random.nextInt(this.boundingBox.getXSpan());
                if (k + 3 > this.boundingBox.getXSpan()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + k, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BoundingBox(blockBox.minX(), blockBox.minY(), this.boundingBox.minZ(), blockBox.maxX(), blockBox.maxY(), this.boundingBox.minZ() + 1));
                }
            }

            for (k = 0; k < this.boundingBox.getXSpan(); k += 4) {
                k += random.nextInt(this.boundingBox.getXSpan());
                if (k + 3 > this.boundingBox.getXSpan()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() + k, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BoundingBox(blockBox.minX(), blockBox.minY(), this.boundingBox.maxZ() - 1, blockBox.maxX(), blockBox.maxY(), this.boundingBox.maxZ()));
                }
            }

            for (k = 0; k < this.boundingBox.getZSpan(); k += 4) {
                k += random.nextInt(this.boundingBox.getZSpan());
                if (k + 3 > this.boundingBox.getZSpan()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.minZ() + k, Direction.WEST, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BoundingBox(this.boundingBox.minX(), blockBox.minY(), blockBox.minZ(), this.boundingBox.minX() + 1, blockBox.maxY(), blockBox.maxZ()));
                }
            }

            for (k = 0; k < this.boundingBox.getZSpan(); k += 4) {
                k += random.nextInt(this.boundingBox.getZSpan());
                if (k + 3 > this.boundingBox.getZSpan()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + random.nextInt(j) + 1, this.boundingBox.minZ() + k, Direction.EAST, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BoundingBox(this.boundingBox.maxX() - 1, blockBox.minY(), blockBox.minZ(), this.boundingBox.maxX(), blockBox.maxY(), blockBox.maxZ()));
                }
            }

        }

        @Override
        public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, @NotNull RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {
                this.generateBox(world, chunkBox, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
                Iterator<BoundingBox> var8 = this.entrances.iterator();

                while (var8.hasNext()) {
                    BoundingBox blockBox = var8.next();
                    this.generateBox(world, chunkBox, blockBox.minX(), blockBox.maxY() - 2, blockBox.minZ(), blockBox.maxX(), blockBox.maxY(), blockBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
                }

                this.generateUpperHalfSphere(world, chunkBox, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
            }
        }

        @Override
        public void move(int x, int y, int z) {
            super.move(x, y, z);
            Iterator<BoundingBox> var4 = this.entrances.iterator();

            while (var4.hasNext()) {
                BoundingBox blockBox = var4.next();
                blockBox.move(x, y, z);
            }

        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag nbt) {
            super.addAdditionalSaveData(context, nbt);
            DataResult<Tag> var10000 = BoundingBox.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.entrances);
            Logger var10001 = BygoneMineshaftGenerator.LOGGER;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> {
                nbt.put("Entrances", nbtElement);
            });
        }
    }
}
