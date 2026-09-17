package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.buildings;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.GreatTrailSettings;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator.PathSample;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.PathRasterizer.RasterizedSegment;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.PathResolver.ResolvedPath;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments.SegmentState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public record BuildingGenerator(
    Structure.GenerationContext context,
    GreatPathGenerator pathGenerator,
    GreatTrailSettings settings,
    Holder<StructureTemplatePool> buildingPool,
    Holder<StructureTemplatePool> topPool
) {

    public void place(ResolvedPath path, StructurePiecesBuilder pieces) {
        Set<BlockPos> reservedPathColumns = this.reservedPathColumns(path.segments());
        Set<BlockPos> occupiedColumns = new HashSet<>();

        Map<StructurePoolElement, Integer> acceptedBases = new IdentityHashMap<>();
        Map<StructurePoolElement, Integer> acceptedTops = new IdentityHashMap<>();
        BoundingBox allowedArea = this.pathGenerator.allowedArea();

        for (RasterizedSegment segment : path.segments()) {
            List<BlockPos> line = new ArrayList<>(segment.samples().size());
            for (PathSample sample : segment.samples())
                line.add(sample.position());

            for (int side = -1; side <= 1; side += 2) {
                for (int i = 0; i < line.size(); i++) {
                    SegmentState anchoredState = segment
                        .samples().get(i).state();
                    if (!anchoredState.canPlaceBuildings()) continue;

                    Optional<BuildingStack> stack = this.tryPlace(
                        line, i, side, reservedPathColumns,
                        occupiedColumns, acceptedBases,
                        acceptedTops, allowedArea
                    );
                    if (stack.isEmpty()) continue;

                    BuildingStack buildingStack = stack.get();
                    buildingStack.getBuildings().forEach(pieces::addPiece);

                    BoundingBox buildingBounds = buildingStack.boundingBox();
                    this.reserve(occupiedColumns, buildingBounds);
                }
            }
        }
    }

    private Set<BlockPos> reservedPathColumns(List<RasterizedSegment> segments) {
        Set<BlockPos> pathReservations = new HashSet<>();
        int width = this.settings.pathWidth();
        for (RasterizedSegment segment : segments) {
            for (PathSample sample : segment.samples()) {
                BlockPos position = sample.position();
                for (int x = position.getX() - width; x <= position.getX() + width; x++) {
                    for (int z = position.getZ() - width; z <= position.getZ() + width; z++)
                        pathReservations.add(new BlockPos(x, 0, z));
                }
            }
        }
        return pathReservations;
    }

    private Optional<BuildingStack> tryPlace(
        List<BlockPos> path, int index, int side,
        Set<BlockPos> reservedPathColumns,
        Set<BlockPos> occupiedColumns,
        Map<StructurePoolElement, Integer> acceptedBases,
        Map<StructurePoolElement, Integer> acceptedTops,
        BoundingBox allowedArea
    ) {
        Optional<BuildingStack.PathOrientation> orientation =
            BuildingStack.PathOrientation.fromPath(path, index, side);
        if (orientation.isEmpty()) return Optional.empty();

        List<StructurePoolElement> baseCandidates =
            this.prioritizeTemplates(acceptedBases, this.buildingPool);
        List<StructurePoolElement> topCandidates =
            this.prioritizeTemplates(acceptedTops, this.topPool, this.buildingPool);
        BlockPos anchor = path.get(index);

        for (int i = 0; i < this.settings.buildingPlacementRetries(); i++) {
            StructurePoolElement baseElement = baseCandidates.get(i % baseCandidates.size());
            StructurePoolElement topElement = topCandidates.get((i + index) % topCandidates.size());
            BuildingStack stack = new BuildingStack(
                this.context.structureTemplateManager(),
                baseElement, topElement, anchor, orientation.get(),
                this.settings.pathWidth(), this.context.random()
            );

            if (!stack.pushOutOfPath(reservedPathColumns)
            || !stack.pushOutOfNeighbors(reservedPathColumns, occupiedColumns, allowedArea)
            || !stack.placeOnGround(this.pathGenerator, anchor.getY(), baseCandidates))
                continue;

            BoundingBox stackBounds = stack.boundingBox();
            if (allowedArea.isInside(stackBounds.minX(), stackBounds.minY(), stackBounds.minZ())
            && allowedArea.isInside(stackBounds.maxX(), stackBounds.maxY(), stackBounds.maxZ())) {
                acceptedBases.merge(baseElement, 1, Integer::sum);
                acceptedTops.merge(topElement, 1, Integer::sum);
                return Optional.of(stack);
            }
        }
        return Optional.empty();
    }

    private void reserve(Set<BlockPos> columns, BoundingBox bounds) {
        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            for (int z = bounds.minZ(); z <= bounds.maxZ(); z++)
                columns.add(new BlockPos(x, 0, z));
        }
    }


    @SafeVarargs private List<StructurePoolElement> prioritizeTemplates(
        Map<StructurePoolElement, Integer> acceptedCounts,
        Holder<StructureTemplatePool>... pools
    ) {
        Map<StructurePoolElement, Integer> weights = new IdentityHashMap<>();
        List<StructurePoolElement> unique = new ArrayList<>();
        for (Holder<StructureTemplatePool> pool : pools) {
            for (StructurePoolElement element : pool.value()
                .getShuffledTemplates(this.context.random())) {
                if (!weights.containsKey(element)) unique.add(element);
                weights.merge(element, 1, Integer::sum);
            }
        }
        unique.sort(
            Comparator.comparingDouble(element ->
            ((double) acceptedCounts.getOrDefault(element, 0)) / weights.get(element)
        ));
        return unique;
    }

}
