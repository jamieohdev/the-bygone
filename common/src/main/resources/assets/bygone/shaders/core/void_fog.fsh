#version 150

uniform sampler2D DepthSampler;

uniform mat4 ScreenToView;
uniform mat4 ViewToWorld;
uniform vec2 NoiseStart;
uniform float RenderDistance;

uniform float CameraPosition;
uniform float VoidPosition;

uniform float Intensity;
uniform float Anisotropy;

uniform float FadeHeight;
uniform float FadeFactor;

in vec2 texCoord;
out vec4 fragColor;

// https://thebookofshaders.com/13/
float random(in vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898,78.233)))*
    43758.5453123);
}

// Based on Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
float noise(in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}

#define OCTAVES 6
float fbm(in vec2 st) {
    // Initial values
    float value = 0.0;
    float amplitude = .5;
    float frequency = 0.;
    //
    // Loop of octaves
    for (int i = 0; i < OCTAVES; i++) {
        value += amplitude * noise(st);
        st *= 2.;
        amplitude *= .5;
    }
    return value;
}

#define noiseScale 0.025
#define noiseAmount 0.75

vec2 noisePosition(in vec3 point) {
    return NoiseStart + noiseScale * (point.xz + point.y * vec2(.37, -.23));
}

#define sampleReach .5
#define detailScale 2.
#define ridgeMix .16

vec2 fogNoise(in vec3 direction, in float distance) {
    vec2 point = noisePosition(direction * distance * sampleReach);
    float large = fbm(point);
    float detail = fbm(point * detailScale + vec2(37., 61.));
    float ridges = 1. - abs(detail * 2. - 1.);
    detail = mix(detail, ridges, ridgeMix);
    return vec2(large, detail);
}

#define epsilon 0.0001

// tried using it directly at one point and it didnt  look very good or maybe i just screwed it up
// https://advances.realtimerendering.com/s2017/DecimaSiggraph2017.pdf
float measureHeightFog(
    in float start, in float end,
    in float rise, in float fade
) {
    if (end <= start) return 0.;
    if (abs(rise) < epsilon) return (end - start) * exp(-fade * max(CameraPosition - VoidPosition, 0.));

    float startHeight = CameraPosition + rise * start;
    float endHeight = CameraPosition + rise * end;
    if (startHeight <= VoidPosition && endHeight <= VoidPosition) return end - start;
    if (startHeight > VoidPosition && endHeight > VoidPosition)
        return (exp(-fade * (startHeight - VoidPosition)) - exp(-fade * (endHeight - VoidPosition))) / (fade * rise);

    float crossing = clamp((VoidPosition - CameraPosition) / rise, start, end);
    if (startHeight <= VoidPosition)
        return crossing - start + (1. - exp(-fade * (endHeight - VoidPosition))) / (fade * rise);
    return (exp(-fade * (startHeight - VoidPosition)) - 1.) / (fade * rise) + end - crossing;
}

float measureLightBlocking(in float height) {
    if (height < VoidPosition) return VoidPosition - height + 1. / FadeFactor;
    return exp(-FadeFactor * (height - VoidPosition)) / FadeFactor;
}

#define fogStrength 0.06
#define voidStrength 0.0015

#define lowColor vec3(0.082, 0.098, 0.153)
#define highColor vec3(0.38, 0.443, 0.518)

// https://advances.realtimerendering.com/s2014/wronski/bwronski_volumetric_fog_siggraph2014.pdf
void main() {
    float depth = texture(DepthSampler, texCoord).r;
    vec4 scaledView = ScreenToView * vec4(
        (texCoord * 2.0) - 1.0, (depth * 2.0) - 1.0, 1.0
    );
    float safeScale = (abs(scaledView.w) >= epsilon)
        ? scaledView.w : (scaledView.w < 0. ? -epsilon : epsilon);
    vec3 viewPoint = scaledView.xyz / safeScale;

    float distance = (depth >= 1. - epsilon) ? RenderDistance : min(length(viewPoint), RenderDistance);
    vec3 direction = normalize((ViewToWorld * vec4(normalize(viewPoint), 0.)).xyz);
    vec2 pattern = fogNoise(direction, distance);

    float pathFog = measureHeightFog(0., distance, direction.y, FadeFactor);
    float voidFog = measureHeightFog(0., distance, direction.y, FadeFactor * 2.35); // scales void height density

    float middle = distance * .5;
    float middleHeight = CameraPosition + direction.y * middle;

    // "cloudiness" density
    float fade = max((middleHeight - VoidPosition) / FadeHeight, 0.);
    float upperMix = smoothstep(.08, 1.2, fade);
    float largeShape = mix(.5, 1.5, pattern.x);
    float detailShape = mix(
        .56, 1.64, smoothstep(.16, .84, pattern.y)
    );

    float detailMix = noiseAmount * (.5 + (upperMix / 2.));
    float shapeThickness = clamp(largeShape * mix(1., detailShape, detailMix), .2, 1.9);

    // composite thickness
    float voidShape = clamp(mix(
            .52, 1.48, smoothstep(.1, .9, pattern.x)
        ) * mix(.84, 1.16, pattern.y), .42, 1.62
    );
    float voidDepth = voidStrength * voidFog * voidShape;
    float strength = fogStrength * (Intensity * Intensity);
    float thickness = strength * pathFog * shapeThickness;
    thickness += voidDepth;
    float visibility = exp(-min(thickness, 80.));

    float colorMix = clamp((middleHeight - VoidPosition) / FadeHeight, 0., 1.);
    vec3 color = mix(lowColor, highColor, colorMix);
    color = mix(color, lowColor, .72 * (voidDepth / max(thickness, epsilon))); // color bias multiplier

    // composite glow
    float scatterShape = pow(max(1. + Anisotropy * Anisotropy - 2. * Anisotropy * -direction.y, .001), 1.5);
    float forwardGlow = (1. - Anisotropy * Anisotropy) / scatterShape;
    color *= .86 + .14 * clamp(forwardGlow, .25, 4.);

    //
    float shadeAmount = measureLightBlocking(middleHeight);
    float lightBlock = strength * shapeThickness * shadeAmount;
    float lightReach = exp(-min(lightBlock, 80.));
    float shadedLight = .68 + .57 * lightReach;

    float shadeMix = .35 + (.35 * noiseAmount);
    color *= mix(1., shadedLight, shadeMix);

    fragColor = vec4(color, 1. - visibility);
}
