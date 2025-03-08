package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGItems;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VerdigrisBowItem extends BowItem {
    public VerdigrisBowItem(Properties settings) {
        super(settings);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {}

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.onUseTick(world, user, stack, remainingUseTicks);
        arrowShootLogic(user, stack, world);
    }

    public void arrowShootLogic(Entity user, ItemStack stack, Level world) {
        if (user instanceof Player playerEntity) {
            ItemStack itemStack = playerEntity.getProjectile(stack);
            if (!itemStack.isEmpty()) {
                List<ItemStack> list = draw(stack, itemStack, playerEntity);
                if (world instanceof ServerLevel serverWorld) {
                    if (!list.isEmpty()) {
                        this.shoot(serverWorld, playerEntity, playerEntity.getUsedItemHand(), stack, list, 1 * 3.0F, 1.0F, false, null);
                    }
                }

                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
                playerEntity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        AbstractArrow projectile = (AbstractArrow) super.createProjectile(level, shooter, weapon, ammo, isCrit);
        projectile.setBaseDamage(projectile.getBaseDamage() * 0.1);
        return projectile;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return ingredient.is(BGItems.VERDIGRIS_INGOT.get());
    }
}
