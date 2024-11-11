package com.jamiedev.mod.common.worldgen.structure;

import com.google.common.collect.Lists;
import com.jamiedev.mod.common.blocks.gourds.GourdDangoWallBlock;
import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.jamiedev.mod.fabric.init.JamiesModLootTables;
import com.jamiedev.mod.fabric.init.JamiesModStructures;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BygoneMineshaftGenerator {
    static final Logger LOGGER = LogUtils.getLogger();
    private static final int field_31551 = 3;
    private static final int field_31552 = 3;
    private static final int field_31553 = 5;
    private static final int field_31554 = 20;
    private static final int field_31555 = 50;
    private static final int field_31556 = 8;
    public static final int field_34729 = 50;


    public BygoneMineshaftGenerator() {
    }

    private static BygoneMineshaftGenerator.BygoneMineshaftPart pickPiece(StructurePiecesHolder holder, Random random, int x, int y, int z, @Nullable Direction orientation, int chainLength, BygoneMineshaftStructure.Type type) {
        int i = random.nextInt(100);
        BlockBox blockBox;
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

    static BygoneMineshaftGenerator.BygoneMineshaftPart pieceGenerator(StructurePiece start, StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
        if (chainLength > 8) {
            return null;
        } else if (Math.abs(x - start.getBoundingBox().getMinX()) <= 80 && Math.abs(z - start.getBoundingBox().getMinZ()) <= 80) {
            BygoneMineshaftStructure.Type type = ((BygoneMineshaftGenerator.BygoneMineshaftPart)start).mineshaftType;
            BygoneMineshaftGenerator.BygoneMineshaftPart mineshaftPart = pickPiece(holder, random, x, y, z, orientation, chainLength + 1, type);
            if (mineshaftPart != null) {
                holder.addPiece(mineshaftPart);
                mineshaftPart.fillOpenings(start, holder, random);
            }

            return mineshaftPart;
        } else {
            return null;
        }
    }

    public static class BygoneMineshaftCrossing extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        private final Direction direction;


        public BygoneMineshaftCrossing(StructureContext context, NbtCompound nbt) {
            super(JamiesModStructures.BYGONE_MINESHAFT_CROSSING, nbt);
            this.direction = Direction.fromHorizontal(nbt.getInt("D"));
        }

        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putInt("D", this.direction.getHorizontal());
        }

        public BygoneMineshaftCrossing(int chainLength, BlockBox boundingBox, @Nullable Direction orientation, BygoneMineshaftStructure.Type type) {
            super(JamiesModStructures.BYGONE_MINESHAFT_CROSSING, chainLength, type, boundingBox);
            this.direction = orientation;
        }

        @Nullable
        public static BlockBox getBoundingBox(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation) {
            byte i;
            if (random.nextInt(4) == 0) {
                i = 6;
            } else {
                i = 2;
            }

            BlockBox blockBox;
            switch (orientation) {
                case NORTH:
                default:
                    blockBox = new BlockBox(-1, 0, -4, 3, i, 0);
                    break;
                case SOUTH:
                    blockBox = new BlockBox(-1, 0, 0, 3, i, 4);
                    break;
                case WEST:
                    blockBox = new BlockBox(-4, 0, -1, 0, i, 3);
                    break;
                case EAST:
                    blockBox = new BlockBox(0, 0, -1, 4, i, 3);
            }

            blockBox.move(x, y, z);
            return holder.getIntersecting(blockBox) != null ? null : blockBox;
        }

        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            int i = this.getChainLength();
            switch (this.direction) {
                case NORTH:
                default:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, Direction.NORTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, Direction.WEST, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, Direction.EAST, i);
                    break;
                case SOUTH:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, Direction.WEST, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, Direction.EAST, i);
                    break;
                case WEST:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, Direction.NORTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, Direction.WEST, i);
                    break;
                case EAST:
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, Direction.NORTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i);
                    BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, Direction.EAST, i);
            }



        }

        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {
                BlockState blockState = this.mineshaftType.getPlanks();
                this.fillWithOutline(world, chunkBox, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ(), this.boundingBox.getMaxX() - 1, this.boundingBox.getMaxY(), this.boundingBox.getMaxZ(), AIR, AIR, false);
                this.fillWithOutline(world, chunkBox, this.boundingBox.getMinX(), this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, this.boundingBox.getMaxX(), this.boundingBox.getMaxY(), this.boundingBox.getMaxZ() - 1, AIR, AIR, false);


                this.generateCrossingPillar(world, chunkBox, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, this.boundingBox.getMaxY());
                this.generateCrossingPillar(world, chunkBox, this.boundingBox.getMinX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() - 1, this.boundingBox.getMaxY());
                this.generateCrossingPillar(world, chunkBox, this.boundingBox.getMaxX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + 1, this.boundingBox.getMaxY());
                this.generateCrossingPillar(world, chunkBox, this.boundingBox.getMaxX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() - 1, this.boundingBox.getMaxY());
                int i = this.boundingBox.getMinY() - 1;

                for(int j = this.boundingBox.getMinX(); j <= this.boundingBox.getMaxX(); ++j) {
                    for(int k = this.boundingBox.getMinZ(); k <= this.boundingBox.getMaxZ(); ++k) {
                        this.tryPlaceFloor(world, chunkBox, blockState, j, i, k);
                    }
                }

            }
        }

        private void generateCrossingPillar(StructureWorldAccess world, BlockBox boundingBox, int x, int minY, int z, int maxY) {
            if (!this.getBlockAt(world, x, maxY + 1, z, boundingBox).isAir()) {
                this.fillWithOutline(world, boundingBox, x, minY, z, x, maxY, z, this.mineshaftType.getPlanks(), AIR, false);
            }

        }
    }

    public static class BygoneMineshaftStairs extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        public BygoneMineshaftStairs(int chainLength, BlockBox boundingBox, Direction orientation, BygoneMineshaftStructure.Type type) {
            super(JamiesModStructures.BYGONE_MINESHAFT_STAIRS, chainLength, type, boundingBox);
            this.setOrientation(orientation);
        }

        public BygoneMineshaftStairs(StructureContext context, NbtCompound nbt) {
            super(JamiesModStructures.BYGONE_MINESHAFT_STAIRS, nbt);
        }

        @Nullable
        public static BlockBox getBoundingBox(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation) {
            BlockBox blockBox;
            switch (orientation) {
                case NORTH:
                default:
                    blockBox = new BlockBox(0, -5, -8, 2, 2, 0);
                    break;
                case SOUTH:
                    blockBox = new BlockBox(0, -5, 0, 2, 2, 8);
                    break;
                case WEST:
                    blockBox = new BlockBox(-8, -5, 0, 0, 2, 2);
                    break;
                case EAST:
                    blockBox = new BlockBox(0, -5, 0, 8, 2, 2);
            }

            blockBox.move(x, y, z);
            return holder.getIntersecting(blockBox) != null ? null : blockBox;
        }

        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            int i = this.getChainLength();
            Direction direction = this.getFacing();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, Direction.NORTH, i);
                        break;
                    case SOUTH:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i);
                        break;
                    case WEST:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ(), Direction.WEST, i);
                        break;
                    case EAST:
                        BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ(), Direction.EAST, i);
                }
            }

        }

        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {
                this.fillWithOutline(world, chunkBox, 0, 5, 0, 2, 7, 1, AIR, AIR, false);
                this.fillWithOutline(world, chunkBox, 0, 0, 7, 2, 2, 8, AIR, AIR, false);

                for(int i = 0; i < 5; ++i) {
                    this.fillWithOutline(world, chunkBox, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, AIR, AIR, false);
                }

            }
        }
    }

    public static class BygoneMineshaftCorridor extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        private final boolean hasRails;
        private final boolean hasCobwebs;
        private boolean hasSpawner;
        private final int length;

        public BygoneMineshaftCorridor(NbtCompound nbt) {
            super(JamiesModStructures.BYGONE_MINESHAFT_CORRIDOR, nbt);
            this.hasRails = nbt.getBoolean("hr");
            this.hasCobwebs = nbt.getBoolean("sc");
            this.hasSpawner = nbt.getBoolean("hps");
            this.length = nbt.getInt("Num");
        }

        public BygoneMineshaftCorridor(StructureContext structureContext, NbtCompound nbt) {
            super(JamiesModStructures.BYGONE_MINESHAFT_CORRIDOR, nbt);
            this.hasRails = nbt.getBoolean("hr");
            this.hasCobwebs = nbt.getBoolean("sc");
            this.hasSpawner = nbt.getBoolean("hps");
            this.length = nbt.getInt("Num");
        }

        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putBoolean("hr", this.hasRails);
            nbt.putBoolean("sc", this.hasCobwebs);
            nbt.putBoolean("hps", this.hasSpawner);
            nbt.putInt("Num", this.length);
        }

        public BygoneMineshaftCorridor(int chainLength, Random random, BlockBox boundingBox, Direction orientation, BygoneMineshaftStructure.Type type) {
            super(JamiesModStructures.BYGONE_MINESHAFT_CORRIDOR, chainLength, type, boundingBox);
            this.setOrientation(orientation);
            this.hasRails = random.nextInt(3) == 0;
            this.hasCobwebs = !this.hasRails && random.nextInt(23) == 0;
            if (Objects.requireNonNull(this.getFacing()).getAxis() == Direction.Axis.Z) {
                this.length = boundingBox.getBlockCountZ() / 5;
            } else {
                this.length = boundingBox.getBlockCountX() / 5;
            }

        }

        @Nullable
        public static BlockBox getBoundingBox(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation) {
            for(int i = random.nextInt(3) + 2; i > 0; --i) {
                int j = i * 5;
                BlockBox blockBox;
                switch (orientation) {
                    case NORTH:
                    default:
                        blockBox = new BlockBox(0, 0, -(j - 1), 2, 2, 0);
                        break;
                    case SOUTH:
                        blockBox = new BlockBox(0, 0, 0, 2, 2, j - 1);
                        break;
                    case WEST:
                        blockBox = new BlockBox(-(j - 1), 0, 0, 0, 2, 2);
                        break;
                    case EAST:
                        blockBox = new BlockBox(0, 0, 0, j - 1, 2, 2);
                }

                blockBox.move(x, y, z);
                if (holder.getIntersecting(blockBox) == null) {
                    return blockBox;
                }
            }

            return null;
        }

        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            int i = this.getChainLength();
            int j = random.nextInt(4);
            Direction direction = this.getFacing();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMinZ() - 1, direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMinZ(), Direction.WEST, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMinZ(), Direction.EAST, i);
                        }
                        break;
                    case SOUTH:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMaxZ() + 1, direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMaxZ() - 3, Direction.WEST, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMaxZ() - 3, Direction.EAST, i);
                        }
                        break;
                    case WEST:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMinZ(), direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMinZ() - 1, Direction.NORTH, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i);
                        }
                        break;
                    case EAST:
                        if (j <= 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMinZ(), direction, i);
                        } else if (j == 2) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() - 3, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMinZ() - 1, Direction.NORTH, i);
                        } else {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() - 3, this.boundingBox.getMinY() - 1 + random.nextInt(3), this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i);
                        }
                }
            }

            if (i < 8) {
                int k;
                int l;
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    for(k = this.boundingBox.getMinX() + 3; k + 3 <= this.boundingBox.getMaxX(); k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, k, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, Direction.NORTH, i + 1);
                        } else if (l == 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, k, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i + 1);
                        }
                    }
                } else {
                    for(k = this.boundingBox.getMinZ() + 3; k + 3 <= this.boundingBox.getMaxZ(); k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), k, Direction.WEST, i + 1);
                        } else if (l == 1) {
                            BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), k, Direction.EAST, i + 1);
                        }
                    }
                }
            }

        }

        protected boolean addChest(StructureWorldAccess world, BlockBox boundingBox, Random random, int x, int y, int z, RegistryKey<LootTable> lootTable) {
            BlockPos blockPos = this.offsetPos(x, y, z);
            if (boundingBox.contains(blockPos) && world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos.down()).isAir()) {
                BlockState blockState = (BlockState) Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                this.addBlock(world, blockState, x, y, z, boundingBox);
                ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(world.toServerWorld(), (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
                chestMinecartEntity.setLootTable(lootTable, random.nextLong());
                world.spawnEntity(chestMinecartEntity);
                return true;
            } else {
                return false;
            }
        }

        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {

                int m = this.length * 5 - 1;
                BlockState blockState = this.mineshaftType.getPlanks();
                this.fillWithOutline(world, chunkBox, 0, 0, 0, 2, 1, m, AIR, AIR, false);
                this.fillWithOutlineUnderSeaLevel(world, chunkBox, random, 0.8F, 0, 2, 0, 2, 2, m, AIR, AIR, false, false);
                if (this.hasCobwebs) {
                    this.fillWithOutlineUnderSeaLevel(world, chunkBox, random, 0.6F, 0, 0, 0, 2, 1, m, Blocks.COBWEB.getDefaultState(), AIR, false, true);
                }

                int n;
                int o;
                for(n = 0; n < this.length; ++n) {
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
                        this.addChest(world, chunkBox, random, 2, 0, o - 1, JamiesModLootTables.ABANDONED_MINESHAFT_CHEST);
                    }

                    if (random.nextInt(100) == 0) {
                        this.addChest(world, chunkBox, random, 0, 0, o + 1, JamiesModLootTables.ABANDONED_MINESHAFT_CHEST);
                    }

                    if (this.hasCobwebs && !this.hasSpawner) {

                        int q = o - 1 + random.nextInt(3);
                        BlockPos blockPos = this.offsetPos(1, 0, q);
                        if (chunkBox.contains(blockPos) && this.isUnderSeaLevel(world, 1, 0, q, chunkBox)) {
                            this.hasSpawner = true;
                            world.setBlockState(blockPos, Blocks.SPAWNER.getDefaultState(), 2);
                            BlockEntity blockEntity = world.getBlockEntity(blockPos);
                            if (blockEntity instanceof MobSpawnerBlockEntity) {
                                MobSpawnerBlockEntity mobSpawnerBlockEntity = (MobSpawnerBlockEntity)blockEntity;
                                mobSpawnerBlockEntity.setEntityType(EntityType.CAVE_SPIDER, random);
                            }
                        }
                    }
                }

                for(n = 0; n <= 2; ++n) {
                    for(o = 0; o <= m; ++o) {
                        this.tryPlaceFloor(world, chunkBox, blockState, n, -1, o);
                    }
                }


                this.fillSupportBeam(world, chunkBox, 0, -1, 2);
                if (this.length > 1) {
                    o = m - 2;
                    this.fillSupportBeam(world, chunkBox, 0, -1, o);
                }

                if (this.hasRails) {
                    BlockState blockState2 = (BlockState)Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.NORTH_SOUTH);

                    for(int p = 0; p <= m; ++p) {
                        BlockState blockState3 = this.getBlockAt(world, 1, -1, p, chunkBox);
                        if (!blockState3.isAir() && blockState3.isOpaqueFullCube(world, this.offsetPos(1, -1, p))) {
                            float f = this.isUnderSeaLevel(world, 1, 0, p, chunkBox) ? 0.7F : 0.9F;
                            this.addBlockWithRandomThreshold(world, chunkBox, random, f, 1, 0, p, blockState2);
                        }
                    }
                }

            }
        }

        private void fillSupportBeam(StructureWorldAccess world, BlockBox box, int x, int y, int z) {
            BlockState blockState = this.mineshaftType.getLog();
            BlockState blockState2 = this.mineshaftType.getPlanks();
            if (this.getBlockAt(world, x, y, z, box).isOf(blockState2.getBlock())) {
                this.fillSupportBeam(world, blockState, x, y, z, box);
            }

            if (this.getBlockAt(world, x + 2, y, z, box).isOf(blockState2.getBlock())) {
                this.fillSupportBeam(world, blockState, x + 2, y, z, box);
            }

        }

        protected void fillDownwards(StructureWorldAccess world, BlockState state, int x, int y, int z, BlockBox box) {
            BlockPos.Mutable mutable = this.offsetPos(x, y, z);
            if (box.contains(mutable)) {
                int i = mutable.getY();

                while(this.canReplace(world.getBlockState(mutable)) && mutable.getY() > world.getBottomY() + 1) {
                    mutable.move(Direction.DOWN);
                }

                if (this.isUpsideSolidFullSquare(world, mutable, world.getBlockState(mutable))) {
                    while(mutable.getY() < i) {
                        mutable.move(Direction.UP);
                        world.setBlockState(mutable, state, 2);
                    }

                }
            }
        }

        protected void fillSupportBeam(StructureWorldAccess world, BlockState state, int x, int y, int z, BlockBox box) {
            BlockPos.Mutable mutable = this.offsetPos(x, y, z);
            if (box.contains(mutable)) {
                int i = mutable.getY();
                int j = 1;
                boolean bl = true;

                for(boolean bl2 = true; bl || bl2; ++j) {
                    BlockState blockState;
                    boolean bl3;
                    if (bl) {
                        mutable.setY(i - j);
                        blockState = world.getBlockState(mutable);
                        bl3 = this.canReplace(blockState) && !blockState.isOf(Blocks.LAVA);
                        if (!bl3 && this.isUpsideSolidFullSquare(world, mutable, blockState)) {
                            fillColumn(world, state, mutable, i - j + 1, i);
                            return;
                        }

                        bl = j <= 20 && bl3 && mutable.getY() > world.getBottomY() + 1;
                    }

                    if (bl2) {
                        mutable.setY(i + j);
                        blockState = world.getBlockState(mutable);
                        bl3 = this.canReplace(blockState);
                        if (!bl3 && this.sideCoversSmallSquare(world, mutable, blockState)) {
                            world.setBlockState(mutable.setY(i + 1), this.mineshaftType.getFence(), 2);
                            fillColumn(world, Blocks.CHAIN.getDefaultState(), mutable, i + 2, i + j);
                            return;
                        }

                        bl2 = j <= 50 && bl3 && mutable.getY() < world.getTopY() - 1;
                    }
                }

            }
        }

        private static void fillColumn(StructureWorldAccess world, BlockState state, BlockPos.Mutable pos, int startY, int endY) {
            for(int i = startY; i < endY; ++i) {
                world.setBlockState(pos.setY(i), state, 2);
            }

        }

        private boolean isUpsideSolidFullSquare(WorldView world, BlockPos pos, BlockState state) {
            return state.isSideSolidFullSquare(world, pos, Direction.UP);
        }

        private boolean sideCoversSmallSquare(WorldView world, BlockPos pos, BlockState state) {
            return Block.sideCoversSmallSquare(world, pos, Direction.DOWN) && !(state.getBlock() instanceof FallingBlock);
        }

        private void generateSupports(StructureWorldAccess world, BlockBox boundingBox, int minX, int minY, int z, int maxY, int maxX, Random random) {
            if (this.isSolidCeiling(world, boundingBox, minX, maxX, maxY, z)) {
                BlockState blockState = this.mineshaftType.getPlanks();
                BlockState blockState2 = this.mineshaftType.getFence();
                this.fillWithOutline(world, boundingBox, minX, minY, z, minX, maxY - 1, z, (BlockState)blockState2.with(FenceBlock.WEST, true), AIR, false);
                this.fillWithOutline(world, boundingBox, maxX, minY, z, maxX, maxY - 1, z, (BlockState)blockState2.with(FenceBlock.EAST, true), AIR, false);
                if (random.nextInt(4) == 0) {
                    this.fillWithOutline(world, boundingBox, minX, maxY, z, minX, maxY, z, blockState, AIR, false);
                    this.fillWithOutline(world, boundingBox, maxX, maxY, z, maxX, maxY, z, blockState, AIR, false);
                } else {
                    this.fillWithOutline(world, boundingBox, minX, maxY, z, maxX, maxY, z, blockState, AIR, false);
                    this.addBlockWithRandomThreshold(world, boundingBox, random, 0.05F, minX + 1, maxY, z - 1, (BlockState) JamiesModBlocks.GOURD_DANGO_WALL.getDefaultState().with(GourdDangoWallBlock.FACING, Direction.SOUTH));
                    this.addBlockWithRandomThreshold(world, boundingBox, random, 0.05F, minX + 1, maxY, z + 1, (BlockState) JamiesModBlocks.GOURD_DANGO_WALL.getDefaultState().with(GourdDangoWallBlock.FACING, Direction.NORTH));
                }

            }
        }

        private void addCobwebsUnderground(StructureWorldAccess world, BlockBox box, Random random, float threshold, int x, int y, int z) {
            if (this.isUnderSeaLevel(world, x, y, z, box) && random.nextFloat() < threshold && this.hasSolidNeighborBlocks(world, box, x, y, z, 2)) {
                this.addBlock(world, Blocks.COBWEB.getDefaultState(), x, y, z, box);
            }

        }

        private boolean hasSolidNeighborBlocks(StructureWorldAccess world, BlockBox box, int x, int y, int z, int count) {
            BlockPos.Mutable mutable = this.offsetPos(x, y, z);
            int i = 0;
            Direction[] var9 = Direction.values();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                Direction direction = var9[var11];
                mutable.move(direction);
                if (box.contains(mutable) && world.getBlockState(mutable).isSideSolidFullSquare(world, mutable, direction.getOpposite())) {
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

        public BygoneMineshaftPart(StructurePieceType structurePieceType, int chainLength, BygoneMineshaftStructure.Type type, BlockBox box) {
            super(structurePieceType, chainLength, box);
            this.mineshaftType = type;
        }

        public BygoneMineshaftPart(StructurePieceType structurePieceType, NbtCompound nbtCompound) {
            super(structurePieceType, nbtCompound);
            this.mineshaftType = BygoneMineshaftStructure.Type.byId(nbtCompound.getInt("MST"));
        }

        protected boolean canAddBlock(WorldView world, int x, int y, int z, BlockBox box) {
            BlockState blockState = this.getBlockAt(world, x, y, z, box);
            return !blockState.isOf(this.mineshaftType.getPlanks().getBlock()) && !blockState.isOf(this.mineshaftType.getLog().getBlock()) && !blockState.isOf(this.mineshaftType.getFence().getBlock()) && !blockState.isOf(Blocks.CHAIN);
        }

        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            nbt.putInt("MST", this.mineshaftType.ordinal());
        }

        protected boolean isSolidCeiling(BlockView world, BlockBox boundingBox, int minX, int maxX, int y, int z) {
            for(int i = minX; i <= maxX; ++i) {
                if (this.getBlockAt(world, i, y + 1, z, boundingBox).isAir()) {
                    return false;
                }
            }

            return true;
        }

        protected boolean cannotGenerate(WorldAccess world, BlockBox box) {
            int i = Math.max(this.boundingBox.getMinX() - 1, box.getMinX());
            int j = Math.max(this.boundingBox.getMinY() - 1, box.getMinY());
            int k = Math.max(this.boundingBox.getMinZ() - 1, box.getMinZ());
            int l = Math.min(this.boundingBox.getMaxX() + 1, box.getMaxX());
            int m = Math.min(this.boundingBox.getMaxY() + 1, box.getMaxY());
            int n = Math.min(this.boundingBox.getMaxZ() + 1, box.getMaxZ());
            BlockPos.Mutable mutable = new BlockPos.Mutable((i + l) / 2, (j + m) / 2, (k + n) / 2);
            if (world.getBiome(mutable).isIn(BiomeTags.MINESHAFT_BLOCKING)) {
                return true;
            } else {
                int o;
                int p;
                for(o = i; o <= l; ++o) {
                    for(p = k; p <= n; ++p) {
                        if (world.getBlockState(mutable.set(o, j, p)).isLiquid()) {
                            return true;
                        }

                        if (world.getBlockState(mutable.set(o, m, p)).isLiquid()) {
                            return true;
                        }
                    }
                }

                for(o = i; o <= l; ++o) {
                    for(p = j; p <= m; ++p) {
                        if (world.getBlockState(mutable.set(o, p, k)).isLiquid()) {
                            return true;
                        }

                        if (world.getBlockState(mutable.set(o, p, n)).isLiquid()) {
                            return true;
                        }
                    }
                }

                for(o = k; o <= n; ++o) {
                    for(p = j; p <= m; ++p) {
                        if (world.getBlockState(mutable.set(i, p, o)).isLiquid()) {
                            return true;
                        }

                        if (world.getBlockState(mutable.set(l, p, o)).isLiquid()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected void tryPlaceFloor(StructureWorldAccess world, BlockBox box, BlockState state, int x, int y, int z) {
            if (this.isUnderSeaLevel(world, x, y, z, box)) {
                BlockPos blockPos = this.offsetPos(x, y, z);
                BlockState blockState = world.getBlockState(blockPos);
                if (!blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
                    world.setBlockState(blockPos, state, 2);
                }

            }
        }
    }

    public static class BygoneMineshaftRoom extends BygoneMineshaftGenerator.BygoneMineshaftPart {
        private final List<BlockBox> entrances = Lists.newLinkedList();

        public BygoneMineshaftRoom(int chainLength, Random random, int x, int z, BygoneMineshaftStructure.Type type) {
            super(JamiesModStructures.BYGONE_MINESHAFT_ROOM, chainLength, type, new BlockBox(x, 100, z, x + 7 + random.nextInt(6), 200, z + 7 + random.nextInt(6)));
            this.mineshaftType = type;
        }

        public BygoneMineshaftRoom(StructureContext context, NbtCompound nbt) {
            super(JamiesModStructures.BYGONE_MINESHAFT_ROOM, nbt);
            DataResult<List<BlockBox>> var10000 = BlockBox.CODEC.listOf().parse(NbtOps.INSTANCE, nbt.getList("Entrances", 11));
            Logger var10001 = BygoneMineshaftGenerator.LOGGER;
            Objects.requireNonNull(var10001);
            Optional<List<BlockBox>> var2 = var10000.resultOrPartial(var10001::error);
            List<BlockBox> var3 = this.entrances;
            Objects.requireNonNull(var3);
            var2.ifPresent(var3::addAll);
        }

        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            int i = this.getChainLength();
            int j = this.boundingBox.getBlockCountY() - 3 - 1;
            if (j <= 0) {
                j = 1;
            }

            int k;
            BygoneMineshaftGenerator.BygoneMineshaftPart structurePiece;
            BlockBox blockBox;
            for(k = 0; k < this.boundingBox.getBlockCountX(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountX());
                if (k + 3 > this.boundingBox.getBlockCountX()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + k, this.boundingBox.getMinY() + random.nextInt(j) + 1, this.boundingBox.getMinZ() - 1, Direction.NORTH, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BlockBox(blockBox.getMinX(), blockBox.getMinY(), this.boundingBox.getMinZ(), blockBox.getMaxX(), blockBox.getMaxY(), this.boundingBox.getMinZ() + 1));
                }
            }

            for(k = 0; k < this.boundingBox.getBlockCountX(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountX());
                if (k + 3 > this.boundingBox.getBlockCountX()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + k, this.boundingBox.getMinY() + random.nextInt(j) + 1, this.boundingBox.getMaxZ() + 1, Direction.SOUTH, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BlockBox(blockBox.getMinX(), blockBox.getMinY(), this.boundingBox.getMaxZ() - 1, blockBox.getMaxX(), blockBox.getMaxY(), this.boundingBox.getMaxZ()));
                }
            }

            for(k = 0; k < this.boundingBox.getBlockCountZ(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountZ());
                if (k + 3 > this.boundingBox.getBlockCountZ()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY() + random.nextInt(j) + 1, this.boundingBox.getMinZ() + k, Direction.WEST, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BlockBox(this.boundingBox.getMinX(), blockBox.getMinY(), blockBox.getMinZ(), this.boundingBox.getMinX() + 1, blockBox.getMaxY(), blockBox.getMaxZ()));
                }
            }

            for(k = 0; k < this.boundingBox.getBlockCountZ(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountZ());
                if (k + 3 > this.boundingBox.getBlockCountZ()) {
                    break;
                }

                structurePiece = BygoneMineshaftGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY() + random.nextInt(j) + 1, this.boundingBox.getMinZ() + k, Direction.EAST, i);
                if (structurePiece != null) {
                    blockBox = structurePiece.getBoundingBox();
                    this.entrances.add(new BlockBox(this.boundingBox.getMaxX() - 1, blockBox.getMinY(), blockBox.getMinZ(), this.boundingBox.getMaxX(), blockBox.getMaxY(), blockBox.getMaxZ()));
                }
            }

        }

        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            if (!this.cannotGenerate(world, chunkBox)) {
                this.fillWithOutline(world, chunkBox, this.boundingBox.getMinX(), this.boundingBox.getMinY() + 1, this.boundingBox.getMinZ(), this.boundingBox.getMaxX(), Math.min(this.boundingBox.getMinY() + 3, this.boundingBox.getMaxY()), this.boundingBox.getMaxZ(), AIR, AIR, false);
                Iterator var8 = this.entrances.iterator();

                while(var8.hasNext()) {
                    BlockBox blockBox = (BlockBox)var8.next();
                    this.fillWithOutline(world, chunkBox, blockBox.getMinX(), blockBox.getMaxY() - 2, blockBox.getMinZ(), blockBox.getMaxX(), blockBox.getMaxY(), blockBox.getMaxZ(), AIR, AIR, false);
                }

                this.fillHalfEllipsoid(world, chunkBox, this.boundingBox.getMinX(), this.boundingBox.getMinY() + 4, this.boundingBox.getMinZ(), this.boundingBox.getMaxX(), this.boundingBox.getMaxY(), this.boundingBox.getMaxZ(), AIR, false);
            }
        }

        public void translate(int x, int y, int z) {
            super.translate(x, y, z);
            Iterator var4 = this.entrances.iterator();

            while(var4.hasNext()) {
                BlockBox blockBox = (BlockBox)var4.next();
                blockBox.move(x, y, z);
            }

        }

        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            DataResult<NbtElement> var10000 = BlockBox.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.entrances);
            Logger var10001 = BygoneMineshaftGenerator.LOGGER;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> {
                nbt.put("Entrances", (NbtElement) nbtElement);
            });
        }
    }
}
