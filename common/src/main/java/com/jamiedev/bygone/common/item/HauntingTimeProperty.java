package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.client.renderer.weather.HauntingsRenderer;
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

import static com.jamiedev.bygone.common.weather.weather_types.HauntingsEvent.*;

public class HauntingTimeProperty implements ClampedItemPropertyFunction {

	protected double rotation;
	protected double rota;
	protected long lastUpdateTick;

	@Override
	public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if ((level == null && entity != null) && entity.level() instanceof ClientLevel clientLevel) level = clientLevel;
		if (level == null) return 0;

		double rotation = Math.random();
        HauntingsEvent hauntingsEvent = HauntingsRenderer.getClientHauntings();

        // the clock ends at the right time but starts a bit late so i think its just skewed
        float remainingCycle = 0.595f;
		if (hauntingsEvent != null) {
			int cycleTime = hauntingsEvent.<Integer>getProperty(TIME).getValue();
			if (cycleTime < HAUNTING_CYCLE) rotation = (1.0f - remainingCycle) * ((double) cycleTime / HAUNTING_CYCLE);
			else rotation = (1.0f - remainingCycle) + (remainingCycle * ((double) (cycleTime - HAUNTING_CYCLE) / HAUNTING_DURATION));
		}
		return (float) this.wobble(level, rotation);
	}

    /* going to comment this out for two reasons:

    - the tag doesn't actually contain a "source of truth" right now. hauntings and other weather events are
    inherently tied to the bygone and so the tag is kind of just redundant. if there needs to be a tag that
    dictates what dimensions contain hauntings, then I'd rather it actually control that

    - since we have to access the event itself, that already tells us if the event exists in the dimension

	public boolean dimensionHasHauntings(Level level) {
		Registry<DimensionType> registry = level.registryAccess().registry(Registries.DIMENSION_TYPE).orElseThrow();
		ResourceLocation location = registry.getKey(level.dimensionType());
		if (location == null) return false;

		return registry.getHolder(location).orElseThrow().is(JamiesModTag.DimensionTypes.HAS_HAUNTINGS);
	}
     */

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
