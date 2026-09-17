package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BygonePortalEntity extends LivingEntity implements Portal {
    private static final EntityDataAccessor<Integer> DATA_LIFETIME = SynchedEntityData.defineId(BygonePortalEntity .class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_RETURN = SynchedEntityData.defineId(BygonePortalEntity .class, EntityDataSerializers.BOOLEAN);
    private int triggerCooldown = 0;
    public boolean wasActivated = false;

    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState triggerAnimationState = new AnimationState();
    public AnimationState activeAnimationState = new AnimationState();

    public Map<Player, Integer> teleportCountdown = new HashMap<>();

    public BygonePortalEntity(EntityType<? extends BygonePortalEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public BygonePortalEntity(Level level) {
        this(BGEntityTypes.BYGONE_PORTAL.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LIFETIME, 12000);
        builder.define(DATA_RETURN, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("LifeTime")) {
            this.setLifeTime(compound.getInt("LifeTime"));
        }
        if (compound.contains("isReturn")) {
            this.setReturn(compound.getBoolean("isReturn"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("LifeTime", getLifeTime());
        compound.putBoolean("isReturn", isReturn());
    }

    public void setLifeTime(int lifeTime) {
        this.getEntityData().set(DATA_LIFETIME, lifeTime);
    }

    public int getLifeTime() {
        return this.getEntityData().get(DATA_LIFETIME);
    }

    public void setReturn(boolean isReturn) {
        this.getEntityData().set(DATA_RETURN, isReturn);
    }

    public boolean isReturn() {
        return this.getEntityData().get(DATA_RETURN);
    }

    public ItemStack getPickResult() {
        return new ItemStack(BGItems.ARCANE_MECHANISM.get());
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            int lifetime = getLifeTime();
            if (lifetime <= 0) {
                this.discard();
                return;
            }
            setLifeTime(lifetime - 1);
        }

        if (this.level() instanceof ServerLevel serverLevel && this.isReturn()) {
            List<BygonePortalEntity> existingPortals = serverLevel
                    .getEntities(this, this.getBoundingBox().inflate(3), entity -> entity instanceof BygonePortalEntity)
                    .stream()
                    .map(entity -> (BygonePortalEntity)entity)
                    .toList();
            if (!existingPortals.isEmpty()) {
                for (BygonePortalEntity portal : existingPortals) {
                    portal.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20));
                }
                existingPortals.getFirst().setLifeTime(12000);
                this.discard();
            }
        }

        if (wasActivated) {
            double range = 2.0;
            AABB bounds = this.getBoundingBox().inflate(range);
            List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, bounds);

            for (Player player : nearbyPlayers) {
                if (teleportCountdown.containsKey(player)) {
                    int remaining = teleportCountdown.get(player);

                    if (remaining > 0) {
                        teleportCountdown.put(player, remaining - 1);
                        player.setAsInsidePortal(this, this.blockPosition());
                        //if (player instanceof Player localPlayer) localPlayer.spinningEffectIntensity = 0.5f;

                    } else {
                        teleportCountdown.remove(player);
                        teleportPlayer(player);
                    }
                } else {
                    teleportCountdown.put(player, 80);
                }
            }
        }

        if (!teleportCountdown.isEmpty()) {
            teleportCountdown.forEach((player, integer) -> {
                if (player.distanceTo(this) > 3 && level().isClientSide && player instanceof Player localPlayer) {
                    if (localPlayer.portalProcess!=null) localPlayer.portalProcess.setAsInsidePortalThisTick(false);
                }
            });

            teleportCountdown.entrySet().removeIf(entry -> entry.getKey().distanceTo(this) > 4 || !entry.getKey().isAlive() || entry.getKey().level() != this.level());
        }

        if (triggerCooldown > 0) {
            if (triggerCooldown == 1) level().broadcastEntityEvent(this, (byte) 1);
            triggerCooldown--;
        }

        if (this.level().isClientSide) {
            updateAnimations();
        }

        if (triggerCooldown == 0 && tickCount % 40 == 0) {
            checkForNearbyPlayers();
        }

        if (this.level().isClientSide && tickCount % (40 + this.random.nextInt(40)) == 0) {
            level().playSound(this, this.blockPosition(), BGSoundEvents.ENTITY_ARCANE_MECHANISM_IDLE_EVENT, SoundSource.BLOCKS, 1.0F, this.random.nextFloat()+1);
        }

        spawnPortalParticles();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (this.level().isClientSide) {
            if (id == (byte) 0) {
                this.idleAnimationState.stop();
                this.activeAnimationState.stop();
                this.triggerAnimationState.start(this.tickCount);
                return;
            }
            if (id == (byte) 1) {
                this.idleAnimationState.stop();
                this.triggerAnimationState.stop();
                this.activeAnimationState.start(this.tickCount);
                return;
            }
            if (id == (byte) 2) {
                this.activeAnimationState.stop();
                this.triggerAnimationState.stop();
                this.idleAnimationState.start(this.tickCount);

                level().addParticle(ParticleTypes.EXPLOSION,
                        this.getX() + 0.5,
                        this.getY() + 0.5,
                        this.getZ() + 0.5,
                        0, 0.1, 0);

                level().playSound(this, this.blockPosition(), BGSoundEvents.ENTITY_ARCANE_MECHANISM_CLOSE_EVENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                return;
            }
        }

        super.handleEntityEvent(id);
    }


    private void updateAnimations() {
        if (!triggerAnimationState.isStarted() && !activeAnimationState.isStarted()) {
            idleAnimationState.startIfStopped(this.tickCount);
        }
    }

    private void checkForNearbyPlayers() {
        if (triggerCooldown > 0) return;

        double range = 10.0;
        AABB bounds = this.getBoundingBox().inflate(range);
        List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, bounds);

        boolean hasPlayerNearby = nearbyPlayers.stream().anyMatch(player -> player.distanceTo(this) <= range);

        if (hasPlayerNearby && !wasActivated) {
            level().broadcastEntityEvent(this, (byte) 0);
            wasActivated = true;
            triggerCooldown = 40;

            this.playSound(BGSoundEvents.ENTITY_ARCANE_MECHANISM_OPEN_EVENT, 1.0F, 1.0F);
            return;
        }

        if (!hasPlayerNearby && this.random.nextFloat() > 0.8) {
            if (wasActivated) {
                resetPortal();
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    private void resetPortal() {
        wasActivated = false;
        level().broadcastEntityEvent(this, (byte) 2);
    }

    private void spawnPortalParticles() {
        for (int i = 0; i < 1; i++) {
            double x = this.getX() + (this.random.nextDouble() - 0.5) * 1.5;
            double y = this.getY() + (this.random.nextDouble() - 0.5) * 2.5;
            double z = this.getZ() + (this.random.nextDouble() - 0.5) * 1.5;

            this.level().addParticle(
                    net.minecraft.core.particles.ParticleTypes.PORTAL,
                    x, y, z,
                    (this.random.nextDouble() - 0.5) * 0.5,
                    this.random.nextDouble() * 0.5,
                    (this.random.nextDouble() - 0.5) * 0.5
            );
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == this.damageSources().genericKill()) {
            return super.hurt(source, amount);
        }

        if (!this.level().isClientSide) {
            this.playSound(BGSoundEvents.ENTITY_ARCANE_MECHANISM_IDLE_EVENT, 0.5F, 1.2F);
        }

        return false;
    }

    @Override
    public ProjectileDeflection deflection(Projectile projectile) {
        return ProjectileDeflection.REVERSE;
    }

    public void teleportPlayer(Player player) {
        if (this.isRemoved() || !this.wasActivated || this.tickCount <= 90) return;

		ResourceKey<Level> bygone = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "bygone"));
		ResourceKey<Level> resourcekey = this.level().dimension() == bygone ? Level.OVERWORLD : bygone;
		if (!(this.level() instanceof ServerLevel server)) return;

        ServerLevel serverlevel = server.getServer().getLevel(resourcekey);
        if (serverlevel == null) return;

        int sourceMinY = this.level().getMinBuildHeight();
        int sourceMaxY = this.level().getMaxBuildHeight();
        int destMinY = serverlevel.getMinBuildHeight();
        int destMaxY = serverlevel.getMaxBuildHeight();

        double sourceY = player.getY();
        double normalizedY = (sourceY - sourceMinY) / (sourceMaxY - sourceMinY);
        double destY = destMinY + normalizedY * (destMaxY - destMinY);

        destY = Math.clamp(destY, destMinY + 2, destMaxY - 3);

        WorldBorder worldborder = serverlevel.getWorldBorder();
        BlockPos blockpos = worldborder.clampToBounds(player.getX(), destY, player.getZ());
        BlockPos finalBlockpos = findSafeTeleportPosition(serverlevel, blockpos, player);

        teleport(player, serverlevel, finalBlockpos);

        BygonePortalEntity newPortal = new BygonePortalEntity(serverlevel);
        newPortal.moveTo(finalBlockpos.getBottomCenter().add(0, 1, 0));
        newPortal.setLifeTime(6000);
        newPortal.setReturn(true);
        newPortal.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
        serverlevel.addFreshEntity(newPortal);

        player.playSound(SoundEvents.PORTAL_TRAVEL, 1.0F, 1.0F);
	}

    private static void teleport(Player player, ServerLevel serverlevel, BlockPos finalBlockpos) {
        player.teleportTo(serverlevel, finalBlockpos.getX() + 0.5, finalBlockpos.getY(), finalBlockpos.getZ() + 0.5, Set.of(), player.getYRot(), player.getXRot());
    }

    private BlockPos findSafeTeleportPosition(ServerLevel level, BlockPos pos, Entity player) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int searchRadius = 32;

        for (int radius = 0; radius <= searchRadius; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    for (int yOffset = 0; yOffset < 64; yOffset++) {
                        int checkY = pos.getY() + yOffset;
                        if (checkY <= level.getMaxBuildHeight() - 2) {
                            mutablePos.set(pos.getX() + x, checkY, pos.getZ() + z);
                            if (isValidSpawnSpot(level, mutablePos)) {
                                return mutablePos.immutable();
                            }
                        }

                        checkY = pos.getY() - yOffset;
                        if (checkY >= level.getMinBuildHeight() + 1) {
                            mutablePos.set(pos.getX() + x, checkY, pos.getZ() + z);
                            if (isValidSpawnSpot(level, mutablePos)) {
                                return mutablePos.immutable();
                            }
                        }
                    }
                }
            }
        }

        for (int y = level.getMaxBuildHeight() - 2; y >= level.getMinBuildHeight() + 1; y--) {
            mutablePos.set(pos.getX(), y, pos.getZ());
            if (isValidSpawnSpot(level, mutablePos)) {
                return mutablePos.immutable();
            }
        }

        return pos;
    }

    private boolean isValidSpawnSpot(ServerLevel level, BlockPos.MutableBlockPos pos) {
        if (!level.getBlockState(pos).isAir()) return false;
        if (!level.getBlockState(pos.above()).isAir()) return false;

        BlockPos groundPos = pos.below();
        if (!level.getBlockState(groundPos).isSolid()) return false;
        if (!level.getBlockState(pos).getFluidState().isEmpty()) return false;

        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {

    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return 80;
    }

    @Override
    public @Nullable DimensionTransition getPortalDestination(ServerLevel serverLevel, Entity player, BlockPos blockPos) {
        return null;
    }

    @Override
    public Transition getLocalTransition() {
        return Transition.NONE;
    }
}
