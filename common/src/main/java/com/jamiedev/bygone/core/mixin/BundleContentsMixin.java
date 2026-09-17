package com.jamiedev.bygone.core.mixin;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BundleContents.class)
public class BundleContentsMixin {

	@Shadow @Final @Mutable public static Codec<BundleContents> CODEC;

	@Shadow @Final @Mutable public static StreamCodec<RegistryFriendlyByteBuf, BundleContents> STREAM_CODEC;

	@Invoker("getWeight")
	public static Fraction invokeGetWeight(ItemStack stack) {
		throw new AssertionError();
	}

	@Invoker("<init>")
	static BundleContents newBundleContents(List<ItemStack> items, Fraction weight) {
		throw new AssertionError();
	}

	@Unique
	private static List<ItemStack> getStacks(BundleContents component) {
		return Lists.newArrayList(component.items());
	}

	@Unique private static final Codec<Fraction> FRACTION_CODEC = Codec.STRING.xmap(
			Fraction::getFraction,
			Fraction::toProperString
	);

	@Unique private static final StreamCodec<RegistryFriendlyByteBuf, Fraction> FRACTION_STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			Fraction::toProperString,
			Fraction::getFraction
	);

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void injectCodec(CallbackInfo ci) {
		CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemStack.CODEC.listOf().fieldOf("items").forGetter(BundleContentsMixin::getStacks),
				FRACTION_CODEC.fieldOf("weight").forGetter(BundleContents::weight)
		).apply(instance, BundleContentsMixin::newBundleContents));

		STREAM_CODEC = StreamCodec.composite(
				ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
				BundleContentsMixin::getStacks,
				FRACTION_STREAM_CODEC,
				BundleContents::weight,
				BundleContentsMixin::newBundleContents
		);
	}

}
