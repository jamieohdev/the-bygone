package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.worldgen.feature.config.CenteredTreeConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

/*
* putting this here but i didnt want to touch world generation in case I messed anything up
* so the json for this is actually a little redundant. ideally the placed feature would
* reference the configured feature but theyre individually inlined in the placed feature
*
* if it werent for centering I would've made the placed feature just reference the
* configured feature but then saplings place sable trees that drift off really badly
*
* i dont know that much about worldgen i dont wanna break anything
*/
public class CenteredTreeFeature extends Feature<CenteredTreeConfig> {
    public CenteredTreeFeature(Codec<CenteredTreeConfig> codec) { super(codec);}

    @Override
    public boolean place(FeaturePlaceContext<CenteredTreeConfig> context) {
        List<ResourceLocation> templates = context.config().templates();
        if (templates.isEmpty()) return false;

        RandomSource random = context.random();
        StructureTemplate template = context.level()
            .getLevel().getServer().getStructureManager()
            .getOrCreate(templates.get(random.nextInt(templates.size())));

        Vec3i size = template.getSize();
        BlockPos centeredPosition = context.origin()
            .offset(-size.getX() / 2, 0, -size.getZ() / 2);
        StructurePlaceSettings settings = new StructurePlaceSettings()
            .setRotation(Rotation.NONE).setRandom(random);

        return template.placeInWorld(
            context.level(), centeredPosition,
            centeredPosition, settings, random, 3
        );
    }
}