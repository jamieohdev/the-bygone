package com.jamiedev.bygone.common.block.entity;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.block.GumboPotBlock;
import com.jamiedev.bygone.core.registry.BGBlockEntities;
import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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

    public void addIngredient(GumboIngredientComponent ingredient) {
        this.potContents.add(Pair.of(BASE_SERVINGS_PER_INGREDIENT, ingredient.properties));
    }


    public boolean canAddIngredient(ItemStack ingredient) {
        BlockState state = this.getBlockState();

        if (GumboPotBlock.canFitAdditionalIngredients(state)) {

            FoodProperties foodProperties = ingredient.get(DataComponents.FOOD);
            if (foodProperties != null) {
                return true;
            }
            GumboIngredientComponent gumboIngredientComponent = ingredient.get(BGDataComponents.GUMBO_INGREDIENT_DATA.value());
            return gumboIngredientComponent != null;
        }
        return false;
    }

    // Returns true if the ingredient is accepted, false otherwise. Does not modify the stack.
    public boolean addIngredient(ItemStack ingredient) {
        BlockState state = this.getBlockState();

        if (GumboPotBlock.canFitAdditionalIngredients(state)) {

            FoodProperties foodProperties = ingredient.get(DataComponents.FOOD);
            if (foodProperties != null) {
                this.addIngredient(foodProperties);
                return true;
            }
            GumboIngredientComponent gumboIngredientComponent = ingredient.get(BGDataComponents.GUMBO_INGREDIENT_DATA.value());
            if (gumboIngredientComponent != null) {
                this.addIngredient(gumboIngredientComponent);
                return true;
            }
        }
        return false;
    }

    public boolean canScoopBowl(ItemStack ingredient) {
        BlockState state = this.getBlockState();
        if (GumboPotBlock.canScoopBowl(state)) {
            GumboScoopComponent scoopComponent = ingredient.get(BGDataComponents.GUMBO_SCOOP_DATA.value());
            return scoopComponent != null;
        }
        return false;
    }

    // Returns the result of scooping a bowl (empty if the stack doesn't have the proper component). Does not modify the stack.
    public ItemStack scoopBowl(ItemStack ingredient) {
        BlockState state = this.getBlockState();

        if (GumboPotBlock.canScoopBowl(state)) {

            GumboScoopComponent scoopComponent = ingredient.get(BGDataComponents.GUMBO_SCOOP_DATA.value());
            if (scoopComponent != null) {
                Item filled = BuiltInRegistries.ITEM.get(scoopComponent.filled());
                ItemStack scooped = filled == null ? ItemStack.EMPTY : new ItemStack(filled);
                Optional<ItemStack> turnsIntoWhenConsumed = scoopComponent.emptied().map(key -> {
                    Item consumeResult = BuiltInRegistries.ITEM.get(key);
                    if (consumeResult == null) {
                        return ItemStack.EMPTY;
                    }
                    return new ItemStack(consumeResult);
                }).filter(stack -> !stack.isEmpty());
                scooped.set(DataComponents.FOOD, this.extractFood(turnsIntoWhenConsumed));
                return scooped;
            }
        }
        return ItemStack.EMPTY;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private FoodProperties extractFood(Optional<ItemStack> convertsTo) {
        List<Pair<Integer, FoodProperties>> resultingContents = new ArrayList<>();
        List<FoodProperties> toCombine = new ArrayList<>();
        for (Pair<Integer, FoodProperties> pair : this.potContents) {
            FoodProperties currentIngredient = pair.getSecond();
            int count = pair.getFirst();
            if (count > 1) {
                resultingContents.add(Pair.of(count - 1, currentIngredient));
            }
            toCombine.add(currentIngredient);
        }
        this.potContents.clear();
        this.potContents.addAll(resultingContents);
        return this.combineFoods(toCombine, convertsTo);
    }

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
        totalNutrition = Mth.ceil(scaleFactor * totalNutrition);
        totalSaturation = Mth.ceil(scaleFactor * totalSaturation);
        eatSeconds = Mth.ceil(scaleFactor * eatSeconds);
        // TODO Maybe scale down for dilution?
        //        effects.replaceAll(possibleEffect -> new FoodProperties.PossibleEffect(
        //                possibleEffect.effect(),
        //                scaleFactor * possibleEffect.probability()
        //        ));

        return new FoodProperties(totalNutrition, totalSaturation, canAlwaysEat, eatSeconds, convertsTo, effects);
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

    public record GumboScoopComponent(ResourceKey<Item> filled, Optional<ResourceKey<Item>> emptied) {
        public static final Codec<GumboScoopComponent> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ResourceKey.codec(Registries.ITEM).fieldOf("filled").forGetter(GumboScoopComponent::filled),
                ResourceKey.codec(Registries.ITEM).optionalFieldOf("emptied").forGetter(GumboScoopComponent::emptied)
        ).apply(instance, GumboScoopComponent::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, GumboScoopComponent> STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(Registries.ITEM),
                GumboScoopComponent::filled,
                ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.ITEM)),
                GumboScoopComponent::emptied,
                GumboScoopComponent::new
        );
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
