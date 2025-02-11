package com.jamiedev.bygone.common.blocks.entity;

import com.jamiedev.bygone.fabric.init.JamiesModBlockEntities;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class BygoneBrushableBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String LOOT_TABLE_NBT_KEY = "LootTable";
    private static final String LOOT_TABLE_SEED_NBT_KEY = "LootTableSeed";
    private static final String HIT_DIRECTION_NBT_KEY = "hit_direction";
    private static final String ITEM_NBT_KEY = "item";
    private static final int field_42806 = 10;
    private static final int field_42807 = 40;
    private static final int field_42808 = 10;
    private int brushesCount;
    private long nextDustTime;
    private long nextBrushTime;
    private ItemStack item;
    @Nullable
    private Direction hitDirection;
    @Nullable
    private ResourceKey<LootTable> lootTable;
    private long lootTableSeed;

    public BygoneBrushableBlockEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.BRUSHABLE_BLOCK, pos, state);
        this.item = ItemStack.EMPTY;
    }
    @Override
    public BlockEntityType<?> getType() {
        return JamiesModBlockEntities.BRUSHABLE_BLOCK;
    }
    public boolean brush(long worldTime, Player player, Direction hitDirection) {
        if (this.hitDirection == null) {
            this.hitDirection = hitDirection;
        }

        this.nextDustTime = worldTime + 40L;
        if (worldTime >= this.nextBrushTime && this.level instanceof ServerLevel) {
            this.nextBrushTime = worldTime + 10L;
            this.generateItem(player);
            int i = this.getDustedLevel();
            if (++this.brushesCount >= 10) {
                this.finishBrushing(player);
                return true;
            } else {
                this.level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 2);
                int j = this.getDustedLevel();
                if (i != j) {
                    BlockState blockState = this.getBlockState();
                    BlockState blockState2 = blockState.setValue(BlockStateProperties.DUSTED, j);
                    this.level.setBlock(this.getBlockPos(), blockState2, 3);
                }

                return false;
            }
        } else {
            return false;
        }
    }

    public void generateItem(Player player) {
        if (this.lootTable != null && this.level != null && !this.level.isClientSide() && this.level.getServer() != null) {
            LootTable lootTable = this.level.getServer().reloadableRegistries().getLootTable(this.lootTable);
            if (player instanceof ServerPlayer serverPlayerEntity) {
                CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayerEntity, this.lootTable);
            }

            LootParams lootContextParameterSet = (new LootParams.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition)).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.CHEST);
            ObjectArrayList<ItemStack> objectArrayList = lootTable.getRandomItems(lootContextParameterSet, this.lootTableSeed);
            ItemStack var10001;
            switch (objectArrayList.size()) {
                case 0:
                    var10001 = ItemStack.EMPTY;
                    break;
                case 1:
                    var10001 = objectArrayList.get(0);
                    break;
                default:
                    LOGGER.warn("Expected max 1 loot from loot table {}, but got {}", this.lootTable.location(), objectArrayList.size());
                    var10001 = objectArrayList.get(0);
            }

            this.item = var10001;
            this.lootTable = null;
            this.setChanged();
        }
    }

    private void finishBrushing(Player player) {
        if (this.level != null && this.level.getServer() != null) {
            this.spawnItem(player);
            BlockState blockState = this.getBlockState();
            this.level.levelEvent(3008, this.getBlockPos(), Block.getId(blockState));
            Block block = this.getBlockState().getBlock();
            Block block2;
            if (block instanceof BrushableBlock brushableBlock) {
                block2 = brushableBlock.getTurnsInto();
            } else {
                block2 = Blocks.AIR;
            }

            this.level.setBlock(this.worldPosition, block2.defaultBlockState(), 3);
        }
    }

    private void spawnItem(Player player) {
        if (this.level != null && this.level.getServer() != null) {
            this.generateItem(player);
            if (!this.item.isEmpty()) {
                double d = EntityType.ITEM.getWidth();
                double e = 1.0 - d;
                double f = d / 2.0;
                Direction direction = Objects.requireNonNullElse(this.hitDirection, Direction.UP);
                BlockPos blockPos = this.worldPosition.relative(direction, 1);
                double g = (double)blockPos.getX() + 0.5 * e + f;
                double h = (double)blockPos.getY() + 0.5 + (double)(EntityType.ITEM.getHeight() / 2.0F);
                double i = (double)blockPos.getZ() + 0.5 * e + f;
                ItemEntity itemEntity = new ItemEntity(this.level, g, h, i, this.item.split(this.level.random.nextInt(21) + 10));
                itemEntity.setDeltaMovement(Vec3.ZERO);
                this.level.addFreshEntity(itemEntity);
                this.item = ItemStack.EMPTY;
            }

        }
    }

    public void scheduledTick() {
        if (this.level != null) {
            if (this.brushesCount != 0 && this.level.getGameTime() >= this.nextDustTime) {
                int i = this.getDustedLevel();
                this.brushesCount = Math.max(0, this.brushesCount - 2);
                int j = this.getDustedLevel();
                if (i != j) {
                    this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.DUSTED, j), 3);
                }

                this.nextDustTime = this.level.getGameTime() + 4L;
            }

            if (this.brushesCount == 0) {
                this.hitDirection = null;
                this.nextDustTime = 0L;
                this.nextBrushTime = 0L;
            } else {
                this.level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 2);
            }

        }
    }

    private boolean readLootTableFromNbt(CompoundTag nbt) {
        if (nbt.contains("LootTable", 8)) {
            this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(nbt.getString("LootTable")));
            this.lootTableSeed = nbt.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    private boolean writeLootTableToNbt(CompoundTag nbt) {
        if (this.lootTable == null) {
            return false;
        } else {
            nbt.putString("LootTable", this.lootTable.location().toString());
            if (this.lootTableSeed != 0L) {
                nbt.putLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        CompoundTag nbtCompound = super.getUpdateTag(registryLookup);
        if (this.hitDirection != null) {
            nbtCompound.putInt("hit_direction", this.hitDirection.ordinal());
        }

        if (!this.item.isEmpty()) {
            nbtCompound.put("item", this.item.save(registryLookup));
        }

        return nbtCompound;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        if (!this.readLootTableFromNbt(nbt) && nbt.contains("item")) {
            this.item = ItemStack.parse(registryLookup, nbt.getCompound("item")).orElse(ItemStack.EMPTY);
        } else {
            this.item = ItemStack.EMPTY;
        }

        if (nbt.contains("hit_direction")) {
            this.hitDirection = Direction.values()[nbt.getInt("hit_direction")];
        }

    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (!this.writeLootTableToNbt(nbt) && !this.item.isEmpty()) {
            nbt.put("item", this.item.save(registryLookup));
        }

    }

    public void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
        this.lootTable = lootTable;
        this.lootTableSeed = seed;
    }

    private int getDustedLevel() {
        if (this.brushesCount == 0) {
            return 0;
        } else if (this.brushesCount < 3) {
            return 1;
        } else {
            return this.brushesCount < 6 ? 2 : 3;
        }
    }

    @Nullable
    public Direction getHitDirection() {
        return this.hitDirection;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
