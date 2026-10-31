package com.jamiedev.bygone.core.mixin;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

import net.minecraft.world.item.component.BundleContents;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * old gear bag code overwrote the codec for vanilla's BundleContents.
 * this is here to deal with the reprecussions of that.
 *
 * this just provides a fallback so bundles aren't obliterated when updating.
 * XXX: ONCE WE STOP GUARANTEEING DIRECT UPDATESS FROM 3.1 (such as after
 *      updating to a new mc ver) PLEASE DELETE THIS MIXIN!
 **/
@Mixin(value = BundleContents.class, priority = 1291439781)
public abstract class BundleContentsLegacyMixin{
    @Shadow @Final @Mutable private static Codec<BundleContents> CODEC;

    @Inject(method = "<clinit>", at = @At("TAIL"), require = 0)
    private static void fallback(CallbackInfo cbi){
        CODEC = Codec.either(CODEC, CODEC.fieldOf("items").codec()).xmap(Either::unwrap, Either::left);
    }
}
