package com.jamiedev.mod.common.entities;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class SewerEntity  extends HostileEntity
{
    PolarBearEntity ref;


    protected SewerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean canSpawn(EntityType<SewerEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isOf(Blocks.WATER) || world.getBlockState(pos.down()).isOf(Blocks.MOSS_BLOCK) || world.getBlockState(pos.down()).isOf(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
    }
}
