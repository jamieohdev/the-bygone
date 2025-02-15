package com.jamiedev.bygone.worldgen.feature;

import com.jamiedev.bygone.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import org.jetbrains.annotations.NotNull;

public class AmberFeature  extends Feature<BlockStateConfiguration> {
    public AmberFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        BlockPos blockPos;// = context.getOrigin();
        WorldGenLevel structureWorldAccess = context.level();
        blockPos = context.origin();//= new BlockPos(blockPos.getX(), context.getGenerator().getMinimumY(), blockPos.getZ());
        RandomSource random = context.random();
        boolean bl = random.nextDouble() > 0.7;
        BlockState blockState = context.config().state;
        double d = random.nextDouble() * 2.0 * Math.PI;
        int i = 11 - random.nextInt(5);
        int j = 3 + random.nextInt(3);
        boolean bl2 = random.nextDouble() > 0.7;
        int l = bl2 ? random.nextInt(6) + 6 : random.nextInt(15) + 3;
        if (!bl2 && random.nextDouble() > 0.9) {
            l += random.nextInt(19) + 7;
        }

        int m = Math.min(l + random.nextInt(11), 18);
        int n = Math.min(l + random.nextInt(7) - random.nextInt(5), 11);
        int o = bl2 ? i : 11;

        int p;
        int q;
        int r;
        int s;
        for(p = -o; p < o; ++p) {
            for(q = -o; q < o; ++q) {
                for(r = 0; r < l; ++r) {
                    s = bl2 ? this.method_13417(r, l, n) : this.method_13419(random, r, l, n);
                    if (bl2 || p < s) {
                        this.placeAt(structureWorldAccess, random, blockPos, l, p, r, q, s, o, bl2, j, d, bl, blockState);
                    }
                }
            }
        }

        this.method_13418(structureWorldAccess, blockPos, n, l, bl2, i);

        for(p = -o; p < o; ++p) {
            for(q = -o; q < o; ++q) {
                for(r = -1; r > -m; --r) {
                    s = bl2 ? Mth.ceil((float)o * (1.0F - (float)Math.pow(r, 2.0) / ((float)m * 16.0F))) : o;
                    int t = this.method_13427(random, -r, m, n);
                    if (p < t) {
                        this.placeAt(structureWorldAccess, random, blockPos, m, p, r , q , t -1, s, bl2, j, d, bl, JamiesModBlocks.FLOWING_AMBER.defaultBlockState());
                    }
                }
            }
        }

        boolean bl3 = bl2 ? random.nextDouble() > 0.1 : random.nextDouble() > 0.7;
        if (bl3) {
            this.method_13428(random, structureWorldAccess, n, l, blockPos, bl2, i, d, j);
        }

