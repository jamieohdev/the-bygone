package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.entity.BygonePortalBlockEntity;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.MapCodec;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.AXIS;

public class BygonePortalBlock extends Block implements Portal {
    public static final MapCodec<BygonePortalBlock> CODEC = BlockBehaviour.simpleCodec(BygonePortalBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    EndPortalBlock ref;

    public BygonePortalBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<BygonePortalBlock> codec() {
        return CODEC;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BygonePortalBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        //Block block = getPortalBase((Level) world, pos);
        //PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
        ////Corrinedev: Remove portal frame check
        //if (link != null) {
        //    PortalFrameTester portalFrameTester = link.getFrameTester().createInstanceOfPortalFrameTester().init(world, pos, CustomPortalHelper.getAxisFrom(state), block);
        //    if (portalFrameTester.isAlreadyLitPortalFrame())
                return super.updateShape(state, direction, newState, world, pos, posFrom);
        //    else
        //        return Blocks.AIR.defaultBlockState();
        //} else
        //    return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound((double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F, (double) pos.getZ() + (double) 0.5F, BGSoundEvents.BLOCK_PORTAL_AMBIENT_EVENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int i = 0; i < 4; ++i) {
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() + random.nextDouble();
            double f = (double) pos.getZ() + random.nextDouble();
            double g = ((double) random.nextFloat() - (double) 0.5F) * (double) 0.5F;
            double h = ((double) random.nextFloat() - (double) 0.5F) * (double) 0.5F;
            double j = ((double) random.nextFloat() - (double) 0.5F) * (double) 0.5F;
            int k = random.nextInt(2) * 2 - 1;
            if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                d = (double) pos.getX() + (double) 0.5F + (double) 0.25F * (double) k;
                g = random.nextFloat() * 2.0F * (float) k;
            } else {
                f = (double) pos.getZ() + (double) 0.5F + (double) 0.25F * (double) k;
                j = random.nextFloat() * 2.0F * (float) k;
            }

            //level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.getPortalBase(level, pos).defaultBlockState()), d, e, f, g, h, j);
        }

    }

    public Portal.Transition getLocalTransition() {
        return Transition.CONFUSION;
    }

    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        int time;
        if (entity instanceof Player player) {
            time = Math.max(1, level.getGameRules().getInt(player.getAbilities().invulnerable ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
        } else {
            time = 0;
        }

        return time;
    }

    @Nullable
    public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        ResourceKey<Level> bygone = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID,"bygone"));
        ResourceKey<Level> resourcekey = level.dimension() == bygone ? Level.OVERWORLD : bygone;

        ServerLevel serverlevel = level.getServer().getLevel(resourcekey);
        if (serverlevel == null) {
            return null;
        } else {
            boolean flag = serverlevel.dimension() == bygone;
            WorldBorder worldborder = serverlevel.getWorldBorder();
            double d0 = DimensionType.getTeleportationScale(level.dimensionType(), serverlevel.dimensionType());
            BlockPos blockpos = worldborder.clampToBounds(entity.getX() * d0, entity.getY(), entity.getZ() * d0);

            return this.getExitPortal(serverlevel, entity, pos, blockpos, flag, worldborder);
        }
    }


    @Nullable
    private DimensionTransition getExitPortal(ServerLevel level, Entity entity, BlockPos pos, BlockPos exitPos, boolean isNether, WorldBorder worldBorder) {
        Optional<BlockPos> optional = level.getPortalForcer().findClosestPortalPosition(exitPos, isNether, worldBorder);
        BlockUtil.FoundRectangle blockutil$foundrectangle;
        DimensionTransition.PostDimensionTransition dimensiontransition$postdimensiontransition;
        if (optional.isPresent()) {
            BlockPos blockpos = (BlockPos)optional.get();
            BlockState blockstate = level.getBlockState(blockpos);
            blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(blockpos, (Direction.Axis)blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (p_351970_) -> level.getBlockState(p_351970_) == blockstate);
            dimensiontransition$postdimensiontransition = DimensionTransition.PLAY_PORTAL_SOUND.then((p_351967_) -> p_351967_.placePortalTicket(blockpos));
        } else {
            Direction.Axis direction$axis = (Direction.Axis)entity.level().getBlockState(pos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> optional1 = level.getPortalForcer().createPortal(exitPos, direction$axis);
            if (optional1.isEmpty()) {
                System.out.println("Unable to create a portal, likely target out of worldborder");
                return null;
            }

            blockutil$foundrectangle = (BlockUtil.FoundRectangle)optional1.get();
            dimensiontransition$postdimensiontransition = DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET);
        }

        return getDimensionTransitionFromExit(entity, pos, blockutil$foundrectangle, level, dimensiontransition$postdimensiontransition);
    }

    private static DimensionTransition getDimensionTransitionFromExit(Entity entity, BlockPos pos, BlockUtil.FoundRectangle rectangle, ServerLevel level, DimensionTransition.PostDimensionTransition postDimensionTransition) {
        BlockState blockstate = entity.level().getBlockState(pos);
        Direction.Axis direction$axis;
        Vec3 vec3;
        if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            direction$axis = (Direction.Axis)blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            BlockUtil.FoundRectangle blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(pos, direction$axis, 21, Direction.Axis.Y, 21, (p_351016_) -> entity.level().getBlockState(p_351016_) == blockstate);
            vec3 = entity.getRelativePortalPosition(direction$axis, blockutil$foundrectangle);
        } else {
            direction$axis = Direction.Axis.X;
            vec3 = new Vec3((double)0.5F, (double)0.0F, (double)0.0F);
        }

        return createDimensionTransition(level, rectangle, direction$axis, vec3, entity, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), postDimensionTransition);
    }

    private static DimensionTransition createDimensionTransition(ServerLevel level, BlockUtil.FoundRectangle rectangle, Direction.Axis axis, Vec3 offset, Entity entity, Vec3 speed, float yRot, float xRot, DimensionTransition.PostDimensionTransition postDimensionTransition) {
        BlockPos blockpos = rectangle.minCorner;
        BlockState blockstate = level.getBlockState(blockpos);
        Direction.Axis direction$axis = (Direction.Axis)blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double d0 = (double)rectangle.axis1Size;
        double d1 = (double)rectangle.axis2Size;
        EntityDimensions entitydimensions = entity.getDimensions(entity.getPose());
        int i = axis == direction$axis ? 0 : 90;
        Vec3 vec3 = axis == direction$axis ? speed : new Vec3(speed.z, speed.y, -speed.x);
        double d2 = (double)entitydimensions.width() / (double)2.0F + (d0 - (double)entitydimensions.width()) * offset.x();
        double d3 = (d1 - (double)entitydimensions.height()) * offset.y();
        double d4 = (double)0.5F + offset.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec31 = new Vec3((double)blockpos.getX() + (flag ? d2 : d4), (double)blockpos.getY() + d3, (double)blockpos.getZ() + (flag ? d4 : d2));
        Vec3 vec32 = PortalShape.findCollisionFreePosition(vec31, level, entity, entitydimensions);
        return new DimensionTransition(level, vec32, vec3, yRot + (float)i, xRot, postDimensionTransition);
    }
}
