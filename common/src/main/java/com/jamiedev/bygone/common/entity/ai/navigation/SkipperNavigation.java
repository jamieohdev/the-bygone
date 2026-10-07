package com.jamiedev.bygone.common.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class SkipperNavigation extends GroundPathNavigation {
    public SkipperNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new WalkNodeEvaluator() {
            @Override
            public PathType getPathType(PathfindingContext pathfindingContext, int x, int y, int z) {
                BlockPos blockPos = new BlockPos(x, y, z);
                PathType pathType = super.getPathType(pathfindingContext, x, y, z);
                if (pathType == PathType.WATER && pathfindingContext.getBlockState(blockPos.above()).isAir()) {
                    return PathType.WALKABLE;
                }
                if (pathType == PathType.OPEN && pathfindingContext.level().getFluidState(blockPos.below()).is(FluidTags.WATER)) {
                    return PathType.WALKABLE;
                }
                return pathType;
            }

            @Override
            protected double getFloorLevel(BlockPos blockPos) {
                if (this.currentContext.level().getFluidState(blockPos).is(FluidTags.WATER)) {
                    return blockPos.getY() + 8.0 / 9.0;
                }
                if (this.currentContext.level().getFluidState(blockPos.below()).is(FluidTags.WATER)) {
                    return blockPos.getY() - 1.0 / 9.0;
                }
                return super.getFloorLevel(blockPos);
            }
        };
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.mob.isInWater() || super.canUpdatePath();
    }

    @Override
    protected Vec3 getTempMobPos() {
        return this.mob.position();
    }

    @Override
    protected double getGroundY(Vec3 targetPosition) {
        BlockPos blockPos = BlockPos.containing(targetPosition);
        if (this.level.getFluidState(blockPos).is(FluidTags.WATER)) {
            return blockPos.getY() + 8.0 / 9.0;
        }
        if (this.level.getFluidState(blockPos.below()).is(FluidTags.WATER)) {
            return blockPos.getY() - 1.0 / 9.0;
        }
        return super.getGroundY(targetPosition);
    }

    @Override
    public boolean isStableDestination(BlockPos blockPos) {
        return this.level.getFluidState(blockPos.below()).is(FluidTags.WATER) || super.isStableDestination(blockPos);
    }
}
