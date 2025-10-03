package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.Bygone;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public abstract class CowEntityMixinFabric {

    @Inject(method = "customServerAiStep",at = @At("RETURN"))
    private void onServerTick(CallbackInfo ci) {
        if ((Object)this instanceof Cow cow) {
           // Bygone.tickCow(cow);
        }
    }
}
