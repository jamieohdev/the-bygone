package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.common.entities.RisingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LandingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface LandingBlock2 extends LandingBlock {
    default void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, RisingBlockEntity fallingBlockEntity) {
    }

    default void onDestroyedOnLanding(World world, BlockPos pos, RisingBlockEntity fallingBlockEntity) {
    }

    default DamageSource getDamageSource(Entity attacker) {
        return attacker.getDamageSources().fallingBlock(attacker);
    }
}
