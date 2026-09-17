package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.common.weather.InvertedHeightmap;
import com.jamiedev.bygone.core.extension.LevelChunkExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements LevelChunkExtension {

    @Unique private InvertedHeightmap bygone$InvertedHeightmap;
    @Override public InvertedHeightmap bygone$getInvertedHeightmap() {
        if (bygone$InvertedHeightmap != null && bygone$InvertedHeightmap.dirty)
            bygone$InvertedHeightmap.primeSelf();
        return bygone$InvertedHeightmap;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/ticks/LevelChunkTicks;Lnet/minecraft/world/ticks/LevelChunkTicks;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;Lnet/minecraft/world/level/levelgen/blending/BlendingData;)V",
        at = @At("TAIL")
    )
    private void bygone$levelChunkConstructor(
        Level level, ChunkPos pos, UpgradeData data,
        LevelChunkTicks blockTicks, LevelChunkTicks fluidTicks,
        long inhabitedTime, LevelChunkSection[] sections,
        LevelChunk.PostLoadProcessor postLoad,
        BlendingData blendingData, CallbackInfo ci
    ) {
        bygone$InvertedHeightmap = new InvertedHeightmap((LevelChunk) (Object) this);
    }

    @Inject(
        method = "setBlockState",
        at = @At("RETURN")
    )
    private void bygone$setHeightmaps(BlockPos pos, BlockState state, boolean isMoving, CallbackInfoReturnable<BlockState> cir) {
        int previousHeight = bygone$InvertedHeightmap.getHeight(pos.getX(), pos.getZ());
        if (pos.getY() <= previousHeight + 1) bygone$InvertedHeightmap.encapsulatedPrime(new BlockPos.MutableBlockPos(), pos.getX(), pos.getZ());
    }
}
