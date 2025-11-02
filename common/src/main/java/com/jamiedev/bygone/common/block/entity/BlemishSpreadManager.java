package com.jamiedev.bygone.common.block.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jamiedev.bygone.common.block.BlemishVeinBlock;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Stream;

public class BlemishSpreadManager {
    public static final int field_37609 = 24;
    public static final int MAX_CHARGE = 1000;
    public static final float field_37611 = 0.5F;
    public static final int field_37612 = 11;
    private static final int MAX_CURSORS = 32;
    private static final Logger LOGGER = LogUtils.getLogger();
    final boolean worldGen;
    private final TagKey<Block> replaceableTag;
    private final int extraBlockChance;
    private final int maxDistance;
    private final int spreadChance;
    private final int decayChance;
    private List<BlemishSpreadManager.Cursor> cursors = new ArrayList<>();

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

    public void readNbt(CompoundTag nbt) {
        if (nbt.contains("cursors", 9)) {
            this.cursors.clear();
            DataResult<List> var10000 = Cursor.CODEC.listOf().parse(new Dynamic(NbtOps.INSTANCE, nbt.getList("cursors", 10)));
            Logger var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            List list = var10000.resultOrPartial(var10001::error).orElseGet(ArrayList::new);
            int i = Math.min(list.size(), 32);

            for (int j = 0; j < i; ++j) {
                this.addCursor((BlemishSpreadManager.Cursor) list.get(j));
            }
        }

    }

