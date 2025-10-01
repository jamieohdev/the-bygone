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
import org.spongepowered.asm.mixin.injection.ModifyArg;
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

    @ModifyArg(
        method = "retrieve",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/ReloadableServerRegistries$Holder;getLootTable(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/storage/loot/LootTable;"
        ),
        index = 0
    )
    private ResourceKey<LootTable> modifyFishingLootTable(ResourceKey<LootTable> original) {
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockPos pos = this.blockPosition();
            Holder<Biome> biomeHolder = serverLevel.getBiome(pos);
            
            boolean isInBaitwormWater = BaitwormWaterEffect.isInBaitwormWater(serverLevel, pos);
            boolean useRareTable = isInBaitwormWater && serverLevel.random.nextFloat() < 0.5F;
            
            ResourceKey<LootTable> customTable = BGFishingTables.getFishingTableForBiome(biomeHolder, useRareTable);
            System.out.println("Fishing at " + pos + " in biome " + biomeHolder.unwrapKey().orElse(null) + (isInBaitwormWater ? " with Baitworm effect" : "") + ", using table: " + (customTable != null ? customTable.location() : "default"));
            if (customTable != null) {
                return customTable;
            }
        }
        
        return original;
    }
}