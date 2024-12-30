package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import com.jamiedev.mod.fabric.init.JamiesModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import com.jamiedev.mod.common.blocks.entity.CopperbugNestBlockEntity;
import com.jamiedev.mod.common.entities.CopperbugEntity;

import java.util.Iterator;
import java.util.List;

public class CopperbugNestBlock extends BlockWithEntity
{

    public static final MapCodec<CopperbugNestBlock> CODEC = createCodec(CopperbugNestBlock::new);
    public static final DirectionProperty FACING;
    public static final IntProperty OXIDIZATION_LEVEL;
    public static final int FULL_OXIDIZATION_LEVEL = 5;
    private static final int DROPPED_HONEYCOMB_COUNT = 3;

    public MapCodec<CopperbugNestBlock> getCodec() {
        return CODEC;
    }

    public CopperbugNestBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(OXIDIZATION_LEVEL, 0)).with(FACING, Direction.NORTH));
    }

    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return (Integer)state.get(OXIDIZATION_LEVEL);
    }

    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (!world.isClient && blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
            if (!EnchantmentHelper.hasAnyEnchantmentsIn(tool, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
                beehiveBlockEntity.angerCopperbugs(player, state, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
                world.updateComparators(pos, this);
                this.angerNearbyCopperbugs(world, pos);
            }

            Criteria.BEE_NEST_DESTROYED.trigger((ServerPlayerEntity)player, state, tool, beehiveBlockEntity.getCopperbugCount());
        }

    }

    private void angerNearbyCopperbugs(World world, BlockPos pos) {
        Box box = (new Box(pos)).expand(8.0, 6.0, 8.0);
        List<CopperbugEntity> list = world.getNonSpectatingEntities(CopperbugEntity.class, box);
        if (!list.isEmpty()) {
            List<PlayerEntity> list2 = world.getNonSpectatingEntities(PlayerEntity.class, box);
            if (list2.isEmpty()) {
                return;
            }

            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
                CopperbugEntity beeEntity = (CopperbugEntity)var6.next();
                if (beeEntity.getTarget() == null) {
                    PlayerEntity playerEntity = (PlayerEntity) Util.getRandom(list2, world.random);
                    beeEntity.setTarget(playerEntity);
                }
            }
        }

    }

    public static void dropItems(World world, BlockPos pos) {
        if (world.random.nextInt(10) == 1)
        {
            dropStack(world, pos, new ItemStack(JamiesModItems.VERDIGRIS_SCRAP, 1));
        }
        else
        {
            dropStack(world, pos, new ItemStack(Items.COPPER_INGOT, 3));   
        }
    }

    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int i = (Integer)state.get(OXIDIZATION_LEVEL);
        boolean bl = false;
        if (i >= 5) {
            Item item = stack.getItem();
            if (stack.isIn(ItemTags.AXES)) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                dropItems(world, pos);
                stack.damage(1, player, LivingEntity.getSlotForHand(hand));
                bl = true;
                world.emitGameEvent(player, GameEvent.SHEAR, pos);
            } else if (stack.isOf(Items.BUCKET)) {
                stack.decrement(1);
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));
                } else if (!player.getInventory().insertStack(new ItemStack(Items.WATER_BUCKET))) {
                    player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                }

                bl = true;
                world.emitGameEvent(player, GameEvent.FLUID_PICKUP, pos);
            }

            if (!world.isClient() && bl) {
                player.incrementStat(Stats.USED.getOrCreateStat(item));
            }
        }

        if (bl) {
            if (!CampfireBlock.isLitCampfireInRange(world, pos)) {
                if (this.hasCopperbugs(world, pos)) {
                    this.angerNearbyCopperbugs(world, pos);
                }

                this.takeOxidization(world, state, pos, player, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
            } else {
                this.takeOxidization(world, state, pos);
            }

            return ItemActionResult.success(world.isClient);
        } else {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        }
    }

    private boolean hasCopperbugs(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
            return !beehiveBlockEntity.hasNoCopperbugs();
        } else {
            return false;
        }
    }

    public void takeOxidization(World world, BlockState state, BlockPos pos, @Nullable PlayerEntity player, CopperbugNestBlockEntity.CopperbugState beeState) {
        this.takeOxidization(world, state, pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
            beehiveBlockEntity.angerCopperbugs(player, state, beeState);
        }

    }

    public void takeOxidization(World world, BlockState state, BlockPos pos) {
        world.setBlockState(pos, (BlockState)state.with(OXIDIZATION_LEVEL, 0), 3);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Integer)state.get(OXIDIZATION_LEVEL) >= 5) {
            for(int i = 0; i < random.nextInt(1) + 1; ++i) {
                this.spawnHoneyParticles(world, pos, state);
            }
        }

    }

    private void spawnHoneyParticles(World world, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && !(world.random.nextFloat() < 0.3F)) {
            VoxelShape voxelShape = state.getCollisionShape(world, pos);
            double d = voxelShape.getMax(Direction.Axis.Y);
            if (d >= 1.0 && !state.isIn(BlockTags.IMPERMEABLE)) {
                double e = voxelShape.getMin(Direction.Axis.Y);
                if (e > 0.0) {
                    this.addHoneyParticle(world, pos, voxelShape, (double)pos.getY() + e - 0.05);
                } else {
                    BlockPos blockPos = pos.down();
                    BlockState blockState = world.getBlockState(blockPos);
                    VoxelShape voxelShape2 = blockState.getCollisionShape(world, blockPos);
                    double f = voxelShape2.getMax(Direction.Axis.Y);
                    if ((f < 1.0 || !blockState.isFullCube(world, blockPos)) && blockState.getFluidState().isEmpty()) {
                        this.addHoneyParticle(world, pos, voxelShape, (double)pos.getY() - 0.05);
                    }
                }
            }

        }
    }

    private void addHoneyParticle(World world, BlockPos pos, VoxelShape shape, double height) {
        this.addHoneyParticle(world, (double)pos.getX() + shape.getMin(Direction.Axis.X), (double)pos.getX() + shape.getMax(Direction.Axis.X), (double)pos.getZ() + shape.getMin(Direction.Axis.Z), (double)pos.getZ() + shape.getMax(Direction.Axis.Z), height);
    }

    private void addHoneyParticle(World world, double minX, double maxX, double minZ, double maxZ, double height) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, MathHelper.lerp(world.random.nextDouble(), minX, maxX), height, MathHelper.lerp(world.random.nextDouble(), minZ, maxZ), 0.0, 0.0, 0.0);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{OXIDIZATION_LEVEL, FACING});
    }

    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperbugNestBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, JamiesModBlockEntities.COPPERBUGNEST, CopperbugNestBlockEntity::serverTick);
    }

    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CopperbugNestBlockEntity) {
                CopperbugNestBlockEntity beehiveBlockEntity = (CopperbugNestBlockEntity)blockEntity;
                int i = (Integer)state.get(OXIDIZATION_LEVEL);
                boolean bl = !beehiveBlockEntity.hasNoCopperbugs();
                if (bl || i > 0) {
                    ItemStack itemStack = new ItemStack(this);
                    itemStack.applyComponentsFrom(beehiveBlockEntity.createComponentMap());
                    itemStack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(OXIDIZATION_LEVEL, i));
                    ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack);
                    itemEntity.setToDefaultPickupDelay();
                    world.spawnEntity(itemEntity);
                }
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        Entity entity = (Entity)builder.getOptional(LootContextParameters.THIS_ENTITY);
        if (entity instanceof TntEntity || entity instanceof CreeperEntity || entity instanceof WitherSkullEntity || entity instanceof WitherEntity || entity instanceof TntMinecartEntity) {
            BlockEntity blockEntity = (BlockEntity)builder.getOptional(LootContextParameters.BLOCK_ENTITY);
            if (blockEntity instanceof CopperbugNestBlockEntity) {
                CopperbugNestBlockEntity beehiveBlockEntity = (CopperbugNestBlockEntity)blockEntity;
                beehiveBlockEntity.angerCopperbugs((PlayerEntity)null, state, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
            }
        }

        return super.getDroppedStacks(state, builder);
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (world.getBlockState(neighborPos).getBlock() instanceof FireBlock) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CopperbugNestBlockEntity) {
                CopperbugNestBlockEntity beehiveBlockEntity = (CopperbugNestBlockEntity)blockEntity;
                beehiveBlockEntity.angerCopperbugs((PlayerEntity)null, state, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
            }
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        OXIDIZATION_LEVEL = Properties.HONEY_LEVEL;
    }
}
