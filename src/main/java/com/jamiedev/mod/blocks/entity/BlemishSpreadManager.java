package com.jamiedev.mod.blocks.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jamiedev.mod.blocks.BlemishVeinBlock;
import com.jamiedev.mod.init.JamiesModTag;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Stream;

public class BlemishSpreadManager {
    public static final int field_37609 = 24;
    public static final int MAX_CHARGE = 1000;
    public static final float field_37611 = 0.5F;
    private static final int MAX_CURSORS = 32;
    public static final int field_37612 = 11;
    final boolean worldGen;
    private final TagKey<Block> replaceableTag;
    private final int extraBlockChance;
    private final int maxDistance;
    private final int spreadChance;
    private final int decayChance;
    private List<BlemishSpreadManager.Cursor> cursors = new ArrayList();
    private static final Logger LOGGER = LogUtils.getLogger();

    public BlemishSpreadManager(boolean worldGen, TagKey<Block> replaceableTag, int extraBlockChance, int maxDistance, int spreadChance, int decayChance) {
        this.worldGen = worldGen;
        this.replaceableTag = replaceableTag;
        this.extraBlockChance = extraBlockChance;
        this.maxDistance = maxDistance;
        this.spreadChance = spreadChance;
        this.decayChance = decayChance;
    }

    public static BlemishSpreadManager create() {
        return new BlemishSpreadManager(false, JamiesModTag.BLEMISH_REPLACEABLE, 10, 4, 10, 5);
    }

    public static BlemishSpreadManager createWorldGen() {
        return new BlemishSpreadManager(true, JamiesModTag.BLEMISH_REPLACEABLE_WORLD_GEN, 50, 1, 5, 10);
    }

    public TagKey<Block> getReplaceableTag() {
        return this.replaceableTag;
    }

    public int getExtraBlockChance() {
        return this.extraBlockChance;
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public int getSpreadChance() {
        return this.spreadChance;
    }

    public int getDecayChance() {
        return this.decayChance;
    }

    public boolean isWorldGen() {
        return this.worldGen;
    }

    @VisibleForTesting
    public List<BlemishSpreadManager.Cursor> getCursors() {
        return this.cursors;
    }

    public void clearCursors() {
        this.cursors.clear();
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("cursors", 9)) {
            this.cursors.clear();
            DataResult<List> var10000 = Cursor.CODEC.listOf().parse(new Dynamic(NbtOps.INSTANCE, nbt.getList("cursors", 10)));
            Logger var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            List list = (List)var10000.resultOrPartial(var10001::error).orElseGet(ArrayList::new);
            int i = Math.min(list.size(), 32);

            for(int j = 0; j < i; ++j) {
                this.addCursor((BlemishSpreadManager.Cursor)list.get(j));
            }
        }

    }

