package com.jamiedev.bygone.common.entity.projectile;

import com.google.common.base.MoreObjects;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScuttleSpikeEntity extends AbstractArrow {
    private boolean dealtDamage;

    public ScuttleSpikeEntity(EntityType<? extends ScuttleSpikeEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ScuttleSpikeEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(BGEntityTypes.SCUTTLE_SPIKE.get(), owner, world, stack, null);
    }

    public ScuttleSpikeEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(BGEntityTypes.SCUTTLE_SPIKE.get(), x, y, z, world, stack, stack);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (this.inGroundTime > 8) {
            this.kill();
        }

        super.tick();
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 currentPosition, Vec3 nextPosition) {
        return this.dealtDamage ? null : super.findHitEntity(currentPosition, nextPosition);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 8.0F;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, entity2 == null ? this : entity2);
        Level var7 = this.level();
        if (var7 instanceof ServerLevel serverWorld) {
            f = EnchantmentHelper.modifyDamage(serverWorld, Objects.requireNonNull(this.getWeaponItem()), entity, damageSource, f);
        }

        this.dealtDamage = true;
        if (entity.hurt(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            var7 = this.level();
            if (var7 instanceof ServerLevel serverWorld) {
                serverWorld = (ServerLevel) var7;
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverWorld, entity, damageSource, this.getWeaponItem());
            }

            if (entity instanceof LivingEntity livingEntity) {
                this.doKnockback(livingEntity, damageSource);
                this.doPostHurtEffects(livingEntity);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200), MoreObjects.firstNonNull(entity2, this));
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.GLOW_INK_SAC_USE, 1.0F, 1.0F);
    }

    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        this.kill();
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(BGItems.SCUTTLE_SPIKE.get());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }

    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }


    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}
