package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.jamiedev.bygone.core.util.HeightGetter;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static com.jamiedev.bygone.Bygone.MAP_HEIGHT;

@Mixin(MapItem.class)
/*
 *  Partially taken from BetterNetherMap, MIT License, core logic is different though and not interchangeable, HeightGetter is a Functional Interface
 */
public class MapItemMixin {

    @Unique
    private static ItemStack the_bygone$getMapHandItem(LivingEntity entity) {
        ItemStack mapItem = entity.getMainHandItem();
        if (mapItem.isEmpty() || !(mapItem.getItem() instanceof MapItem)) {
            mapItem = entity.getOffhandItem();
        }
        return mapItem;
    }

    @WrapOperation(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/dimension/DimensionType;hasCeiling()Z"
        )
    )
    private boolean hasCeiling(DimensionType type, Operation<Boolean> original, @Local(argsOnly = true) Level level) {
        Optional<Registry<DimensionType>> registry = level.registryAccess().registry(Registries.DIMENSION_TYPE);
        if (registry.isPresent()) {
            ResourceLocation location = registry.orElseThrow().getKey(type);
            if (location != null) {
                if (registry.orElseThrow().getHolder(location).orElseThrow().is(JamiesModTag.DimensionTypes.MAPS_IGNORE_CEILING)) {
                    return false;
                }
            }
        }
        return original.call(type);
    }

    @Inject(method = "update", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;getHoldingPlayer(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$HoldingPlayer;"))
    private void provideHeightSelector(Level level, Entity viewer, MapItemSavedData data, CallbackInfo ci, @Share("height_getter") LocalRef<HeightGetter> heightGetter) {
        if (viewer.level().dimension() != BGDimensions.BYGONE_LEVEL_KEY)
            heightGetter.set(null);
        else if (viewer instanceof LivingEntity e)
            heightGetter.set(() -> the_bygone$getMapHandItem(e).get(MAP_HEIGHT.get()));
    }

    @ModifyExpressionValue(method = "update", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/LevelChunk;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I"))
	/*
	  Change the height at which the map starts to scan for blocks.
	 */
    public int sampleHeightmap(int value, @Share("height_getter") LocalRef<HeightGetter> heightGetter) {
        return heightGetter.get() == null ? value : heightGetter.get().getHeight();
    }
}