        return true;
    }

    private void method_13428(RandomSource random, LevelAccessor world, int i, int j, BlockPos pos, boolean bl, int k, double d, int l) {
        int m = random.nextBoolean() ? -1 : 1;
        int n = random.nextBoolean() ? -1 : 1;
        int o = random.nextInt(Math.max(i / 2 - 2, 1));
        if (random.nextBoolean()) {
            o = i / 2 + 1 - random.nextInt(Math.max(i - i / 2 - 1, 1));
        }

        int p = random.nextInt(Math.max(i / 2 - 2, 1));
        if (random.nextBoolean()) {
            p = i / 2 + 1 - random.nextInt(Math.max(i - i / 2 - 1, 1));
        }

        if (bl) {
            o = p = random.nextInt(Math.max(k - 5, 1));
        }

        BlockPos blockPos = new BlockPos(m * o, 0, n * p);
        double e = bl ? d + 1.5707963267948966 : random.nextDouble() * 2.0 * Math.PI;

        int q;
        int r;
        for(q = 0; q < j - 3; ++q) {
            r = this.method_13419(random, q, j, i);
            this.method_13415(r, q, pos, world, false, e, blockPos, k, l);
        }

        for(q = -1; q > -j + random.nextInt(5); --q) {
            r = this.method_13427(random, -q, j, i);
            this.method_13415(r, q, pos, world, true, e, blockPos, k, l);
        }

    }

    private void method_13415(int i, int y, BlockPos pos, LevelAccessor world, boolean placeWater, double d, BlockPos blockPos, int j, int k) {
        int l = i + 1 + j / 3;
        int m = Math.min(i - 3, 3) + k / 2 - 1;

        for(int n = -l; n < l; ++n) {
            for(int o = -l; o < l; ++o) {
                double e = this.getDistance(n, o, blockPos, l, m, d);
                if (e < 0.0) {
                    BlockPos blockPos2 = pos.offset(n, y, o);
                    BlockState blockState = world.getBlockState(blockPos2);
                    if (isSnowOrHONEY_BLOCK(blockState) || blockState.is(JamiesModBlocks.COBBLED_AMBER)) {
                        if (placeWater) {
                            this.setBlock(world, blockPos2, JamiesModBlocks.UMBER.defaultBlockState());
                        } else {
                            this.setBlock(world, blockPos2, Blocks.AIR.defaultBlockState());
                            this.clearSnowAbove(world, blockPos2);
                        }
                    }
                }
            }
        }

    }

    private void clearSnowAbove(LevelAccessor world, BlockPos pos) {
        if (world.getBlockState(pos.above()).is(Blocks.SNOW)) {
            this.setBlock(world, pos.above(), Blocks.AIR.defaultBlockState());
        }

    }

    private void placeAt(LevelAccessor world, @NotNull RandomSource random, BlockPos pos, int height, int offsetX, int offsetY, int offsetZ, int i, int j, boolean bl, int k, double randomSine, boolean placeSnow, BlockState state) {
        double d = bl ? this.getDistance(offsetX, offsetZ, BlockPos.ZERO, j, this.decreaseValueNearTop(offsetY, height, k), randomSine) : this.method_13421(offsetX, offsetZ, BlockPos.ZERO, i, random);
        if (d < 0.0) {
            BlockPos blockPos = pos.offset(offsetX, offsetY, offsetZ);
            double e = bl ? -0.5 : (double)(-6 - random.nextInt(3));
            if (d > e && random.nextDouble() > 0.9) {
                return;
            }

            this.placeBlockOrSnow(blockPos, world, random, height - offsetY, height, bl, placeSnow, state);
        }

    }

    private void placeBlockOrSnow(BlockPos pos, LevelAccessor world, @NotNull RandomSource random, int heightRemaining, int height, boolean lessSnow, boolean placeSnow, BlockState state) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isAir() || blockState.is(JamiesModBlocks.COBBLED_AMBER) || blockState.is(JamiesModBlocks.AMBER) || blockState.is(JamiesModBlocks.UMBER)) {
            boolean bl = !lessSnow || random.nextDouble() > 0.05;
            int i = lessSnow ? 3 : 2;
            if (placeSnow && !blockState.is(JamiesModBlocks.UMBER) && (double)heightRemaining <= (double)random.nextInt(Math.max(1, height / i)) + (double)height * 0.6 && bl) {
                this.setBlock(world, pos, JamiesModBlocks.COBBLED_AMBER.defaultBlockState());
            } else {
                this.setBlock(world, pos, state);
            }
        }

    }

    private int decreaseValueNearTop(int y, int height, int value) {
        int i = value;
        if (y > 0 && height - y <= 3) {
            i = value - (4 - (height - y));
        }

        return i;
    }

    private double method_13421(int x, int z, BlockPos pos, int i, @NotNull RandomSource random) {
        float f = 10.0F * Mth.clamp(random.nextFloat(), 0.2F, 0.8F) / (float)i;
        return (double)f + Math.pow(x - pos.getX(), 2.0) + Math.pow(z - pos.getZ(), 2.0) - Math.pow(i, 2.0);
    }

    private double getDistance(int x, int z, BlockPos pos, int divisor1, int divisor2, double randomSine) {
        return Math.pow(((double)(x - pos.getX()) * Math.cos(randomSine) - (double)(z - pos.getZ()) * Math.sin(randomSine)) / (double)divisor1, 2.0) + Math.pow(((double)(x - pos.getX()) * Math.sin(randomSine) + (double)(z - pos.getZ()) * Math.cos(randomSine)) / (double)divisor2, 2.0) - 1.0;
    }

    private int method_13419(RandomSource random, int y, int height, int factor) {
        float f = 3.5F - random.nextFloat();
        float g = (1.0F - (float)Math.pow(y, 2.0) / ((float)height * f)) * (float)factor;
        if (height > 15 + random.nextInt(5)) {
            int i = y < 3 + random.nextInt(6) ? y / 2 : y;
            g = (1.0F - (float)i / ((float)height * f * 0.4F)) * (float)factor;
        }

        return Mth.ceil(g / 2.0F);
    }

    private int method_13417(int y, int height, int factor) {
        float f = 1.0F;
        float g = (1.0F - (float)Math.pow(y, 2.0) / ((float) height)) * (float)factor;
        return Mth.ceil(g / 2.0F);
    }

    private int method_13427(RandomSource random, int y, int height, int factor) {
        float f = 1.0F + random.nextFloat() / 2.0F;
        float g = (1.0F - (float)y / ((float)height * f)) * (float)factor;
        return Mth.ceil(g / 2.0F);
    }

    private static boolean isSnowOrHONEY_BLOCK(BlockState state) {
        return state.is(Blocks.YELLOW_WOOL) || state.is(JamiesModBlocks.COBBLED_AMBER) || state.is(Blocks.YELLOW_STAINED_GLASS) || state.is(JamiesModBlocks.AMBER);
    }

    private boolean isAirBelow(BlockGetter world, BlockPos pos) {
        return world.getBlockState(pos.below()).isAir();
    }

    private void method_13418(LevelAccessor world, BlockPos pos, int i, int height, boolean bl, int j) {
        int k = bl ? j : i / 2;

        for(int l = -k; l <= k; ++l) {
            for(int m = -k; m <= k; ++m) {
                for(int n = 0; n <= height; ++n) {
                    BlockPos blockPos = pos.offset(l, n, m);
                    BlockState blockState = world.getBlockState(blockPos);
                    if (isSnowOrHONEY_BLOCK(blockState) || blockState.is(Blocks.SNOW)) {
                        if (this.isAirBelow(world, blockPos)) {
                            this.setBlock(world, blockPos, Blocks.AIR.defaultBlockState());
                            this.setBlock(world, blockPos.above(), Blocks.AIR.defaultBlockState());
                        } else if (isSnowOrHONEY_BLOCK(blockState)) {
                            BlockState[] blockStates = new BlockState[]{world.getBlockState(blockPos.west()), world.getBlockState(blockPos.east()), world.getBlockState(blockPos.north()), world.getBlockState(blockPos.south())};
                            int o = 0;
                            BlockState[] var15 = blockStates;
                            int var16 = blockStates.length;

                            for(int var17 = 0; var17 < var16; ++var17) {
                                BlockState blockState2 = var15[var17];
                                if (!isSnowOrHONEY_BLOCK(blockState2)) {
                                    ++o;
                                }
                            }

                            if (o >= 3) {
                                this.setBlock(world, blockPos, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
            }
        }

    }
}
