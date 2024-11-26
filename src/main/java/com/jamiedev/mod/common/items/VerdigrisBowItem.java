package com.jamiedev.mod.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class VerdigrisBowItem  extends RangedWeaponItem {
    public static final int TICKS_PER_SECOND = 20;
    public static final int RANGE = 15;

    public VerdigrisBowItem(Item.Settings settings) {
        super(settings);
    }





    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed, divergence);
    }

    public static float getPullProgress(int useTicks) {
        float f = (float) useTicks / 10.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !user.getProjectileType(itemStack).isEmpty();
        ItemStack itemStack1 = user.getProjectileType(itemStack);
        if (!bl) {
            if (user instanceof PlayerEntity playerEntity) {

                if (!itemStack.isEmpty()) {
                    int i = this.getMaxUseTime(itemStack, user) - 1;
                    float f = getPullProgress(i);
                    if (!((double) f < 0.1)) {
                        List<ItemStack> list = load(itemStack, itemStack, playerEntity);
                        if (world instanceof ServerWorld serverWorld) {
                            if (!list.isEmpty()) {
                                this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), itemStack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity) null);
                            }
                        }

                        world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    }
                }
            }
        } else {
            if (user instanceof PlayerEntity playerEntity) {
                if (!itemStack.isEmpty()) {
                    int i = this.getMaxUseTime(itemStack, user) - 1;
                    float f = getPullProgress(i);
                    if (!((double) f < 0.1)) {
                        List<ItemStack> list = load(itemStack, itemStack, playerEntity);
                        if (world instanceof ServerWorld serverWorld) {
                            if (!list.isEmpty()) {
                                this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), itemStack1, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity) null);
                            }
                        }

                        world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    }
                }
            }
        }
        return TypedActionResult.consume(itemStack1);
    }

    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    public int getRange() {
        return 15;
    }
}