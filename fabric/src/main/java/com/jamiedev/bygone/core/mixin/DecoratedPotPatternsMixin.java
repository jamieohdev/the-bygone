package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.registry.BGDecoratedPotPatternsFabric;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.level.block.entity.DecoratedPotPatterns.class)
public abstract class DecoratedPotPatternsMixin {

    @Inject(method = "getPatternFromItem", at = @At("RETURN"), cancellable = true)
    private static void bygone$getPatternFromItem(Item item, CallbackInfoReturnable<ResourceKey<DecoratedPotPattern>> cir) {
        if (item == BGItems.BEAK_POTTERY_SHERD.get()) {
            cir.setReturnValue(BGDecoratedPotPatternsFabric.BEAK);
        }
        if (item == BGItems.MURKY_POTTERY_SHERD.get()) {
            cir.setReturnValue(BGDecoratedPotPatternsFabric.MURKY);
        }
    }

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bygone$bootstrap(Registry<DecoratedPotPattern> registry, CallbackInfoReturnable<DecoratedPotPattern> cir) {
        BGDecoratedPotPatternsFabric.register(registry, BGDecoratedPotPatternsFabric.BEAK, "beak_pottery_pattern");
        BGDecoratedPotPatternsFabric.register(registry, BGDecoratedPotPatternsFabric.MURKY, "murky_pottery_pattern");
    }
}