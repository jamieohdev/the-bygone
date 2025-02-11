package com.jamiedev.bygone.mixin;
import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.fabric.BygoneFabric;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.AnimalArmorItem;

import static net.minecraft.world.item.AnimalArmorItem.BodyType.*;


@Unique
@Mixin(AnimalArmorItem.BodyType.class)
public class AnimalArmorItemTypeMixin {


    @Unique
    private static AnimalArmorItem.BodyType[] bygone$values = new AnimalArmorItem.BodyType[]{
            EQUESTRIAN,
            CANINE
    };

    @Shadow
    @Final
    @Mutable
    public static AnimalArmorItem.BodyType[] values() {
        return bygone$values;
    }

    @Unique
    private static final AnimalArmorItem.BodyType BIG_BEAK = bygone$addVariant("big_beak", id ->
            Bygone.getModId( "textures/entity/big_beak/beak_" + id.getPath()), SoundEvents.ITEM_BREAK);

    @Invoker("<init>")
    public static AnimalArmorItem.BodyType bygone$invokeInit(String name, int index, final Function<ResourceLocation, ResourceLocation> textureIdFunction, final SoundEvent breakSound) {
        throw new AssertionError();
    }

    @Unique
    private static AnimalArmorItem.BodyType bygone$addVariant(String internalName, final Function<ResourceLocation, ResourceLocation> textureIdFunction, final SoundEvent breakSound) {
        var variants = new ArrayList<>(Arrays.asList(AnimalArmorItemTypeMixin.bygone$values));
        var beakArmor = bygone$invokeInit(internalName,variants.getLast().ordinal() + 1, textureIdFunction, breakSound);
        variants.add(beakArmor);
        AnimalArmorItemTypeMixin.bygone$values = variants.toArray(new AnimalArmorItem.BodyType[0]);

        BygoneFabric.BIG_BEAK_ARMOR = beakArmor;

        return beakArmor;
    }
}