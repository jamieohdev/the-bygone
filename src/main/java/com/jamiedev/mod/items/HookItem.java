package com.jamiedev.mod.items;

import com.jamiedev.mod.entities.projectile.HookEntity;
import com.jamiedev.mod.util.PlayerWithHook;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class HookItem extends Item
{
    static boolean isGrappling;

    public HookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        PlayerWithHook hookuser = (PlayerWithHook)user;
        ItemStack itemStack = user.getStackInHand(hand);
        HookEntity hook = hookuser.bygone$getHook();
        boolean secondaryUse = user.shouldCancelInteraction();
        boolean used = false;
        if(!secondaryUse){
            if(hook != null){
            } else{
            }
            user.setCurrentHand(hand);
            used = true;
        }
        if (hook != null && secondaryUse) {
            retrieve(world, user, hook);
            used = true;
        }

        return used ? TypedActionResult.consume(itemStack) : TypedActionResult.fail(itemStack);
    }

    private static void retrieve(World level, PlayerEntity player, HookEntity hook) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClient()) {
            hook.discard();
            ((PlayerWithHook)player).bygone$setHook(null);
            isGrappling = false;
        }

        player.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            HookEntity hook = ((PlayerWithHook) player).bygone$getHook();
            if (hook != null) {
                if(remainingUseTicks % 5 == 0){
                    world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                }



                if (hook.isInsideWall())
                {
                    grapple(hook, player);
                }


            }
        }
    }

    // Based on how TridentItem launches the player when enchanted with Riptide
    private static void grapple(HookEntity hook, PlayerEntity player) {
        float xStep = (float) (hook.getX() - player.getX());
        float yStep = (float) (hook.getY() - player.getY());
        float zStep = (float) (hook.getZ() - player.getZ());
        float distance = MathHelper.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
        int speedLevel = 1;
        float customScale = 0.1F;
        float speed = 3.0F * ((1.0F + (float) speedLevel) / 4.0F) * customScale;
        xStep *= speed / distance;
        yStep *= speed / distance;
        zStep *= speed / distance;
        player.addVelocity(xStep, yStep, zStep);
        isGrappling = true;
        // Bump the player up by 1.2 blocks if they're on the ground or horizontally colliding with a block
        if (player.isOnGround() || player.horizontalCollision) {
            player.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999F, 0.0D));
        }
        Vec3d vec3d = player.getVelocity();

        if (!player.isOnGround() && vec3d.y < 0.0) {
            player.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
            player.fallDistance--;
        }

    }


    // Similar to a bow
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            if (((PlayerWithHook) player).bygone$getHook() != null) return;

            int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (useTime < 0) return;

            float powerForTime = getPullProgress(useTime);
            if (powerForTime >= 0.1D) {
                if (!world.isClient) {
                    stack.damage(1, player, LivingEntity.getSlotForHand(user.getActiveHand()));
                    HookEntity hook = new HookEntity(world, player);
                    this.shoot(user, hook, powerForTime *  8.0F);
                    if(world.spawnEntity(hook)){
                        ((PlayerWithHook)player).bygone$setHook(hook);
                    }
                    player.incrementStat(Stats.USED.getOrCreateStat(this));
                }
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            }
        }
    }

    public static float getPullProgress(int useTicks) {
        float pullProgress = (float)useTicks / 20.0F;
        pullProgress = (pullProgress * pullProgress + pullProgress * 2.0F) / 3.0F;
        if (pullProgress > 1.0F) {
            pullProgress = 1.0F;
        }

        return pullProgress;
    }


    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, float speed) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, speed, 0.0F);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }


}
