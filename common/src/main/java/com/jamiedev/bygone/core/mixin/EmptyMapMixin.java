package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.registry.BGDimensions;
import com.jamiedev.bygone.core.util.HeightGetter;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EmptyMapItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EmptyMapItem.class)
public class EmptyMapMixin {
    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MapItem;create(Lnet/minecraft/world/level/Level;IIBZZ)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack addHeightComponent(ItemStack original, @Local(argsOnly = true) Player entity) {
        if (entity.level().dimension() == BGDimensions.BYGONE_LEVEL_KEY)
            original.set(HeightGetter.MAP_HEIGHT, entity.getBlockY());
        return original;
    }
}
