package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGFogTypes;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private boolean initialized;
    @Shadow
    public abstract Camera.NearPlane getNearPlane();
    @Shadow
    private Vec3 position;
    @Shadow
    private BlockGetter level;
    @Shadow
    @Final
    private Vector3f forwards;

    @Inject(method = "getFluidInCamera()Lnet/minecraft/world/level/material/FogType;", at = @At("RETURN"), cancellable = true)
    void LithoFogType(CallbackInfoReturnable<FogType> cir) {
        if (this.initialized) {
            Camera.NearPlane nearplane = this.getNearPlane();
            for (Vec3 vec3 : Arrays.asList(new Vec3(this.forwards).scale(0.05F), nearplane.getTopLeft(), nearplane.getTopRight(), nearplane.getBottomLeft(), nearplane.getBottomRight())) {
                Vec3 vec31 = this.position.add(vec3);
                BlockPos blockpos = BlockPos.containing(vec31);
                FluidState fluidstate = this.level.getFluidState(blockpos);

                if (fluidstate.is(JamiesModTag.LITHO)) {
                    if (vec31.y <= (double)(fluidstate.getHeight(this.level, blockpos) + (float)blockpos.getY())) {
                        cir.setReturnValue(BGFogTypes.LITHO);
                        cir.cancel();
                    }
                }
            }
        }
    }
}