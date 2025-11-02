package com.jamiedev.bygone.common.entity.ai;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

import java.util.EnumSet;
import java.util.function.Predicate;

public class EatCropGoal extends Goal {
    private static final int EAT_ANIMATION_TICKS = 40;
    private static final Predicate<BlockState> IS_AMARANTH, IS_OTHER_CROP;

    static {
        IS_AMARANTH = BlockStatePredicate.forBlock(BGBlocks.AMARANTH_CROP.get());
        IS_OTHER_CROP = BlockStatePredicate.forBlock(Blocks.BEETROOTS);
    }

    private final Mob mob;
    private final Level level;
    private int eatAnimationTick;

    public EatCropGoal(Mob mob) {
        this.mob = mob;
        this.level = mob.level();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public boolean canUse() {
        BlockPos blockpos = this.mob.blockPosition();
        return IS_OTHER_CROP.test(this.level.getBlockState(blockpos)) || IS_AMARANTH.test(this.level.getBlockState(blockpos)) || this.level.getBlockState(blockpos.below()).is(BGBlocks.MOSSY_CLAYSTONE.get()) || this.level.getBlockState(blockpos.below()).is(BGBlocks.CLAYSTONE_FARMLAND.get());

    }

    public void start() {
        this.eatAnimationTick = this.adjustedTickDelay(40);
        this.level.broadcastEntityEvent(this.mob, (byte) 10);
        this.mob.getNavigation().stop();
    }

    public void stop() {
        this.eatAnimationTick = 0;
    }

    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0;
    }

    public int getEatAnimationTick() {
        return this.eatAnimationTick;
    }

    public void tick() {
        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
            BlockPos blockpos = this.mob.blockPosition();
            if (IS_AMARANTH.test(this.level.getBlockState(blockpos))) {
                if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level.destroyBlock(blockpos, false);
                }

                this.mob.ate();
            } else if (IS_OTHER_CROP.test(this.level.getBlockState(blockpos))) {
                if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level.destroyBlock(blockpos, false);
                }

                this.mob.ate();
            } else {
                BlockPos blockpos1 = blockpos.below();
                if (this.level.getBlockState(blockpos1).is(BGBlocks.MOSSY_CLAYSTONE.get())) {
                    if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.level.levelEvent(2001, blockpos1, Block.getId(BGBlocks.MOSSY_CLAYSTONE.get().defaultBlockState()));
                        this.level.setBlock(blockpos1, BGBlocks.CLAYSTONE.get().defaultBlockState(), 2);
                    }

                    this.mob.ate();
                }
                if (this.level.getBlockState(blockpos1).is(BGBlocks.CLAYSTONE_FARMLAND.get())) {
                    if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.level.levelEvent(2001, blockpos1, Block.getId(BGBlocks.CLAYSTONE_FARMLAND.get().defaultBlockState()));
                        this.level.setBlock(blockpos1, BGBlocks.CLAYSTONE.get().defaultBlockState(), 2);
                    }

                    this.mob.ate();
                }
            }
        }

    }
}
