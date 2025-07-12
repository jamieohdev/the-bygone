package com.jamiedev.bygone.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.LevelTickAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {

    @Shadow public abstract ServerLevel getLevel();

    @Shadow
    private final ServerLevelData serverLevelData;

    @Shadow
    public static final IntProvider RAIN_DELAY = UniformInt.of(12000, 180000);
    @Shadow
    public static final IntProvider RAIN_DURATION = UniformInt.of(12000, 24000);
    @Shadow
    private static final IntProvider THUNDER_DELAY = UniformInt.of(12000, 180000);
    @Shadow
    public static final IntProvider THUNDER_DURATION = UniformInt.of(3600, 15600);

    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates, ServerLevelData serverLevelData) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
        this.serverLevelData = serverLevelData;
    }

    //TO RAIN IN DIMENSIONS THAT HAVE NO SKYLIGHT
    @Inject(method="advanceWeatherCycle", at = @At(value = "HEAD"))
    public void advanceWeatherCycle(CallbackInfo ci) {
        ServerLevel $this = (ServerLevel)  (Object)  this;
        boolean flag = $this.getLevel().isRaining();
        if ($this.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
            int i = this.serverLevelData.getClearWeatherTime();
            int j = this.serverLevelData.getThunderTime();
            int k = this.serverLevelData.getRainTime();
            boolean bl2 = $this.getLevelData().isThundering();
            boolean bl3 = $this.getLevelData().isRaining();
            if (i > 0) {
                --i;
                j = bl2 ? 0 : 1;
                k = bl3 ? 0 : 1;
                bl2 = false;
                bl3 = false;
            } else {
                if (j > 0) {
                    --j;
                    if (j == 0) {
                        bl2 = !bl2;
                    }
                } else if (bl2) {
                    j = THUNDER_DURATION.sample($this.random);
                } else {
                    j = THUNDER_DELAY.sample($this.random);
                }

                if (k > 0) {
                    --k;
                    if (k == 0) {
                        bl3 = !bl3;
                    }
                } else if (bl3) {
                    k = RAIN_DURATION.sample($this.random);
                } else {
                    k = RAIN_DELAY.sample($this.random);
                }
            }

            this.serverLevelData.setThunderTime(j);
            this.serverLevelData.setRainTime(k);
            this.serverLevelData.setClearWeatherTime(i);
            this.serverLevelData.setThundering(bl2);
            this.serverLevelData.setRaining(bl3);
        }

        this.oThunderLevel = this.thunderLevel;
        if ($this.getLevelData().isThundering()) {
            this.thunderLevel += 0.01F;
        } else {
            this.thunderLevel -= 0.01F;
        }

        this.thunderLevel = Mth.clamp(this.thunderLevel, 0.0F, 1.0F);
        this.oRainLevel = this.rainLevel;
        if ($this.getLevelData().isRaining()) {
            this.rainLevel += 0.01F;
        } else {
            this.rainLevel -= 0.01F;
        }

        this.rainLevel = Mth.clamp(this.rainLevel, 0.0F, 1.0F);

        if (this.oRainLevel != this.rainLevel) {
        $this.getServer().getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel), this.dimension());
    }

        if (this.oThunderLevel != this.thunderLevel) {
        $this.getServer().getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel), this.dimension());
    }


        if (flag != $this.isRaining()) {
        if (flag) {
            $this.getServer().getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.STOP_RAINING, 0.0F));
        } else {
            $this.getServer().getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
        }

        $this.getServer().getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel));
        $this.getServer().getPlayerList().broadcastAll(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel));
    }

    }

    @Shadow
    public void sendBlockUpdated(BlockPos blockPos, BlockState blockState, BlockState blockState1, int i) {

    }
    @Shadow
    public void playSeededSound(@Nullable Player player, double v, double v1, double v2, Holder<SoundEvent> holder, SoundSource soundSource, float v3, float v4, long l) {

    }
    @Shadow
    public void playSeededSound(@Nullable Player player, Entity entity, Holder<SoundEvent> holder, SoundSource soundSource, float v, float v1, long l) {

    }
    @Shadow
    public String gatherChunkSourceStats() {
        return "";
    }
    @Shadow
    public @Nullable Entity getEntity(int i) {
        return null;
    }
    @Shadow
    public TickRateManager tickRateManager() {
        return null;
    }
    @Shadow
    public @Nullable MapItemSavedData getMapData(MapId mapId) {
        return null;
    }
    @Shadow
    public void setMapData(MapId mapId, MapItemSavedData mapItemSavedData) {

    }
    @Shadow
    public MapId getFreeMapId() {
        return null;
    }
    @Shadow
    public void destroyBlockProgress(int i, BlockPos blockPos, int i1) {

    }
    @Shadow
    public Scoreboard getScoreboard() {
        return null;
    }
    @Shadow
    public RecipeManager getRecipeManager() {
        return null;
    }
    @Shadow
    protected LevelEntityGetter<Entity> getEntities() {
        return null;
    }
    @Shadow
    public PotionBrewing potionBrewing() {
        return null;
    }
    @Shadow
    public LevelTickAccess<Block> getBlockTicks() {
        return null;
    }
    @Shadow
    public LevelTickAccess<Fluid> getFluidTicks() {
        return null;
    }
    @Shadow
    public ChunkSource getChunkSource() {
        return null;
    }
    @Shadow
    public void levelEvent(@Nullable Player player, int i, BlockPos blockPos, int i1) {

    }
    @Shadow
    public void gameEvent(Holder<GameEvent> holder, Vec3 vec3, GameEvent.Context context) {

    }
    @Shadow
    public float getShade(Direction direction, boolean b) {
        return 0;
    }
    @Shadow
    public List<? extends Player> players() {
        return List.of();
    }
    @Shadow
    public Holder<Biome> getUncachedNoiseBiome(int i, int i1, int i2) {
        return null;
    }
    @Shadow
    public FeatureFlagSet enabledFeatures() {
        return null;
    }
}
