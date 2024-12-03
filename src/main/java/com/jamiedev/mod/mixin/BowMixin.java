package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.items.VerdigrisBowItem;
import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedWeaponItem.class)
public class BowMixin {
    @Inject(method = "createArrowEntity", at = @At("TAIL"), cancellable = true)
    protected void bygone$createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> cir) {
        if (!(weaponStack.getItem() instanceof VerdigrisBowItem)) return;
        PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity) cir.getReturnValue();
        persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() * 0.1);
        cir.setReturnValue(persistentProjectileEntity);
    }
}
