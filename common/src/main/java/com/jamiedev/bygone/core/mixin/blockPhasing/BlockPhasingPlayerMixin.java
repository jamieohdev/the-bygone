package com.jamiedev.bygone.core.mixin.blockPhasing;

import com.jamiedev.bygone.common.entity.BlockPhasingEntity;
import com.jamiedev.bygone.core.registry.BGAttributes;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class BlockPhasingPlayerMixin extends LivingEntity implements BlockPhasingEntity {

	protected BlockPhasingPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow @Final private Abilities abilities;

	@Shadow public abstract void onUpdateAbilities();

	@Unique int MAX_PHASING_REGENERATION_COOLDOWN = 40;
	@Unique boolean phasing = false;
	@Unique boolean insideBlock = false;
	@Unique int phasingTime = 0;
	@Unique int phasingRegenerationCooldown = 0;

	@WrapMethod(method = "createAttributes")
	private static AttributeSupplier.Builder addPhasingAttribute(Operation<AttributeSupplier.Builder> original) {
		return original.call().add(BGAttributes.PHASING_DURATION.get(), 0);
	}

	@WrapMethod(method = "readAdditionalSaveData")
	private void readPhasingData(CompoundTag compound, Operation<Void> original) {
		original.call(compound);
		this.phasingTime = compound.getInt("phasing_time");
	}

	@WrapMethod(method = "addAdditionalSaveData")
	private void addPhasingData(CompoundTag compound, Operation<Void> original) {
		original.call(compound);
		compound.putInt("phasing_time", this.phasingTime);
	}

	@WrapMethod(method = "tick")
	private void tick(Operation<Void> original) {
		original.call();
		this.setPhasing(this.insideBlock && this.phasingTime >= 0);

		if (this.isPhasing()) this.tickPhasing();
		else if (!this.isInsideBlock()) this.tickPhasingRegen();
	}

	@Override
	public void tickPhasing() {
		this.phasingTime--;
		if (this.onGround()) this.setDeltaMovement(this.getDeltaMovement().multiply(0.2F, 1, 0.2F));
		if (this.phasingTime <= 0) this.setPhasing(false);
	}

	@Unique
	private void tickPhasingRegen() {
		if (this.phasingRegenerationCooldown <= MAX_PHASING_REGENERATION_COOLDOWN) {
			this.phasingRegenerationCooldown++;
		} if (this.phasingRegenerationCooldown == MAX_PHASING_REGENERATION_COOLDOWN) {
			this.phasingTime = Math.min(MAX_PHASING_REGENERATION_COOLDOWN, this.getMaxPhasingTicks());
		} else {
			this.phasingTime = Math.min(this.phasingTime + 1, this.getMaxPhasingTicks());
		}
	}

	@Override
	protected void checkInsideBlocks() {
		this.insideBlock = false;
		super.checkInsideBlocks();
	}

	@Override
	public boolean isInsideBlock() {
		return this.insideBlock;
	}

	@Override
	public void setInsideBlock(boolean value) {
		this.insideBlock = value;
	}

	@Override
	public void setPhasing(boolean value) {
		boolean wasPhasing = this.isPhasing();
		if (wasPhasing == value) return;
		this.phasing = value;

		if (value) this.onStartPhasing();
		else this.onStopPhasing();
	}

	@Override
	public void onStartPhasing() {
		this.abilities.mayfly = true;
		this.abilities.flying = true;
		this.onUpdateAbilities();
		this.setPos(this.position().add(0, 0.1F, 0));
		this.setDeltaMovement(this.getDeltaMovement().add(0, 0.1F, 0));
		this.level().playSound(null, this.blockPosition(), BGSoundEvents.PLAYER_PHASING_START_ADDITIONS_EVENT, SoundSource.PLAYERS);
	}

	@Override
	public void onStopPhasing() {
		this.abilities.mayfly = false;
		this.abilities.flying = false;
		this.onUpdateAbilities();
		this.level().playSound(null, this.blockPosition(), BGSoundEvents.PLAYER_PHASING_STOP_ADDITIONS_EVENT, SoundSource.PLAYERS);
		this.phasingRegenerationCooldown = 0;
	}

	@Override
	public boolean isPhasing() {
		return this.phasing;
	}

	@Override
	public boolean canStartPhasing() {
		if (this.phasingTime <= 0) return false;

		ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
		return stack.is(BGItems.WALLOW_SHAWL.get());
	}

	@Override
	public int getPhasingTime() {
		return this.phasingTime;
	}

	@Override
	public int getMaxPhasingTicks() {
		return (int) (this.level().tickRateManager().tickrate() * this.getAttributeValue(BGAttributes.PHASING_DURATION.get()));
	}

	@Override
	public boolean isInWall() {
		if (this.isPhasing()) return false;
		return super.isInWall();
	}

}
