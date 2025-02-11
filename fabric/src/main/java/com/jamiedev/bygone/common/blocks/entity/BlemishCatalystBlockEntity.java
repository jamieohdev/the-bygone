package com.jamiedev.bygone.common.blocks.entity;

import com.google.common.annotations.VisibleForTesting;
import com.jamiedev.bygone.common.blocks.BlemishCatalystBlock;
import com.jamiedev.bygone.fabric.init.JamiesModBlockEntities;
import com.jamiedev.bygone.fabric.init.JamiesModCriteria;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class BlemishCatalystBlockEntity extends BlockEntity implements GameEventListener.Provider<BlemishCatalystBlockEntity.Listener> {
    private final BlemishCatalystBlockEntity.Listener eventListener;

    public BlemishCatalystBlockEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.BLEMISH_CATALYST, pos, state);
        this.eventListener = new BlemishCatalystBlockEntity.Listener(state, new BlockPositionSource(pos));
    }

    public static void tick(Level world, BlockPos pos, BlockState state, BlemishCatalystBlockEntity blockEntity) {
        blockEntity.eventListener.getSpreadManager().tick(world, pos, world.getRandom(), true);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        this.eventListener.spreadManager.readNbt(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        this.eventListener.spreadManager.writeNbt(nbt);
        super.saveAdditional(nbt, registryLookup);
    }

    @Override
    public BlemishCatalystBlockEntity.Listener getListener() {
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

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return 8;
        }

        @Override
        public GameEventListener.DeliveryMode getDeliveryMode() {
            return DeliveryMode.BY_DISTANCE;
        }

        @Override
        public boolean handleGameEvent(ServerLevel world, Holder<GameEvent> event, GameEvent.Context emitter, Vec3 emitterPos) {
            if (event.is(GameEvent.ENTITY_DIE)) {
                Entity var6 = emitter.sourceEntity();
                if (var6 instanceof LivingEntity livingEntity) {
                    if (!livingEntity.wasExperienceConsumed()) {
                        DamageSource damageSource = livingEntity.getLastDamageSource();
                        int i = livingEntity.getExperienceReward(world, Optionull.map(damageSource, DamageSource::getEntity));
                        if (livingEntity.shouldDropExperience() && i > 0) {
                            this.spreadManager.spread(BlockPos.containing(emitterPos.relative(Direction.UP, 0.5)), i);
                            this.triggerCriteria(world, livingEntity);
                        }

                        livingEntity.skipDropExperience();
                        this.positionSource.getPosition(world).ifPresent((pos) -> {
                            this.bloom(world, BlockPos.containing(pos), this.state, world.getRandom());
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

        private void bloom(ServerLevel world, BlockPos pos, BlockState state, RandomSource random) {
            world.setBlock(pos, state.setValue(BlemishCatalystBlock.BLOOM, true), 3);
            world.scheduleTick(pos, state.getBlock(), 8);
            world.sendParticles(ParticleTypes.WITCH, (double)pos.getX() + 0.5, (double)pos.getY() + 1.15, (double)pos.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
            world.playSound(null, pos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + random.nextFloat() * 0.4F);
        }

        private void triggerCriteria(Level world, LivingEntity deadEntity) {
            LivingEntity livingEntity = deadEntity.getLastHurtByMob();
            if (livingEntity instanceof ServerPlayer serverPlayerEntity) {
                DamageSource damageSource = deadEntity.getLastDamageSource() == null ? world.damageSources().playerAttack(serverPlayerEntity) : deadEntity.getLastDamageSource();
                JamiesModCriteria.KILLED_BY_BLEMISH_CRITERION.trigger(serverPlayerEntity, deadEntity, damageSource);
            }

        }
    }
}
