package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.fabric.init.JamiesModParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlueAlgueBlock extends PlantBlock {
    public static final MapCodec<BlueAlgueBlock> CODEC = createCodec(BlueAlgueBlock::new);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.5, 15.0);

    protected SimpleParticleType particle;
    Blocks ref;

    public MapCodec<BlueAlgueBlock> getCodec() {
        return CODEC;
    }



    public BlueAlgueBlock(SimpleParticleType particle, AbstractBlock.Settings settings) {
        super(settings);
        this.particle = particle;
    }

    public BlueAlgueBlock(AbstractBlock.Settings settings)
    {
        super(settings);
    }

    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (world instanceof ServerWorld && entity instanceof BoatEntity) {

            double d = (double)pos.getX() + 0.5;
            double e = (double)pos.getY() + 0.7;
            double f = (double)pos.getZ() + 0.5;
            ((ServerWorld) world).spawnParticles((SimpleParticleType) JamiesModParticleTypes.ALGAE_BLOOM, (double)pos.getX() + 0.7, (double)pos.getY(),
                    (double)pos.getZ() + 0.7, 1, 0.5, 0.0, 0.5, 0.0);
        }

    }
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidState2 = world.getFluidState(pos.up());
        return (fluidState.getFluid() == Fluids.WATER || floor.getBlock() instanceof IceBlock) && fluidState2.getFluid() == Fluids.EMPTY;
    }
}
