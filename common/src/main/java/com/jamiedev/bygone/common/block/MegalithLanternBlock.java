package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class MegalithLanternBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<MegalithLanternBlock> CODEC = simpleCodec(MegalithLanternBlock::new);
    public static final DirectionProperty FACING;
    @Nullable
    private BlockPattern snowGolemBase;
    @Nullable
    private BlockPattern snowGolemFull;
    @Nullable
    private BlockPattern ironGolemBase;
    @Nullable
    private BlockPattern ironGolemFull;
    private static final Predicate<BlockState> PUMPKINS_PREDICATE;

    public MapCodec<? extends MegalithLanternBlock> codec() {
        return CODEC;
    }

    public MegalithLanternBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnGolem(level, pos);
        }
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(500) == 0) {
            BlockState blockState1 = level.getBlockState(pos.above());
            BlockState blockState = level.getBlockState(pos.below());
            if (blockState1.is(JamiesModTag.MEGALITH_BLOCKS) || blockState.is(JamiesModTag.MEGALITH_BLOCKS) ||
            blockState1.is(BGBlocks.MEGALITH_BLOCK.get()) || blockState.is(BGBlocks.MEGALITH_BLOCK.get())
                    || blockState1.is(BGBlocks.CRACKED_MEGALITH_BLOCK.get())|| blockState.is(BGBlocks.CRACKED_MEGALITH_BLOCK.get())) {
                level.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), BGSoundEvents.BLOCK_MEGALITH_BLOCK_IDLE_ADDITIONS_EVENT, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
        }

    }

    public boolean canSpawnGolem(LevelReader level, BlockPos pos) {
        return this.getOrCreateSnowGolemBase().find(level, pos) != null || this.getOrCreateIronGolemBase().find(level, pos) != null;
    }

    private void trySpawnGolem(Level level, BlockPos pos) {
        BlockPattern.BlockPatternMatch blockPatternMatch = this.getOrCreateSnowGolemFull().find(level, pos);
        if (blockPatternMatch != null) {
            SnowGolem snowGolem = (SnowGolem) EntityType.SNOW_GOLEM.create(level);
            if (snowGolem != null) {
                spawnGolemInWorld(level, blockPatternMatch, snowGolem, blockPatternMatch.getBlock(0, 2, 0).getPos());
            }
        } else {
            BlockPattern.BlockPatternMatch blockPatternMatch2 = this.getOrCreateIronGolemFull().find(level, pos);
            if (blockPatternMatch2 != null) {
                IronGolem ironGolem = (IronGolem)EntityType.IRON_GOLEM.create(level);
                if (ironGolem != null) {
                    ironGolem.setPlayerCreated(true);
                    spawnGolemInWorld(level, blockPatternMatch2, ironGolem, blockPatternMatch2.getBlock(1, 2, 0).getPos());
                }
            }
        }

    }

    private static void spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch patternMatch, Entity golem, BlockPos pos) {
        clearPatternBlocks(level, patternMatch);
        golem.moveTo((double)pos.getX() + (double)0.5F, (double)pos.getY() + 0.05, (double)pos.getZ() + (double)0.5F, 0.0F, 0.0F);
        level.addFreshEntity(golem);

        for(ServerPlayer serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate((double)5.0F))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, golem);
        }

        updatePatternBlocks(level, patternMatch);
    }

    public static void clearPatternBlocks(Level level, BlockPattern.BlockPatternMatch patternMatch) {
        for(int i = 0; i < patternMatch.getWidth(); ++i) {
            for(int j = 0; j < patternMatch.getHeight(); ++j) {
                BlockInWorld blockInWorld = patternMatch.getBlock(i, j, 0);
                level.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                level.levelEvent(2001, blockInWorld.getPos(), Block.getId(blockInWorld.getState()));
            }
        }

    }

    public static void updatePatternBlocks(Level level, BlockPattern.BlockPatternMatch patternMatch) {
        for(int i = 0; i < patternMatch.getWidth(); ++i) {
            for(int j = 0; j < patternMatch.getHeight(); ++j) {
                BlockInWorld blockInWorld = patternMatch.getBlock(i, j, 0);
                level.blockUpdated(blockInWorld.getPos(), Blocks.AIR);
            }
        }

    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    private BlockPattern getOrCreateSnowGolemBase() {
        if (this.snowGolemBase == null) {
            this.snowGolemBase = BlockPatternBuilder.start().aisle(new String[]{" ", "#", "#"}).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }

        return this.snowGolemBase;
    }

    private BlockPattern getOrCreateSnowGolemFull() {
        if (this.snowGolemFull == null) {
            this.snowGolemFull = BlockPatternBuilder.start().aisle(new String[]{"^", "#", "#"}).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }

        return this.snowGolemFull;
    }

    private BlockPattern getOrCreateIronGolemBase() {
        if (this.ironGolemBase == null) {
            this.ironGolemBase = BlockPatternBuilder.start().aisle(new String[]{"~ ~", "###", "~#~"}).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', (blockInWorld) -> blockInWorld.getState().isAir()).build();
        }

        return this.ironGolemBase;
    }

    private BlockPattern getOrCreateIronGolemFull() {
        if (this.ironGolemFull == null) {
            this.ironGolemFull = BlockPatternBuilder.start().aisle(new String[]{"~^~", "###", "~#~"}).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', (blockInWorld) -> blockInWorld.getState().isAir()).build();
        }

        return this.ironGolemFull;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        PUMPKINS_PREDICATE = (blockState) -> blockState != null && (blockState.is(BGBlocks.MEGALITH_LANTERN.get()) || blockState.is(BGBlocks.MEGALITH_FACE.get()));
    }
}
