package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.BlockPhasingEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class EntityPhaseMeterGuiMixin {

	@Unique private static final ResourceLocation BASE_PHASE_METER_SPRITE = Bygone.id("hud/phase_meter");
	@Unique private static final int PHASE_METER_SPRITE_COUNT = 3;

	@Shadow @Final private Minecraft minecraft;
	@Shadow @Final private LayeredDraw layers;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addPhaseMeterLayer(Minecraft minecraft, CallbackInfo ci) {
		LayeredDraw phaseMeterLayer = new LayeredDraw().add(this::renderPhaseMeter);
		this.layers.add(phaseMeterLayer, () -> !this.minecraft.options.hideGui);
	}

	@Unique
	private void renderPhaseMeter(GuiGraphics graphics, DeltaTracker tracker) {
		Player player = this.minecraft.player;
		if (player instanceof BlockPhasingEntity phasing) {
			int maxTicks = phasing.getMaxPhasingTicks();
			if (maxTicks <= 0) return;

			int ticks = phasing.getPhasingTime();
			float value = (float) ticks / maxTicks;
			int spriteIndex = (PHASE_METER_SPRITE_COUNT - 1) - (int) (value * (PHASE_METER_SPRITE_COUNT - 1));
			ResourceLocation sprite = BASE_PHASE_METER_SPRITE.withSuffix("/" + (spriteIndex));

			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			graphics.blitSprite(sprite, (graphics.guiWidth() / 2) - 8, (graphics.guiHeight() / 2) + 16, 16, 16);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
		}
	}

}
