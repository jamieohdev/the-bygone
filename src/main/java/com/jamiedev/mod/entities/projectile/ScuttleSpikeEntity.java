package com.jamiedev.mod.entities.projectile;

import com.google.common.base.MoreObjects;
import com.jamiedev.mod.init.JamiesModEntityTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScuttleSpikeEntity  extends PersistentProjectileEntity
{
    private boolean dealtDamage;

    public ScuttleSpikeEntity(EntityType<? extends ScuttleSpikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public ScuttleSpikeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(JamiesModEntityTypes.SCUTTLE_SPIKE, owner, world, stack, (ItemStack)null);
    }

    public ScuttleSpikeEntity(World world, double x, double y, double z, ItemStack stack) {
        super(JamiesModEntityTypes.SCUTTLE_SPIKE, x, y, z, world, stack, stack);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (this.inGroundTime > 8)
        {
            this.kill();
        }

        super.tick();
    }
    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 8.0F;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, (Entity)(entity2 == null ? this : entity2));
        World var7 = this.getWorld();
        if (var7 instanceof ServerWorld serverWorld) {
            f = EnchantmentHelper.getDamage(serverWorld, Objects.requireNonNull(this.getWeaponStack()), entity, damageSource, f);
        }

        this.dealtDamage = true;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            var7 = this.getWorld();
            if (var7 instanceof ServerWorld serverWorld) {
                serverWorld = (ServerWorld)var7;
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack());
            }

            if (entity instanceof LivingEntity livingEntity) {
                this.knockback(livingEntity, damageSource);
                this.onHit(livingEntity);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200), (Entity) MoreObjects.firstNonNull(entity2, this));
            }
        }

        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ITEM_GLOW_INK_SAC_USE, 1.0F, 1.0F);
    }

    protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        this.kill();
    }

    public ItemStack getWeaponStack() {
        return this.getItemStack();
    }

    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.TRIDENT);
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }

    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }


    protected float getDragInWater() {
        return 0.99F;
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}
