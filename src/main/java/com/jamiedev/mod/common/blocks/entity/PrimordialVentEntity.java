package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.common.blocks.PrimordialVentBlock;
import com.jamiedev.mod.common.init.JamiesModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class PrimordialVentEntity  extends BlockEntity implements Clearable
{

    CampfireBlockEntity ref;

    FurnaceBlock ref2;

    public PrimordialVentEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.PRIMORDIAL_VENT, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, PrimordialVentEntity campfire) {
        Random random = world.random;
        int i;

        if (random.nextFloat() < 0.11F) {
            for(i = 0; i < random.nextInt(2) + 2; ++i) {
                PrimordialVentBlock.spawnSmokeParticle(world, pos, false);
            }
        }

    }

    public static void litServerTick(World world, BlockPos pos, BlockState state, PrimordialVentEntity campfire) {


    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    @Override
    public void clear() {

    }
}
