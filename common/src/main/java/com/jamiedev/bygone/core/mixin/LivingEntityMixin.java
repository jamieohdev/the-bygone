package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.item.VerdigrisBladeItem;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;
import com.jamiedev.bygone.core.extension.LivingEntityExtension;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGMobEffects;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtension {

    @Shadow
    protected ItemStack useItem;
    @Shadow
    protected int useItemRemaining;

	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
    public abstract boolean isUsingItem();

	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> effect);

	@WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    public void scaleWaterTravelSpeed(LivingEntity instance, float distance, Vec3 direction, Operation<Void> original) {
        MobEffectInstance carapaceEffect = instance.getEffect(BGMobEffects.CARAPACE.get());
        float modifiedDistance = distance;
        if (carapaceEffect != null && (instance.isInWater() && instance.getFluidHeight(FluidTags.WATER) > 0.1F)) {
            modifiedDistance = distance * (1 + 0.2f * (carapaceEffect.getAmplifier() + 1));
        }
        original.call(instance, modifiedDistance, direction);
    }

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true)
    //Fix for compatibility with Guarding mod git issue #20
    private void isBlocking(CallbackInfoReturnable<Boolean> cir) {
        Item item = this.useItem.getItem();
        if (item instanceof VerdigrisBladeItem) {
            if (this.isUsingItem() && !this.useItem.isEmpty()) {

                boolean canItemBlock = item.getUseAnimation(this.useItem) == UseAnim.BLOCK;
                boolean isUsingItemForLongEnough = (item.getUseDuration(
                        this.useItem,
                        (LivingEntity) (Object) this
                ) - this.useItemRemaining) >= ShieldItem.EFFECTIVE_BLOCK_DELAY;
                cir.setReturnValue(canItemBlock && isUsingItemForLongEnough);
            } else {
                cir.setReturnValue(false);
            }
        }
    }

	@Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
	private void beforeAddingEffect(MobEffectInstance instance, CallbackInfoReturnable<Boolean> cir) {
		if (!this.hasEffect(BGMobEffects.PLASMILK.get())) return;
		if (instance.getEffect().is(JamiesModTag.IGNORES_PLASMILK)) return;
		cir.setReturnValue(false);
	}

	@WrapMethod(method = "isInvulnerableTo")
	private boolean wrapIsInvulnerableTo(DamageSource source, Operation<Boolean> original) {

		if (this.getType().is(JamiesModTag.SPECTRAL)) {
			if (source.getDirectEntity() != null && source.getDirectEntity().getType().is(JamiesModTag.SPECTRAL_VULNERABLE_TO_ENTITY)) {
				return false;
			} else if (source.is(JamiesModTag.SPECTRAL_VULNERABLE_TO_DAMAGE)) {
				return false;
			} else if (source.getWeaponItem() != null && source.getWeaponItem().is(JamiesModTag.SPECTRAL_VULNERABLE_TO_ITEM)) {
				return false;
			}
			return true;
		}
		return original.call(source);
	}

	@WrapMethod(method = "dropAllDeathLoot")
	private void spawnHauntedGround(ServerLevel level, DamageSource source, Operation<Void> original) {
		original.call(level, source);
		if (!this.getType().is(JamiesModTag.SPECTRAL)) return;

		BlockState groundState = BGBlocks.HAUNTED_GROUND.get().defaultBlockState();
		BlockPos.MutableBlockPos pos = this.getOnPos().above(2).mutable();
		for (int i = 0; i < 16; i++) {
			pos.move(Direction.DOWN);
			BlockState state = level.getBlockState(pos);
			if (!(state.isAir() || state.canBeReplaced())) continue;
			if (!groundState.canSurvive(level, pos)) continue;
			break;
		}
		level.setBlock(pos, groundState, Block.UPDATE_CLIENTS);
	}

    @Unique private static final String BYGONE_HAUNTINGS_RISE_TICKS_TAG = "BygoneHauntingsMobRiseTicks";
    @Unique private static final int BYGONE_HAUNTINGS_RISE_DURATION = 20;
    @Unique private int bygone$hauntingsRiseTicks = 0;

    @Override public void bygone$startHauntingsRise() {
        if (bygone$isHauntingsMob()) this.bygone$hauntingsRiseTicks = BYGONE_HAUNTINGS_RISE_DURATION;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void bygone$saveHauntingsMob(CompoundTag compound, CallbackInfo ci) {
        if (bygone$isHauntingsMob() && this.bygone$hauntingsRiseTicks > 0)
            compound.putInt(BYGONE_HAUNTINGS_RISE_TICKS_TAG, this.bygone$hauntingsRiseTicks);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void bygone$readHauntingsMob(CompoundTag compound, CallbackInfo ci) {
        this.bygone$hauntingsRiseTicks = bygone$isHauntingsMob()
            ? compound.getInt(BYGONE_HAUNTINGS_RISE_TICKS_TAG) : 0;
    }

    // since they sometimes get stuck i might just change it to the behavior in here idk why
    // i wanted them to rise up at the same time anyways just thought itd be cool
    @Inject(method = "isInWall", at = @At("HEAD"), cancellable = true)
    private void bygone$bypassWallDamage(CallbackInfoReturnable<Boolean> cir) {
        if (!bygone$isHauntingsMob()) return;
        if (this.bygone$hauntingsRiseTicks <= 0) return;
        cir.setReturnValue(false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void bygone$hauntingsPhaseTick(CallbackInfo ci) {
        if (!bygone$isHauntingsMob()) return;
        if (this.bygone$hauntingsRiseTicks <= 0) return;

        this.fallDistance = 0.0F;
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
        this.setPos(this.getX(), this.getY() + (this.getBbHeight() * 2.0D) / BYGONE_HAUNTINGS_RISE_DURATION, this.getZ());
        this.bygone$hauntingsRiseTicks--;
    }

    @Unique private boolean bygone$isHauntingsMob() {
        return this.getType().getCategory() == HauntingsCategoryHolder.HAUNTING_MOB_CATEGORY;
    }

}
