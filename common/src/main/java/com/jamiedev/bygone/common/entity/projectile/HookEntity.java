package com.jamiedev.bygone.common.entity.projectile;

import com.jamiedev.bygone.common.component.entity.HookData;
import com.jamiedev.bygone.core.registry.BGAttachmentTypes;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;

import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// we use Projectile bc AbstractArrow has a fuckton of handling for. like.
// entity hitting! and piercing!
public class HookEntity extends Projectile{
    private static final EntityDataAccessor<Boolean> DATA_LODGED =
            SynchedEntityData.defineId(HookEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_RETRACTING =
            SynchedEntityData.defineId(HookEntity.class, EntityDataSerializers.BOOLEAN);

    public static final double MAX_RANGE = 160;
    public static final double RETRACT_RANGE = 128;

    // all of these are only relevant clientside
    private float equilibrium = 0F;
    private float pullSpeed = 0F;
    private boolean pulling = false;

    public HookEntity(EntityType<? extends HookEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }
    public HookEntity(Entity shooter) {
        this(BGEntityTypes.HOOK.get(), shooter.level());
        this.setOwner(shooter);
        // on normal shooter heights, this is equivalent to -0.1F
        this.setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight() * 1.52F / 1.62F, shooter.getZ());
        this.setDeltaMovement(shooter.getLookAngle().normalize().scale(2.0));
        this.setNoGravity(true); // only disable gravity if we have an initial velocity
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // super is abstract
        builder.define(DATA_LODGED, false);
        builder.define(DATA_RETRACTING, false);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key){
        if(DATA_LODGED.equals(key) && this.isLodged()){
            Entity owner = this.getOwner();
            if(owner != null) this.equilibrium = (float)Math.min(this.distanceTo(owner), MAX_RANGE);
        }
    }

    private boolean isLodged(){ return this.entityData.get(DATA_LODGED); }
    private void setLodged(boolean b){ this.entityData.set(DATA_LODGED, b); }
    private boolean isRetracting(){ return this.entityData.get(DATA_RETRACTING); }
    private void setRetracting(boolean b){ this.entityData.set(DATA_RETRACTING, b); }

    public void pull(){ this.pulling = true; }
    public float equilibrium(){ return equilibrium; }

    /*
    private Vec3 chain(Function<Entity,Vec3> pos){
        Entity owner = this.getOwner();
        if(owner == null) return Vec3.ZERO;
        return SableCompanion.INSTANCE.projectOutOfSubLevel(this.level(), pos.apply(owner))
                .subtract(SableCompanion.INSTANCE.projectOutOfSubLevel(this.level(), pos.apply(this)));
    }
    public Vec3 chain(){
        return this.chain(Entity::getEyePosition);
    }
    public Vec3 chain(float delta){
        return this.chain((Entity e) -> SableCompanion.INSTANCE.getEyePositionInterpolated(e, delta));
    }
    */
    /*
    private Vec3 chain(Function<Entity,Vec3> pos, Optional<Pose3dc> pose){
        Entity owner = this.getOwner();
        if(owner == null) return Vec3.ZERO;
        Function<Vec3,Vec3> trans = pose.<Function<Vec3,Vec3>>map((Pose3dc p) -> p::transformPositionInverse).orElse(Function.identity());
        Vec3 origin = trans.apply(SableCompanion.INSTANCE.projectOutOfSubLevel(this.level(), pos.apply(this)));
        Vec3 dest = trans.apply(SableCompanion.INSTANCE.projectOutOfSubLevel(owner.level(), pos.apply(owner)));
        return dest.subtract(origin);
    }
    public Vec3 chain(Entity perspective){
        return this.chain(Entity::getEyePosition, Optional.ofNullable(SableCompanion.INSTANCE.getContaining(perspective)).map(SubLevelAccess::logicalPose));
    }
    public Vec3 chain(Entity perspective, float delta){
        var x = Optional.ofNullable(SableCompanion.INSTANCE.getContainingClient(perspective)).map(a -> a.renderPose(delta));
        // XXX: System.out.println(x); AND ABOVE
        return this.chain(e -> e.getEyePosition(delta), x);
    }
    */
    /*
    private Vec3 chain(Pose3dc pose, Function<Entity,Vec3> pos){
        Entity owner = this.getOwner();
        if(owner == null) return Vec3.ZERO;
        Vec3 origin = pose.transformPositionInverse(SableCompanion.INSTANCE.projectOutOfSubLevel(this.level(), pos.apply(this)));
        Vec3 dest = pose.transformPositionInverse(SableCompanion.INSTANCE.projectOutOfSubLevel(owner.level(), pos.apply(owner)));
        return dest.subtract(origin);
    }
    public Vec3 chain(SubLevelAccess sublevel){
        return this.chain(sublevel != null ? sublevel.logicalPose() : GLOBAL_POSE, Entity::getEyePosition);
    }
    public Vec3 chain(ClientSubLevelAccess sublevel, float delta){
        return this.chain(sublevel != null ? sublevel.renderPose(delta) : GLOBAL_POSE, e -> e.getEyePosition(delta));
    }
    */
    public static Pose3dc GLOBAL_POSE = new Pose3d();
    public Vec3 chain(SubLevelAccess sublevel){
        Entity owner = this.getOwner();
        if(owner == null) return Vec3.ZERO;
        // project both the hook and the shooter into the world from any sublevels, then back into the designated sublevel
        Pose3dc pose = sublevel != null ? sublevel.logicalPose() : GLOBAL_POSE;
        Vec3 origin = pose.transformPositionInverse(SableCompanion.INSTANCE.projectOutOfSubLevel(this.level(), this.getEyePosition()));
        Vec3 dest = pose.transformPositionInverse(SableCompanion.INSTANCE.projectOutOfSubLevel(owner.level(), owner.getEyePosition()));
        return dest.subtract(origin);
    }



