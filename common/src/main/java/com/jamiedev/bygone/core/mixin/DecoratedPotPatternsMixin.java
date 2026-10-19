package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.registry.BGDecoratedPotPatterns;
import com.jamiedev.bygone.core.registry.BGItems;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotPatterns.class)
public abstract class DecoratedPotPatternsMixin{
    @Inject(method = "getPatternFromItem", at = @At("HEAD"), cancellable = true)
    private static void ustoo(Item item, CallbackInfoReturnable<ResourceKey<DecoratedPotPattern>> cbir){
        if(item != null){
            if(item.equals(BGItems.BEAK_POTTERY_SHERD.get())) cbir.setReturnValue(BGDecoratedPotPatterns.BEAK_KEY);
            else if(item.equals(BGItems.MURKY_POTTERY_SHERD.get())) cbir.setReturnValue(BGDecoratedPotPatterns.MURKY_KEY);
        }
    }
}
