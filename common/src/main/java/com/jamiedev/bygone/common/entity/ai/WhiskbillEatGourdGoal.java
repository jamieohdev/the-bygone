package com.jamiedev.bygone.common.entity.ai;

import com.jamiedev.bygone.common.block.gourds.GourdLanternBlock;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WhiskbillEatGourdGoal extends MoveToBlockGoal {

    private static final Map<Block, Item> blocksToRemove;
    private static final int WAIT_AFTER_BLOCK_FOUND = 20;

    static {
        Map<Block, Item> blockItemMap = new HashMap<>();
        blockItemMap.put(BGBlocks.GOURD_LANTERN_BEIGE.get(), BGItems.BEIGE_GOURD_SEEDS.get());
        blockItemMap.put(BGBlocks.GOURD_LANTERN_VERDANT.get(), BGItems.VERDANT_GOURD_SEEDS.get());
        blockItemMap.put(BGBlocks.GOURD_LANTERN_MUAVE.get(), BGItems.MUAVE_GOURD_SEEDS.get());

        blocksToRemove = Collections.unmodifiableMap(blockItemMap);
    }

    private final Mob removerMob;
    private final int minAmount = 1;
    private final int maxAmount = 1;
    private int ticksSinceReachedGoal;
    private int ticksSinceStartedGoal;
    private int ticksWaitForGourd;
    private Item dropItem = Items.AIR;
    private Block blockToRemove;

    public WhiskbillEatGourdGoal(PathfinderMob mob, double speedModifier, int searchRange, int verticalSearchRange) {
        super(mob, speedModifier, searchRange, verticalSearchRange);
        this.removerMob = mob;
    }

    @Override
    protected @NotNull BlockPos getMoveToTarget() {
        return this.blockPos;
    }

    public boolean canUse() {
        if (!this.removerMob.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else if (this.findNearestBlock()) {
            this.nextStartTick = reducedTickDelay(3600);
            return true;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            return false;
        }
    }

    public void stop() {
        super.stop();
        this.removerMob.fallDistance = 1.0F;
    }

    public void start() {
        super.start();
        this.ticksSinceReachedGoal = 0;
        this.ticksSinceStartedGoal = 0;
        this.ticksWaitForGourd = 0;
    }

    public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
    }

    public void playBreakSound(Level level, BlockPos pos) {
    }


    public void tick() {
        super.tick();
        Level level = this.removerMob.level();
        BlockPos blockpos = this.removerMob.blockPosition();
        BlockPos blockpos1 = this.blockPos;
        RandomSource randomsource = this.removerMob.getRandom();
        if (this.getMoveToTarget().distToCenterSqr(blockpos.getX(), blockpos.getY(), blockpos.getZ()) < 8.2F) {
            mob.getNavigation().stop();

        }

        /*if (blockpos1 == null) {
            for (BlockPos pos : BlockPos.betweenClosed(blockpos.offset(-2, -1, -2), blockpos.offset(2, 1, 2))) {
                if (level.getBlockState(pos).is(this.blockToRemove)) {
                    blockpos1 = pos;
                    break;
                }
            }
        }*/

        if (this.isReachedTarget() && blockpos1 != null) {
            Vec3 starter = this.removerMob.getEyePosition();
            Vec3 ender = blockpos1.getCenter().subtract(starter);
            this.removerMob.getLookControl().setLookAt(ender);
            if (this.ticksSinceReachedGoal > 0) {
                Vec3 vec3 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement(vec3.x, 0.3, vec3.z);
                if (!level.isClientSide) {
                    double d0 = 0.08;
                    ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(this.blockToRemove.asItem())), (double) blockpos1.getX() + (double) 0.5F, (double) blockpos1.getY() + 0.7, (double) blockpos1.getZ() + (double) 0.5F, 3, ((double) randomsource.nextFloat() - (double) 0.5F) * 0.08, ((double) randomsource.nextFloat() - (double) 0.5F) * 0.08, ((double) randomsource.nextFloat() - (double) 0.5F) * 0.08, 0.15F);
                }
            }

            if (this.ticksSinceReachedGoal % 2 == 0) {
                Vec3 vec31 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement(vec31.x, -0.3, vec31.z);
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    this.playDestroyProgressSound(level, this.blockPos);
                }
            }

            if (this.ticksSinceReachedGoal > 60) {
                level.removeBlock(blockpos1, false);
                if (!level.isClientSide) {
                    for (int i = 0; i < 20; ++i) {
                        double d3 = randomsource.nextGaussian() * 0.02;
                        double d1 = randomsource.nextGaussian() * 0.02;
                        double d2 = randomsource.nextGaussian() * 0.02;
                        ((ServerLevel) level).sendParticles(ParticleTypes.POOF, (double) blockpos1.getX() + (double) 0.5F, blockpos1.getY(), (double) blockpos1.getZ() + (double) 0.5F, 1, d3, d1, d2, 0.15F);
                    }
                    if (this.dropItem != null) {
                        int randomInt = this.removerMob.getRandom().nextInt(Math.max(this.minAmount, 1), Math.max(this.maxAmount, this.minAmount + 1));
                        for (int i = 0; i < randomInt; i++) {
                            this.removerMob.spawnAtLocation(this.dropItem);
                        }
                    }

                    this.playBreakSound(level, blockpos1);
                }
            }

            ++this.ticksSinceReachedGoal;
        }

        if ((!this.isReachedTarget() && this.ticksSinceStartedGoal > 220) ||
                (!this.isReachedTarget() && blockpos1.getY() > blockpos.getY() + 1 &&
                        Math.abs(blockpos.getX() - blockpos1.getX()) < 4 && Math.abs(blockpos.getZ() - blockpos1.getZ()) < 4)) {

            if (this.ticksWaitForGourd > 65) {
                if (!level.isClientSide) {
                    Vec3 starter = this.removerMob.getEyePosition();
                    Vec3 ender = blockpos1.getCenter().subtract(starter);
                    Vec3 normVec3 = ender.normalize();
                    int disLength = Mth.floor(ender.length());

                    for (int i = 1; i < disLength; i++) {
                        Vec3 placeVec3 = starter.add(normVec3.scale(i));
                        for (int j = 0; j < 4; j++) {
                            double d3 = randomsource.nextGaussian() * 0.02;
                            double d1 = randomsource.nextGaussian() * 0.02;
                            double d2 = randomsource.nextGaussian() * 0.02;
                            ((ServerLevel) level).sendParticles(ParticleTypes.SMALL_GUST, placeVec3.x, placeVec3.y, placeVec3.z, 1, d3, d1, d2, 0.15);
                        }
                    }
                }
                level.playSound(this.removerMob, blockpos, BGSoundEvents.WHISKBILL_ROAR_ADDITIONS_EVENT, SoundSource.HOSTILE, 0.9F, 1.5F);
                level.playSound(this.removerMob, blockpos, BGSoundEvents.WHISKBILL_ROAR_ADDITIONS_EVENT, SoundSource.HOSTILE, 0.05F, 1.0F);
                BlockState gourdState = level.getBlockState(blockpos1);
                FallingBlockEntity fallingGourd = FallingBlockEntity.fall(level, blockpos1, gourdState);
                level.addFreshEntity(fallingGourd);
                this.nextStartTick = reducedTickDelay(60);
                this.stop();
            }
            ++this.ticksWaitForGourd;
        }
        ++this.ticksSinceStartedGoal;

    }

    public Item getDropItem() {
        return this.dropItem;
    }

    public void setDropItem(Item item) {
        this.dropItem = item;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        ChunkAccess chunkaccess = level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
        //return chunkaccess == null ? false : chunkaccess.getBlockState(pos).is(this.blockToRemove) && chunkaccess.getBlockState(pos.above()).isAir() && chunkaccess.getBlockState(pos.above(2)).isAir();
        if (chunkaccess != null && blocksToRemove.containsKey(chunkaccess.getBlockState(pos).getBlock())) {
            Block tempBlock = chunkaccess.getBlockState(pos).getBlock();
            this.blockToRemove = tempBlock;
            if (!((ServerLevel) level).isClientSide && ((GourdLanternBlock) tempBlock).isFullyGrown(chunkaccess.getBlockState(pos))) {
                this.dropItem = blocksToRemove.getOrDefault(this.blockToRemove, Items.AIR);
                return true;
            }
        }
        return false;
        //return chunkaccess == null ? false : blocksToRemove.containsKey(chunkaccess.getBlockState(pos).getBlock());
    }

    /*@Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        ChunkAccess chunkaccess = level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
        //return chunkaccess == null ? false : chunkaccess.getBlockState(pos).is(this.blockToRemove) && chunkaccess.getBlockState(pos.above()).isAir() && chunkaccess.getBlockState(pos.above(2)).isAir();
        if (chunkaccess == null){
            return false;
        }
        for (Block block : this.blocksToRemove) {
            if (chunkaccess.getBlockState(pos).is(block)) {
                return true;
            }
        }
        return false;
    }*/
}
