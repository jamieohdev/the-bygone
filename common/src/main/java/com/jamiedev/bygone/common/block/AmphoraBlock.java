package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.AmphoraBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;


public class AmphoraBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<AmphoraBlock> CODEC = simpleCodec(AmphoraBlock::new);
    public static final ResourceLocation SHERDS_DYNAMIC_DROP_ID = ResourceLocation.withDefaultNamespace("sherds");
    private static final VoxelShape BOUNDING_BOX = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 16.0F, 15.0F);

    public static final BooleanProperty CRACKED;
    private static final BooleanProperty WATERLOGGED;
    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water_level", 0, 8);

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);


    public MapCodec<AmphoraBlock> codec() {
        return CODEC;
    }

    public AmphoraBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(CRACKED, false)
                .setValue(WATER_LEVEL, 0)
        );
    }

    @Deprecated
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(CRACKED, false);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity itemStack1 = level.getBlockEntity(pos);
        if (itemStack1 instanceof AmphoraBlockEntity AmphoraBlockEntity) {
            if (stack.is(Items.WATER_BUCKET) && state.getValue(WATER_LEVEL) < 8) {
                AmphoraBlockEntity.wobble(com.jamiedev.bygone.common.block.entity.AmphoraBlockEntity.WobbleStyle.POSITIVE);
                doWaterParticles(level, pos);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                updateWaterLevel(level, state.getValue(WATER_LEVEL) + 1, state, pos);
                if (!player.hasInfiniteMaterials())
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                return ItemInteractionResult.SUCCESS;
            } else if (stack.is(Items.BUCKET) && state.getValue(WATER_LEVEL) > 0) {
                AmphoraBlockEntity.wobble(com.jamiedev.bygone.common.block.entity.AmphoraBlockEntity.WobbleStyle.POSITIVE);
                doWaterParticles(level, pos);
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                updateWaterLevel(level, state.getValue(WATER_LEVEL) - 1, state, pos);
                if (!player.hasInfiniteMaterials())
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
                return ItemInteractionResult.SUCCESS;
            }
        } else {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static void updateWaterLevel(Level level, int waterLevel, BlockState state, BlockPos pos) {
        BlockState newState = state.setValue(WATER_LEVEL, Integer.valueOf(waterLevel));
        level.setBlockAndUpdate(pos, newState);
    }

    public static void doWaterParticles(Level level, BlockPos pos) {
        if (level.isClientSide) {
            level.addParticle(ParticleTypes.SPLASH,
                    pos.getX() + (double) level.random.nextFloat(),
                    pos.getY() + 1,
                    pos.getZ() + (double) level.random.nextFloat(),
                    0.0,
                    0.0,
                    0.0);
        }
    }


    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity var7 = level.getBlockEntity(pos);
        if (var7 instanceof AmphoraBlockEntity AmphoraBlockEntity) {
            level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
            AmphoraBlockEntity.wobble(com.jamiedev.bygone.common.block.entity.AmphoraBlockEntity.WobbleStyle.NEGATIVE);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }

    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CRACKED, WATER_LEVEL);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AmphoraBlockEntity(pos, state);
    }

    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof AmphoraBlockEntity AmphoraBlockEntity) {
            params.withDynamicDrop(SHERDS_DYNAMIC_DROP_ID, (consumer) -> {
                for (Item item : AmphoraBlockEntity.getDecorations().ordered()) {
                    consumer.accept(item.getDefaultInstance());
                }
            });
        }

        return super.getDrops(state, params);
    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        ItemStack itemStack = player.getMainHandItem();
        BlockState blockState = state;
        if (itemStack.is(ItemTags.BREAKS_DECORATED_POTS) && !EnchantmentHelper.hasTag(itemStack, EnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING)) {
            blockState = state.setValue(CRACKED, true);
            level.setBlock(pos, blockState, 4);
        }

        return super.playerWillDestroy(level, pos, blockState, player);
    }

    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected SoundType getSoundType(BlockState state) {
        return state.getValue(CRACKED) ? SoundType.DECORATED_POT_CRACKED : SoundType.DECORATED_POT;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        PotDecorations potDecorations = stack.getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
        if (!potDecorations.equals(PotDecorations.EMPTY)) {
            tooltipComponents.add(CommonComponents.EMPTY);
            Stream.of(potDecorations.front(), potDecorations.left(), potDecorations.right(), potDecorations.back()).forEach((optional) -> tooltipComponents.add((new ItemStack(optional.orElse(Items.BRICK), 1)).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY)));
        }
    }

    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockPos = hit.getBlockPos();
        if (!level.isClientSide && projectile.mayInteract(level, blockPos) && projectile.mayBreak(level)) {
            level.setBlock(blockPos, state.setValue(CRACKED, true), 4);
            level.destroyBlock(blockPos, true, projectile);
        }
    }

    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    static {
        CRACKED = BlockStateProperties.CRACKED;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}