    public void writeNbt(CompoundTag nbt) {
        DataResult<Tag> var10000 = BlemishSpreadManager.Cursor.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.cursors);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((cursorsNbt) -> {
            nbt.put("cursors", cursorsNbt);
        });
    }

    public void spread(BlockPos pos, int charge) {
        while (charge > 0) {
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

    public void tick(LevelAccessor world, BlockPos pos, @NotNull RandomSource random, boolean shouldConvertToBlock) {
        if (!this.cursors.isEmpty()) {
            List<BlemishSpreadManager.Cursor> list = new ArrayList<>();
            Map<BlockPos, BlemishSpreadManager.Cursor> map = new HashMap<>();
            Object2IntMap<BlockPos> object2IntMap = new Object2IntOpenHashMap<>();
            Iterator<Cursor> var8 = this.cursors.iterator();

            while (true) {
                BlockPos blockPos;
                while (var8.hasNext()) {
                    BlemishSpreadManager.Cursor cursor = var8.next();
                    cursor.spread(world, pos, random, this, shouldConvertToBlock);
                    if (cursor.charge <= 0) {
                        world.levelEvent(6006, cursor.getPos(), 0);
                    } else {
                        blockPos = cursor.getPos();
                        object2IntMap.computeInt(blockPos, (posx, charge) -> {
                            return (charge == null ? 0 : charge) + cursor.charge;
                        });
                        BlemishSpreadManager.Cursor cursor2 = map.get(blockPos);
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

                ObjectIterator<Object2IntMap.Entry<BlockPos>> var16 = object2IntMap.object2IntEntrySet().iterator();

                while (var16.hasNext()) {
                    Object2IntMap.Entry<BlockPos> entry = var16.next();
                    blockPos = entry.getKey();
                    int i = entry.getIntValue();
                    BlemishSpreadManager.Cursor cursor3 = map.get(blockPos);
                    Collection<Direction> collection = cursor3 == null ? null : cursor3.getFaces();
                    if (i > 0 && collection != null) {
                        int j = (int) (Math.log1p(i) / 2.299999952316284) + 1;
                        int k = (j << 6) + MultifaceBlock.pack(collection);
                        world.levelEvent(6006, blockPos, k);
                    }
                }

                this.cursors = list;
                return;
            }
        }
    }

    public static class Cursor {
        public static final int field_37622 = 1;
        public static final Codec<BlemishSpreadManager.Cursor> CODEC;
        private static final ObjectArrayList OFFSETS = Util.make(new ObjectArrayList(18), (list) -> {
            Stream<BlockPos> var10000 = BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter((pos) -> {
                return (pos.getX() == 0 || pos.getY() == 0 || pos.getZ() == 0) && !pos.equals(BlockPos.ZERO);
            }).map(BlockPos::immutable);
            Objects.requireNonNull(list);
            var10000.forEach(list::add);
        });
        private static final Codec<Set<Direction>> DIRECTION_SET_CODEC;

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

        int charge;
        private BlockPos pos;
        private int update;
        private int decay;
        @Nullable
        private Set<Direction> faces;

        private Cursor(BlockPos pos, int charge, int decay, int update, Optional<Set<Direction>> faces) {
            this.pos = pos;
            this.charge = charge;
            this.decay = decay;
            this.update = update;
            this.faces = faces.orElse(null);
        }

        public Cursor(BlockPos pos, int charge) {
            this(pos, charge, 1, 0, Optional.empty());
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

        private static List<Vec3i> shuffleOffsets(RandomSource random) {
            return Util.shuffledCopy(OFFSETS, random);
        }

        @Nullable
        private static BlockPos getSpreadPos(LevelAccessor world, BlockPos pos, @NotNull RandomSource random) {
            BlockPos.MutableBlockPos mutable = pos.mutable();
            BlockPos.MutableBlockPos mutable2 = pos.mutable();
            Iterator<Vec3i> var5 = shuffleOffsets(random).iterator();

            while (var5.hasNext()) {
                Vec3i vec3i = var5.next();
                mutable2.setWithOffset(pos, vec3i);
                BlockState blockState = world.getBlockState(mutable2);
                if (blockState.getBlock() instanceof BlemishSpreadable && canSpread(world, pos, mutable2)) {
                    mutable.set(mutable2);
                    if (BlemishVeinBlock.veinCoversBlemishReplaceable(world, blockState, mutable2)) {
                        break;
                    }
                }
            }

            return mutable.equals(pos) ? null : mutable;
        }

        private static boolean canSpread(LevelAccessor world, BlockPos sourcePos, BlockPos targetPos) {
            if (sourcePos.distManhattan(targetPos) == 1) {
                return true;
            } else {
                BlockPos blockPos = targetPos.subtract(sourcePos);
                Direction direction = Direction.fromAxisAndDirection(Direction.Axis.X, blockPos.getX() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                Direction direction2 = Direction.fromAxisAndDirection(Direction.Axis.Y, blockPos.getY() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                Direction direction3 = Direction.fromAxisAndDirection(Direction.Axis.Z, blockPos.getZ() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                if (blockPos.getX() == 0) {
                    return canSpread(world, sourcePos, direction2) || canSpread(world, sourcePos, direction3);
                } else if (blockPos.getY() == 0) {
                    return canSpread(world, sourcePos, direction) || canSpread(world, sourcePos, direction3);
                } else {
                    return canSpread(world, sourcePos, direction) || canSpread(world, sourcePos, direction2);
                }
            }
        }

        private static boolean canSpread(LevelAccessor world, BlockPos pos, Direction direction) {
            BlockPos blockPos = pos.relative(direction);
            return !world.getBlockState(blockPos).isFaceSturdy(world, blockPos, direction.getOpposite());
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

        private boolean canSpread(LevelAccessor world, BlockPos pos, boolean worldGen) {
            if (this.charge <= 0) {
                return false;
            } else if (worldGen) {
                return true;
            } else if (world instanceof ServerLevel serverWorld) {
                return serverWorld.shouldTickBlocksAt(pos);
            } else {
                return false;
            }
        }

        public void spread(LevelAccessor world, BlockPos pos, @NotNull RandomSource random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
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

                        world.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }

                    this.charge = BlemishSpreadable.spread(this, world, pos, random, spreadManager, shouldConvertToBlock);
                    if (this.charge <= 0) {
                        BlemishSpreadable.spreadAtSamePosition(world, blockState, this.pos, random);
                    } else {
                        BlockPos blockPos = getSpreadPos(world, this.pos, random);
                        if (blockPos != null) {
                            BlemishSpreadable.spreadAtSamePosition(world, blockState, this.pos, random);
                            this.pos = blockPos.immutable();
                            if (spreadManager.isWorldGen() && !this.pos.closerThan(new Vec3i(pos.getX(), this.pos.getY(), pos.getZ()), 15.0)) {
                                this.charge = 0;
                                return;
                            }

                            blockState = world.getBlockState(blockPos);
                        }

                        if (blockState.getBlock() instanceof BlemishSpreadable) {
                            this.faces = MultifaceBlock.availableFaces(blockState);
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
    }
}
