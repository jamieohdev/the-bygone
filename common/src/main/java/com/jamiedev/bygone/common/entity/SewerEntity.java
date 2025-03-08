package com.jamiedev.bygone.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class SewerEntity  extends Monster
{
    PolarBear ref;


    protected SewerEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    public static boolean canSpawn(EntityType<SewerEntity> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, @NotNull RandomSource random) {
        return world.getBlockState(pos.below()).is(Blocks.WATER) || world.getBlockState(pos.below()).is(Blocks.MOSS_BLOCK) || world.getBlockState(pos.below()).is(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
    }
}
