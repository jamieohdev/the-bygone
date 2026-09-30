package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class BygonePortalEntity extends Entity implements Portal {
    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(BygonePortalEntity .class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> DATA_EXPIRY = SynchedEntityData.defineId(BygonePortalEntity .class, EntityDataSerializers.LONG);

    private static final TicketType<BlockPos> BYGONE_TICKET = TicketType.create("bygone:bygone_portal", Vec3i::compareTo, 41);

    private AnimationState[] animations = new AnimationState[State.values().length];
    private int soundCooldown = 40;

    public BygonePortalEntity(EntityType<? extends BygonePortalEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setNoGravity(true);
        if(level.isClientSide){
            for(int i = 0; i < State.values().length; i++) this.animations[i] = new AnimationState();
            State.IDLE.get(this).start(this.tickCount);
        }
    }



    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_STATE, 0);
        builder.define(DATA_EXPIRY, 0L);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Expiry")) this.setExpiry(compound.getLong("Expiry"));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        // intentionally don't save state. doesn't make sense to!
        compound.putLong("Expiry", getExpiry());
    }
    public State getState(){ return State.of(this.getEntityData().get(DATA_STATE)); }
    public void setState(State state){ this.getEntityData().set(DATA_STATE, state.ordinal()); }
    public long getExpiry(){ return this.getEntityData().get(DATA_EXPIRY); }
    public void setExpiry(long expiry){ this.getEntityData().set(DATA_EXPIRY, expiry); }
    public boolean isExpired(){ return this.getExpiry() != 0 && this.getExpiry() < this.level().getGameTime(); }



    @Override
    public void tick(){
        super.tick();
        if(this.level().isClientSide){
            State state = getState();
            for(State s : State.values()){
                if(s == state) s.get(this).startIfStopped(this.tickCount);
                else s.get(this).stop();
            }

            if(soundCooldown-- == 0){
                this.level().playSound(this, this.blockPosition(), BGSoundEvents.ENTITY_ARCANE_MECHANISM_IDLE_EVENT, SoundSource.BLOCKS, 1.0F, this.random.nextFloat() + 1);
                soundCooldown = 40 + this.random.nextInt(40);
            }
            this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(1F), this.getRandomY() - 0.25, this.getRandomZ(1F), (this.random.nextDouble() - 0.5) * 0.5, -this.random.nextDouble() * 0.5, (this.random.nextDouble() - 0.5) * 0.5);
        } else{
            if(this.getExpiry() == 0) setExpiry(this.level().getGameTime() + 12000L);
            else if(this.isExpired()){
                this.level().addParticle(ParticleTypes.EXPLOSION, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, 0, 0.1, 0);
                this.discard();
                return;
            }

            if(this.tickCount % 40 == 0){
                // handle portal activation when players are in range
                State state = this.getState();
                boolean playered  = this.level().hasNearbyAlivePlayer(this.getX(), this.getY(), this.getZ(), 10);
                if(playered == (state == State.IDLE)) switch(state){
                    case State.IDLE: this.setState(State.TRIGGERED); break;
                    case State.TRIGGERED: // fallthrough
                    case State.ACTIVE: this.setState(State.IDLE); break;
                } else if(state == State.TRIGGERED) this.setState(State.ACTIVE);
                if(this.getState() == State.ACTIVE && this.level() instanceof ServerLevel origin){
                    // keep other side loaded while active so we can scan for portal entities!
                    ServerLevel dest = getDestinationDimension(origin);
                    if(dest != null) dest.getChunkSource().addRegionTicket(BYGONE_TICKET, new ChunkPos(this.blockPosition()), 0, this.blockPosition());
                }
            }
        }

        // called on client too for the portal overlay
        if(this.getState() == State.ACTIVE){
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2F)).forEach(e -> {
                if(e.portalProcess != null && e.portalProcess.isInsidePortalThisTick()) return; // already claimed!
                if(e.canUsePortal(false)) e.setAsInsidePortal(this, this.blockPosition());
            });
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key){
        if (key.equals(DATA_STATE) && this.getState() == State.IDLE){
            this.level().playSound(this, this.blockPosition(), BGSoundEvents.ENTITY_ARCANE_MECHANISM_CLOSE_EVENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.level().addParticle(ParticleTypes.EXPLOSION, this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, 0, 0.1, 0);
        }
        super.onSyncedDataUpdated(key);
    }



    @Override protected double getDefaultGravity() { return 0; }
    @Override public boolean isPushable(){ return false; }
    @Override public boolean isPushedByFluid(){ return false; }
    @Override public void push(Entity entity){}
    @Override public boolean canBeCollidedWith(){ return true; }
    @Override public boolean isPickable(){ return true; }
    @Override public ItemStack getPickResult(){ return new ItemStack(BGItems.ARCANE_MECHANISM.get()); }
    @Override public ProjectileDeflection deflection(Projectile projectile){ return ProjectileDeflection.REVERSE; }
    @Override public boolean isCurrentlyGlowing(){ return this.getState() == State.ACTIVE || super.isCurrentlyGlowing(); }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == this.damageSources().genericKill()) return super.hurt(source, amount);
        if (!this.level().isClientSide) this.playSound(BGSoundEvents.ENTITY_ARCANE_MECHANISM_IDLE_EVENT, 0.5F, 1.2F);
        return false;
    }



    @Override @Nullable
    public DimensionTransition getPortalDestination(ServerLevel origin, Entity entity, BlockPos ignored){
        ServerLevel dest = getDestinationDimension(origin);
        if(dest == null) return null;
        dest.getChunkAt(this.blockPosition()); // we request a load earlier, but this'll block till load. otherwise we'll find no entities

        // check for existing portals, 16 square radius, unlimited vertically (same as when genning below)
        AABB aabb = AABB.ofSize(this.position(), 32.1, 0, 32.1).setMinY((double)dest.getMinBuildHeight()).setMaxY((double)dest.getMaxBuildHeight());
        BygonePortalEntity sibling = dest.getEntitiesOfClass(BygonePortalEntity.class, aabb).stream().filter(
            Predicate.not(BygonePortalEntity::isExpired)
        ).min((a, b) ->
            Double.compare(this.position().distanceToSqr(a.position()), this.position().distanceToSqr(b.position()))
        ).orElse(null);
        if(sibling != null) return getDestinationAt(dest, sibling.blockPosition().offset(0, -1, 0), entity, sibling, false);

        // try to find a valid spot. you ever watch Martha Speaks? this is just like Martha Speaks.
        for(BlockPos pos : BlockPos.withinManhattan(this.blockPosition(), 16, 32, 16)){
            if(isValidDestination(dest, pos)) return getDestinationAt(dest, pos, entity, null, false);
        }
        // try to find spots outside of range directly up or down. this one's like Dog With a Blog
        BlockPos.MutableBlockPos pos = this.blockPosition().mutable();
        for(pos.setY(dest.getMaxBuildHeight() - 2); pos.getY() >= dest.getMinBuildHeight() + 1; pos.move(0, -1, 0)){
            if(Math.abs(pos.getY() - this.blockPosition().getY()) <= 32) pos.move(0, -64, 0);
            else if(isValidDestination(dest, pos)) return getDestinationAt(dest, pos, entity, null, false);
        }
        // give up. Zapped (2014)
        return getDestinationAt(dest, this.blockPosition(), entity, null, true);
    }

    private boolean isValidDestination(ServerLevel dest, BlockPos pos){
        if(!dest.getBlockState(pos).isAir()) return false;
        if(!dest.getBlockState(pos.above()).isAir()) return false;
        if(!dest.getBlockState(pos.below()).isSolid()) return false;
        return true;
    }

    private DimensionTransition getDestinationAt(ServerLevel dest, BlockPos pos, Entity entity, BygonePortalEntity portal, boolean platform){
        if(platform){
            if(!dest.getBlockState(pos).isAir() || !dest.getBlockState(pos.above()).isAir())
                for(BlockPos b : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 4, 1)))
                    dest.setBlockAndUpdate(b, Blocks.AIR.defaultBlockState());
            if(!dest.getBlockState(pos.below()).isSolid())
                for(BlockPos b : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, -1, 1)))
                    dest.setBlockAndUpdate(b, BGBlocks.COBBLED_BYSTONE.get().defaultBlockState());
        }
        if(portal == null){
            portal = new BygonePortalEntity(BGEntityTypes.BYGONE_PORTAL.get(), dest);
            portal.moveTo(pos.getBottomCenter().add(0, 1, 0));
            portal.setExpiry(this.getExpiry());
            dest.addFreshEntity(portal);
        }
        portal.setState(State.ACTIVE);
        Vec3 landing = BlockPos.findClosestMatch(pos, 2, 1, b -> !b.equals(pos) && isValidDestination(dest, b)).orElse(pos).getBottomCenter();
        landing = landing.add(landing.subtract(portal.position()).multiply(1, 0, 1).normalize().scale(0.25)); // portal box obstructs a lil too much
        return new DimensionTransition(dest, landing, Vec3.ZERO, entity.getYRot(), entity.getXRot(), false, DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
    }

    private ServerLevel getDestinationDimension(ServerLevel origin){
        ResourceKey<Level> bygone =  ResourceKey.create(Registries.DIMENSION, Bygone.id("bygone"));
        if(origin.dimension() != Level.OVERWORLD && !origin.dimension().equals(bygone)) return null;
        return origin.getServer().getLevel(origin.dimension() == Level.OVERWORLD ? bygone : Level.OVERWORLD);
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity){
        return 80;
    }



    public static enum State{
        IDLE, TRIGGERED, ACTIVE;
        public AnimationState get(BygonePortalEntity portal){
            return portal.animations[this.ordinal()];
        }
        public static State of(int i){
            if(i >= State.values().length) return State.IDLE;
            return State.values()[i];
        }
    }
}
