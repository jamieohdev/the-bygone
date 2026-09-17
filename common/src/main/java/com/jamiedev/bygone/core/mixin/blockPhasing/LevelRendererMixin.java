package com.jamiedev.bygone.core.mixin.blockPhasing;

import com.jamiedev.bygone.common.entity.BlockPhasingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Shadow @Final private Minecraft minecraft;

	@ModifyArg(
			method = "renderLevel",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;setupRender(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/culling/Frustum;ZZ)V"
			),
			index = 3
	)
	private boolean isPhasing(boolean isSpectator) {
		if (this.minecraft.player instanceof BlockPhasingEntity phasing) return phasing.isPhasing();
		return isSpectator;
	}

}
