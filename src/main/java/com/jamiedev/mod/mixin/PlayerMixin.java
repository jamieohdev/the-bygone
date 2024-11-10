package com.jamiedev.mod.mixin;

import com.jamiedev.mod.common.entities.projectile.HookEntity;
import com.jamiedev.mod.common.network.SyncPlayerHookS2C;
import com.jamiedev.mod.common.util.PlayerWithHook;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerWithHook {
    @Unique
    @Nullable
    private UUID hookUUID;
    @Unique
    private int hookId;
    @Unique
    @Nullable
    private HookEntity hook;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
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
        else if (this.getWorld() instanceof ServerWorld serverWorld) {
            Entity entityByUuid = serverWorld.getEntity(this.hookUUID);
            this.bygone$setHook(entityByUuid instanceof HookEntity foundHook ? foundHook : null);
            return this.hook;
        }
        // If we are on the client-side, look up the hook by the id we stored
        else if(this.hookId > 0){
            Entity entityById = this.getWorld().getEntityById(this.hookId);
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
        this.hookUUID = pHook == null ? null : pHook.getUuid();
        this.hookId = pHook == null ? 0 : pHook.getId();
        // Sync our hook to the client-side counterparts of other players and ourselves
        if(changed && !this.getWorld().isClient){
            Collection<ServerPlayerEntity> trackingMe = PlayerLookup.tracking(this);
            boolean sendToSelf = !trackingMe.contains(this);
            for (ServerPlayerEntity player : trackingMe) {
                ServerPlayNetworking.send(player, new SyncPlayerHookS2C(pHook == null ? 0 : pHook.getId(), this.getUuid()));
            }
            if(sendToSelf){
                ServerPlayNetworking.send((ServerPlayerEntity)(Object)this, new SyncPlayerHookS2C(pHook == null ? 0 : pHook.getId(), this.getUuid()));
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void post_writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        if(this.hookUUID != null){
            nbt.putUuid("HookUUID", this.hookUUID);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void post_readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        if(nbt.containsUuid("HookUUID")){
            this.hookUUID = nbt.getUuid("HookUUID");
        }
    }


}
