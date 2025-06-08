package com.jamiedev.bygone.common.block.gourds;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GourdVineBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<GourdVineBlock> CODEC = simpleCodec(GourdVineBlock::new);
    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    private final double growPerTickProbability;
    public static final IntegerProperty GOURD_TYPE;

    @Override
    public MapCodec<GourdVineBlock> codec() {
        return CODEC;
    }

    public GourdVineBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState(this.getStateDefinition().any().setValue(GOURD_TYPE, 0));
        this.growPerTickProbability = 0.1;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(GOURD_TYPE);
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return NetherVines.getBlocksToGrowWhenBonemealed(random);
    }

    @Override
    protected Block getBodyBlock() {
        return BGBlocks.GOURD_VINE.get();
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return NetherVines.isValidGrowthState(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Going to keep this for now just in case
        /*if (state.getValue(AGE) < 25 && random.nextDouble() < this.growPerTickProbability) {
            if (random.nextInt(0, 6) == 1){
                BlockPos blockpos = pos.relative(this.growthDirection);
                *//*if (this.canGrowInto(level.getBlockState(blockpos))) {
                   Set<Block> gourds = Set.of(
                            BGBlocks.GOURD_LANTERN_BEIGE.get(),
                            BGBlocks.GOURD_LANTERN_MUAVE.get(),
                            BGBlocks.GOURD_LANTERN_VERDANT.get()
                   );
                   List<Block> gourdsList = new ArrayList<>(gourds);
                   level.setBlockAndUpdate(blockpos, gourdsList.get(random.nextInt(gourdsList.size())).defaultBlockState());
                   //level.setBlockAndUpdate(blockpos, BGBlocks.GOURD_LANTERN_BEIGE.get().defaultBlockState());
                }*//*
            }
            else {
                BlockPos blockpos = pos.relative(this.growthDirection);
                if (this.canGrowInto(level.getBlockState(blockpos))) {
                    level.setBlockAndUpdate(blockpos, this.getGrowIntoState(state, level.random));
                }
            }
        }*/
        if (random.nextDouble() < this.growPerTickProbability) {
            if (random.nextInt(0, 6) == 1){
                BlockPos blockpos = pos.relative(this.growthDirection);
                if (this.canGrowInto(level.getBlockState(blockpos))) {
                    Set<Block> gourds = Set.of(
                            BGBlocks.GOURD_LANTERN_BEIGE.get(),
                            BGBlocks.GOURD_LANTERN_MUAVE.get(),
                            BGBlocks.GOURD_LANTERN_VERDANT.get()
                    );
                    List<Block> gourdsList = new ArrayList<>(gourds);
                    level.setBlockAndUpdate(blockpos, gourdsList.get(state.getValue(GOURD_TYPE)).defaultBlockState().setValue(GourdLanternBlock.GROW_VINE, false));
                }
            }
        }
    }

    /*@Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockpos = hit.getBlockPos();
            if (projectile.mayInteract(level, blockpos)
                    && projectile.mayBreak(level)
                    && projectile.getDeltaMovement().length() > 0.6) {
                level.destroyBlock(blockpos, false);
            }
        }
    }*/

    static {
        GOURD_TYPE = IntegerProperty.create("gourd_type", 0, 2);
    }
}
