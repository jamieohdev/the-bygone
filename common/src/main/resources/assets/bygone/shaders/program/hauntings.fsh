#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform vec2 ScreenSize;
uniform float DarknessStrength;
uniform float CutThroughStrength;
uniform float EdgeStrength;
uniform float DistanceScale;
uniform float MinEdgePixels;

in vec2 texCoord;

out vec4 fragColor;

vec3 rimColor = vec3(1., 1., 1.);

float shapeLightMask(float lightLevel) { return mix(lightLevel, sqrt(lightLevel), clamp(CutThroughStrength, 0.0, 1.0)); }
float luminance(vec3 color) { return dot(color, vec3(0.2126, 0.7152, 0.0722)); }

float compatibleLuminance(vec2 uv, float centerDepth, float centerLuma, vec2 offset, vec2 pixel) {
    vec2 sampleUv = uv + (pixel * offset);
    float sampleDepth = texture(DepthSampler, sampleUv).r;
    float sampleLuma = luminance(texture(DiffuseSampler, sampleUv).rgb);
    float depthDelta = abs(sampleDepth - centerDepth);
    float sameSurface = 1.0 - smoothstep(0.0015, 0.025, depthDelta);
    float compatibleBrightness = 1.0 - smoothstep(0.10, 0.36, abs(sampleLuma - centerLuma));
    return sampleLuma * sameSurface * compatibleBrightness;
}

float sampleVisibleLight(vec2 uv, float centerDepth, vec2 pixel) {
    float direct = luminance(texture(DiffuseSampler, uv).rgb);
    float wide = direct * 0.22;

    vec2 offsets[12] = vec2[12](
        vec2(2.0, 0.0), vec2(-2.0, 0.0), vec2(0.0, 2.0), vec2( 0.0, -2.0),
        vec2(6.0, 3.0), vec2(-6.0, -3.0), vec2(3.0, -6.0), vec2(-3.0, 6.0),
        vec2(16.0, 0.0), vec2(-16.0, 0.0), vec2(0.0, 16.0), vec2( 0.0, -16.0)
    );
    // lazy lol
    float weights[12] = float[12](0.08, 0.08, 0.08, 0.08, 0.06, 0.06, 0.06, 0.06, 0.04, 0.04, 0.04, 0.04);
    for (int i = 0; i < 12; i++) wide += compatibleLuminance(uv, centerDepth, direct, offsets[i], pixel) * weights[i];

    return smoothstep(0.04, 0.42, wide);
}

void main() {
    vec4 source = texture(DiffuseSampler, texCoord);
    float centerDepth = texture(DepthSampler, texCoord).r;
    vec2 screenPixel = vec2(1.0) / ScreenSize;

    float terrainMask = centerDepth < 0.999999 ? 1.0 : 0.0;
    float distanceThickness = mix(3.0, 2.25, smoothstep(0.0, 1.0, centerDepth));
    float edgePixels = max(MinEdgePixels, DistanceScale * distanceThickness);
    vec2 pixel = vec2(edgePixels) / ScreenSize;

    float depthEdge = 0.0;
    for (int i = 0; i < 2; i++) {
        vec2 offset = i == 0 ? vec2(pixel.x, 0.0) : vec2(0.0, pixel.y);
        depthEdge += abs(texture(DepthSampler, texCoord - offset).r - texture(DepthSampler, texCoord + offset).r);
    }
    float farEdge = smoothstep(0.45, 1.0, centerDepth);
    float edgeStart = mix(0.00035, 0.00004, farEdge);
    float edgeEnd = mix(0.009, 0.0018, farEdge);
    depthEdge = smoothstep(edgeStart, edgeEnd, depthEdge);

    float sourceLuma = luminance(source.rgb);
    float lightLevel = sampleVisibleLight(texCoord, centerDepth, screenPixel);
    float lightMask = shapeLightMask(lightLevel);
    float emitterProtection = smoothstep(0.50, 0.88, sourceLuma);
    float effectEligibility = terrainMask * (1.0 - emitterProtection);
    float darknessAmount = effectEligibility * DarknessStrength * (1.0 - lightMask);
    float litAreaSuppression = smoothstep(0.18, 0.70, lightMask);
    float rimVisibility = (1.0 - emitterProtection) * (1.0 - emitterProtection) * (1.0 - litAreaSuppression);
    float edgeMask = depthEdge * rimVisibility;

    vec3 darkOutput = rimColor * edgeMask * EdgeStrength * darknessAmount;
    float originalBlend = mix(1.0, lightMask, effectEligibility * DarknessStrength);
    originalBlend = max(originalBlend, emitterProtection);
    vec3 color = mix(darkOutput, source.rgb, originalBlend);

    fragColor = vec4(color, 1.0);
}