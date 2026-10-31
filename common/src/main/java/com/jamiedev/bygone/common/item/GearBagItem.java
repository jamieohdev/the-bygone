package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.component.item.GearBagData;
import com.jamiedev.bygone.core.registry.BGDataComponentTypes;

import java.util.List;
import java.util.Optional;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;

import org.apache.commons.lang3.math.Fraction;

import org.jetbrains.annotations.Nullable;

public class GearBagItem extends Item{
    public GearBagItem(Item.Properties properties){
        super(properties);
    }

    // XXX: remove this once we stop guaranteeing 3.1 upgrade compat!
    // this is only here bc gear bags used to use BundleContents
    @Override
    public void verifyComponentsAfterLoad(ItemStack stack){
        if(stack.has(DataComponents.BUNDLE_CONTENTS)){
            BundleContents old = stack.remove(DataComponents.BUNDLE_CONTENTS);
            GearBagData.Mutable bag = GearBagData.Mutable.from(stack);
            for(ItemStack is : old.items()) bag.insert(is);
            stack.set(BGDataComponentTypes.GEAR_BAG.get(), bag.toImmutable());
        }
    }




    // we need to reimplement a good amount of bundle methods, since they all hardcode the bundle component.
    // however, it's not the worst. we can simplify the logic a good amount here.

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player){
        if(stack.getCount() != 1 || action != ClickAction.SECONDARY) return false;
        GearBagData.Mutable bag = GearBagData.Mutable.from(stack);

        if(slot.getItem().isEmpty()){
            if(bag.remove(slot)) this.ding(SoundEvents.BUNDLE_REMOVE_ONE, player);
            else return false;
        } else if(bag.insert(slot, player)) this.ding(SoundEvents.BUNDLE_INSERT, player);
        else return false;
        stack.set(BGDataComponentTypes.GEAR_BAG.get(), bag.toImmutable());
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access){
        if(stack.getCount() != 1 || action != ClickAction.SECONDARY) return false;
        GearBagData.Mutable bag = GearBagData.Mutable.from(stack);

        if(other.isEmpty()){
            ItemStack retrieved = bag.remove();
            if(retrieved != null){
                access.set(retrieved);
                this.ding(SoundEvents.BUNDLE_REMOVE_ONE, player);
            } else return false;
        } else if(bag.insert(other)) this.ding(SoundEvents.BUNDLE_INSERT, player);
        else return false;
        stack.set(BGDataComponentTypes.GEAR_BAG.get(), bag.toImmutable());
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        GearBagData bag = GearBagData.from(stack);
        if(bag.isEmpty()) return InteractionResultHolder.fail(stack);

        stack.set(BGDataComponentTypes.GEAR_BAG.get(), GearBagData.EMPTY);
        for(ItemStack is : bag.items()) player.drop(is, true);
        player.awardStat(Stats.ITEM_USED.get(this));
        this.ding(SoundEvents.BUNDLE_DROP_CONTENTS, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void onDestroyed(ItemEntity entity){
        GearBagData bag = GearBagData.from(entity.getItem());
        if(!bag.isEmpty()) ItemUtils.onContainerDestroyed(entity, bag.itemsCopy());
    }

    @Override
    public boolean canFitInsideContainerItems(){
        return false;
    }



    @Override
    public boolean isBarVisible(ItemStack stack){
        return !GearBagData.from(stack).isEmpty();
    }
    @Override
    public int getBarWidth(ItemStack stack){
        return (int)(13.0 * GearBagData.from(stack).weight().doubleValue());
    }
    @Override
    public int getBarColor(ItemStack stack){
        return Items.BUNDLE.getBarColor(stack); // ... it doesn't use stack so.
    }



    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack){
        if(stack.has(DataComponents.HIDE_TOOLTIP) || stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)) return Optional.empty();
        return Optional.of(new BundleTooltip(GearBagData.from(stack)));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag){
        Fraction weight = GearBagData.from(stack).weight();
        tooltip.add(Component.translatable("item.minecraft.bundle.fullness", weight.getNumerator(), weight.getDenominator()).withStyle(ChatFormatting.GRAY));
    }



    private void ding(SoundEvent sound, Entity entity){
        entity.playSound(sound, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
