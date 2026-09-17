package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.SpawnState.class)
public abstract class SpawnStateMixin {

    @Shadow
    @Final
    private LocalMobCapCalculator localMobCapCalculator;

    // PROBABLY fine to ignore for category since otherwise they spawn with other category???
//    @Inject(method = "canSpawnForCategory", at = @At("RETURN"), cancellable = true)
//    private void bygone$redirectCategory(MobCategory category, ChunkPos pos, CallbackInfoReturnable<Boolean> cir) {
//        if (!category.equals(HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)) return;
//        // ignore local cap??? i dont know man this is annoying
//        cir.setReturnValue(this.localMobCapCalculator.canSpawn(category, pos));
//    }
}
