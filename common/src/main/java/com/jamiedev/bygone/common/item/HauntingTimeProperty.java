package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.weather.weather_types.HauntingsEvent;
import com.jamiedev.bygone.core.init.JamiesModTag;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import javax.annotation.Nullable;

public class HauntingTimeProperty implements ClampedItemPropertyFunction {

	public static final long HAUNTING_DURATION = HauntingsEvent.HAUNTING_DURATION;
	public static final long HAUNTING_CYCLE = HauntingsEvent.HAUNTING_CYCLE;
	public static final long TOTAL_CYCLE = HauntingsEvent.HAUNTING_CYCLE + HauntingsEvent.HAUNTING_DURATION;

	protected double rotation;
	protected double rota;
	protected long lastUpdateTick;

	@Override
	public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
		if (level == null && entity.level() instanceof ClientLevel) {
			level = (ClientLevel)entity.level();
		}

		if (level == null) return 0;

		double rotation;
		if (this.dimensionHasHauntings(level)) {
			long cycleTime = level.getGameTime() % TOTAL_CYCLE;
			if (cycleTime < HAUNTING_CYCLE) {
				rotation = 0.5F * ((double) cycleTime / HAUNTING_CYCLE);
			} else {
				rotation = 0.5F + 0.5F * ((double) (cycleTime - HAUNTING_CYCLE) / HAUNTING_DURATION);
			}
		} else {
			rotation = Math.random();
		}

		return (float) this.wobble(level, rotation);
	}

	public boolean dimensionHasHauntings(Level level) {
		Registry<DimensionType> registry = level.registryAccess().registry(Registries.DIMENSION_TYPE).orElseThrow();
		ResourceLocation location = registry.getKey(level.dimensionType());
		if (location == null) return false;

		return registry.getHolder(location).orElseThrow().is(JamiesModTag.DimensionTypes.HAS_HAUNTINGS);
	}

	protected double wobble(Level level, double rotation) {
		if (level.getGameTime() == this.lastUpdateTick) return this.rotation;

		this.lastUpdateTick = level.getGameTime();
		rotation = Mth.positiveModulo(rotation - this.rotation + 0.5F, 1) - 0.5F;
		this.rota += rotation * 0.1F;
		this.rota *= 0.9F;
		this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1);
		return this.rotation;
	}

}
