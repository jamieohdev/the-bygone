package com.jamiedev.bygone.common.block.shelf;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ShelfMushroomBlock extends Block {
    public static final MapCodec<ShelfMushroomBlock> CODEC = simpleCodec(ShelfMushroomBlock::new);
    public int variation;

    public ShelfMushroomBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.variation = -1;
    }

    public ShelfMushroomBlock(BlockBehaviour.Properties settings, int variation) {
        super(settings);
        this.variation = variation;
    }

    @Override
    public @NotNull MapCodec<ShelfMushroomBlock> codec() {
        return CODEC;
    }

    @Override
    public void stepOn(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, Entity entity) {
        double velocity = Math.abs(entity.getDeltaMovement().y);
        if (velocity < 0.1 && !entity.isSteppingCarefully()) {
            double scalingFactor = 0.4 + velocity * 0.2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(scalingFactor, 1.0, scalingFactor));
        }

        super.stepOn(world, pos, state, entity);
    }

    @Override
    public void fallOn(@NotNull Level world, @NotNull BlockState state, @NotNull BlockPos pos, Entity entity, float fallDistance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(world, state, pos, entity, fallDistance);
        } else {
            entity.causeFallDamage(fallDistance, 0.0F, world.damageSources().fall());
        }

    }

    @Override
    public void updateEntityAfterFallOn(@NotNull BlockGetter world, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(world, entity);
        } else {
            this.bounce(entity);
        }

    }

    private void bounce(Entity entity) {
        Vec3 vec3d = entity.getDeltaMovement();
        if (vec3d.y < 0.0) {
            double d = 1.0;
            double s = 0;
            if (this.variation == 0) {
                // orange
                d = entity instanceof LivingEntity ? 1.0 : 0.8;
                s = 1;
            } else if (this.variation == 1) {
                // pink
                d = entity instanceof LivingEntity ? 1.0 : 0.5;
                s = 0.6;
            } else if (this.variation == 2) {
                // purple
                d = entity instanceof LivingEntity ? 1.0 : 0.1;
                s = 1.5;
            }
            entity.setDeltaMovement(vec3d.x, -vec3d.y * d * s, vec3d.z);
        }

    }
}
