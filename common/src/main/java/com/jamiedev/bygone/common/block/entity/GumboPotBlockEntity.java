package com.jamiedev.bygone.common.block.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.jamiedev.bygone.common.block.GumboPotBlock;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlockEntities;
import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GumboPotBlockEntity extends BlockEntity {

    public static final int BASE_SERVINGS_PER_INGREDIENT = 12;
    public static final Codec<Pair<Integer, FoodProperties>> INGREDIENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("servings").forGetter(Pair::getFirst),
            FoodProperties.DIRECT_CODEC.fieldOf("properties").forGetter(Pair::getSecond)
    ).apply(instance, Pair::new));
    public static final Codec<List<Pair<Integer, FoodProperties>>> CONTENTS_CODEC = INGREDIENT_CODEC.listOf();
    public static final String NBT_KEY_CONTENTS = "contents";
    protected final List<Pair<Integer, FoodProperties>> potContents;


    public GumboPotBlockEntity(BlockPos pos, BlockState blockState) {
        super(BGBlockEntities.GUMBO_POT.get(), pos, blockState);
        potContents = new ArrayList<>();
    }

    public ImmutableList<Pair<Integer, FoodProperties>> ingredientsView() {
        return ImmutableList.copyOf(potContents);
    }

    public void addIngredient(FoodProperties ingredient) {
        this.potContents.add(Pair.of(BASE_SERVINGS_PER_INGREDIENT, ingredient));
    }


    public boolean canAddIngredient(ItemStack ingredient) {
        BlockState state = this.getBlockState();

        if (GumboPotBlock.canFitAdditionalIngredients(state) && !ingredient.is(JamiesModTag.CANNOT_ADD_TO_GUMBO)) {

            FoodProperties foodProperties = ingredient.get(DataComponents.FOOD);
            if (foodProperties != null) {
                return true;
            }
            GumboIngredientComponent gumboIngredientComponent = ingredient.get(BGDataComponents.GUMBO_INGREDIENT_DATA.value());
            return gumboIngredientComponent != null;
        }
        return false;
    }

    // Returns the remainder itemstack if the ingredient is accepted, empty itemstack otherwise. Does not modify the stack.
    public void addIngredient(ItemStack ingredient) {
        BlockState state = this.getBlockState();

        if (GumboPotBlock.canFitAdditionalIngredients(state) && !ingredient.is(JamiesModTag.CANNOT_ADD_TO_GUMBO)) {

            SuspiciousStewEffects suspiciousStewEffects = ingredient.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);


            FoodProperties foodProperties = ingredient.get(DataComponents.FOOD);
            if (foodProperties == null) {
                GumboIngredientComponent gumboIngredientComponent = ingredient.get(BGDataComponents.GUMBO_INGREDIENT_DATA.value());
                if (gumboIngredientComponent != null) {
                    foodProperties = gumboIngredientComponent.properties;
                }
            }

            if (foodProperties != null) {
                this.addIngredient(removeNegativeEffectsIf(
                        spliceStewEffects(foodProperties, suspiciousStewEffects),
                        ingredient.is(JamiesModTag.GUMBO_MAKES_SAFE) || this.getBlockState()
                                .getOptionalValue(GumboPotBlock.HEATED)
                                .orElse(false)
                ));
            }
        }
    }

    public boolean canScoopBowl(@Nullable ItemStack bowl) {
        if (bowl == null) {
            return false;
        }
        BlockState state = this.getBlockState();
        if (GumboPotBlock.canScoopBowl(state)) {
            return GumboScooping.getFilled(bowl.getItem()) != null;
        }
        return false;
    }

    // Returns the result of scooping a bowl (empty if the stack doesn't have the proper component). Does not modify the stack.
    public @NotNull ItemStack scoopBowl(@Nullable ItemStack bowl) {
        BlockState state = this.getBlockState();

        if ((bowl != null) && GumboPotBlock.canScoopBowl(state)) {

            Item filledItem = GumboScooping.getFilled(bowl.getItem());
            if (filledItem != null) {
                ItemStack filledStack = new ItemStack(filledItem);
                filledStack.set(
                        DataComponents.FOOD,
                        this.extractFood(Optional.ofNullable(GumboScooping.getEmptied(filledItem)).map(ItemStack::new))
                );
                return filledStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private FoodProperties extractFood(Optional<ItemStack> convertsTo) {
        boolean thisIsTheLastOfIt = this.getBlockState()
                .getOptionalValue(GumboPotBlock.LEVEL)
                .orElse(GumboPotBlock.MIN_LEVEL) <= (GumboPotBlock.MIN_LEVEL + 1);
        List<Pair<Integer, FoodProperties>> resultingContents = new ArrayList<>();
        List<FoodProperties> toCombine = new ArrayList<>();
        for (Pair<Integer, FoodProperties> pair : this.potContents) {
            FoodProperties currentIngredient = pair.getSecond();
            int count = pair.getFirst();
            if (count > 1 && !thisIsTheLastOfIt) {
                resultingContents.add(Pair.of(count - 1, currentIngredient));
            }
            toCombine.add(currentIngredient);
        }
        this.potContents.clear();
        this.potContents.addAll(resultingContents);
        return this.combineFoods(toCombine, convertsTo);
    }

    // Maybe scale down for dilution?
    //        effects.replaceAll(possibleEffect -> new FoodProperties.PossibleEffect(
    //                possibleEffect.effect(),
    //                scaleFactor * possibleEffect.probability()
    //        ));
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private FoodProperties combineFoods(List<FoodProperties> toCombine, Optional<ItemStack> convertsTo) {

        int count = toCombine.size();
        float scaleFactor = 1.0f / count;
        int totalNutrition = 0;
        float totalSaturation = 0;
        boolean canAlwaysEat = false;
        float eatSeconds = 0;
        List<FoodProperties.PossibleEffect> effects = new ArrayList<>();
        for (FoodProperties properties : toCombine) {
            totalNutrition += properties.nutrition();
            totalSaturation += properties.saturation();
            eatSeconds += properties.eatSeconds();
            canAlwaysEat |= properties.canAlwaysEat();
            effects.addAll(properties.effects());
        }
        // Tweaks prevent errors while giving slight buffs.
        totalNutrition = Mth.ceil(scaleFactor * totalNutrition) + 1;
        totalSaturation = Mth.ceil(scaleFactor * totalSaturation) + 0.05f;
        eatSeconds = Mth.ceil(scaleFactor * eatSeconds);
        if (eatSeconds == 0) {
            eatSeconds += 0.05f;
        }
        effects.replaceAll(possibleEffect -> new FoodProperties.PossibleEffect(
                possibleEffect.effect(),
                scaleFactor * possibleEffect.probability()
        ));

        return new FoodProperties(totalNutrition, totalSaturation, canAlwaysEat, eatSeconds, convertsTo, effects);
    }


    protected FoodProperties removeNegativeEffectsIf(FoodProperties properties, boolean removeIfTrue) {
        return removeIfTrue ? removeNegativeEffects(properties) : properties;
    }

    protected FoodProperties removeNegativeEffects(FoodProperties properties) {
        return new FoodProperties(
                properties.nutrition(),
                properties.saturation(),
                properties.canAlwaysEat(),
                properties.eatSeconds(),
                properties.usingConvertsTo(),
                properties.effects()
                        .stream()
                        .filter(possibleEffect -> possibleEffect.effect().getEffect().value().isBeneficial())
                        .toList()
        );
    }

    protected FoodProperties spliceStewEffects(FoodProperties properties, @Nullable SuspiciousStewEffects effects) {
        return effects == null ? properties : new FoodProperties(
                properties.nutrition(),
                properties.saturation(),
                properties.canAlwaysEat(),
                properties.eatSeconds(),
                properties.usingConvertsTo(),
                Streams.concat(
                        properties.effects().stream(),
                        effects.effects()
                                .stream()
                                .map(entry -> new FoodProperties.PossibleEffect(entry.createEffectInstance(), 1.0f))
                ).toList()
        );
    }


    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);

        this.potContents.clear();
        Tag contents = nbt.get(NBT_KEY_CONTENTS);
        this.potContents.addAll(CONTENTS_CODEC.decode(NbtOps.INSTANCE, contents).getOrThrow().getFirst());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);

        DataResult<Tag> contentsResult = CONTENTS_CODEC.encodeStart(NbtOps.INSTANCE, this.potContents);
        contentsResult.ifError(tagError -> System.out.println(
                "Warning! Gumbo pot block entity encountered an error while saving: " + tagError.message()));

        contentsResult.ifSuccess(contents -> nbt.put(NBT_KEY_CONTENTS, contents));

    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registryLookup) {
        CompoundTag nbtCompound = super.getUpdateTag(registryLookup);
        DataResult<Tag> contentsResult = CONTENTS_CODEC.encodeStart(NbtOps.INSTANCE, this.potContents);
        contentsResult.ifError(tagError -> System.out.println(
                "Warning! Gumbo pot block entity encountered an error while constructing an update tag: " + tagError.message()));
        contentsResult.ifSuccess(contents -> nbtCompound.put(NBT_KEY_CONTENTS, contents));

        return nbtCompound;
    }

    public static class GumboScooping {

        protected static final BiMap<Item, Item> SCOOPING_MAP = HashBiMap.create();

        public static @Nullable Item getFilled(Item in) {
            return SCOOPING_MAP.get(in);
        }

        public static @Nullable Item getEmptied(Item out) {
            return SCOOPING_MAP.inverse().get(out);
        }


        public static @Nullable Item setFilled(Item in, Item out) {
            return SCOOPING_MAP.put(in, out);
        }
    }

    public record GumboIngredientComponent(FoodProperties properties) {
        public static final Codec<GumboIngredientComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                        FoodProperties.DIRECT_CODEC.fieldOf("food_properties").forGetter(GumboIngredientComponent::properties))
                .apply(instance, GumboIngredientComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, GumboIngredientComponent> STREAM_CODEC = StreamCodec.composite(
                FoodProperties.DIRECT_STREAM_CODEC,
                GumboIngredientComponent::properties,
                GumboIngredientComponent::new
        );
    }
}
