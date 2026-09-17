package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;
import com.jamiedev.bygone.core.extension.LivingEntityExtension;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {

    @Unique private static List<EntityType<?>> bygone$HAUNTINGS_MOB_TYPES;

    @WrapOperation(
        method = "spawnForChunk",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/NaturalSpawner$SpawnState;canSpawnForCategory(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/world/level/ChunkPos;)Z"
        )
    )
    private static boolean bygone$canSpawnForHauntingsCategory(
        NaturalSpawner.SpawnState spawnState, MobCategory category, ChunkPos pos, Operation<Boolean> original,
        ServerLevel level
    ) {
        if (category == HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY
        && !HauntingsCategoryHolder.checkHauntingsActive(level)) return false;
        return original.call(spawnState, category, pos);
    }

    @Inject(method = "getRandomSpawnMobAt", at = @At("HEAD"), cancellable = true)
    private static void bygone$getHauntingsMobPool(
        ServerLevel level, StructureManager structureManager, ChunkGenerator generator,
        MobCategory category, RandomSource random, BlockPos pos,
        CallbackInfoReturnable<Optional<MobSpawnSettings.SpawnerData>> cir
    ) {
        if (category != HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY) return;
        if (!HauntingsCategoryHolder.checkHauntingsActive(level)) return;

        List<EntityType<?>> mobs = bygone$getHauntingsMobTypes();
        EntityType<?> chosen = mobs.get(random.nextInt(mobs.size()));
        cir.setReturnValue(Optional.of(new MobSpawnSettings.SpawnerData(
            chosen, 10, 1, 2
        )));
    }

    @Inject(method = "isValidSpawnPostitionForType", at = @At("HEAD"), cancellable = true)
    private static void bygone$allowHauntingsOnValidGround(
        ServerLevel level, MobCategory category, StructureManager structureManager,
        ChunkGenerator generator, MobSpawnSettings.SpawnerData data, BlockPos.MutableBlockPos pos,
        double distance, CallbackInfoReturnable<Boolean> cir
    ) {
        if (category != HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY) return;
        if (data.type.getCategory() != HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY) return;
        if (!HauntingsCategoryHolder.checkHauntingsActive(level)) return;

        cir.setReturnValue(level.noCollision(data.type.getSpawnAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D)));
    }

    @Inject(method = "isValidPositionForMob", at = @At("HEAD"), cancellable = true)
    private static void bygone$allowHauntingsMobPosition(
        ServerLevel level, Mob mob, double distance, CallbackInfoReturnable<Boolean> cir
    ) {
        if (mob.getType().getCategory() != HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY) return;
        if (!HauntingsCategoryHolder.checkHauntingsActive(level)) return;

        cir.setReturnValue(true);
    }

    @Inject(
        method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"
        ),
        cancellable = true
    )
    private static void bygone$prepareHauntingMobRise(
        CallbackInfo ci, @Local(ordinal = 0) Mob mob
    ) {
        if (mob.getType().getCategory() != HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY) return;
        if (!HauntingsCategoryHolder.checkHauntingsActive(mob.level())) return;

        Level level = mob.level();
        BlockPos.MutableBlockPos pos = mob.blockPosition().mutable();
        while (pos.getY() >= level.getMinBuildHeight()) {
            BlockState state = level.getBlockState(pos);
            if (bygone$isHauntingsGround(level, pos, state)) break;
            pos.move(Direction.DOWN);
        }
        if (pos.getY() < level.getMinBuildHeight()) {
            mob.discard();
            ci.cancel();
        }

        mob.moveTo(mob.getX(), pos.getY() + 0.5D - mob.getBbHeight(), mob.getZ());
        ((LivingEntityExtension) mob).bygone$startHauntingsRise();
    }

    @Unique
    private static List<EntityType<?>> bygone$getHauntingsMobTypes() {
        if (bygone$HAUNTINGS_MOB_TYPES == null) {
            bygone$HAUNTINGS_MOB_TYPES = BuiltInRegistries.ENTITY_TYPE.stream()
                .filter((type) -> type.getCategory() == HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY)
                .toList();
        }
        return bygone$HAUNTINGS_MOB_TYPES;
    }

    @Unique
    private static boolean bygone$isHauntingsGround(Level level, BlockPos pos, BlockState state) {
        return !state.isAir() && state.isFaceSturdy(level, pos, Direction.UP);
    }
}
