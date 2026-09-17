package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.entity.projectile.ExoticArrowEntity;
import com.jamiedev.bygone.common.entity.projectile.LithoArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LithoplasmArrowItem extends ArrowItem {
    TridentItem ref;

    public LithoplasmArrowItem(Properties settings) {
        super(settings);
    }

    @Override
    public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new LithoArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
        LithoArrowEntity arrowEntity = new LithoArrowEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        arrowEntity.pickup = AbstractArrow.Pickup.DISALLOWED;
        return arrowEntity;
    }
}