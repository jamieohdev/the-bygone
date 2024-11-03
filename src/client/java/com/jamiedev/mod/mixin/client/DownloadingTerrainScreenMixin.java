package com.jamiedev.mod.mixin.client;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.client.JamiesModUtilClient;
import com.jamiedev.mod.client.screen.DimensionScreen;
import com.jamiedev.mod.init.JamiesModDimension;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DownloadingTerrainScreen.class, priority = 1200)
public class DownloadingTerrainScreenMixin extends Screen {
    protected DownloadingTerrainScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void bygone$renderNewScreenWhenEnteringBygone(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (JamiesModUtilClient.getClientPlayer() != null && JamiesModUtilClient.getClientPlayer().getWorld().getRegistryKey() == JamiesModDimension.BYGONE_LEVEL_KEY) {
            DimensionScreen.renderScreenAndText(((DownloadingTerrainScreen)(Object)this), guiGraphics);
            ci.cancel();
        }
    }
}
