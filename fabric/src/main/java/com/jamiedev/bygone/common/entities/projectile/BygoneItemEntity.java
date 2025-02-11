package com.jamiedev.bygone.common.entities.projectile;

import com.google.common.collect.Lists;
import com.jamiedev.bygone.fabric.init.JamiesModEntityTypes;
import com.jamiedev.bygone.fabric.init.JamiesModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BygoneItemEntity  extends Entity implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> ITEM;
    private int stepCount;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int lifespan;
    private boolean dropsItem;
    @Nullable
    private Direction direction;

    BlockPos target;

    public BygoneItemEntity(EntityType<? extends BygoneItemEntity> entityType, Level world) {
        super(entityType, world);
    }

    public BygoneItemEntity(Level world, double x, double y, double z) {
        this(JamiesModEntityTypes.BYGONE_ITEM, world);
        this.setPos(x, y, z);
        this.target = target;
        this.direction = Direction.UP;
    }

    public Direction getDirection() {
        return this.direction;
    }

    private void setDirection(@Nullable Direction direction) {
        this.direction = direction;
    }


    public void setItem(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            this.getEntityData().set(ITEM, this.getItem());
        } else {
            this.getEntityData().set(ITEM, itemStack.copyWithCount(1));
        }

    }

    public ItemStack getItem() {
        return (ItemStack)this.getEntityData().get(ITEM);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ITEM, this.getItem());
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        double d = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(d)) {
            d = 4.0;
        }

        d *= 64.0;
        return distance < d * d;
    }

    private void changeTargetDirection(@Nullable Direction.Axis axis) {
        double d = 0.5;
        BlockPos blockPos;
        if (this.target == null) {
            blockPos = this.blockPosition().below();
        } else {
            d = (double)this.target.asLong() * 0.5;
            blockPos = BlockPos.containing(this.target.getX(), this.target.getY() + d, this.target.getZ());
        }

        double e = (double)blockPos.getX() + 0.5;
        double f = (double)blockPos.getY() + d;
        double g = (double)blockPos.getZ() + 0.5;
        Direction direction = null;
        if (!blockPos.closerToCenterThan(this.position(), 2.0)) {
            BlockPos blockPos2 = this.blockPosition();
            List<Direction> list = Lists.newArrayList();
            if (axis != Direction.Axis.X) {
                if (blockPos2.getX() < blockPos.getX() && this.level().isEmptyBlock(blockPos2.east())) {
                    list.add(Direction.EAST);
                } else if (blockPos2.getX() > blockPos.getX() && this.level().isEmptyBlock(blockPos2.west())) {
                    list.add(Direction.WEST);
                }
            }

            if (axis != Direction.Axis.Y) {
                if (blockPos2.getY() < blockPos.getY() && this.level().isEmptyBlock(blockPos2.above())) {
                    list.add(Direction.UP);
                } else if (blockPos2.getY() > blockPos.getY() && this.level().isEmptyBlock(blockPos2.below())) {
                    list.add(Direction.DOWN);
                }
            }

            if (axis != Direction.Axis.Z) {
                if (blockPos2.getZ() < blockPos.getZ() && this.level().isEmptyBlock(blockPos2.south())) {
                    list.add(Direction.SOUTH);
                } else if (blockPos2.getZ() > blockPos.getZ() && this.level().isEmptyBlock(blockPos2.north())) {
                    list.add(Direction.NORTH);
                }
            }

            direction = Direction.getRandom(this.random);
            if (list.isEmpty()) {
                for(int i = 5; !this.level().isEmptyBlock(blockPos2.relative(direction)) && i > 0; --i) {
                    direction = Direction.getRandom(this.random);
                }
            } else {
                direction = (Direction)list.get(this.random.nextInt(list.size()));
            }

            e = this.getX() + (double)direction.getStepX();
            f = this.getY() + (double)direction.getStepY();
            g = this.getZ() + (double)direction.getStepZ();
        }

        this.setDirection(direction);
        double h = e - this.getX();
        double j = f - this.getY();
        double k = g - this.getZ();
        double l = Math.sqrt(h * h + j * j + k * k);
        if (l == 0.0) {
            this.targetX = 0.0;
            this.targetY = 0.0;
            this.targetZ = 0.0;
        } else {
            this.targetX = h / l * 0.15;
            this.targetY = j / l * 0.15;
            this.targetZ = k / l * 0.15;
        }

        this.hasImpulse = true;
        //this.stepCount = 10 + this.random.nextInt(5) * 10;
    }

    public void initTargetPos(BlockPos pos) {
        double d = (double)pos.getX();
        int i = pos.getY();
        double e = (double)pos.getZ();
        double f = d - this.getX();
        double g = e - this.getZ();
        double h = Math.sqrt(f * f + g * g);
        if (h > 12.0) {
            this.targetX = this.getX() + f / h * 12.0;
            this.targetZ = this.getZ() + g / h * 12.0;
            this.targetY = this.getY() + 8.0;
        } else {
            this.targetX = d;
            this.targetY = (double)i;
            this.targetZ = e;
        }

        this.lifespan = 0;
        this.dropsItem = this.random.nextInt(5) > 0;
    }

    public void lerpMotion(double x, double y, double z) {
        this.setDeltaMovement(x, y, z);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d = Math.sqrt(x * x + z * z);
            this.setYRot((float)(Mth.atan2(x, z) * 57.2957763671875));
            this.setXRot((float)(Mth.atan2(y, d) * 57.2957763671875));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

    }

    protected static float updateRotation(float prevRot, float newRot) {
        while(newRot - prevRot < -180.0F) {
            prevRot -= 360.0F;
        }

        while(newRot - prevRot >= 180.0F) {
            prevRot += 360.0F;
        }

        return Mth.lerp(0.2F, prevRot, newRot);
    }

    protected double getDefaultGravity() {
        return 0.01;
    }


    public void tick() {
        super.tick();
        Vec3 vec3d = this.getDeltaMovement();
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        double g = vec3d.horizontalDistance();
        this.setXRot(updateRotation(this.xRotO, (float)(Mth.atan2(vec3d.y, g) * 37.2957763671875)));
        this.setYRot(updateRotation(this.yRotO, (float)(Mth.atan2(vec3d.x, vec3d.z) * 37.2957763671875)));
        if (!this.level().isClientSide) {
            double h = this.targetX - d;
            double i = this.targetZ - f;
            float j = (float)Math.sqrt(h * h + i * i);
            float k = (float)Mth.atan2(i, h);
            double l = Mth.lerp(0.0025, g, (double)j);
            double m = vec3d.y;
            if (j < 1.0F) {
                l *= 0.8;
                m *= 0.8;
            }

            int n = this.getY() < this.targetY ? 1 : -1;
           // vec3d = new Vec3d(Math.cos((double)k) * l, m + ((double)n - m) * 0.014999999664723873, Math.sin((double)k) * l);
            this.targetX = Mth.clamp(this.targetX * 1.025, -1.0, 1.0);
            this.targetY = Mth.clamp(this.targetY * 1.025, -1.0, 1.0);
            this.targetZ = Mth.clamp(this.targetZ * 1.025, -1.0, 1.0);
            vec3d = this.getDeltaMovement();
            this.setDeltaMovement(vec3d.add((this.targetX - vec3d.x) * 0.1, (this.targetY - vec3d.y) * 0.1, (this.targetZ - vec3d.z) * 0.1));

        }

        float o = 0.25F;
        if (this.isInWater()) {
            for(int p = 0; p < 4; ++p) {
                this.level().addParticle(ParticleTypes.BUBBLE, d - vec3d.x * 0.25, e - vec3d.y * 0.25, f - vec3d.z * 0.25, vec3d.x, vec3d.y, vec3d.z);
            }
        } else {
            this.level().addParticle(ParticleTypes.PORTAL, d - vec3d.x * 0.25 + this.random.nextDouble() * 0.6 - 0.3, e - vec3d.y * 0.25 - 0.5, f - vec3d.z * 0.25 + this.random.nextDouble() * 0.6 - 0.3, vec3d.x, vec3d.y, vec3d.z);
        }

        if (!this.level().isClientSide) {
            this.setPos(d, e, f);
            ++this.lifespan;
            if (this.lifespan > 100 && !this.level().isClientSide) {
                this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                this.discard();
                if (this.dropsItem) {
                    this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItem()));
                } else {
                    this.level().levelEvent(2003, this.blockPosition(), 0);
                }
            }
        } else {
            this.setPosRaw(d, e, f);
        }

        this.checkInsideBlocks();
        vec3d = this.getDeltaMovement();
        this.setPos(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z);
        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.END_ROD, this.getX() - vec3d.x, this.getY() - vec3d.y + 0.15, this.getZ() - vec3d.z, 0.0, 0.0, 0.0);
        } else if (this.target != null) {
            if (this.stepCount > 0) {
                --this.stepCount;
                if (this.stepCount == 0) {
                    this.changeTargetDirection(this.direction == null ? null : this.direction.getAxis());
                }
            }

            if (this.direction != null) {
                BlockPos blockPos = this.blockPosition();
                Direction.Axis axis = this.direction.getAxis();
                if (this.level().loadedAndEntityCanStandOn(blockPos.relative(this.direction), this)) {
                    this.changeTargetDirection(axis);
                } else {
                    BlockPos blockPos2 = this.target.above();
                    if (axis == Direction.Axis.X && blockPos.getX() == blockPos2.getX() || axis == Direction.Axis.Z && blockPos.getZ() == blockPos2.getZ() || axis == Direction.Axis.Y && blockPos.getY() == blockPos2.getY()) {
                        this.changeTargetDirection(axis);
                    }
                }
            }
        }

    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.put("Item", this.getItem().save(this.registryAccess()));
        if (this.target != null) {

        }
        if (this.direction != null) {
            nbt.putInt("Dir", this.direction.get3DDataValue());
        }


        nbt.putDouble("TXD", this.targetX);
        nbt.putDouble("TYD", this.targetY);
        nbt.putDouble("TZD", this.targetZ);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Item", 10)) {
            this.setItem((ItemStack)ItemStack.parse(this.registryAccess(), nbt.getCompound("Item")).orElse(this.getItem()));
        } else {
            this.setItem(this.getItem());
        }
        this.targetX = nbt.getDouble("TXD");
        this.targetY = nbt.getDouble("TYD");
        this.targetZ = nbt.getDouble("TZD");
        if (nbt.contains("Dir", 99)) {
            this.direction = Direction.from3DDataValue(nbt.getInt("Dir"));
        }
        if (nbt.hasUUID("Target")) {

        }
    }

    private ItemStack getDefaultItem() {
        return new ItemStack(JamiesModItems.BEIGE_SLICE);
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public boolean isAttackable() {
        return false;
    }
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        double d = packet.getXa();
        double e = packet.getYa();
        double f = packet.getZa();
        this.setDeltaMovement(d, e, f);
    }

    static {
        ITEM = SynchedEntityData.defineId(BygoneItemEntity.class, EntityDataSerializers.ITEM_STACK);
    }
}
