package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.extension.ExperimentalWarningExtension;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimaryLevelData.class)
public abstract class PrimaryLevelDataMixinFabric implements ExperimentalWarningExtension {
    @Unique private static final String BYGONE_CONFIRMED_WARNING = "confirmedExperimentalSettings";
    @Unique private boolean bygone$confirmedExperimentalWarning;

    @Inject(method = "parse", at = @At("RETURN"))
    private static void bygone$loadExperimentalWarningConfirmation(Dynamic<?> tag, LevelSettings settings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty,
        WorldOptions worldOptions, Lifecycle lifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        if (lifecycle != Lifecycle.stable() && tag.get(BYGONE_CONFIRMED_WARNING).asBoolean(false)) {
            ((ExperimentalWarningExtension) cir.getReturnValue()).bygone$confirmExperimentalWarning();
        }
    }

    @Inject(method = "setTagData", at = @At("TAIL"))
    private void bygone$saveExperimentalWarningConfirmation(RegistryAccess registries, CompoundTag tag, CompoundTag playerTag, CallbackInfo ci) {
        tag.putBoolean(BYGONE_CONFIRMED_WARNING, this.bygone$confirmedExperimentalWarning);
    }

    @Override
    public boolean bygone$hasConfirmedExperimentalWarning() {
        return this.bygone$confirmedExperimentalWarning;
    }

    @Override
    public void bygone$confirmExperimentalWarning() {
        this.bygone$confirmedExperimentalWarning = true;
    }
}
