package com.jamiedev.bygone.common.blocks;

import com.jamiedev.bygone.common.entities.RisingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;

public interface LandingBlock2 extends Fallable {
    default void onLanding(Level world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, RisingBlockEntity fallingBlockEntity) {
    }

    default void onDestroyedOnLanding(Level world, BlockPos pos, RisingBlockEntity fallingBlockEntity) {
    }

    default DamageSource getFallDamageSource(Entity attacker) {
        return attacker.damageSources().fallingBlock(attacker);
    }
}
