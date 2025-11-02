package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.BygonePortalBlockEntity;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.MapCodec;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.portal.frame.PortalFrameTester;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BygonePortalBlock extends CustomPortalBlock {
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
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        Block block = getPortalBase((Level) world, pos);
        PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
        //Corrinedev: Remove portal frame check
        if (link != null) {
            PortalFrameTester portalFrameTester = link.getFrameTester().createInstanceOfPortalFrameTester().init(world, pos, CustomPortalHelper.getAxisFrom(state), block);
            if (portalFrameTester.isAlreadyLitPortalFrame())
                return super.updateShape(state, direction, newState, world, pos, posFrom);
            else
                return Blocks.AIR.defaultBlockState();
        } else
            return Blocks.AIR.defaultBlockState();
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

            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.getPortalBase(level, pos).defaultBlockState()), d, e, f, g, h, j);
        }

    }
}
