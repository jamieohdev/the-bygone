package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.item.BaitwormWaterEffect;
import com.jamiedev.bygone.core.registry.BGFishingTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
    
    @Shadow private int timeUntilLured;
    @Shadow private int timeUntilHooked;

    protected FishingHookMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(
        method = "catchingFish",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/projectile/FishingHook;timeUntilLured:I",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    private void reduceFishingTimeInBaitwormWater(BlockPos pos, CallbackInfo ci) {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (BaitwormWaterEffect.isInBaitwormWater(serverLevel, this.blockPosition())) {
                this.timeUntilLured = Mth.clamp(this.timeUntilLured / 2, 20, 600);
            }
        }
    }

    @Redirect(
        method = "retrieve",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/storage/loot/BuiltInLootTables;FISHING:Lnet/minecraft/resources/ResourceKey;"
        )
    )
    private ResourceKey<LootTable> redirectFishingLootTable() {
        FishingHook hook = (FishingHook)(Object)this;
        
        if (hook.level() instanceof ServerLevel serverLevel) {
            BlockPos pos = this.blockPosition();
            Holder<Biome> biomeHolder = serverLevel.getBiome(pos);
            
            ResourceKey<LootTable> customTable = BGFishingTables.getFishingTableForBiome(biomeHolder);
            if (customTable != null) {
                return customTable;
            }
        }
        
        return BuiltInLootTables.FISHING;
    }
}