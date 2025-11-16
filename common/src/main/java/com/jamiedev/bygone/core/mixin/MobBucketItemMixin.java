package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.entity.PrimordialFishEntity;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(MobBucketItem.class)
public class MobBucketItemMixin {
    @Final
    @Shadow
    private EntityType<?> type;

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    public void appendPrimordialFishHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (this.type == BGEntityTypes.PRIMORDIAL_FISH.get()) {
            CustomData entityData = stack.getOrDefault(
                    DataComponents.BUCKET_ENTITY_DATA,
                    CustomData.EMPTY
            );
            if (entityData.isEmpty()) {
                return;
            }

            Optional<PrimordialFishEntity.Variant> optionalVariant = entityData.read(PrimordialFishEntity.VARIANT_FIELD_CODEC)
                    .result();
            if (optionalVariant.isPresent()) {
                PrimordialFishEntity.Variant variant = optionalVariant.get();
                ChatFormatting[] formatting = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
                int variantIndex = PrimordialFishEntity.COMMON_VARIANTS.indexOf(variant);
                if (variantIndex != -1) {
                    tooltipComponents.add(Component.translatable(PrimordialFishEntity.getPredefinedName(variantIndex))
                            .withStyle(formatting));
                    return;
                }

                String baseColor = "color.minecraft." + variant.baseColor();
                String patternColor = "color.minecraft." + variant.patternColor();

                tooltipComponents.add(variant.pattern()
                        .displayName()
                        .plainCopy()
                        .withStyle(formatting));
                MutableComponent baseColorComponent = Component.translatable(baseColor);
                if (!baseColor.equals(patternColor)) {
                    baseColorComponent.append(", ").append(Component.translatable(patternColor));
                }

                baseColorComponent.withStyle(formatting);
                tooltipComponents.add(baseColorComponent);
            }
        }
    }
}
