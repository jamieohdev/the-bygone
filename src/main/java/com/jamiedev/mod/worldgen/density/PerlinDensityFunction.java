package com.jamiedev.mod.worldgen.density;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
public class PerlinDensityFunction implements DensityFunction
{
    public static final CodecHolder<PerlinDensityFunction> CODEC =
            CodecHolder.of(RecordCodecBuilder.mapCodec(
            p_208798_ -> p_208798_.group(
                            DoublePerlinNoiseSampler.NoiseParameters.CODEC.fieldOf("noise").forGetter((func)
                                    -> func.param),
                            Codec.DOUBLE.fieldOf("xz_scale").forGetter((func) -> func.xz),
                            Codec.DOUBLE.fieldOf("y_scale").forGetter((func) -> func.y),
                            Codec.LONG.fieldOf("seed").forGetter((func) -> func.seed)
                    )
                    .apply(p_208798_, PerlinDensityFunction::new)));
    @Nullable
    public DoublePerlinNoiseSampler noise = null;
    private static final  Map<Long, DensityFunctionVisitor> VISITORS = new HashMap();

    public DoublePerlinNoiseSampler.NoiseParameters param;
    public DoublePerlinNoiseSampler fake;
    long seed;
    double xz, y;

    public PerlinDensityFunction(DoublePerlinNoiseSampler.NoiseParameters params, double xz, double y, long seed)
    {
        this.seed = seed;
        this.param = params;
        this.xz = xz;
        this.y = y;
        this.fake = DoublePerlinNoiseSampler.create(new Xoroshiro128PlusPlusRandom(seed), params.firstOctave(), params.amplitudes().getDouble(params.firstOctave()));
    }

    @Override
    public double sample(NoisePos pos) {
        return 0;
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(this);
    }

    @Override
    public double minValue() {
        return -this.maxValue();
    }

    @Override
    public double maxValue() {
        if (this.noise != null)
        {
            return this.noise.getMaxValue();
        }
        else
        {
            return this.fake.getMaxValue();
        }
    }

    public static PerlinNoiseVisitor createOrGetVisitor(long seed) {
        return (PerlinNoiseVisitor) VISITORS.computeIfAbsent(seed, l -> new PerlinNoiseVisitor(noise -> {
            if (noise.initialized()) {
                return noise;
            } else {
                return noise.initialize(offset -> new Xoroshiro128PlusPlusRandom(l + offset));
            }
        }));
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC;
    }

    public PerlinDensityFunction initialize(Function<Long, Xoroshiro128PlusPlusRandom> rand) {
        this.noise = DoublePerlinNoiseSampler.create(rand.apply(this.seed), this.param.firstOctave(),
                this.param.amplitudes().getDouble(this.param.firstOctave()));
        return this;
    }


    public boolean initialized() {
        return this.noise != null;
    }

    public record PerlinNoiseVisitor(UnaryOperator<PerlinDensityFunction> operator) implements DensityFunctionVisitor {
        @Override
        public DensityFunction apply(DensityFunction function) {
            if (function instanceof PerlinDensityFunction pnf) {
                return operator.apply(pnf);
            }
            return function;
        }
    }
}
