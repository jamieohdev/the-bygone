package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.registry.BGDimensions;
import com.jamiedev.bygone.core.util.HeightGetter;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
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

import static com.jamiedev.bygone.Bygone.MAP_HEIGHT;

@Mixin(MapItem.class)
public class MapItemMixin {

    @Redirect(method = "update", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/dimension/DimensionType;hasCeiling()Z"))
	/*
	  Make every dimension have a sky, which makes maps show the surface.
	  @see FilledMapItem#updateColors(World world, Entity entity, MapState state)
	 */
    private boolean hasCeiling(DimensionType type) {
        return false;
    }


    @Inject(method = "update", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;getHoldingPlayer(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$HoldingPlayer;"))
    private void provideHeightSelector(Level level, Entity viewer, MapItemSavedData data, CallbackInfo ci, @Share("height_getter") LocalRef<HeightGetter> heightGetter) {
        if(viewer.level().dimension() != BGDimensions.BYGONE_LEVEL_KEY)
            heightGetter.set(null);
        else
            if(viewer instanceof LivingEntity e)
                heightGetter.set(()-> the_bygone$getMapHandItem(e).get(MAP_HEIGHT.get()));
    }

    @Unique
    private static ItemStack the_bygone$getMapHandItem(LivingEntity entity) {
        ItemStack mapItem = entity.getMainHandItem();
        if (mapItem.isEmpty() || !(mapItem.getItem() instanceof MapItem)) {
            mapItem = entity.getOffhandItem();
        }
        return mapItem;
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
