package com.jamiedev.mod.blocks.entity;

import com.google.common.annotations.VisibleForTesting;
import com.jamiedev.mod.blocks.BlemishCatalystBlock;
import com.jamiedev.mod.init.JamiesModBlockEntities;
import com.jamiedev.mod.init.JamiesModCriteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Nullables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

public class BlemishCatalystBlockEntity extends BlockEntity implements GameEventListener.Holder<BlemishCatalystBlockEntity.Listener> {
    private final BlemishCatalystBlockEntity.Listener eventListener;

    public BlemishCatalystBlockEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.BLEMISH_CATALYST, pos, state);
        this.eventListener = new BlemishCatalystBlockEntity.Listener(state, new BlockPositionSource(pos));
    }

    public static void tick(World world, BlockPos pos, BlockState state, BlemishCatalystBlockEntity blockEntity) {
        blockEntity.eventListener.getSpreadManager().tick(world, pos, world.getRandom(), true);
    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.eventListener.spreadManager.readNbt(nbt);
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.eventListener.spreadManager.writeNbt(nbt);
        super.writeNbt(nbt, registryLookup);
    }

    public BlemishCatalystBlockEntity.Listener getEventListener() {
        return this.eventListener;
    }

    public static class Listener implements GameEventListener {
        public static final int RANGE = 8;
        final BlemishSpreadManager spreadManager;
        private final BlockState state;
        private final PositionSource positionSource;

        public Listener(BlockState state, PositionSource positionSource) {
            this.state = state;
            this.positionSource = positionSource;
            this.spreadManager = BlemishSpreadManager.create();
        }

        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        public int getRange() {
            return 8;
        }

        public GameEventListener.TriggerOrder getTriggerOrder() {
            return TriggerOrder.BY_DISTANCE;
        }

        public boolean listen(ServerWorld world, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (event.matches(GameEvent.ENTITY_DIE)) {
                Entity var6 = emitter.sourceEntity();
                if (var6 instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)var6;
                    if (!livingEntity.isExperienceDroppingDisabled()) {
                        DamageSource damageSource = livingEntity.getRecentDamageSource();
                        int i = livingEntity.getXpToDrop(world, (Entity) Nullables.map(damageSource, DamageSource::getAttacker));
                        if (livingEntity.shouldDropXp() && i > 0) {
                            this.spreadManager.spread(BlockPos.ofFloored(emitterPos.offset(Direction.UP, 0.5)), i);
                            this.triggerCriteria(world, livingEntity);
                        }

                        livingEntity.disableExperienceDropping();
                        this.positionSource.getPos(world).ifPresent((pos) -> {
                            this.bloom(world, BlockPos.ofFloored(pos), this.state, world.getRandom());
                        });
                    }

                    return true;
                }
            }

            return false;
        }

        @VisibleForTesting
        public BlemishSpreadManager getSpreadManager() {
            return this.spreadManager;
        }

        private void bloom(ServerWorld world, BlockPos pos, BlockState state, Random random) {
            world.setBlockState(pos, (BlockState)state.with(BlemishCatalystBlock.BLOOM, true), 3);
            world.scheduleBlockTick(pos, state.getBlock(), 8);
            world.spawnParticles(ParticleTypes.WITCH, (double)pos.getX() + 0.5, (double)pos.getY() + 1.15, (double)pos.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
            world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 2.0F, 0.6F + random.nextFloat() * 0.4F);
        }

        private void triggerCriteria(World world, LivingEntity deadEntity) {
            LivingEntity livingEntity = deadEntity.getAttacker();
            if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                DamageSource damageSource = deadEntity.getRecentDamageSource() == null ? world.getDamageSources().playerAttack(serverPlayerEntity) : deadEntity.getRecentDamageSource();
                JamiesModCriteria.KILLED_BY_BLEMISH_CRITERION.trigger(serverPlayerEntity, deadEntity, damageSource);
            }

        }
    }
}
