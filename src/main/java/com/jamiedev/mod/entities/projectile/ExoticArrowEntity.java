package com.jamiedev.mod.entities.projectile;

import com.jamiedev.mod.init.JamiesModEntityTypes;
import com.jamiedev.mod.init.JamiesModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

public class ExoticArrowEntity extends PersistentProjectileEntity {
    private static final TrackedData<Integer> COLOR;

    boolean returns = false;

    public ExoticArrowEntity(EntityType<? extends ExoticArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ExoticArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(JamiesModEntityTypes.EXOTIC_ARROW, x, y, z, world, stack, shotFrom);
        this.initColor();
    }

    public ExoticArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(JamiesModEntityTypes.EXOTIC_ARROW, owner, world, stack, shotFrom);
        this.initColor();
    }

    private PotionContentsComponent getPotionContents() {
        return (PotionContentsComponent)this.getItemStack().getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
    }

    private void setPotionContents(PotionContentsComponent potionContentsComponent) {
        this.getItemStack().set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
        this.initColor();
    }

    protected void setStack(ItemStack stack) {
        super.setStack(stack);
        this.initColor();
    }

    private void initColor() {
        PotionContentsComponent potionContentsComponent = this.getPotionContents();
        this.dataTracker.set(COLOR, potionContentsComponent.equals(PotionContentsComponent.DEFAULT) ? -1 : potionContentsComponent.getColor());
    }

    public void addEffect(StatusEffectInstance effect) {
        this.setPotionContents(this.getPotionContents().with(effect));
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(COLOR, -1);
    }

    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
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
                this.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, i),
                        this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
            }

        }
    }

    public int getColor() {
        return (Integer)this.dataTracker.get(COLOR);
    }

    protected void onHit(LivingEntity target) {
        super.onHit(target);
        Entity entity = this.getEffectCause();
        PotionContentsComponent potionContentsComponent = this.getPotionContents();
        Iterator var4;
        StatusEffectInstance statusEffectInstance;
        if (potionContentsComponent.potion().isPresent()) {
            var4 = ((Potion)((RegistryEntry)potionContentsComponent.potion().get()).value()).getEffects().iterator();

            while(var4.hasNext()) {
                statusEffectInstance = (StatusEffectInstance)var4.next();
                target.addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(), Math.max(statusEffectInstance.mapDuration((i) -> {
                    return i / 8;
                }), 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
            }
        }

        var4 = potionContentsComponent.customEffects().iterator();

        while(var4.hasNext()) {
            statusEffectInstance = (StatusEffectInstance)var4.next();
            target.addStatusEffect(statusEffectInstance, entity);
        }
        this.returns = true;
    }

    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        this.getWorld().sendEntityStatus(this, (byte)0);
        this.setStack(new ItemStack(JamiesModItems.EXOTIC_ARROW));
    }

    protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult blockHitResult, ItemStack weaponStack) {
        super.onBlockHitEnchantmentEffects(world, blockHitResult, weaponStack);
        this.getWorld().sendEntityStatus(this, (byte)0);
        this.setStack(new ItemStack(JamiesModItems.EXOTIC_ARROW, 64));
        dropArrow(world, Objects.requireNonNull(getOwner()).blockPos);
        this.discard();

    }
    public static void dropArrow(World world, BlockPos pos) {
        dropStack(world, pos, new ItemStack(JamiesModItems.EXOTIC_ARROW, 1));
    }
    private static void dropStack(World world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack) {
        if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            ItemEntity itemEntity = (ItemEntity)itemEntitySupplier.get();
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }
    public static void dropStack(World world, BlockPos pos, ItemStack stack) {
        double d = (double)EntityType.ITEM.getHeight() / 2.0;
        double e = (double)pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -0.25, 0.25);
        double f = (double)pos.getY() + 0.5 + MathHelper.nextDouble(world.random, -0.25, 0.25) - d;
        double g = (double)pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -0.25, 0.25);
        dropStack(world, () -> {
            return new ItemEntity(world, e, f, g, stack);
        }, stack);
    }

    protected ItemStack getDefaultItemStack() {
        return new ItemStack(JamiesModItems.EXOTIC_ARROW);
    }

    public double getDamage() {
        return 1.0D;
    }
    protected double getGravity() {
        return 0.07;
    }

    public void handleStatus(byte status) {
        if (status == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float g = (float)(i >> 8 & 255) / 255.0F;
                float h = (float)(i & 255) / 255.0F;

                for(int j = 0; j < 20; ++j) {
                    this.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, g, h), this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0);
                }
            }
        } else {
            super.handleStatus(status);
        }

    }

    static {
        COLOR = DataTracker.registerData(ExoticArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
