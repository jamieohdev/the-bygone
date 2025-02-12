package com.jamiedev.bygone.mixin;

import com.jamiedev.bygone.items.VerdigrisBowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public class BowMixin {
    @Inject(method = "createProjectile", at = @At("TAIL"), cancellable = true)
    protected void bygone$createArrowEntity(Level world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<Projectile> cir) {
        if (!(weaponStack.getItem() instanceof VerdigrisBowItem)) return;
        AbstractArrow persistentProjectileEntity = (AbstractArrow) cir.getReturnValue();
        persistentProjectileEntity.setBaseDamage(persistentProjectileEntity.getBaseDamage() * 0.1);
        cir.setReturnValue(persistentProjectileEntity);
    }
}