    public void writeNbt(NbtCompound nbt) {
        DataResult<NbtElement> var10000 = BlemishSpreadManager.Cursor.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.cursors);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((cursorsNbt) -> {
            nbt.put("cursors", (NbtElement) cursorsNbt);
        });
    }

    public void spread(BlockPos pos, int charge) {
        while(charge > 0) {
            int i = Math.min(charge, 1000);
            this.addCursor(new BlemishSpreadManager.Cursor(pos, i));
            charge -= i;
        }

    }

    private void addCursor(BlemishSpreadManager.Cursor cursor) {
        if (this.cursors.size() < 32) {
            this.cursors.add(cursor);
        }
    }

    public void tick(WorldAccess world, BlockPos pos, Random random, boolean shouldConvertToBlock) {
        if (!this.cursors.isEmpty()) {
            List<BlemishSpreadManager.Cursor> list = new ArrayList();
            Map<BlockPos, BlemishSpreadManager.Cursor> map = new HashMap();
            Object2IntMap<BlockPos> object2IntMap = new Object2IntOpenHashMap();
            Iterator var8 = this.cursors.iterator();

            while(true) {
                BlockPos blockPos;
                while(var8.hasNext()) {
                    BlemishSpreadManager.Cursor cursor = (BlemishSpreadManager.Cursor)var8.next();
                    cursor.spread(world, pos, random, this, shouldConvertToBlock);
                    if (cursor.charge <= 0) {
                        world.syncWorldEvent(3006, cursor.getPos(), 0);
                    } else {
                        blockPos = cursor.getPos();
                        object2IntMap.computeInt(blockPos, (posx, charge) -> {
                            return (charge == null ? 0 : charge) + cursor.charge;
                        });
                        BlemishSpreadManager.Cursor cursor2 = (BlemishSpreadManager.Cursor)map.get(blockPos);
                        if (cursor2 == null) {
                            map.put(blockPos, cursor);
                            list.add(cursor);
                        } else if (!this.isWorldGen() && cursor.charge + cursor2.charge <= 1000) {
                            cursor2.merge(cursor);
                        } else {
                            list.add(cursor);
                            if (cursor.charge < cursor2.charge) {
                                map.put(blockPos, cursor);
                            }
                        }
                    }
                }

                ObjectIterator var16 = object2IntMap.object2IntEntrySet().iterator();

                while(var16.hasNext()) {
                    Object2IntMap.Entry<BlockPos> entry = (Object2IntMap.Entry)var16.next();
                    blockPos = (BlockPos)entry.getKey();
                    int i = entry.getIntValue();
                    BlemishSpreadManager.Cursor cursor3 = (BlemishSpreadManager.Cursor)map.get(blockPos);
                    Collection<Direction> collection = cursor3 == null ? null : cursor3.getFaces();
                    if (i > 0 && collection != null) {
                        int j = (int)(Math.log1p((double)i) / 2.299999952316284) + 1;
                        int k = (j << 6) + MultifaceGrowthBlock.directionsToFlag(collection);
                        world.syncWorldEvent(3006, blockPos, k);
                    }
                }

                this.cursors = list;
                return;
            }
        }
    }

    public static class Cursor {
        private static final ObjectArrayList<Vec3i> OFFSETS = (ObjectArrayList) Util.make(new ObjectArrayList(18), (list) -> {
            Stream var10000 = BlockPos.stream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter((pos) -> {
                return (pos.getX() == 0 || pos.getY() == 0 || pos.getZ() == 0) && !pos.equals(BlockPos.ORIGIN);
            }).map(BlockPos::toImmutable);
            Objects.requireNonNull(list);
            var10000.forEach(list::add);
        });
        public static final int field_37622 = 1;
        private BlockPos pos;
        int charge;
        private int update;
        private int decay;
        @Nullable
        private Set<Direction> faces;
        private static final Codec<Set<Direction>> DIRECTION_SET_CODEC;
        public static final Codec<BlemishSpreadManager.Cursor> CODEC;

        private Cursor(BlockPos pos, int charge, int decay, int update, Optional<Set<Direction>> faces) {
            this.pos = pos;
            this.charge = charge;
            this.decay = decay;
            this.update = update;
            this.faces = (Set)faces.orElse((Set<Direction>) null);
        }

        public Cursor(BlockPos pos, int charge) {
            this(pos, charge, 1, 0, Optional.empty());
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public int getCharge() {
            return this.charge;
        }

        public int getDecay() {
            return this.decay;
        }

        @Nullable
        public Set<Direction> getFaces() {
            return this.faces;
        }

        private boolean canSpread(WorldAccess world, BlockPos pos, boolean worldGen) {
            if (this.charge <= 0) {
                return false;
            } else if (worldGen) {
                return true;
            } else if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)world;
                return serverWorld.shouldTickBlockPos(pos);
            } else {
                return false;
            }
        }

        public void spread(WorldAccess world, BlockPos pos, Random random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
            if (this.canSpread(world, pos, spreadManager.worldGen)) {
                if (this.update > 0) {
                    --this.update;
                } else {
                    BlockState blockState = world.getBlockState(this.pos);
                    BlemishSpreadable BlemishSpreadable = getSpreadable(blockState);
                    if (shouldConvertToBlock && BlemishSpreadable.spread(world, this.pos, blockState, this.faces, spreadManager.isWorldGen())) {
                        if (BlemishSpreadable.shouldConvertToSpreadable()) {
                            blockState = world.getBlockState(this.pos);
                            BlemishSpreadable = getSpreadable(blockState);
                        }

                        world.playSound((PlayerEntity)null, this.pos, SoundEvents.BLOCK_SCULK_SPREAD, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }

                    this.charge = BlemishSpreadable.spread(this, world, pos, random, spreadManager, shouldConvertToBlock);
                    if (this.charge <= 0) {
                        BlemishSpreadable.spreadAtSamePosition(world, blockState, this.pos, random);
                    } else {
                        BlockPos blockPos = getSpreadPos(world, this.pos, random);
                        if (blockPos != null) {
                            BlemishSpreadable.spreadAtSamePosition(world, blockState, this.pos, random);
                            this.pos = blockPos.toImmutable();
                            if (spreadManager.isWorldGen() && !this.pos.isWithinDistance(new Vec3i(pos.getX(), this.pos.getY(), pos.getZ()), 15.0)) {
                                this.charge = 0;
                                return;
                            }

                            blockState = world.getBlockState(blockPos);
                        }

                        if (blockState.getBlock() instanceof BlemishSpreadable) {
                            this.faces = MultifaceGrowthBlock.collectDirections(blockState);
                        }

                        this.decay = BlemishSpreadable.getDecay(this.decay);
                        this.update = BlemishSpreadable.getUpdate();
                    }
                }
            }
        }

        void merge(BlemishSpreadManager.Cursor cursor) {
            this.charge += cursor.charge;
            cursor.charge = 0;
            this.update = Math.min(this.update, cursor.update);
        }

        private static BlemishSpreadable getSpreadable(BlockState state) {
            Block var2 = state.getBlock();
            BlemishSpreadable var10000;
            if (var2 instanceof BlemishSpreadable BlemishSpreadable) {
                var10000 = BlemishSpreadable;
            } else {
                var10000 = BlemishSpreadable.VEIN_ONLY_SPREADER;
            }

            return var10000;
        }

        private static List<Vec3i> shuffleOffsets(Random random) {
            return Util.copyShuffled(OFFSETS, random);
        }

        @Nullable
        private static BlockPos getSpreadPos(WorldAccess world, BlockPos pos, Random random) {
            BlockPos.Mutable mutable = pos.mutableCopy();
            BlockPos.Mutable mutable2 = pos.mutableCopy();
            Iterator var5 = shuffleOffsets(random).iterator();

            while(var5.hasNext()) {
                Vec3i vec3i = (Vec3i)var5.next();
                mutable2.set(pos, vec3i);
                BlockState blockState = world.getBlockState(mutable2);
                if (blockState.getBlock() instanceof BlemishSpreadable && canSpread(world, pos, (BlockPos)mutable2)) {
                    mutable.set(mutable2);
                    if (BlemishVeinBlock.veinCoversBlemishReplaceable(world, blockState, mutable2)) {
                        break;
                    }
                }
            }

            return mutable.equals(pos) ? null : mutable;
        }

        private static boolean canSpread(WorldAccess world, BlockPos sourcePos, BlockPos targetPos) {
            if (sourcePos.getManhattanDistance(targetPos) == 1) {
                return true;
            } else {
                BlockPos blockPos = targetPos.subtract(sourcePos);
                Direction direction = Direction.from(Direction.Axis.X, blockPos.getX() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                Direction direction2 = Direction.from(Direction.Axis.Y, blockPos.getY() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                Direction direction3 = Direction.from(Direction.Axis.Z, blockPos.getZ() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                if (blockPos.getX() == 0) {
                    return canSpread(world, sourcePos, direction2) || canSpread(world, sourcePos, direction3);
                } else if (blockPos.getY() == 0) {
                    return canSpread(world, sourcePos, direction) || canSpread(world, sourcePos, direction3);
                } else {
                    return canSpread(world, sourcePos, direction) || canSpread(world, sourcePos, direction2);
                }
            }
        }

        private static boolean canSpread(WorldAccess world, BlockPos pos, Direction direction) {
            BlockPos blockPos = pos.offset(direction);
            return !world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction.getOpposite());
        }

        static {
            DIRECTION_SET_CODEC = Direction.CODEC.listOf().xmap((directions) -> {
                return Sets.newEnumSet(directions, Direction.class);
            }, Lists::newArrayList);
            CODEC = RecordCodecBuilder.create((instance) -> {
                return instance.group(BlockPos.CODEC.fieldOf("pos").forGetter(BlemishSpreadManager.Cursor::getPos), Codec.intRange(0, 1000).fieldOf("charge").orElse(0).forGetter(BlemishSpreadManager.Cursor::getCharge), Codec.intRange(0, 1).fieldOf("decay_delay").orElse(1).forGetter(BlemishSpreadManager.Cursor::getDecay), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("update_delay").orElse(0).forGetter((cursor) -> {
                    return cursor.update;
                }), DIRECTION_SET_CODEC.lenientOptionalFieldOf("facings").forGetter((cursor) -> {
                    return Optional.ofNullable(cursor.getFaces());
                })).apply(instance, BlemishSpreadManager.Cursor::new);
            });
        }
    }
}
