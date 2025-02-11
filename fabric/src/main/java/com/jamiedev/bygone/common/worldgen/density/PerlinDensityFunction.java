package com.jamiedev.bygone.common.worldgen.density;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
public class PerlinDensityFunction implements DensityFunction
{
    public static final KeyDispatchDataCodec<PerlinDensityFunction> CODEC =
            KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(
            p_208798_ -> p_208798_.group(
                            NormalNoise.NoiseParameters.DIRECT_CODEC.fieldOf("noise").forGetter((func)
                                    -> func.param),
                            Codec.DOUBLE.fieldOf("xz_scale").forGetter((func) -> func.xz),
                            Codec.DOUBLE.fieldOf("y_scale").forGetter((func) -> func.y),
                            Codec.LONG.fieldOf("seed").forGetter((func) -> func.seed)
                    )
                    .apply(p_208798_, PerlinDensityFunction::new)));
    @Nullable
    public NormalNoise noise = null;
    private static final  Map<Long, Visitor> VISITORS = new HashMap();

    public NormalNoise.NoiseParameters param;
    public NormalNoise fake;
    long seed;
    double xz, y;

    public PerlinDensityFunction(NormalNoise.NoiseParameters params, double xz, double y, long seed)
    {
        this.seed = seed;
        this.param = params;
        this.xz = xz;
        this.y = y;
        this.fake = NormalNoise.create(new XoroshiroRandomSource(seed), params.firstOctave(), params.amplitudes().getDouble(params.firstOctave()));
    }

    @Override
    public double compute(FunctionContext pos) {
        return 0;
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
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
            return this.noise.maxValue();
        }
        else
        {
            return this.fake.maxValue();
        }
    }

    public static PerlinNoiseVisitor createOrGetVisitor(long seed) {
        return (PerlinNoiseVisitor) VISITORS.computeIfAbsent(seed, l -> new PerlinNoiseVisitor(noise -> {
            if (noise.initialized()) {
                return noise;
            } else {
                return noise.initialize(offset -> new XoroshiroRandomSource(l + offset));
            }
        }));
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    public PerlinDensityFunction initialize(Function<Long, XoroshiroRandomSource> rand) {
        this.noise = NormalNoise.create(rand.apply(this.seed), this.param.firstOctave(),
                this.param.amplitudes().getDouble(this.param.firstOctave()));
        return this;
    }


    public boolean initialized() {
        return this.noise != null;
    }

    public record PerlinNoiseVisitor(UnaryOperator<PerlinDensityFunction> operator) implements Visitor {
        @Override
        public DensityFunction apply(DensityFunction function) {
            if (function instanceof PerlinDensityFunction pnf) {
                return operator.apply(pnf);
            }
            return function;
        }
    }
}
