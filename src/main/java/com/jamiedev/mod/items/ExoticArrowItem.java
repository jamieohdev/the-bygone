package com.jamiedev.mod.items;

import com.jamiedev.mod.entities.projectile.ExoticArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExoticArrowItem extends ArrowItem {
    public ExoticArrowItem(Item.Settings settings) {
        super(settings);
    }

    TridentItem ref;

    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new ExoticArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ExoticArrowEntity arrowEntity = new ExoticArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), (ItemStack)null);
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        return arrowEntity;
    }
}