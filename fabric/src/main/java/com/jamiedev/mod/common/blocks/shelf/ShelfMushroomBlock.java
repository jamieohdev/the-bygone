package com.jamiedev.mod.common.blocks.shelf;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;

public class ShelfMushroomBlock extends Block
{
    public int variation;
    BedBlock ref;

    public static final MapCodec<ShelfMushroomBlock> CODEC = simpleCodec((Function<Properties, ShelfMushroomBlock>) ShelfMushroomBlock::new);

    public ShelfMushroomBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    public MapCodec<ShelfMushroomBlock> codec() {
        return CODEC;
    }

    public ShelfMushroomBlock(BlockBehaviour.Properties settings, int variation) {
        super(settings);
        this.variation = variation;
    }


    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(world, state, pos, entity, fallDistance);
        } else {
            entity.causeFallDamage(fallDistance, 0.0F, world.damageSources().fall());
        }

    }

    public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(world, entity);
        } else {
            this.bounce(entity);
        }

    }

    private void bounceEntity(Entity entity) {
        Vec3 vec3d = entity.getDeltaMovement();
        if (vec3d.y < 0.0) {
            double d = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vec3d.x, -vec3d.y * 0.6600000262260437 * d, vec3d.z);
        }

    }

    private void bounce(Entity entity) {
        if (variation == 1) // pink
        {
            Vec3 vec3d = entity.getDeltaMovement();
            if (vec3d.y < 0.0) {
                double d = entity instanceof LivingEntity ? 1.0 : 0.5;
                entity.setDeltaMovement(vec3d.x, -vec3d.y * d * 0.6, vec3d.z);
            }
        }

        if (variation == 2) // purple
        {
            Vec3 vec3d = entity.getDeltaMovement();
            if (vec3d.y < 0.0) {
                double d = entity instanceof LivingEntity ? 1.0 : 0.1;
                entity.setDeltaMovement(vec3d.x, -vec3d.y * d * 1.5, vec3d.z);
            }
        }

        else // orange
        {
            Vec3 vec3d = entity.getDeltaMovement();
            if (vec3d.y < 0.0) {
                double d = entity instanceof LivingEntity ? 1.0 : 0.8;
                entity.setDeltaMovement(vec3d.x, -vec3d.y * d, vec3d.z);
            }
        }
    }

    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        double d = Math.abs(entity.getDeltaMovement().y);
        if (d < 0.1 && !entity.isSteppingCarefully()) {
            double e = 0.4 + d * 0.2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(e, 1.0, e));
        }

        super.stepOn(world, pos, state, entity);
    }
}
