package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Iterator;

public class ChantrelleCropBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<ChantrelleCropBlock> CODEC = simpleCodec(ChantrelleCropBlock::new);
    public static final IntegerProperty AGE;
    private static final VoxelShape[] SHAPE_BY_AGE;


    public MapCodec<? extends ChantrelleCropBlock> codec() {
        return CODEC;
    }

    public ChantrelleCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.getAgeProperty(), 0));
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BGBlocks.SHELF_MYCELIUM.get()) || state.is(BGBlocks.CLAYSTONE_FARMLAND.get());
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 3;
    }

    public int getAge(BlockState state) {
        return (Integer)state.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int age) {
        return (BlockState)this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }

    public boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= this.getMaxAge();
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state);
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) <= 12) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, level, pos);
                if (random.nextInt((int)(105.0F / f) + 1) == 0) {
                    level.setBlock(pos, this.getStateForAge(i + 1), 2);
                }
            }
        }

        if (isCropsNearby(level, pos))
        {
            if (level.random.nextInt(2) == 1) {
                for(int i = 1; i <= 2; ++i) {

                    for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-8, -2, -8), pos.offset(8, 2, 8))) {
                        BlockState blockState = level.getBlockState(blockPos);
                        Block block = blockState.getBlock();
                        if (block instanceof AmaranthCropBlock cropBlock) {
                            if (level.random.nextFloat() <= 0.3 && !cropBlock.isMaxAge(blockState)) {
                                if (level instanceof ServerLevel) {
                                    if (cropBlock.isBonemealSuccess(level, level.random, blockPos, blockState)) {
                                        cropBlock.performBonemeal((ServerLevel)level, level.random, blockPos, blockState);
                                        level.levelEvent(1505, blockPos, 15);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private static boolean isCropsNearby(LevelReader world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.betweenClosed(pos.offset(-8, 0, -8), pos.offset(15, 1, 15)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = var2.next();
        } while(world.getBlockState(blockPos).is(BlockTags.CROPS));

        return true;
    }

    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(level);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        level.setBlock(pos, this.getStateForAge(i), 2);


    }

    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 0, 1);
    }

    protected static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockpos = pos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                BlockState blockstate = level.getBlockState(blockpos.offset(i, 0, j));
                if (blockstate.is(BGBlocks.SHELF_MYCELIUM.get())) {
                    f1 = 2.0F;


                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = level.getBlockState(blockpos3).is(block) || level.getBlockState(blockpos4).is(block);
        boolean flag1 = level.getBlockState(blockpos1).is(block) || level.getBlockState(blockpos2).is(block);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = level.getBlockState(blockpos3.north()).is(block) || level.getBlockState(blockpos4.north()).is(block) || level.getBlockState(blockpos4.south()).is(block) || level.getBlockState(blockpos3.south()).is(block);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return hasSufficientLight(level, pos) && super.canSurvive(state, level, pos);
    }

    protected static boolean hasSufficientLight(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) <= 11;
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            level.destroyBlock(pos, true, entity);
        }

        super.entityInside(state, level, pos, entity);
    }

    protected ItemLike getBaseSeedId() {
        return BGItems.CHANTRELLE_SEEDS.get();
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getBaseSeedId());
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !this.isMaxAge(state);
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.growCrops(level, pos, state);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    static {
        AGE = BlockStateProperties.AGE_3;
        SHAPE_BY_AGE = new VoxelShape[]{
                Block.box((double)5.0F, (double)0.0F, (double)5.0F, (double)11.0F, (double)7.0F, (double)11.0F),
                Block.box((double)5.0F, (double)0.0F, (double)5.0F, (double)11.0F, (double)8.0F, (double)11.0F),
                Block.box((double)5.0F, (double)0.0F, (double)5.0F, (double)11.0F, (double)9.0F, (double)11.0F),
                Block.box((double)5.0F, (double)0.0F, (double)5.0F, (double)11.0F, (double)10.0F, (double)11.0F),};
    }
}
