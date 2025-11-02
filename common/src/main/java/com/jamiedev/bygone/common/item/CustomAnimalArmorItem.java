package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CustomAnimalArmorItem extends ArmorItem {
    private final ResourceLocation textureLocation;
    @Nullable
    private final ResourceLocation overlayTextureLocation;
    private final BodyType bodyType;

    public CustomAnimalArmorItem(Holder<ArmorMaterial> armorMaterial, BodyType bodyType, boolean hasOverlay, Item.Properties properties) {
        super(armorMaterial, ArmorItem.Type.BODY, properties);
        this.bodyType = bodyType;
        ResourceLocation resourcelocation = bodyType.textureLocator.apply(armorMaterial.unwrapKey().orElseThrow().location());
        this.textureLocation = resourcelocation.withSuffix(".png");
        if (hasOverlay) {
            this.overlayTextureLocation = resourcelocation.withSuffix("_overlay.png");
        } else {
            this.overlayTextureLocation = null;
        }
    }

    public ResourceLocation getTexture() {
        return this.textureLocation;
    }

    @Nullable
    public ResourceLocation getOverlayTexture() {
        return this.overlayTextureLocation;
    }

    public BodyType getBodyType() {
        return this.bodyType;
    }

    @Override
    public SoundEvent getBreakingSound() {
        return this.bodyType.breakingSound;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public enum BodyType {
        // EQUESTRIAN(p_323547_ -> p_323547_.withPath(p_323717_ -> "textures/entity/horse/armor/horse_armor_" + p_323717_), SoundEvents.ITEM_BREAK),
        //    CANINE(p_323678_ -> p_323678_.withPath("textures/entity/wolf/wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        BIG_BEAK(id -> Bygone.id("textures/entity/big_beak/beak_" + id.getPath()), SoundEvents.ITEM_BREAK);

        final Function<ResourceLocation, ResourceLocation> textureLocator;
        final SoundEvent breakingSound;

        BodyType(Function<ResourceLocation, ResourceLocation> textureLocator, SoundEvent breakingSound) {
            this.textureLocator = textureLocator;
            this.breakingSound = breakingSound;
        }
    }
}
