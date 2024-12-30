package com.jamiedev.mod.common.blocks.entity;

import com.google.common.collect.Lists;
import com.jamiedev.mod.common.entities.CopperbugEntity;
import com.jamiedev.mod.common.blocks.CopperbugNestBlock;
import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import com.jamiedev.mod.fabric.init.JamiesModDataComponentTypes;
import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
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
        super(JamiesModBlockEntities.COPPERBUGNEST, pos, state);
    }

    public void markDirty() {
        if (this.isNearFire()) {
            this.angerCopperbugs((PlayerEntity)null, this.world.getBlockState(this.getPos()), CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
        }

        super.markDirty();
    }

    public boolean isNearFire() {
        if (this.world == null) {
            return false;
        } else {
            Iterator var1 = BlockPos.iterate(this.pos.add(-1, -1, -1), this.pos.add(1, 1, 1)).iterator();

            BlockPos blockPos;
            do {
                if (!var1.hasNext()) {
                    return false;
                }

                blockPos = (BlockPos)var1.next();
            } while(!(this.world.getBlockState(blockPos).getBlock() instanceof FireBlock));

            return true;
        }
    }

    public boolean hasNoCopperbugs() {
        return this.copperbugs.isEmpty();
    }

    public boolean isFullOfCopperbugs() {
        return this.copperbugs.size() == 3;
    }

    public void angerCopperbugs(@Nullable PlayerEntity player, BlockState state, CopperbugNestBlockEntity.CopperbugState beeState) {
        List<Entity> list = this.tryReleaseCopperbug(state, beeState);
        if (player != null) {
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                Entity entity = (Entity)var5.next();
                if (entity instanceof CopperbugEntity) {
                    CopperbugEntity beeEntity = (CopperbugEntity)entity;
                    if (player.getPos().squaredDistanceTo(entity.getPos()) <= 16.0) {
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
            return releaseCopperbug(this.world, this.pos, state, bee.createData(), list, beeState, this.flowerPos);
        });
        if (!list.isEmpty()) {
            super.markDirty();
        }

        return list;
    }

    @Debug
    public int getCopperbugCount() {
        return this.copperbugs.size();
    }

    public static int getOxidizationLevel(BlockState state) {
        return (Integer)state.get(CopperbugNestBlock.OXIDIZATION_LEVEL);
    }

    @Debug
    public boolean isSmoked() {
        return CampfireBlock.isLitCampfireInRange(this.world, this.getPos());
    }

    public void tryEnterNest(Entity entity) {
        if (this.copperbugs.size() < 15) {
            entity.stopRiding();
            entity.removeAllPassengers();
            this.addCopperbug(CopperbugNestBlockEntity.CopperbugData.of(entity));
            if (this.world != null) {
                if (entity instanceof CopperbugEntity) {
                    CopperbugEntity beeEntity = (CopperbugEntity)entity;
                    if (beeEntity.hasCopperBlock() && (!this.hasCopperBlockPos() || this.world.random.nextBoolean())) {
                        this.flowerPos = beeEntity.getCopperBlockPos();
                    }
                }

                BlockPos blockPos = this.getPos();
                this.world.playSound((PlayerEntity)null, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), SoundEvents.BLOCK_COPPER_BULB_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(entity, this.getCachedState()));
            }

            entity.discard();
            super.markDirty();
        }
    }

    public void addCopperbug(CopperbugNestBlockEntity.CopperbugData bee) {
        this.copperbugs.add(new CopperbugNestBlockEntity.Copperbug(bee));
    }

    private static boolean releaseCopperbug(World world, BlockPos pos, BlockState state, CopperbugNestBlockEntity.CopperbugData bee, @Nullable List<Entity> entities, CopperbugNestBlockEntity.CopperbugState beeState, @Nullable BlockPos flowerPos) {
        if ((world.isNight() || world.isRaining()) && beeState != CopperbugNestBlockEntity.CopperbugState.EMERGENCY) {
            return false;
        } else {
            Direction direction = (Direction)state.get(CopperbugNestBlock.FACING);
            BlockPos blockPos = pos.offset(direction);
            boolean bl = !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
            if (bl && beeState != CopperbugNestBlockEntity.CopperbugState.EMERGENCY) {
                return false;
            } else {
                Entity entity = bee.loadEntity(world, pos);
                if (entity != null) {
                    if (entity instanceof CopperbugEntity) {
                        CopperbugEntity beeEntity = (CopperbugEntity)entity;
                        if (flowerPos != null && !beeEntity.hasCopperBlock() && world.random.nextFloat() < 0.9F) {
                            beeEntity.setCopperBlockPos(flowerPos);
                        }

                        if (beeState == CopperbugNestBlockEntity.CopperbugState.HONEY_DELIVERED) {
                            beeEntity.onOxidizationDelivered();
                            if (state.isIn(JamiesModTag.COPPERBUGNESTS, (statex) -> {
                                return statex.contains(CopperbugNestBlock.OXIDIZATION_LEVEL);
                            })) {
                                int i = getOxidizationLevel(state);
                                if (i < 5) {
                                    int j = world.random.nextInt(100) == 0 ? 2 : 1;
                                    if (i + j > 5) {
                                        --j;
                                    }

                                    world.setBlockState(pos, (BlockState)state.with(CopperbugNestBlock.OXIDIZATION_LEVEL, i + j));
                                }
                            }
                        }

                        if (entities != null) {
                            entities.add(beeEntity);
                        }

                        float f = entity.getWidth();
                        double d = bl ? 0.0 : 0.55 + (double)(f / 2.0F);
                        double e = (double)pos.getX() + 0.5 + d * (double)direction.getOffsetX();
                        double g = (double)pos.getY() + 0.5 - (double)(entity.getHeight() / 2.0F);
                        double h = (double)pos.getZ() + 0.5 + d * (double)direction.getOffsetZ();
                        entity.refreshPositionAndAngles(e, g, h, entity.getYaw(), entity.getPitch());
                    }

                    world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, world.getBlockState(pos)));
                    return world.spawnEntity(entity);
                } else {
                    return false;
                }
            }
        }
    }

    private boolean hasCopperBlockPos() {
        return this.flowerPos != null;
    }

    private static void tickCopperbugs(World world, BlockPos pos, BlockState state, List<CopperbugNestBlockEntity.Copperbug> copperbugs, @Nullable BlockPos flowerPos) {
        boolean bl = false;
        Iterator<CopperbugNestBlockEntity.Copperbug> iterator = copperbugs.iterator();

        while(iterator.hasNext()) {
            CopperbugNestBlockEntity.Copperbug bee = (CopperbugNestBlockEntity.Copperbug)iterator.next();
            if (bee.canExitNest()) {
                CopperbugNestBlockEntity.CopperbugState beeState = bee.hasNectar() ? CopperbugNestBlockEntity.CopperbugState.HONEY_DELIVERED : CopperbugNestBlockEntity.CopperbugState.COPPERBUG_RELEASED;
                if (releaseCopperbug(world, pos, state, bee.createData(), (List)null, beeState, flowerPos)) {
                    bl = true;
                    iterator.remove();
                }
            }
        }

        if (bl) {
            markDirty(world, pos, state);
        }

    }

    public static void serverTick(World world, BlockPos pos, BlockState state, CopperbugNestBlockEntity blockEntity) {
        tickCopperbugs(world, pos, state, blockEntity.copperbugs, blockEntity.flowerPos);
        if (!blockEntity.copperbugs.isEmpty() && world.getRandom().nextDouble() < 0.005) {
            double d = (double)pos.getX() + 0.5;
            double e = (double)pos.getY();
            double f = (double)pos.getZ() + 0.5;
            world.playSound((PlayerEntity)null, d, e, f, SoundEvents.BLOCK_COPPER_GRATE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

      //  DebugInfoSender.sendBeehiveDebugData(world, pos, state, blockEntity);
    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.copperbugs.clear();
        if (nbt.contains("copperbugs")) {
            CopperbugNestBlockEntity.CopperbugData.LIST_CODEC.parse(NbtOps.INSTANCE, nbt.get("copperbugs")).resultOrPartial((string) -> {
                LOGGER.error("Failed to parse copperbugs: '{}'", string);
            }).ifPresent((list) -> {
                list.forEach(this::addCopperbug);
            });
        }

        this.flowerPos = (BlockPos) NbtHelper.toBlockPos(nbt, "flower_pos").orElse((BlockPos) null);
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.put("copperbugs", (NbtElement)CopperbugNestBlockEntity.CopperbugData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.createCopperbugsData()).getOrThrow());
        if (this.hasCopperBlockPos()) {
            nbt.put("flower_pos", NbtHelper.fromBlockPos(this.flowerPos));
        }

    }

    protected void readComponents(BlockEntity.ComponentsAccess components) {
        super.readComponents(components);
        this.copperbugs.clear();
        List<CopperbugNestBlockEntity.CopperbugData> list = components.getOrDefault(JamiesModDataComponentTypes.COPPERBUGS, List.of());
        list.forEach(this::addCopperbug);
    }

    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(JamiesModDataComponentTypes.COPPERBUGS, this.createCopperbugsData());
    }

    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        super.removeFromCopiedStackNbt(nbt);
        nbt.remove("copperbugs");
    }

    private List<CopperbugNestBlockEntity.CopperbugData> createCopperbugsData() {
        return this.copperbugs.stream().map(CopperbugNestBlockEntity.Copperbug::createData).toList();
    }

    public static enum CopperbugState {
        HONEY_DELIVERED,
        COPPERBUG_RELEASED,
        EMERGENCY;

        private CopperbugState() {
        }
    }

    public static record CopperbugData(NbtComponent entityData, int ticksInNest, int minTicksInNest) {
        static NbtComponent nbt;
        static int minTicksInNests;

        public static final Codec<CopperbugNestBlockEntity.CopperbugData> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(NbtComponent.CODEC.optionalFieldOf("entity_data", NbtComponent.DEFAULT).forGetter(CopperbugNestBlockEntity.CopperbugData::entityData), Codec.INT.fieldOf("ticks_in_Nest").forGetter(CopperbugNestBlockEntity.CopperbugData::ticksInNest), Codec.INT.fieldOf("min_ticks_in_Nest").forGetter(CopperbugNestBlockEntity.CopperbugData::minTicksInNest)).apply(instance, CopperbugNestBlockEntity.CopperbugData::new);
        });
        public static final Codec<List<CopperbugNestBlockEntity.CopperbugData>> LIST_CODEC;
        public static final PacketCodec<ByteBuf, CopperbugNestBlockEntity.CopperbugData> PACKET_CODEC;

        public CopperbugData(NbtComponent entityData, int ticksInNest, int minTicksInNest) {
            nbt = entityData;
            this.entityData = entityData;
            this.ticksInNest = ticksInNest;
            minTicksInNests = minTicksInNest;
            this.minTicksInNest = minTicksInNest;
        }

        public static CopperbugNestBlockEntity.CopperbugData of(Entity entity) {
            NbtCompound nbtCompound = new NbtCompound();
            entity.saveNbt(nbtCompound);
            List<String> var10000 = CopperbugNestBlockEntity.IRRELEVANT_COPPERBUG_NBT_KEYS;
            Objects.requireNonNull(nbtCompound);
            var10000.forEach(nbtCompound::remove);
            boolean bl = nbtCompound.getBoolean("HasNectar");
            return new CopperbugNestBlockEntity.CopperbugData(NbtComponent.of(nbtCompound), 0, bl ? 2400 : 600);
        }

        public static CopperbugNestBlockEntity.CopperbugData create(int ticksInNest) {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putString("id", Registries.ENTITY_TYPE.getId(JamiesModEntityTypes.COPPERBUG).toString());
            return new CopperbugNestBlockEntity.CopperbugData(NbtComponent.of(nbtCompound), ticksInNest, 600);
        }

        @Nullable
        public Entity loadEntity(World world, BlockPos pos) {
            NbtCompound nbtCompound = this.entityData.copyNbt();
            Objects.requireNonNull(nbtCompound);
            List<String> var10000 = CopperbugNestBlockEntity.IRRELEVANT_COPPERBUG_NBT_KEYS;
            Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, (entityx) -> {
                return entityx;
            });
            var10000.forEach(nbtCompound::remove);
            if (entity != null && entity.getType().isIn(JamiesModTag.COPPERBUGNEST_INHABITORS)) {
                entity.setNoGravity(true);
                if (entity instanceof CopperbugEntity) {
                    CopperbugEntity beeEntity = (CopperbugEntity)entity;
                    beeEntity.setNestPos(pos);
                    tickEntity(this.ticksInNest, beeEntity);
                }

                return entity;
            } else {
                return null;
            }
        }

        private static void tickEntity(int ticksInNest, CopperbugEntity beeEntity) {
            int i = beeEntity.getBreedingAge();
            if (i < 0) {
                beeEntity.setBreedingAge(Math.min(0, i + ticksInNest));
            } else if (i > 0) {
                beeEntity.setBreedingAge(Math.max(0, i - ticksInNest));
            }

            beeEntity.setLoveTicks(Math.max(0, beeEntity.getLoveTicks() - ticksInNest));
        }

        public NbtComponent entityData() {
            return this.entityData;
        }

        public int ticksInNest() {
            return this.ticksInNest;
        }

        public int minTicksInNest() {
            return this.minTicksInNest;
        }

        static {
            LIST_CODEC = CODEC.listOf();
            PACKET_CODEC = PacketCodec.tuple(NbtComponent.PACKET_CODEC, CopperbugNestBlockEntity.CopperbugData::entityData, PacketCodecs.VAR_INT, CopperbugNestBlockEntity.CopperbugData::ticksInNest, PacketCodecs.VAR_INT, CopperbugNestBlockEntity.CopperbugData::minTicksInNest, CopperbugNestBlockEntity.CopperbugData::new);
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
            return this.data.entityData.getNbt().getBoolean("HasNectar");
        }
    }
}
