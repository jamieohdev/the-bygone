package com.jamiedev.bygone.client.screen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.BygoneClient;
import com.jamiedev.bygone.common.entity.BygonePortalEntity;
import com.jamiedev.bygone.core.mixin.PortalProcessorAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PortalProcessor;
import org.joml.Quaternionf;
import org.jetbrains.annotations.NotNull;

public class PortalOverlay implements LayeredDraw.Layer {
    private static final ResourceLocation TEXTURE_VIGNETTE = ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "textures/gui/overlay/vignette.png");
    private static final ResourceLocation TEXTURE_PORTAL = ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "textures/gui/overlay/portal.png");
    private float alpha = 0f;
    private boolean entering = false;
    private int tick = 0;

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (mc.level == null || mc.player == null || mc.options.hideGui) return;

        // XXX: bit of a bodge. ideally, we'd be going alongside vanilla's
        // handling for setAsInsidePortalThisTick on client, but...
        // that requires we use the nausea effect overlay.
        // this is the easiest place to shove clearing it, sadly
        PortalProcessor processor = player.portalProcess;
        if(player.tickCount != tick){ // process SCP-2719ness once per tick, cause that's when it's set :p
            tick = player.tickCount;
            if (processor instanceof PortalProcessorAccessor portalProcessorAccessor && processor.isInsidePortalThisTick()
                    && portalProcessorAccessor.getPortal() instanceof BygonePortalEntity) {
                entering = true;
                processor.setAsInsidePortalThisTick(false);
            } else entering = false;
        }

        if (entering || alpha > 0) {
            if(entering) alpha = alpha * 0.97F + 0.03F;
            else alpha = (alpha - 0.06F) / 0.97F;
            alpha = Math.clamp(alpha, 0F, 1F);

            int portalWidth = 128;
            int portalHeight = 128;

            int tlX = (int) Mth.lerp(alpha, -portalWidth, 0);
            int tlY = (int) Mth.lerp(alpha, -portalHeight, 0);

            int trX = (int) Mth.lerp(alpha, screenWidth + portalWidth, screenWidth);
            int trY = (int) Mth.lerp(alpha, -portalHeight, 0);

            int blX = (int) Mth.lerp(alpha, -portalWidth, 0);
            int blY = (int) Mth.lerp(alpha, screenHeight + portalHeight, screenHeight);

            int brX = (int) Mth.lerp(alpha, screenWidth + portalWidth, screenWidth);
            int brY = (int) Mth.lerp(alpha, screenHeight + portalHeight, screenHeight);


            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            guiGraphics.setColor(1f, 1f, 1f, alpha);
            guiGraphics.blit(TEXTURE_VIGNETTE, 0, 0, screenWidth, screenHeight, 0, 0, 256, 256, 256, 256);
            guiGraphics.setColor(1f, 1f, 1f, 1f);

            guiGraphics.blit(TEXTURE_PORTAL, tlX, tlY, 0, 0, portalWidth, portalHeight, portalWidth, portalHeight);

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(trX, trY, 0);
            poseStack.mulPose(Axis.ZP.rotation((float) Math.PI/2f));
            guiGraphics.blit(TEXTURE_PORTAL, 0, 0, 0, 0, portalWidth, portalHeight, portalWidth, portalHeight);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(blX, blY, 0);
            poseStack.mulPose(Axis.ZN.rotation((float) Math.PI/2f));
            guiGraphics.blit(TEXTURE_PORTAL, 0, 0, 0, 0, portalWidth, portalHeight, portalWidth, portalHeight);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(brX, brY, 0);
            poseStack.mulPose(Axis.ZP.rotation((float) Math.PI));
            guiGraphics.blit(TEXTURE_PORTAL, 0, 0, 0, 0, portalWidth, portalHeight, portalWidth, portalHeight);
            poseStack.popPose();

            guiGraphics.setColor(1f, 1f, 1f, 1f);
            RenderSystem.disableBlend();
        }
    }
}
