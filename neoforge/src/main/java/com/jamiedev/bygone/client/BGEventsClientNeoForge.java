package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.effect.FogEffectRenderer;
import com.jamiedev.bygone.client.renderer.weather.FogWeatherRenderer;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.jamiedev.bygone.client.screen.PortalOverlay;
import com.jamiedev.bygone.core.registry.BGMobEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.GlStateBackup;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;

@EventBusSubscriber(modid = Bygone.MOD_ID, value = Dist.CLIENT)
public class BGEventsClientNeoForge {
    @SubscribeEvent
    public static void onPlayerHeartTypeEvent(PlayerHeartTypeEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide() && player.hasEffect(BGMobEffects.HAUNTED.get())) {
             event.setType(BGEnumClientExtensionsNeoForge.BYGONE_HEART_HAUNTED.getValue());
        }
    }

    private static final GlStateBackup stateBackup = new GlStateBackup();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderLevel(RenderLevelStageEvent event) {
//        boolean fabulousGraphics = Minecraft.useShaderTransparency()
//            && event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL;

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || !minecraft.level.dimension()
            .equals(BGDimensions.BYGONE_LEVEL_KEY)) return;

        FogEffectRenderer fogRenderer = FogEffectRenderer.getInstance();
        RenderSystem.backupGlState(stateBackup);

        fogRenderer.render(
            minecraft, event.getProjectionMatrix(), event.getCamera(),
            event.getPartialTick().getGameTimeDeltaPartialTick(false)
        );

        RenderSystem.restoreGlState(stateBackup);
        minecraft.getMainRenderTarget().bindWrite(true);
    }

    @SubscribeEvent
    public static void fog(ViewportEvent.RenderFog event) {
        Level level = event.getCamera().getEntity().level();
        if (!level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;

        event.setCanceled(true);
        event.scaleFarPlaneDistance(FogEffectRenderer.getDefaultFogMultiplier());
    }
}