package com.jamiedev.mod.common.client.mixin;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.client.JamiesModClient;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;



@Unique
@Mixin(DimensionEffects.SkyType.class)
public class DimensionEffectsSkyTypeMixin 
{
    @Unique
    private static DimensionEffects.SkyType[] bygone$values = new DimensionEffects.SkyType[]{
            DimensionEffects.SkyType.NONE,  DimensionEffects.SkyType.NORMAL, DimensionEffects.SkyType.END
    };

    @Shadow
    @Final
    @Mutable
    public static DimensionEffects.SkyType[] values() {
        return bygone$values;
    }

    @Unique
    private static DimensionEffects.SkyType BYGONE_SKY = bygone$addVariant("bygone_sky", id ->
            JamiesMod.getModId( "textures/environment/" + id.getPath()), SoundEvents.ENTITY_ITEM_BREAK);

    @Invoker("<init>")
    public static DimensionEffects.SkyType bygone$invokeInit(String name, int index, final Function<Identifier, Identifier> textureIdFunction, final SoundEvent breakSound) {
        throw new AssertionError();
    }

    @Unique
    private static DimensionEffects.SkyType bygone$addVariant(String internalName, final Function<Identifier, Identifier> textureIdFunction, final SoundEvent breakSound) {
        var variants = new ArrayList<>(Arrays.asList(DimensionEffectsSkyTypeMixin.bygone$values));
        var beakArmor = bygone$invokeInit(internalName,variants.getLast().ordinal() + 1, textureIdFunction, breakSound);
        variants.add(beakArmor);
        DimensionEffectsSkyTypeMixin.bygone$values = variants.toArray(new DimensionEffects.SkyType[0]);

        JamiesModClient.BYGONE_SKY = beakArmor;

        return beakArmor;
    }
}
