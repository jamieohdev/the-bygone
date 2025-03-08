package com.jamiedev.bygone.common.worldgen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.WeepingVinesFeature;
import org.jetbrains.annotations.NotNull;

public class AncientFungusFeature  extends Feature<HugeFungusConfiguration> {
    private static final float field_31507 = 0.06F;

    public AncientFungusFeature(Codec<HugeFungusConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HugeFungusConfiguration> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        HugeFungusConfiguration hugeFungusFeatureConfig = context.config();
        Block block = hugeFungusFeatureConfig.validBaseState.getBlock();
        BlockPos blockPos2 = null;
        BlockState blockState = structureWorldAccess.getBlockState(blockPos.above());
        if (blockState.is(block)) {
            blockPos2 = blockPos;
        }

        if (blockPos2 == null) {
            return false;
        } else {
            int i = Mth.nextInt(random, 4, 13);
            if (random.nextInt(12) == 0) {
                i *= 2;
            }

            if (!hugeFungusFeatureConfig.planted) {
                int j = chunkGenerator.getGenDepth();
                if (blockPos2.getY() + i + 1 >= j) {
                    return false;
                }
            }

            boolean bl = !hugeFungusFeatureConfig.planted && random.nextFloat() < 0.06F;
            structureWorldAccess.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 4);
            this.generateStem(structureWorldAccess, random, hugeFungusFeatureConfig, blockPos2, i, bl);
            this.generateHat(structureWorldAccess, random, hugeFungusFeatureConfig, blockPos2, i, bl);
            return true;
        }
    }

    private static boolean isReplaceable(WorldGenLevel world, BlockPos pos, HugeFungusConfiguration config, boolean checkConfig) {
        if (world.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::canBeReplaced)) {
            return true;
        } else {
            return checkConfig && config.replaceableBlocks.test(world, pos);
        }
    }

    private void generateStem(WorldGenLevel world, @NotNull RandomSource random, HugeFungusConfiguration config, BlockPos pos, int stemHeight, boolean thickStem) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockState blockState = config.stemState;
        int i = thickStem ? 1 : 0;

        for(int j = -i; j <= i; ++j) {
            for(int k = -i; k <= i; ++k) {
                boolean bl = thickStem && Mth.abs(j) == i && Mth.abs(k) == i;

                for(int l = 0; l < stemHeight; ++l) {
                    mutable.setWithOffset(pos, j, l, k);
                    if (isReplaceable(world, mutable, config, true)) {
                        if (config.planted) {
                            if (!world.getBlockState(mutable.above()).isAir()) {
                                world.destroyBlock(mutable, true);
                            }

                            world.setBlock(mutable, blockState, 3);
                        } else if (bl) {
                            if (random.nextFloat() < 0.1F) {
                                this.setBlock(world, mutable, blockState);
                            }
                        } else {
                            this.setBlock(world, mutable, blockState);
                        }
                    }
                }
            }
        }

    }

    private void generateHat(WorldGenLevel world, @NotNull RandomSource random, HugeFungusConfiguration config, BlockPos pos, int hatHeight, boolean thickStem) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        boolean bl = config.hatState.is(Blocks.JUNGLE_LEAVES);
        int i = Math.min(random.nextInt(1 + hatHeight / 3) + 5, hatHeight);
        int j = hatHeight - i;

        for(int k = j; k <= hatHeight; ++k) {
            int l = k < hatHeight - random.nextInt(3) ? 2 : 1;
            if (i > 8 && k < j + 4) {
                l = 3;
            }

            if (thickStem) {
                ++l;
            }

            for(int m = -l; m <= l; ++m) {
                for(int n = -l; n <= l; ++n) {
                    boolean bl2 = m == -l || m == l;
                    boolean bl3 = n == -l || n == l;
                    boolean bl4 = !bl2 && !bl3 && k != hatHeight;
                    boolean bl5 = bl2 && bl3;
                    boolean bl6 = k < j + 3;
                    mutable.setWithOffset(pos, m, k, n);
                    if (isReplaceable(world, mutable, config, false)) {
                        if (config.planted && !world.getBlockState(mutable.above()).isAir()) {
                            world.destroyBlock(mutable, true);
                        }

                        if (bl6) {
                            if (!bl4) {
                                this.placeWithOptionalVines(world, random, mutable, config.hatState, bl);
                            }
                        } else if (bl4) {
                            this.placeHatBlock(world, random, config, mutable, 0.1F, 0.2F, bl ? 0.1F : 0.0F);
                        } else if (bl5) {
                            this.placeHatBlock(world, random, config, mutable, 0.01F, 0.7F, bl ? 0.083F : 0.0F);
                        } else {
                            this.placeHatBlock(world, random, config, mutable, 5.0E-4F, 0.98F, bl ? 0.07F : 0.0F);
                        }
                    }
                }
            }
        }

    }

    private void placeHatBlock(LevelAccessor world, @NotNull RandomSource random, HugeFungusConfiguration config, BlockPos.MutableBlockPos pos, float decorationChance, float generationChance, float vineChance) {
        if (random.nextFloat() < decorationChance) {
            this.setBlock(world, pos, config.decorState);
        } else if (random.nextFloat() < generationChance) {
            this.setBlock(world, pos, config.hatState);
            if (random.nextFloat() < vineChance) {
                generateVines(pos, world, random);
            }
        }

    }

    private void placeWithOptionalVines(LevelAccessor world, @NotNull RandomSource random, BlockPos pos, BlockState state, boolean vines) {
        if (world.getBlockState(pos.above()).is(state.getBlock())) {
            this.setBlock(world, pos, state);
        } else if ((double)random.nextFloat() < 0.15) {
            this.setBlock(world, pos, state);
            if (vines && random.nextInt(11) == 0) {
                generateVines(pos, world, random);
            }
        }

    }

    private static void generateVines(BlockPos pos, LevelAccessor world, @NotNull RandomSource random) {
        BlockPos.MutableBlockPos mutable = pos.mutable().move(Direction.UP);
        if (world.isEmptyBlock(mutable)) {
            int i = Mth.nextInt(random, 1, 5);
            if (random.nextInt(7) == 0) {
                i *= 2;
            }

            WeepingVinesFeature.placeWeepingVinesColumn(world, random, mutable, i, 23, 25);
        }
    }
}