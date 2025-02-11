package com.jamiedev.mod.common.items;

import com.jamiedev.mod.common.entities.projectile.ExoticArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ExoticArrowItem extends ArrowItem {
    public ExoticArrowItem(Item.Properties settings) {
        super(settings);
    }

    TridentItem ref;

    public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new ExoticArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
        ExoticArrowEntity arrowEntity = new ExoticArrowEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), (ItemStack)null);
        arrowEntity.pickup = AbstractArrow.Pickup.DISALLOWED;
        return arrowEntity;
    }
}