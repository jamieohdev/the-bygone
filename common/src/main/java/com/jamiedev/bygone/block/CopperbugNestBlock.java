package com.jamiedev.bygone.block;

import com.jamiedev.bygone.block.entity.CopperbugNestBlockEntity;
import com.jamiedev.bygone.fabric.init.JamiesModBlockEntities;
import com.jamiedev.bygone.fabric.init.JamiesModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import com.jamiedev.bygone.common.entities.CopperbugEntity;

import java.util.Iterator;
import java.util.List;

public class CopperbugNestBlock extends BaseEntityBlock
{

    public static final MapCodec<CopperbugNestBlock> CODEC = simpleCodec(CopperbugNestBlock::new);
    public static final DirectionProperty FACING;
    public static final IntegerProperty OXIDIZATION_LEVEL;
    public static final int FULL_OXIDIZATION_LEVEL = 5;
    private static final int DROPPED_HONEYCOMB_COUNT = 3;

    @Override
    public MapCodec<CopperbugNestBlock> codec() {
        return CODEC;
    }

    public CopperbugNestBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(OXIDIZATION_LEVEL, 0).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(OXIDIZATION_LEVEL);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
        if (!world.isClientSide && blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
            if (!EnchantmentHelper.hasTag(tool, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
                beehiveBlockEntity.angerCopperbugs(player, state, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
                world.updateNeighbourForOutputSignal(pos, this);
                this.angerNearbyCopperbugs(world, pos);
            }

            CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)player, state, tool, beehiveBlockEntity.getCopperbugCount());
        }

    }

    private void angerNearbyCopperbugs(Level world, BlockPos pos) {
        AABB box = (new AABB(pos)).inflate(8.0, 6.0, 8.0);
        List<CopperbugEntity> list = world.getEntitiesOfClass(CopperbugEntity.class, box);
        if (!list.isEmpty()) {
            List<Player> list2 = world.getEntitiesOfClass(Player.class, box);
            if (list2.isEmpty()) {
                return;
            }

            Iterator<CopperbugEntity> var6 = list.iterator();

            while(var6.hasNext()) {
                CopperbugEntity beeEntity = (CopperbugEntity)var6.next();
                if (beeEntity.getTarget() == null) {
                    Player playerEntity = Util.getRandom(list2, world.random);
                    beeEntity.setTarget(playerEntity);
                }
            }
        }

    }

    public static void dropItems(Level world, BlockPos pos) {
        if (world.random.nextInt(10) == 1)
        {
            popResource(world, pos, new ItemStack(JamiesModItems.VERDIGRIS_SCRAP, 1));
        }
        else
        {
            popResource(world, pos, new ItemStack(Items.COPPER_INGOT, 3));   
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int i = state.getValue(OXIDIZATION_LEVEL);
        boolean bl = false;
        if (i >= 5) {
            Item item = stack.getItem();
            if (stack.is(ItemTags.AXES)) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                dropItems(world, pos);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                bl = true;
                world.gameEvent(player, GameEvent.SHEAR, pos);
            } else if (stack.is(Items.BUCKET)) {
                stack.shrink(1);
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
                } else if (!player.getInventory().add(new ItemStack(Items.WATER_BUCKET))) {
                    player.drop(new ItemStack(Items.WATER_BUCKET), false);
                }

                bl = true;
                world.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            }

            if (!world.isClientSide() && bl) {
                player.awardStat(Stats.ITEM_USED.get(item));
            }
        }

        if (bl) {
            if (!CampfireBlock.isSmokeyPos(world, pos)) {
                if (this.hasCopperbugs(world, pos)) {
                    this.angerNearbyCopperbugs(world, pos);
                }

                this.takeOxidization(world, state, pos, player, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
            } else {
                this.takeOxidization(world, state, pos);
            }

            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return super.useItemOn(stack, state, world, pos, player, hand, hit);
        }
    }

    private boolean hasCopperbugs(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
            return !beehiveBlockEntity.hasNoCopperbugs();
        } else {
            return false;
        }
    }

    public void takeOxidization(Level world, BlockState state, BlockPos pos, @Nullable Player player, CopperbugNestBlockEntity.CopperbugState beeState) {
        this.takeOxidization(world, state, pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
            beehiveBlockEntity.angerCopperbugs(player, state, beeState);
        }

    }

    public void takeOxidization(Level world, BlockState state, BlockPos pos) {
        world.setBlock(pos, state.setValue(OXIDIZATION_LEVEL, 0), 3);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(OXIDIZATION_LEVEL) >= 5) {
            for(int i = 0; i < random.nextInt(1) + 1; ++i) {
                this.spawnHoneyParticles(world, pos, state);
            }
        }

    }

    private void spawnHoneyParticles(Level world, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && !(world.random.nextFloat() < 0.3F)) {
            VoxelShape voxelShape = state.getCollisionShape(world, pos);
            double d = voxelShape.max(Direction.Axis.Y);
            if (d >= 1.0 && !state.is(BlockTags.IMPERMEABLE)) {
                double e = voxelShape.min(Direction.Axis.Y);
                if (e > 0.0) {
                    this.addHoneyParticle(world, pos, voxelShape, (double)pos.getY() + e - 0.05);
                } else {
                    BlockPos blockPos = pos.below();
                    BlockState blockState = world.getBlockState(blockPos);
                    VoxelShape voxelShape2 = blockState.getCollisionShape(world, blockPos);
                    double f = voxelShape2.max(Direction.Axis.Y);
                    if ((f < 1.0 || !blockState.isCollisionShapeFullBlock(world, blockPos)) && blockState.getFluidState().isEmpty()) {
                        this.addHoneyParticle(world, pos, voxelShape, (double)pos.getY() - 0.05);
                    }
                }
            }

        }
    }

    private void addHoneyParticle(Level world, BlockPos pos, VoxelShape shape, double height) {
        this.addHoneyParticle(world, (double)pos.getX() + shape.min(Direction.Axis.X), (double)pos.getX() + shape.max(Direction.Axis.X), (double)pos.getZ() + shape.min(Direction.Axis.Z), (double)pos.getZ() + shape.max(Direction.Axis.Z), height);
    }

    private void addHoneyParticle(Level world, double minX, double maxX, double minZ, double maxZ, double height) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(world.random.nextDouble(), minX, maxX), height, Mth.lerp(world.random.nextDouble(), minZ, maxZ), 0.0, 0.0, 0.0);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OXIDIZATION_LEVEL, FACING);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperbugNestBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : createTickerHelper(type, JamiesModBlockEntities.COPPERBUGNEST, CopperbugNestBlockEntity::serverTick);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
                int i = state.getValue(OXIDIZATION_LEVEL);
                boolean bl = !beehiveBlockEntity.hasNoCopperbugs();
                if (bl || i > 0) {
                    ItemStack itemStack = new ItemStack(this);
                    itemStack.applyComponents(beehiveBlockEntity.collectComponents());
                    itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(OXIDIZATION_LEVEL, i));
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                    itemEntity.setDefaultPickUpDelay();
                    world.addFreshEntity(itemEntity);
                }
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof PrimedTnt || entity instanceof Creeper || entity instanceof WitherSkull || entity instanceof WitherBoss || entity instanceof MinecartTNT) {
            BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
                beehiveBlockEntity.angerCopperbugs(null, state, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
            }
        }

        return super.getDrops(state, builder);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (world.getBlockState(neighborPos).getBlock() instanceof FireBlock) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CopperbugNestBlockEntity beehiveBlockEntity) {
                beehiveBlockEntity.angerCopperbugs(null, state, CopperbugNestBlockEntity.CopperbugState.EMERGENCY);
            }
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        OXIDIZATION_LEVEL = BlockStateProperties.LEVEL_HONEY;
    }
}
