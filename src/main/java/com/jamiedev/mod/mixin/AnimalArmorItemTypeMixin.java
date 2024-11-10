package com.jamiedev.mod.mixin;
import com.jamiedev.mod.common.JamiesMod;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import static net.minecraft.item.AnimalArmorItem.Type.*;


@Unique
@Mixin(AnimalArmorItem.Type.class)
public class AnimalArmorItemTypeMixin {


    @Unique
    private static AnimalArmorItem.Type[] bygone$values = new AnimalArmorItem.Type[]{
            EQUESTRIAN,
            CANINE
    };

    @Shadow
    @Final
    @Mutable
    public static AnimalArmorItem.Type[] values() {
        return bygone$values;
    }

    @Unique
    private static AnimalArmorItem.Type BIG_BEAK = bygone$addVariant("big_beak", id ->
            JamiesMod.getModId( "textures/entity/big_beak/beak_" + id.getPath()), SoundEvents.ENTITY_ITEM_BREAK);

    @Invoker("<init>")
    public static AnimalArmorItem.Type bygone$invokeInit(String name, int index, final Function<Identifier, Identifier> textureIdFunction, final SoundEvent breakSound) {
        throw new AssertionError();
    }

    @Unique
    private static AnimalArmorItem.Type bygone$addVariant(String internalName, final Function<Identifier, Identifier> textureIdFunction, final SoundEvent breakSound) {
        var variants = new ArrayList<>(Arrays.asList(AnimalArmorItemTypeMixin.bygone$values));
        var beakArmor = bygone$invokeInit(internalName,variants.getLast().ordinal() + 1, textureIdFunction, breakSound);
        variants.add(beakArmor);
        AnimalArmorItemTypeMixin.bygone$values = variants.toArray(new AnimalArmorItem.Type[0]);

        JamiesMod.BIG_BEAK_ARMOR = beakArmor;

        return beakArmor;
    }
}