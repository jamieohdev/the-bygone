package com.jamiedev.bygone.common.entities.projectile;

import com.jamiedev.bygone.fabric.init.JamiesModEntityTypes;
import com.jamiedev.bygone.fabric.init.JamiesModItems;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ExoticArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<Integer> COLOR;

    boolean returns = false;

    public ExoticArrowEntity(EntityType<? extends ExoticArrowEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ExoticArrowEntity(Level world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(JamiesModEntityTypes.EXOTIC_ARROW, x, y, z, world, stack, shotFrom);
        this.initColor();
    }

    public ExoticArrowEntity(Level world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(JamiesModEntityTypes.EXOTIC_ARROW, owner, world, stack, shotFrom);
        this.initColor();
    }

    private PotionContents getPotionContents() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    private void setPotionContents(PotionContents potionContentsComponent) {
        this.getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, potionContentsComponent);
        this.initColor();
    }

    protected void setPickupItemStack(ItemStack stack) {
        super.setPickupItemStack(stack);
        this.initColor();
    }

    private void initColor() {
        PotionContents potionContentsComponent = this.getPotionContents();
        this.entityData.set(COLOR, potionContentsComponent.equals(PotionContents.EMPTY) ? -1 : potionContentsComponent.getColor());
    }

    public void addEffect(MobEffectInstance effect) {
        this.setPotionContents(this.getPotionContents().withEffectAdded(effect));
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, -1);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.spawnParticles(2);
                }
            } else {
                this.spawnParticles(3);
            }
        }

    }

    private void spawnParticles(int amount) {
        int i = this.getColor();
        if (i != -1 && amount > 0) {
            for(int j = 0; j < amount; ++j) {
                this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, i),
                        this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }

        }
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }

    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        Entity entity = this.getEffectSource();
        PotionContents potionContentsComponent = this.getPotionContents();
        Iterator var4;
        MobEffectInstance statusEffectInstance;
        if (potionContentsComponent.potion().isPresent()) {
            var4 = ((Potion)((Holder)potionContentsComponent.potion().get()).value()).getEffects().iterator();

            while(var4.hasNext()) {
                statusEffectInstance = (MobEffectInstance)var4.next();
                target.addEffect(new MobEffectInstance(statusEffectInstance.getEffect(), Math.max(statusEffectInstance.mapDuration((i) -> {
                    return i / 8;
                }), 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.isVisible()), entity);
            }
        }

        var4 = potionContentsComponent.customEffects().iterator();

        while(var4.hasNext()) {
            statusEffectInstance = (MobEffectInstance)var4.next();
            target.addEffect(statusEffectInstance, entity);
        }
        this.returns = true;
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        this.level().broadcastEntityEvent(this, (byte)0);
        this.setPickupItemStack(new ItemStack(JamiesModItems.EXOTIC_ARROW));
    }

    protected void hitBlockEnchantmentEffects(ServerLevel world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        super.hitBlockEnchantmentEffects(world, blockHitResult, weaponStack);
        this.level().broadcastEntityEvent(this, (byte)0);
        this.setPickupItemStack(new ItemStack(JamiesModItems.EXOTIC_ARROW, 64));
        dropArrow(world, Objects.requireNonNull(getOwner()).blockPosition());
        this.discard();

    }
    public static void dropArrow(Level world, BlockPos pos) {
        dropStack(world, pos, new ItemStack(JamiesModItems.EXOTIC_ARROW, 1));
    }
    private static void dropStack(Level world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack) {
        if (!world.isClientSide && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            ItemEntity itemEntity = itemEntitySupplier.get();
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
        }
    }
    public static void dropStack(Level world, BlockPos pos, ItemStack stack) {
        double d = (double)EntityType.ITEM.getHeight() / 2.0;
        double e = (double)pos.getX() + 0.5 + Mth.nextDouble(world.random, -0.25, 0.25);
        double f = (double)pos.getY() + 0.5 + Mth.nextDouble(world.random, -0.25, 0.25) - d;
        double g = (double)pos.getZ() + 0.5 + Mth.nextDouble(world.random, -0.25, 0.25);
        dropStack(world, () -> {
            return new ItemEntity(world, e, f, g, stack);
        }, stack);
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(JamiesModItems.EXOTIC_ARROW);
    }

    public double getBaseDamage() {
        return 1.0D;
    }
    protected double getDefaultGravity() {
        return 0.07;
    }

    public void handleEntityEvent(byte status) {
        if (status == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float g = (float)(i >> 8 & 255) / 255.0F;
                float h = (float)(i & 255) / 255.0F;

                for(int j = 0; j < 20; ++j) {
                    this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, g, h), this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
                }
            }
        } else {
            super.handleEntityEvent(status);
        }

    }

    static {
        COLOR = SynchedEntityData.defineId(ExoticArrowEntity.class, EntityDataSerializers.INT);
    }
}
