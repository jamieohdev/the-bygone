package com.jamiedev.bygone.common.block.entity;

import com.google.common.collect.Lists;
import com.jamiedev.bygone.common.block.CopperbugNestBlock;
import com.jamiedev.bygone.common.entity.CopperbugEntity;
import com.jamiedev.bygone.core.registry.BGBlockEntities;
import com.jamiedev.bygone.core.registry.BGDataComponentTypes;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CopperbugNestBlockEntity  extends BlockEntity
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String FLOWER_POS_KEY = "flower_pos";
    private static final String COPPERBUGS_KEY = "copperbugs";
    static final List<String> IRRELEVANT_COPPERBUG_NBT_KEYS = Arrays.asList("Air", "ArmorDropChances", "ArmorItems", "Brain", "CanPickUpLoot", "DeathTime", "FallDistance", "FallFlying", "Fire", "HandDropChances", "HandItems", "HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation", "SleepingX", "SleepingY", "SleepingZ", "CannotEnterNestTicks", "TicksSincePollination", "CropsGrownSincePollination", "Nest_pos", "Passengers", "leash", "UUID");
    public static final int MAX_COPPERBUG_COUNT = 3;
    private static final int ANGERED_CANNOT_ENTER_NEST_TICKS = 400;
    private static final int MIN_OCCUPATION_TICKS_WITH_NECTAR = 2400;
    public static final int MIN_OCCUPATION_TICKS_WITHOUT_NECTAR = 600;
    private final List<CopperbugNestBlockEntity.Copperbug> copperbugs = Lists.newArrayList();
    @Nullable
    private BlockPos flowerPos;

    public CopperbugNestBlockEntity(BlockPos pos, BlockState state) {
        super(BGBlockEntities.COPPERBUGNEST.get(), pos, state);
    }

    @Override
    public void setChanged() {
        if (this.isNearFire()) {
            this.angerCopperbugs(null, this.level.getBlockState(this.getBlockPos()), CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
        }

        super.setChanged();
    }

    public boolean isNearFire() {
        if (this.level == null) {
            return false;
        } else {
            Iterator<BlockPos> var1 = BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(1, 1, 1)).iterator();

            BlockPos blockPos;
            do {
                if (!var1.hasNext()) {
                    return false;
                }

                blockPos = (BlockPos)var1.next();
            } while(!(this.level.getBlockState(blockPos).getBlock() instanceof FireBlock));

            return true;
        }
    }

    public boolean hasNoCopperbugs() {
        return this.copperbugs.isEmpty();
    }

    public boolean isFullOfCopperbugs() {
        return this.copperbugs.size() == 3;
    }

    public void angerCopperbugs(@Nullable Player player, BlockState state, CopperbugNestBlockEntity.CopperbugState beeState) {
        List<Entity> list = this.tryReleaseCopperbug(state, beeState);
        if (player != null) {
            Iterator<Entity> var5 = list.iterator();

            while(var5.hasNext()) {
                Entity entity = (Entity)var5.next();
                if (entity instanceof CopperbugEntity beeEntity) {
                    if (player.position().distanceToSqr(entity.position()) <= 16.0) {
                        if (!this.isSmoked()) {
                            beeEntity.setTarget(player);
                        } else {
                            beeEntity.setCannotEnterNestTicks(400);
                        }
                    }
                }
            }
        }

    }

    private List<Entity> tryReleaseCopperbug(BlockState state, CopperbugNestBlockEntity.CopperbugState beeState) {
        List<Entity> list = Lists.newArrayList();
        this.copperbugs.removeIf((bee) -> {
            return releaseCopperbug(this.level, this.worldPosition, state, bee.createData(), list, beeState, this.flowerPos);
        });
        if (!list.isEmpty()) {
            super.setChanged();
        }

        return list;
    }

    @VisibleForDebug
    public int getCopperbugCount() {
        return this.copperbugs.size();
    }

    public static int getOxidizationLevel(BlockState state) {
        return state.getValue(CopperbugNestBlock.OXIDIZATION_LEVEL);
    }

    @VisibleForDebug
    public boolean isSmoked() {
        return CampfireBlock.isSmokeyPos(this.level, this.getBlockPos());
    }

    public void tryEnterNest(Entity entity) {
        if (this.copperbugs.size() < 15) {
            entity.stopRiding();
            entity.ejectPassengers();
            this.addCopperbug(CopperbugNestBlockEntity.CopperbugData.of(entity));
            if (this.level != null) {
                if (entity instanceof CopperbugEntity beeEntity) {
                    if (beeEntity.hasCopperBlock() && (!this.hasCopperBlockPos() || this.level.random.nextBoolean())) {
                        this.flowerPos = beeEntity.getCopperBlockPos();
                    }
                }

                BlockPos blockPos = this.getBlockPos();
                this.level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.COPPER_BULB_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, this.getBlockState()));
            }

            entity.discard();
            super.setChanged();
        }
    }

    public void addCopperbug(CopperbugNestBlockEntity.CopperbugData bee) {
        this.copperbugs.add(new CopperbugNestBlockEntity.Copperbug(bee));
    }

    private static boolean releaseCopperbug(Level world, BlockPos pos, BlockState state, CopperbugNestBlockEntity.CopperbugData bee, @Nullable List<Entity> entities, CopperbugNestBlockEntity.CopperbugState beeState, @Nullable BlockPos flowerPos) {
        if ((world.isNight() || world.isRaining()) && beeState != CopperbugNestBlockEntity.CopperbugState.EMERGENCY) {
            return false;
        } else {
            Direction direction = state.getValue(CopperbugNestBlock.FACING);
            BlockPos blockPos = pos.relative(direction);
            boolean bl = !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
            if (bl && beeState != CopperbugNestBlockEntity.CopperbugState.EMERGENCY) {
                return false;
            } else {
                Entity entity = bee.loadEntity(world, pos);
                if (entity != null) {
                    if (entity instanceof CopperbugEntity beeEntity) {
                        if (flowerPos != null && !beeEntity.hasCopperBlock() && world.random.nextFloat() < 0.9F) {
                            beeEntity.setCopperBlockPos(flowerPos);
                        }

                        if (beeState == CopperbugNestBlockEntity.CopperbugState.HONEY_DELIVERED) {
                            beeEntity.onOxidizationDelivered();
                            if (state.is(JamiesModTag.COPPERBUGNESTS, (statex) -> {
                                return statex.hasProperty(CopperbugNestBlock.OXIDIZATION_LEVEL);
                            })) {
                                int i = getOxidizationLevel(state);
                                if (i < 5) {
                                    int j = world.random.nextInt(100) == 0 ? 2 : 1;
                                    if (i + j > 5) {
                                        --j;
                                    }

                                    world.setBlockAndUpdate(pos, state.setValue(CopperbugNestBlock.OXIDIZATION_LEVEL, i + j));
                                }
                            }
                        }

                        if (entities != null) {
                            entities.add(beeEntity);
                        }

                        float f = entity.getBbWidth();
                        double d = bl ? 0.0 : 0.55 + (double)(f / 2.0F);
                        double e = (double)pos.getX() + 0.5 + d * (double)direction.getStepX();
                        double g = (double)pos.getY() + 0.5 - (double)(entity.getBbHeight() / 2.0F);
                        double h = (double)pos.getZ() + 0.5 + d * (double)direction.getStepZ();
                        entity.moveTo(e, g, h, entity.getYRot(), entity.getXRot());
                    }

                    world.playSound(null, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, world.getBlockState(pos)));
                    return world.addFreshEntity(entity);
                } else {
                    return false;
                }
            }
        }
    }

    private boolean hasCopperBlockPos() {
        return this.flowerPos != null;
    }

    private static void tickCopperbugs(Level world, BlockPos pos, BlockState state, List<CopperbugNestBlockEntity.Copperbug> copperbugs, @Nullable BlockPos flowerPos) {
        boolean bl = false;
        Iterator<CopperbugNestBlockEntity.Copperbug> iterator = copperbugs.iterator();

        while(iterator.hasNext()) {
            CopperbugNestBlockEntity.Copperbug bee = iterator.next();
            if (bee.canExitNest()) {
                CopperbugNestBlockEntity.CopperbugState beeState = bee.hasNectar() ? CopperbugNestBlockEntity.CopperbugState.HONEY_DELIVERED : CopperbugNestBlockEntity.CopperbugState.COPPERBUG_RELEASED;
                if (releaseCopperbug(world, pos, state, bee.createData(), null, beeState, flowerPos)) {
                    bl = true;
                    iterator.remove();
                }
            }
        }

        if (bl) {
            setChanged(world, pos, state);
        }

    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, CopperbugNestBlockEntity blockEntity) {
        tickCopperbugs(world, pos, state, blockEntity.copperbugs, blockEntity.flowerPos);
        if (!blockEntity.copperbugs.isEmpty() && world.getRandom().nextDouble() < 0.005) {
            double d = (double)pos.getX() + 0.5;
            double e = pos.getY();
            double f = (double)pos.getZ() + 0.5;
            world.playSound(null, d, e, f, SoundEvents.COPPER_GRATE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

      //  DebugInfoSender.sendBeehiveDebugData(world, pos, state, blockEntity);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.copperbugs.clear();
        if (nbt.contains("copperbugs")) {
            CopperbugNestBlockEntity.CopperbugData.LIST_CODEC.parse(NbtOps.INSTANCE, nbt.get("copperbugs")).resultOrPartial((string) -> {
                LOGGER.error("Failed to parse copperbugs: '{}'", string);
            }).ifPresent((list) -> {
                list.forEach(this::addCopperbug);
            });
        }

        this.flowerPos = NbtUtils.readBlockPos(nbt, "flower_pos").orElse(null);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.put("copperbugs", CopperbugData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.createCopperbugsData()).getOrThrow());
        if (this.hasCopperBlockPos()) {
            nbt.put("flower_pos", NbtUtils.writeBlockPos(this.flowerPos));
        }

    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput components) {
        super.applyImplicitComponents(components);
        this.copperbugs.clear();
        List<CopperbugNestBlockEntity.CopperbugData> list = components.getOrDefault(BGDataComponentTypes.COPPERBUGS, List.of());
        list.forEach(this::addCopperbug);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
        componentMapBuilder.set(BGDataComponentTypes.COPPERBUGS, this.createCopperbugsData());
    }

    @Override
    public void removeComponentsFromTag(CompoundTag nbt) {
        super.removeComponentsFromTag(nbt);
        nbt.remove("copperbugs");
    }

    private List<CopperbugNestBlockEntity.CopperbugData> createCopperbugsData() {
        return this.copperbugs.stream().map(CopperbugNestBlockEntity.Copperbug::createData).toList();
    }

    public enum CopperbugState {
        HONEY_DELIVERED,
        COPPERBUG_RELEASED,
        EMERGENCY;

        CopperbugState() {
        }
    }

    public record CopperbugData(CustomData entityData, int ticksInNest, int minTicksInNest) {
        static CustomData nbt;
        static int minTicksInNests;

        public static final Codec<CopperbugNestBlockEntity.CopperbugData> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(CustomData.CODEC.optionalFieldOf("entity_data", CustomData.EMPTY).forGetter(CopperbugNestBlockEntity.CopperbugData::entityData), Codec.INT.fieldOf("ticks_in_Nest").forGetter(CopperbugNestBlockEntity.CopperbugData::ticksInNest), Codec.INT.fieldOf("min_ticks_in_Nest").forGetter(CopperbugNestBlockEntity.CopperbugData::minTicksInNest)).apply(instance, CopperbugNestBlockEntity.CopperbugData::new);
        });
        public static final Codec<List<CopperbugNestBlockEntity.CopperbugData>> LIST_CODEC;
        public static final StreamCodec<ByteBuf, CopperbugNestBlockEntity.CopperbugData> PACKET_CODEC;

        public CopperbugData(CustomData entityData, int ticksInNest, int minTicksInNest) {
            nbt = entityData;
            this.entityData = entityData;
            this.ticksInNest = ticksInNest;
            minTicksInNests = minTicksInNest;
            this.minTicksInNest = minTicksInNest;
        }

        public static CopperbugNestBlockEntity.CopperbugData of(Entity entity) {
            CompoundTag nbtCompound = new CompoundTag();
            entity.save(nbtCompound);
            List<String> var10000 = CopperbugNestBlockEntity.IRRELEVANT_COPPERBUG_NBT_KEYS;
            Objects.requireNonNull(nbtCompound);
            var10000.forEach(nbtCompound::remove);
            boolean bl = nbtCompound.getBoolean("HasNectar");
            return new CopperbugNestBlockEntity.CopperbugData(CustomData.of(nbtCompound), 0, bl ? 2400 : 600);
        }

        public static CopperbugNestBlockEntity.CopperbugData create(int ticksInNest) {
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(BGEntityTypes.COPPERBUG.get()).toString());
            return new CopperbugNestBlockEntity.CopperbugData(CustomData.of(nbtCompound), ticksInNest, 600);
        }

        @Nullable
        public Entity loadEntity(Level world, BlockPos pos) {
            CompoundTag nbtCompound = this.entityData.copyTag();
            Objects.requireNonNull(nbtCompound);
            List<String> var10000 = CopperbugNestBlockEntity.IRRELEVANT_COPPERBUG_NBT_KEYS;
            Entity entity = EntityType.loadEntityRecursive(nbtCompound, world, (entityx) -> {
                return entityx;
            });
            var10000.forEach(nbtCompound::remove);
            if (entity != null && entity.getType().is(JamiesModTag.COPPERBUGNEST_INHABITORS)) {
                entity.setNoGravity(true);
                if (entity instanceof CopperbugEntity beeEntity) {
                    beeEntity.setNestPos(pos);
                    tickEntity(this.ticksInNest, beeEntity);
                }

                return entity;
            } else {
                return null;
            }
        }

        private static void tickEntity(int ticksInNest, CopperbugEntity beeEntity) {
            int i = beeEntity.getAge();
            if (i < 0) {
                beeEntity.setAge(Math.min(0, i + ticksInNest));
            } else if (i > 0) {
                beeEntity.setAge(Math.max(0, i - ticksInNest));
            }

            beeEntity.setInLoveTime(Math.max(0, beeEntity.getInLoveTime() - ticksInNest));
        }

        @Override
        public CustomData entityData() {
            return this.entityData;
        }

        @Override
        public int ticksInNest() {
            return this.ticksInNest;
        }

        @Override
        public int minTicksInNest() {
            return this.minTicksInNest;
        }

        static {
            LIST_CODEC = CODEC.listOf();
            PACKET_CODEC = StreamCodec.composite(CustomData.STREAM_CODEC, CopperbugNestBlockEntity.CopperbugData::entityData, ByteBufCodecs.VAR_INT, CopperbugNestBlockEntity.CopperbugData::ticksInNest, ByteBufCodecs.VAR_INT, CopperbugNestBlockEntity.CopperbugData::minTicksInNest, CopperbugNestBlockEntity.CopperbugData::new);
        }
    }

    static class Copperbug {
        private final CopperbugNestBlockEntity.CopperbugData data;
        private int ticksInNest;

        Copperbug(CopperbugNestBlockEntity.CopperbugData data) {
            this.data = data;
            this.ticksInNest = data.ticksInNest();
        }

        public boolean canExitNest() {
            return this.ticksInNest++ > this.data.minTicksInNest;
        }

        public CopperbugNestBlockEntity.CopperbugData createData() {
            return new CopperbugNestBlockEntity.CopperbugData(this.data.entityData, this.ticksInNest, this.data.minTicksInNest);
        }

        public boolean hasNectar() {
            return this.data.entityData.getUnsafe().getBoolean("HasNectar");
        }
    }
}
