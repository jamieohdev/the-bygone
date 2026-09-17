package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;
import com.jamiedev.bygone.core.registry.BGBlockEntities;
import com.jamiedev.bygone.core.registry.BGDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

public class LithineLampBlockEntity extends BlockEntity {
    public LithineLampBlockEntity(BlockPos pos, BlockState blockState) {
        super(BGBlockEntities.LITHINE_LAMP.get(), pos, blockState);
    }

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static void tick(Level level, BlockPos pos, BlockState state, LithineLampBlockEntity blockEntity) {
        AABB boundingBox = new AABB(pos).inflate(15);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, boundingBox);
        if (entities.size() > 1) entities.sort(
            Comparator.comparingDouble(e ->
                e.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())
            ));
        if (!entities.isEmpty()) {
            int lastDistance = (int) Math.floor(pos.getCenter()
                .distanceTo(entities.getFirst().position()));
            lastDistance = Math.clamp(lastDistance - 1, 0, 15);
            level.setBlock(pos, state.setValue(POWER, 15 - lastDistance), 10);
        } else level.setBlock(pos, state.setValue(POWER, 0), 10);

        // going for more of a strobe here than a flicker because the flicker was bad
        if (level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)
            && HauntingsCategoryHolder.checkHauntingsActive(level)) {
            float timeScale = 1.5f;
            long flooredTicks = level.getGameTime() % (int) (40 * timeScale);
            level.setBlock(pos, state.setValue(POWER, Math.max(level.getBlockState(pos)
                .getValue(POWER) - (int) ((Math.sin((flooredTicks / (20f * timeScale))
                    * (2f * Math.PI)) / 2f + 0.5) * (3f - .01f)), 0)), 10
            );
        }
    }
}
