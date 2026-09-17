package com.jamiedev.bygone.common.entity.ai.goal;

import com.jamiedev.bygone.common.entity.GeistEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class GeistGotoLightGoal extends MoveToBlockGoal {

    protected final GeistEntity geist;

    public GeistGotoLightGoal(GeistEntity geist, double speedModifier, int searchRange) {
        super(geist, speedModifier, searchRange);
        this.geist = geist;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public void start() {
        super.start();
        this.geist.setAggressive(false);
        this.geist.setTarget(null);
    }

    @Override
    public void tick() {
        super.tick();
        this.geist.getLookControl().setLookAt(
            this.blockPos.getX() + 0.5F,
            this.blockPos.getY() + 1,
            this.blockPos.getZ() + 0.5F,
            10,
            (float)this.geist.getMaxHeadXRot()
        );
        if (this.isReachedTarget()) {
            this.nextStartTick = 200;
            this.stop();
        }
    }
    
    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (this.geist.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 4) return false;

        BlockState state = level.getBlockState(pos);
        int lightLevel = state.getLightEmission();
        int threshold = this.geist.getLightThreshold();
        boolean aboveThreshold = lightLevel > threshold || this.geist.isOnFire();
        return level.isEmptyBlock(pos.above()) && aboveThreshold;
    }

}
