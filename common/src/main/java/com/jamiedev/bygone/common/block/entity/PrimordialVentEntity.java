package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.common.block.PrimordialVentBlock;
import com.jamiedev.bygone.core.registry.BGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PrimordialVentEntity  extends BlockEntity implements Clearable
{

    CampfireBlockEntity ref;

    FurnaceBlock ref2;

    public PrimordialVentEntity(BlockPos pos, BlockState state) {
        super(BGBlockEntities.PRIMORDIAL_VENT, pos, state);
    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, PrimordialVentEntity campfire) {
        RandomSource random = world.random;
        int i;

        if (random.nextFloat() < 0.11F) {
            for(i = 0; i < random.nextInt(2) + 2; ++i) {
                PrimordialVentBlock.spawnSmokeParticle(world, pos, false);
            }
        }

    }

    public static void litServerTick(Level world, BlockPos pos, BlockState state, PrimordialVentEntity campfire) {


    }

    private void updateListeners() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void clearContent() {

    }
}
