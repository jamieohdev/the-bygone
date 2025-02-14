package com.jamiedev.bygone.mixin.client;

import com.jamiedev.bygone.Bygone;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;


//todo CRASHES, needs a replacement that doesn't involve adding enum entries
@Unique
@Mixin(DimensionSpecialEffects.SkyType.class)
public class DimensionEffectsSkyTypeMixin 
{
    @Unique
    private static DimensionSpecialEffects.SkyType[] bygone$values = new DimensionSpecialEffects.SkyType[]{
            DimensionSpecialEffects.SkyType.NONE,  DimensionSpecialEffects.SkyType.NORMAL, DimensionSpecialEffects.SkyType.END
    };

    @Shadow
    @Final
    @Mutable
    public static DimensionSpecialEffects.SkyType[] values() {
        return bygone$values;
    }

    @Unique
    private static final DimensionSpecialEffects.SkyType BYGONE_SKY = bygone$addVariant("bygone_sky", id ->
            Bygone.id( "textures/environment/" + id.getPath()), SoundEvents.ITEM_BREAK);

    @Invoker("<init>")
    public static DimensionSpecialEffects.SkyType bygone$invokeInit(String name, int index, final Function<ResourceLocation, ResourceLocation> textureIdFunction, final SoundEvent breakSound) {
        throw new AssertionError();
    }

    @Unique
    private static DimensionSpecialEffects.SkyType bygone$addVariant(String internalName, final Function<ResourceLocation, ResourceLocation> textureIdFunction, final SoundEvent breakSound) {
        var variants = new ArrayList<>(Arrays.asList(DimensionEffectsSkyTypeMixin.bygone$values));
        var beakArmor = bygone$invokeInit(internalName,variants.getLast().ordinal() + 1, textureIdFunction, breakSound);
        variants.add(beakArmor);
        DimensionEffectsSkyTypeMixin.bygone$values = variants.toArray(new DimensionSpecialEffects.SkyType[0]);

        //BygoneClientFabric.BYGONE_SKY = beakArmor;

        return beakArmor;
    }
}
