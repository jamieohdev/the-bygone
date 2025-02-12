package com.jamiedev.bygone.mixin;

import com.jamiedev.bygone.entities.projectile.HookEntity;
import com.jamiedev.bygone.items.VerdigrisBladeItem;
import com.jamiedev.bygone.network.SyncPlayerHookS2C;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
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
        if(this.hookUUID == null) return null;

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
        else if(this.hookId > 0){
            Entity entityById = this.level().getEntity(this.hookId);
            this.bygone$setHook(entityById instanceof HookEntity foundHook ? foundHook : null);
            return this.hook;
        }
        // We don't have a hook, so return null
        else {
            return null;
        }
    }

    @Override
    public void bygone$setHook(@Nullable HookEntity pHook) {
        boolean changed = this.hook != pHook;
        this.hook = pHook;
        this.hookUUID = pHook == null ? null : pHook.getUUID();
        this.hookId = pHook == null ? 0 : pHook.getId();
        // Sync our hook to the client-side counterparts of other players and ourselves
        if(changed && !this.level().isClientSide){
            Collection<ServerPlayer> trackingMe = PlayerLookup.tracking(this);
            boolean sendToSelf = !trackingMe.contains(this);
            for (ServerPlayer player : trackingMe) {
                ServerPlayNetworking.send(player, new SyncPlayerHookS2C(pHook == null ? 0 : pHook.getId(), this.getUUID()));
            }
            if(sendToSelf){
                ServerPlayNetworking.send((ServerPlayer)(Object)this, new SyncPlayerHookS2C(pHook == null ? 0 : pHook.getId(), this.getUUID()));
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void post_writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci){
        if(this.hookUUID != null){
            nbt.putUUID("HookUUID", this.hookUUID);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void post_readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci){
        if(nbt.hasUUID("HookUUID")){
            this.hookUUID = nbt.getUUID("HookUUID");
        }
    }

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
    }

}
