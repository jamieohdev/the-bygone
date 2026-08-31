package com.jamiedev.bygone.common.worldgen.structure.trail_ruins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

// mostly just its own file for cleanliness sake
public record GreatTrailSettings(
    int chunkRadius,
    int pathWidth,
    int nodeCount,
    int nodeSpacingMin,
    int nodeSpacingMax,
    int bridgingDifference
) {
    public static final MapCodec<GreatTrailSettings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("chunk_radius").forGetter(GreatTrailSettings::chunkRadius),
        Codec.INT.fieldOf("path_width").forGetter(GreatTrailSettings::pathWidth),
        // nodes
        Codec.INT.fieldOf("node_count").forGetter(GreatTrailSettings::nodeCount),
        Codec.INT.fieldOf("node_spacing_min").forGetter(GreatTrailSettings::nodeSpacingMin),
        Codec.INT.fieldOf("node_spacing_max").forGetter(GreatTrailSettings::nodeSpacingMax),

        Codec.INT.fieldOf("bridging_difference").forGetter(GreatTrailSettings::bridgingDifference)
    ).apply(instance, GreatTrailSettings::new));

    public float maxTurnAngle() { return 60f; }
}
