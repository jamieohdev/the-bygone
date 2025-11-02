package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class HookItem extends Item {
    static boolean isGrappling;

    public HookItem(Properties settings) {
        super(settings);
    }

    private static void retrieve(Level level, Player player, HookEntity hook) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), BGSoundEvents.HOOK_RETRIEVE_ADDITIONS_EVENT, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide()) {
            hook.discard();
            ((PlayerWithHook) player).bygone$setHook(null);
            isGrappling = false;
        }

        player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }

    // Based on how TridentItem launches the player when enchanted with Riptide
    private static void grapple(HookEntity hook, Player player) {
        float xStep = (float) (hook.getX() - player.getX());
        float yStep = (float) (hook.getY() - player.getY());
        float zStep = (float) (hook.getZ() - player.getZ());
        float distance = Mth.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
        int speedLevel = 1;
        float customScale = 0.1F;
        float speed = 3.0F * ((1.0F + (float) speedLevel) / 4.0F) * customScale;
        xStep *= speed / distance;
        yStep *= speed / distance;
        zStep *= speed / distance;
        player.push(xStep, yStep, zStep);
        isGrappling = true;
        // Bump the player up by 1.2 blocks if they're on the ground or horizontally colliding with a block
        if (player.onGround() || player.horizontalCollision) {
            player.move(MoverType.SELF, new Vec3(0.0D, 0.1F, 0.0D));
        }
        Vec3 vec3d = player.getDeltaMovement();

        if (!player.onGround() && vec3d.y < 0.0) {
            player.setDeltaMovement(vec3d.multiply(1.0, 0.6, 1.0));
            player.fallDistance--;
        }

    }

    public static float getPullProgress(int useTicks) {
        float pullProgress = (float) useTicks / 20.0F;
        pullProgress = (pullProgress * pullProgress + pullProgress * 2.0F) / 3.0F;
        if (pullProgress > 1.0F) {
            pullProgress = 1.0F;
        }

        return pullProgress;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        PlayerWithHook hookuser = (PlayerWithHook) user;
        ItemStack itemStack = user.getItemInHand(hand);
        HookEntity hook = hookuser.bygone$getHook();
        boolean secondaryUse = user.isSecondaryUseActive();
        boolean used = false;
        if (!secondaryUse) {
            user.startUsingItem(hand);
            used = true;
            user.awardStat(Stats.ITEM_USED.get(this));
            user.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
        if (hook != null && secondaryUse) {
            retrieve(world, user, hook);
            used = true;
            user.awardStat(Stats.ITEM_USED.get(this));
            user.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
        user.awardStat(Stats.ITEM_USED.get(this));
        user.gameEvent(GameEvent.ITEM_INTERACT_START);
        return used ? InteractionResultHolder.consume(itemStack) : InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof Player player) {
            HookEntity hook = ((PlayerWithHook) player).bygone$getHook();
            if (hook != null) {
                if (remainingUseTicks % 5 == 0) {
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), BGSoundEvents.HOOK_RETRIEVE_ADDITIONS_EVENT, SoundSource.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                }

                if (hook.isInWall()) {
                    grapple(hook, player);
                }


            }
        }
    }

    // Similar to a bow
    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player player) {
            if (((PlayerWithHook) player).bygone$getHook() != null) return;

            int useTime = this.getUseDuration(stack, user) - remainingUseTicks;
            if (useTime < 0) return;

            float powerForTime = getPullProgress(useTime);
            if (powerForTime >= 0.1D) {
                if (!world.isClientSide) {
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(user.getUsedItemHand()));
                    HookEntity hook = new HookEntity(world, player);
                    this.shoot(user, hook, powerForTime * 15.0F);
                    if (world.addFreshEntity(hook)) {
                        ((PlayerWithHook) player).bygone$setHook(hook);
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
                world.playSound(null, user.getX(), user.getY(), user.getZ(), BGSoundEvents.HOOK_THROW_ADDITIONS_EVENT, SoundSource.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            }
        }
    }

    protected void shoot(LivingEntity shooter, Projectile projectile, float speed) {
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed, 0.0F);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }


}
