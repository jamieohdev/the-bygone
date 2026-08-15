package com.jamiedev.bygone.common.entity.projectile;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ScareBoltEntity extends Projectile {

    public static final int MAX_LIFETIME = 90;
    public static final float DAMAGE = 4.0F;

    private int life;

    public ScareBoltEntity(EntityType<? extends ScareBoltEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public ScareBoltEntity(Level level, LivingEntity owner) {
        this(BGEntityTypes.SCARE_BOLT.get(), level);
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return super.canHitEntity(entity) && !entity.getType().is(JamiesModTag.SPECTRAL) && !entity.noPhysics;
    }

    @Override
    public void tick() {
        super.tick();
        this.noPhysics = true;

        Vec3 movement = this.getDeltaMovement();
        Vec3 position = this.position();
        Vec3 next = position.add(movement);

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                this.level(),
                this,
                position,
                next,
                this.getBoundingBox().expandTowards(movement).inflate(0.3),
                this::canHitEntity
        );
        if (hitResult != null) {
            this.hitTargetOrDeflectSelf(hitResult);
        }

        this.setPos(next.x, next.y, next.z);
        this.updateRotation();

        if (this.level().isClientSide()) {
            this.level().addParticle(
                    ParticleTypes.SCULK_SOUL,
                    this.getX() + this.random.triangle(0, 0.1),
                    this.getY() + this.random.triangle(0, 0.1),
                    this.getZ() + this.random.triangle(0, 0.1),
                    0.0, 0.0, 0.0
            );
        } else if (++this.life > MAX_LIFETIME) {
            this.discard();
        }
    }


    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
        if (!this.level().isClientSide()) {
            Entity target = result.getEntity();
            Entity owner = this.getOwner();
            LivingEntity shooter = owner instanceof LivingEntity living ? living : null;
            target.hurt(this.damageSources().mobProjectile(this, shooter), DAMAGE);
            ((LivingEntity)target).addEffect(new MobEffectInstance(BGMobEffects.HAUNTED.get(), 140 * (int)f), this);
            this.discard();
        }
    }
}
