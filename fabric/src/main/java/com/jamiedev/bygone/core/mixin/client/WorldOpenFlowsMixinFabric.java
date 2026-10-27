package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.core.extension.ExperimentalWarningExtension;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.WorldStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixinFabric {
    @WrapOperation(method = "openWorldCheckWorldStemCompatibility", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;askForBackup(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;ZLjava/lang/Runnable;Ljava/lang/Runnable;)V"))
    private void bygone$confirmExperimentalWarningOnce(
        WorldOpenFlows instance, LevelStorageSource.LevelStorageAccess levelStorage, boolean customized,
        Runnable loadLevel, Runnable onCancel, Operation<Void> original,
        @Local(argsOnly = true) WorldStem worldStem) {
        if (customized || !(worldStem.worldData() instanceof ExperimentalWarningExtension confirmation)) {
            original.call(instance, levelStorage, customized, loadLevel, onCancel);
            return;
        }

        if (confirmation.bygone$hasConfirmedExperimentalWarning()) {
            loadLevel.run();
            return;
        }

        Runnable confirmedLoadLevel = () -> {
            confirmation.bygone$confirmExperimentalWarning();
            loadLevel.run();
        };
        original.call(instance, levelStorage, false, confirmedLoadLevel, onCancel);
    }

    @Inject(method = "createLevelFromExistingSettings", at = @At("HEAD"))
    private void bygone$rememberWorldCreationConfirmation(
        LevelStorageSource.LevelStorageAccess levelStorage, ReloadableServerResources resources,
        LayeredRegistryAccess<RegistryLayer> registries, WorldData worldData, CallbackInfo ci) {
        if (worldData.worldGenSettingsLifecycle() != Lifecycle.stable() && worldData instanceof ExperimentalWarningExtension confirmation) {
            confirmation.bygone$confirmExperimentalWarning();
        }
    }
}
