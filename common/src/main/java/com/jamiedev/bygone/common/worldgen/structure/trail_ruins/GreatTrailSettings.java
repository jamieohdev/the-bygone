package com.jamiedev.bygone.common.worldgen.structure.trail_ruins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// mostly just its own file for cleanliness sake
public record GreatTrailSettings(
    int chunkRadius, int pathWidth,
    int nodeCount, int nodeSpacingMin,
    int nodeSpacingMax, float maxTurnAngle,
    int bridgingDifference, int sampleRate,
    int buildingPlacementRetries
) {
    public static final MapCodec<GreatTrailSettings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("chunk_radius").forGetter(GreatTrailSettings::chunkRadius),
        Codec.INT.fieldOf("path_width").forGetter(GreatTrailSettings::pathWidth),
        // nodes
        Codec.INT.fieldOf("node_count").forGetter(GreatTrailSettings::nodeCount),
        Codec.INT.fieldOf("node_spacing_min").forGetter(GreatTrailSettings::nodeSpacingMin),
        Codec.INT.fieldOf("node_spacing_max").forGetter(GreatTrailSettings::nodeSpacingMax),
        Codec.FLOAT.optionalFieldOf("max_turn_angle", 60.0F).forGetter(GreatTrailSettings::maxTurnAngle),

        Codec.INT.optionalFieldOf("bridging_difference", 32).forGetter(GreatTrailSettings::bridgingDifference),
        Codec.INT.optionalFieldOf("sample_rate", 32).forGetter(GreatTrailSettings::sampleRate),
        Codec.INT.fieldOf("building_placement_retries").forGetter(GreatTrailSettings::buildingPlacementRetries)
    ).apply(instance, GreatTrailSettings::new));
}
