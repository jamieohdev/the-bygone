package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntFunction;

// Problems to be solved: how to add more data without breaking the int-packing?
public class PrimordialFishEntity extends AbstractSchoolingFish implements VariantHolder<PrimordialFishEntity.Pattern> {
    public static final String BUCKET_VARIANT_TAG = "PrimordialBucketVariantTag";
    public static final String VARIANT_TAG = "PrimordialVariant";
    public static final float COMMON_VARIANT_CHANCE = 0.75f;
    public static final int MAX_COLOR_VALUE = 255;
    public static final List<PrimordialFishEntity.Variant> COMMON_VARIANTS;
    public static final MapCodec<PrimordialFishEntity.Variant> VARIANT_FIELD_CODEC = PrimordialFishEntity.Variant.CODEC.fieldOf(
            BUCKET_VARIANT_TAG);
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT;

    static {
        DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(PrimordialFishEntity.class, EntityDataSerializers.INT);
        COMMON_VARIANTS = List.of(
                // 0
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.BROCHO, DyeColor.GREEN, DyeColor.RED),
                // 1
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.AGONUS, DyeColor.BROWN, DyeColor.WHITE),
                // 2
                new PrimordialFishEntity.Variant(
                        PrimordialFishEntity.Pattern.AGONUS,
                        DyeColor.BROWN,
                        DyeColor.LIGHT_GRAY
                ),
                // 3
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.EUSTHENO, DyeColor.BLUE, DyeColor.BROWN),
                // 4
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.EUSTHENO, DyeColor.GREEN, DyeColor.GRAY),
                // 5
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.ABSALOM, DyeColor.GREEN, DyeColor.CYAN),
                // 6
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.ACENTRO, DyeColor.BROWN, DyeColor.WHITE),
                // 7
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.ACENTRO, DyeColor.WHITE, DyeColor.RED),
                // 8
                new PrimordialFishEntity.Variant(
                        PrimordialFishEntity.Pattern.ACENTRO,
                        DyeColor.LIGHT_GRAY,
                        DyeColor.PINK
                ),
                // 9
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.HAIKO, DyeColor.LIGHT_BLUE, DyeColor.RED),
                // 10
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.HAIKO, DyeColor.BROWN, DyeColor.BROWN),
                // 11
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.HAIKO, DyeColor.LIME, DyeColor.BLUE),
                // 12
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.HAIKO, DyeColor.BROWN, DyeColor.GRAY),
                // 13
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.MESA, DyeColor.GRAY, DyeColor.LIGHT_GRAY),
                // 14
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.MESA, DyeColor.GRAY, DyeColor.YELLOW),
                // 15
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.PSEUDA, DyeColor.WHITE, DyeColor.RED),
                // 16
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.TETAN, DyeColor.WHITE, DyeColor.BLACK),
                // 17
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.TETAN, DyeColor.WHITE, DyeColor.BROWN),
                // 18
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.TETAN, DyeColor.WHITE, DyeColor.GRAY),
                // 19
                new PrimordialFishEntity.Variant(
                        PrimordialFishEntity.Pattern.TETAN,
                        DyeColor.LIGHT_BLUE,
                        DyeColor.PURPLE
                ),
                // 20
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.BROCHO, DyeColor.BROWN, DyeColor.RED),
                // 21
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.SIMULA, DyeColor.YELLOW, DyeColor.BLUE),
                // 22
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.SIMULA, DyeColor.MAGENTA, DyeColor.LIME),
                // 23
                new PrimordialFishEntity.Variant(PrimordialFishEntity.Pattern.SIMULA, DyeColor.RED, DyeColor.LIGHT_GRAY)
        );
    }

    private boolean isSchool = true;

    public PrimordialFishEntity(EntityType<? extends PrimordialFishEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static String getPredefinedName(int variantId) {
        // TODO
        return "entity.bygone.primordial_fish.predefined." + variantId;
    }

    static int packVariant(PrimordialFishEntity.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
        return (pattern.getPackedId() & '\uffff') | ((baseColor.getId() & MAX_COLOR_VALUE) << 16) | ((patternColor.getId() & MAX_COLOR_VALUE) << 24);
    }

    public static DyeColor getBaseColor(int variantId) {
        return DyeColor.byId(variantId >> 16 & MAX_COLOR_VALUE);
    }

    public static DyeColor getPatternColor(int variantId) {
        return DyeColor.byId(variantId >> 24 & MAX_COLOR_VALUE);
    }

    public static PrimordialFishEntity.Pattern getPattern(int variantId) {
        return PrimordialFishEntity.Pattern.byId(variantId & '\uffff');
    }

    public static boolean checkPrimordialFishEntitySpawnRules(EntityType<PrimordialFishEntity> primordialFishType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getFluidState(pos.below()).is(FluidTags.WATER) && level.getBlockState(pos.above())
                .is(Blocks.WATER) && (level.getBiome(pos)
                .is(JamiesModTag.ALLOWS_PRIMORDIAL_FISH_SPAWNS_AT_ANY_HEIGHT) || WaterAnimal.checkSurfaceWaterAnimalSpawnRules(
                primordialFishType,
                level,
                spawnType,
                pos,
                random
        ));
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt(VARIANT_TAG, this.getPackedVariant());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setPackedVariant(compound.getInt(VARIANT_TAG));
    }

    public void saveToBucketTag(@NotNull ItemStack stack) {
        super.saveToBucketTag(stack);
        CustomData.update(
                DataComponents.BUCKET_ENTITY_DATA,
                stack,
                (tag) -> tag.putInt(BUCKET_VARIANT_TAG, this.getPackedVariant())
        );
    }

    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        super.loadFromBucketTag(tag);
        if (tag.contains(BUCKET_VARIANT_TAG, CompoundTag.TAG_INT)) {
            this.setPackedVariant(tag.getInt(BUCKET_VARIANT_TAG));
        }

    }

    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    private int getPackedVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setPackedVariant(int packedVariant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, packedVariant);
    }

    public DyeColor getBaseColor() {
        return getBaseColor(this.getPackedVariant());
    }

    public DyeColor getPatternColor() {
        return getPatternColor(this.getPackedVariant());
    }

    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(BGItems.PRIMORDIAL_FISH_BUCKET.get());
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    public boolean isMaxGroupSizeReached(int size) {
        return !this.isSchool;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        RandomSource random = level.getRandom();
        PrimordialFishEntity.Variant variant;
        if (spawnGroupData instanceof PrimordialFishGroupData primordialFishGroupData) {
            variant = primordialFishGroupData.variant;
        } else if ((double) random.nextFloat() < COMMON_VARIANT_CHANCE) {
            variant = Util.getRandom(COMMON_VARIANTS, random);
            spawnGroupData = new PrimordialFishGroupData(this, variant);
        } else {
            this.isSchool = false;
            PrimordialFishEntity.Pattern pattern = Util.getRandom(PrimordialFishEntity.Pattern.values(), random);
            DyeColor[] dyeColors = DyeColor.values();
            DyeColor baseColor = Util.getRandom(dyeColors, random);
            DyeColor patternColor = Util.getRandom(dyeColors, random);
            variant = new PrimordialFishEntity.Variant(pattern, baseColor, patternColor);
        }

        this.setPackedVariant(variant.getPackedId());
        return spawnGroupData;
    }

    public enum Base implements StringRepresentable {
        SMALL("a", null, 0, false),
        LARGE("b", null, 1, false),
        SMALL_GLOW("a_glows", SMALL, 2, true),
        LARGE_GLOW("b_glows", LARGE, 3, true);

        final int id;
        final @NotNull String name;
        final @Nullable Base variantOf;
        final boolean glows;

        Base(@NotNull String name, @Nullable Base variantOf, int id, boolean glows) {
            this.name = name;
            this.variantOf = variantOf;
            this.id = id;
            this.glows = glows;
        }

        public boolean glows() {
            return this.glows;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public @NotNull String getTextureType() {
            return this.variantOf == null ? this.getSerializedName() : this.variantOf.getTextureType();
        }
    }

    public enum Pattern implements StringRepresentable {
        AGONUS("agonus", PrimordialFishEntity.Base.SMALL, 0),
        EUSTHENO("eustheno", PrimordialFishEntity.Base.SMALL, 1),
        ABSALOM("absalom", PrimordialFishEntity.Base.SMALL, 2),
        ACENTRO("acentro", PrimordialFishEntity.Base.SMALL, 3),
        HAIKO("haiko", PrimordialFishEntity.Base.SMALL, 4),
        MESA("mesa", PrimordialFishEntity.Base.SMALL, 5),

        BROCHO("brocho", PrimordialFishEntity.Base.LARGE, 0),
        PAREXUS("parexus", PrimordialFishEntity.Base.LARGE, 1),
        PSEUDA("pseuda", PrimordialFishEntity.Base.LARGE, 2),
        PTOMA("ptoma", PrimordialFishEntity.Base.LARGE, 3),
        TETAN("tetan", PrimordialFishEntity.Base.LARGE, 4),
        SIMULA("simula", PrimordialFishEntity.Base.LARGE, 5);

        public static final Codec<PrimordialFishEntity.Pattern> CODEC = StringRepresentable.fromEnum(
                PrimordialFishEntity.Pattern::values);
        private static final IntFunction<PrimordialFishEntity.Pattern> BY_ID = ByIdMap.sparse(
                PrimordialFishEntity.Pattern::getPackedId,
                PrimordialFishEntity.Pattern.values(),
                AGONUS
        );
        private final String name;
        private final Component displayName;
        private final PrimordialFishEntity.Base base;
        private final int packedId;
        private final int rawId;

        Pattern(String name, PrimordialFishEntity.Base base, int id) {
            this.name = name;
            this.base = base;
            this.rawId = id;
            this.packedId = base.id | id << 8;
            // TODO
            this.displayName = Component.translatable("entity.bygone.primordial_fish.type." + this.name);
        }

        public static PrimordialFishEntity.Pattern byId(int packedId) {
            return BY_ID.apply(packedId);
        }

        public PrimordialFishEntity.Base base() {
            return this.base;
        }

        public int getPackedId() {
            return this.packedId;
        }

        public int getRawId() {
            return this.rawId;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }

        public Component displayName() {
            return this.displayName;
        }
    }

    static class PrimordialFishGroupData extends AbstractSchoolingFish.SchoolSpawnGroupData {
        final PrimordialFishEntity.Variant variant;

        PrimordialFishGroupData(PrimordialFishEntity leader, PrimordialFishEntity.Variant variant) {
            super(leader);
            this.variant = variant;
        }
    }

    public record Variant(PrimordialFishEntity.Pattern pattern, DyeColor baseColor, DyeColor patternColor) {
        public static final Codec<PrimordialFishEntity.Variant> CODEC = Codec.INT.xmap(
                PrimordialFishEntity.Variant::new,
                PrimordialFishEntity.Variant::getPackedId
        );

        public Variant(int packedVariant) {
            this(
                    PrimordialFishEntity.getPattern(packedVariant),
                    PrimordialFishEntity.getBaseColor(packedVariant),
                    PrimordialFishEntity.getPatternColor(packedVariant)
            );
        }

        public int getPackedId() {
            return PrimordialFishEntity.packVariant(this.pattern, this.baseColor, this.patternColor);
        }

        public PrimordialFishEntity.Pattern pattern() {
            return this.pattern;
        }

        public DyeColor baseColor() {
            return this.baseColor;
        }

        public DyeColor patternColor() {
            return this.patternColor;
        }
    }

    public PrimordialFishEntity.@NotNull Pattern getVariant() {
        return PrimordialFishEntity.getPattern(this.getPackedVariant());
    }


    public void setVariant(PrimordialFishEntity.@NotNull Pattern variant) {
        int packedVariant = this.getPackedVariant();
        DyeColor baseColor = getBaseColor(packedVariant);
        DyeColor patternColor = getPatternColor(packedVariant);
        this.setPackedVariant(packVariant(variant, baseColor, patternColor));
    }


}

