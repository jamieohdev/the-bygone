package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.fabric.init.JamiesModParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlueAlgueBlock extends BushBlock {
    public static final MapCodec<BlueAlgueBlock> CODEC = simpleCodec(BlueAlgueBlock::new);
    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 1.5, 15.0);

    protected SimpleParticleType particle;
    Blocks ref;

    public MapCodec<BlueAlgueBlock> codec() {
        return CODEC;
    }



    public BlueAlgueBlock(SimpleParticleType particle, BlockBehaviour.Properties settings) {
        super(settings);
        this.particle = particle;
    }

    public BlueAlgueBlock(BlockBehaviour.Properties settings)
    {
        super(settings);
    }

    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (world instanceof ServerLevel && entity instanceof Boat) {

            double d = (double)pos.getX() + 0.5;
            double e = (double)pos.getY() + 0.7;
            double f = (double)pos.getZ() + 0.5;
            ((ServerLevel) world).sendParticles((SimpleParticleType) JamiesModParticleTypes.ALGAE_BLOOM, (double)pos.getX() + 0.7, (double)pos.getY(),
                    (double)pos.getZ() + 0.7, 1, 0.5, 0.0, 0.5, 0.0);
        }

    }
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidState2 = world.getFluidState(pos.above());
        return (fluidState.getType() == Fluids.WATER || floor.getBlock() instanceof IceBlock) && fluidState2.getType() == Fluids.EMPTY;
    }
}
