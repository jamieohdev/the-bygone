package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import com.jamiedev.bygone.core.network.SyncPlayerHookS2C;
import com.jamiedev.bygone.core.platform.Services;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerWithHook {
    @Unique
    @Nullable
    private UUID hookUUID;
    @Unique
    private int hookId;
    @Unique
    @Nullable
    private HookEntity hook;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public @Nullable HookEntity bygone$getHook() {
        if (this.hookUUID == null) return null;

        // This is definitely our hook, so retrieve it
        if (this.hook != null && !this.hook.isRemoved() && this.hookId == this.hook.getId()) {
            return this.hook;
        }
        // If we are on the server-side, look up the hook by the uuid we stored
        else if (this.level() instanceof ServerLevel serverWorld) {
            Entity entityByUuid = serverWorld.getEntity(this.hookUUID);
            this.bygone$setHook(entityByUuid instanceof HookEntity foundHook ? foundHook : null);
            return this.hook;
        }
        // If we are on the client-side, look up the hook by the id we stored
        else if (this.hookId > 0) {
            Entity entityById = this.level().getEntity(this.hookId);
            this.bygone$setHook(entityById instanceof HookEntity foundHook ? foundHook : null);
            return this.hook;
        }
        // We don't have a hook, so return null
        else {
            return null;
        }
    }

    private void carapaceHelmTick() {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.LEGS);
        if (itemstack.is(BGItems.CARAPACE_GREAVES.get()) && this.isEyeInFluid(FluidTags.WATER)) {
            this.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 200, 0, false, false, false));
        }
    }

    @Override
    public void bygone$setHook(@Nullable HookEntity pHook) {
        boolean changed = this.hook != pHook;
        this.hook = pHook;
        this.hookUUID = pHook == null ? null : pHook.getUUID();
        this.hookId = pHook == null ? 0 : pHook.getId();
        // Sync our hook to the client-side counterparts of other players and ourselves
        if (changed && !this.level().isClientSide) {
            Services.PLATFORM.sendToTracking(new SyncPlayerHookS2C(pHook == null ? 0 : pHook.getId(), this.getUUID()), this, true);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void post_writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
        if (this.hookUUID != null) {
            nbt.putUUID("HookUUID", this.hookUUID);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void post_readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.hasUUID("HookUUID")) {
            this.hookUUID = nbt.getUUID("HookUUID");
        }
    }

    @Inject(at = @At("RETURN"), method = "tick()V")
    public void tick(CallbackInfo ci) {
        this.carapaceHelmTick();
    }

    /*todo use forge event
    @Inject(method = "hurt", at = @At("TAIL"), cancellable = true)
    public void jamies_mod$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!(getUseItem().getItem() instanceof VerdigrisBladeItem)) return;

        boolean bl = cir.getReturnValue();

        if (amount > 0.0f && this.isDamageSourceBlocked(source)) {
            Entity entity;
            if (!source.is(DamageTypeTags.IS_PROJECTILE) && (entity = source.getDirectEntity()) instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                this.blockUsingShield(livingEntity);
            }
            bl = true;

            this.actuallyHurt(source, amount * 0.5f);
        }

        cir.setReturnValue(bl);
    }*/

}
