package com.jamiedev.bygone.mixin.client;

import com.jamiedev.bygone.client.JamiesModUtilClient;
import com.jamiedev.bygone.client.screen.DimensionScreen;
import com.jamiedev.bygone.fabric.init.JamiesModDimension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ReceivingLevelScreen.class, priority = 1200)
public class DownloadingTerrainScreenMixin extends Screen {
    protected DownloadingTerrainScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render",
            at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void bygone$renderNewScreenWhenEnteringBygone(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (JamiesModUtilClient.getClientPlayer() != null && JamiesModUtilClient.getClientPlayer().level().dimension() == JamiesModDimension.BYGONE_LEVEL_KEY) {
            DimensionScreen.renderScreenAndText(((ReceivingLevelScreen)(Object)this), guiGraphics);
            ci.cancel();
        }
    }
}