    @Override
    public void tick() {
        super.tick();

        Entity owner = this.getOwner();
        if(owner == null || (!this.level().isClientSide && this.shouldDiscard(owner))){
            if(!this.level().isClientSide) this.discard();
            return;
        }

        if(!this.level().isClientSide){
            double speed = this.getDeltaMovement().length();
            if(!this.isRetracting() && this.shouldRetract(owner)){
                this.setRetracting(true);
                this.setLodged(false);
                this.equilibrium = 0F;
                this.setPos(SableCompanion.INSTANCE.projectOutOfSubLevel(this.level(), this.position()));
                speed = Math.max(this.chain(SableCompanion.INSTANCE.getContaining(this)).length() / 40, 0.5);
                if(HookData.getHook(owner) == this) owner.remove(BGAttachmentTypes.HOOK);
            }

            if(this.isRetracting()){
                this.setDeltaMovement(this.chain(SableCompanion.INSTANCE.getContaining(this)).normalize().scale(speed + 0.055));
            } else if(this.isLodged()){
                if(this.level().noCollision(this.getBoundingBox().inflate(0.06))) this.setLodged(false);
                owner.checkSlowFallDistance();
            } else{
                this.applyGravity();
                this.onHit(this.level().clip(new ClipContext(this.getEyePosition(), this.getEyePosition().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
            }

            // don't use Entity#move so we don't get deflected n shit
            if(!this.isLodged()){
                this.setPos(this.position().add(this.getDeltaMovement()));
                this.tryCheckInsideBlocks();
            }
        }

        if(owner.isControlledByLocalInstance() && this.isLodged()){
            if(this.pulling) this.equilibrium = Math.max(this.equilibrium - (this.pullSpeed += 0.07F), 0);
            else this.pullSpeed = 0;
            this.pulling = false;

            // fun fact: player movement is clientside!
            Vec3 chain = this.chain(SableCompanion.INSTANCE.getContaining(owner));
            double rebound = chain.length() - this.equilibrium;
            if(rebound > 0){
                Vec3 direction = chain.normalize();
                Vec3 tension = direction.scale(-0.5 * owner.getDeltaMovement().dot(direction));
                owner.push(direction.scale(-rebound * owner.getGravity()).add(tension));
                if(!owner.onGround()) owner.push(owner.getDeltaMovement().multiply(0.075, 0, 0.075)); // counteract air friction
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hit){
        super.onHitBlock(hit);
        this.playSound(BGSoundEvents.HOOK_HIT_ADDITIONS_EVENT, 1F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.setPos(hit.getLocation().add(new Vec3(hit.getDirection().step().mul(this.getEyeHeight() * 0.75F).add(0F, -this.getEyeHeight(), 0F))));
        this.setLodged(true);
        this.setNoGravity(false); // can now fall if dislodged!
    }



    private boolean shouldRetract(Entity owner){
        return HookData.getHook(owner) != this
            || (owner instanceof LivingEntity living && !living.isHolding(BGItems.ANCIENT_HOOK.get()))
            || this.distanceToSqr(owner) > RETRACT_RANGE * RETRACT_RANGE
            || (!this.isLodged() && this.getDeltaMovement().lengthSqr() < Mth.EPSILON)
            || owner.isShiftKeyDown();
    }
    private boolean shouldDiscard(Entity owner){
        return owner.isRemoved()
            || !owner.isAlive()
            || (owner instanceof Player player && player.isSpectator())
            || (this.isRetracting() && this.distanceToSqr(owner.getEyePosition()) < this.getDeltaMovement().length())
            || this.distanceToSqr(owner) > MAX_RANGE * MAX_RANGE;
    }



    @Override public boolean canUsePortal(boolean allowVehicles){ return false; }
    @Override protected double getDefaultGravity(){ return 0.05; }
    @Override public boolean shouldRenderAtSqrDistance(double dist){ return dist < MAX_RANGE * MAX_RANGE; }
    @Override public boolean isAlwaysTicking(){ return !this.isRetracting(); }
}