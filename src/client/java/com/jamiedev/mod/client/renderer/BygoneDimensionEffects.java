package com.jamiedev.mod.client.renderer;

import com.jamiedev.mod.init.JamiesModDimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;




public class BygoneDimensionEffects extends DimensionEffects
{
    public static final BygoneDimensionEffects INSTANCE = new BygoneDimensionEffects(Float.NaN, false, SkyType.NORMAL,
            false, true);
    private final MinecraftClient minecraft = MinecraftClient.getInstance();
    private int rainSoundTime;



    public BygoneDimensionEffects(float cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean brightenLighting, boolean darkened) {
        super(cloudsHeight, true, SkyType.NORMAL, false, false);
    }

    public Vec3d getFogColor() {
        float colorFactor = 0.75f;

        colorFactor *= (float) (100d / 100);


        return new Vec3d((int)(Math.min(Math.min(0.54f * colorFactor, 0.65f + 0)*255, 255)),
                ((int)(Math.min(Math.max(Math.min(0.3f * colorFactor, 0.87f) - 0 * 0.6f, 0)*255, 255))),
                ((int)(Math.min(Math.max(Math.min((0.001f * colorFactor) * (colorFactor * colorFactor), 0.9f) - 0 * 1.9f, 0)*255, 255))));
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return color;
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        //if (player != null) {
        //
       // }

        return true;
    }

    public static boolean fogThicknessAdjustments(
            ClientPlayerEntity player,
            float renderDistance,
            boolean thickFog,
            BackgroundRenderer.FogType fogType,
            Consumer<Float> setFogStart,
            Consumer<Float> setFogEnd,
            Consumer<FogShape> setFogShape)
    {
        if (
                fogType == BackgroundRenderer.FogType.FOG_TERRAIN &&
                thickFog &&
                player != null &&
                player.getWorld().getRegistryKey() == JamiesModDimension.BYGONE_LEVEL_KEY)
        {


            float distanceRationAdjuster = 1;
            if (renderDistance > 352) {
                distanceRationAdjuster = Math.min(renderDistance / 352, 1.25F);
            } else if (renderDistance < 126) {
                distanceRationAdjuster = Math.max(renderDistance / 126, 0.75F);
            }
            float fogStart = (float) (renderDistance / ((4D * distanceRationAdjuster * 0.3f) + 0.00001D));
            setFogStart.accept(Math.min(renderDistance, fogStart));
            setFogEnd.accept(Math.max(renderDistance, fogStart));
            setFogShape.accept(FogShape.CYLINDER);
            return true;
        }

        return false;
    }


}
