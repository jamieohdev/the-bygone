package com.jamiedev.mod.common.blocks.shelf;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.function.Function;

public class ShelfMushroomBlock extends Block
{
    public int variation;
    BedBlock ref;

    public static final MapCodec<ShelfMushroomBlock> CODEC = createCodec((Function<Settings, ShelfMushroomBlock>) ShelfMushroomBlock::new);

    public ShelfMushroomBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public MapCodec<ShelfMushroomBlock> getCodec() {
        return CODEC;
    }

    public ShelfMushroomBlock(AbstractBlock.Settings settings, int variation) {
        super(settings);
        this.variation = variation;
    }


    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.bypassesLandingEffects()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        } else {
            entity.handleFallDamage(fallDistance, 0.0F, world.getDamageSources().fall());
        }

    }

    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) {
            super.onEntityLand(world, entity);
        } else {
            this.bounce(entity);
        }

    }

    private void bounceEntity(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < 0.0) {
            double d = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setVelocity(vec3d.x, -vec3d.y * 0.6600000262260437 * d, vec3d.z);
        }

    }

    private void bounce(Entity entity) {
        if (variation == 1) // pink
        {
            Vec3d vec3d = entity.getVelocity();
            if (vec3d.y < 0.0) {
                double d = entity instanceof LivingEntity ? 1.0 : 0.5;
                entity.setVelocity(vec3d.x, -vec3d.y * d * 0.6, vec3d.z);
            }
        }

        if (variation == 2) // purple
        {
            Vec3d vec3d = entity.getVelocity();
            if (vec3d.y < 0.0) {
                double d = entity instanceof LivingEntity ? 1.0 : 0.1;
                entity.setVelocity(vec3d.x, -vec3d.y * d * 1.5, vec3d.z);
            }
        }

        else // orange
        {
            Vec3d vec3d = entity.getVelocity();
            if (vec3d.y < 0.0) {
                double d = entity instanceof LivingEntity ? 1.0 : 0.8;
                entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
            }
        }
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        double d = Math.abs(entity.getVelocity().y);
        if (d < 0.1 && !entity.bypassesSteppingEffects()) {
            double e = 0.4 + d * 0.2;
            entity.setVelocity(entity.getVelocity().multiply(e, 1.0, e));
        }

        super.onSteppedOn(world, pos, state, entity);
    }
}
