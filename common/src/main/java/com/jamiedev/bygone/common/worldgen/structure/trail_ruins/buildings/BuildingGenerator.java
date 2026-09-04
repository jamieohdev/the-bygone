package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.buildings;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.PathResolver;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;

public record BuildingGenerator(Structure.GenerationContext context) {
    public List<BuildingStack> resolve(PathResolver.ResolvedPath resolvedPath) {
        return List.of();
    }
}
