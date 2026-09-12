package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.buildings;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.*;

public class BuildingStack {
    private static final int MAX_PATH_CLEARANCE_ADJUSTMENT = 16;
    private static final int TANGENT_SAMPLE_RADIUS = 4;

    private final StructureTemplateManager templates;
    private final StructurePoolElement baseElement;

    private final RandomSource random;
    private final Rotation rotation;

    private final double normalX;
    private final double normalZ;

    private final List<PoolElementStructurePiece> buildings;
    public List<PoolElementStructurePiece> getBuildings() {
        return this.buildings.reversed();
    }

    private final PoolElementStructurePiece base;
    private final PoolElementStructurePiece top;

    public BuildingStack(
        StructureTemplateManager templates, StructurePoolElement baseElement,
        StructurePoolElement topElement, BlockPos anchor,
        PathOrientation orientation, int pathWidth, RandomSource random
    ) {
        this.templates = templates;
        this.random = random;
        this.baseElement = baseElement;
        this.normalX = orientation.normalX;
        this.normalZ = orientation.normalZ;
        this.rotation = orientation.rotation;
        this.buildings = new ArrayList<>();
        this.base = this.createPiece(baseElement, anchor);

        this.top = this.createPiece(topElement, this.base.getBoundingBox().getCenter());
        this.moveAbove(this.top, this.base);
        this.buildings.add(this.top);
        this.buildings.add(this.base);

        this.updateBoundingBox();
        this.placeBeside(anchor, pathWidth);
    }

    private BoundingBox boundingBox;
    public BoundingBox boundingBox() { return this.boundingBox; }

    private void placeBeside(BlockPos anchor, int pathWidth) {
        BoundingBox baseBox = this.base.getBoundingBox();
        BlockPos center = baseBox.getCenter();
        double nearExtentX = (this.normalX >= 0.0D)
            ? center.getX() - baseBox.minX() : baseBox.maxX() - center.getX();
        double nearExtentZ = (this.normalZ >= 0.0D)
            ? center.getZ() - baseBox.minZ() : baseBox.maxZ() - center.getZ();
        double buildingExtent = (Math.abs(this.normalX) * nearExtentX)
            + (Math.abs(this.normalZ) * nearExtentZ);

        double pathExtent = pathWidth * (Math.abs(this.normalX) + Math.abs(this.normalZ));
        double centerDistance = pathExtent + buildingExtent + 1;

        int targetCenterX = anchor.getX() + (int) Math.round(this.normalX * centerDistance);
        int targetCenterZ = anchor.getZ() + (int) Math.round(this.normalZ * centerDistance);
        this.move(targetCenterX - center.getX(), 0, targetCenterZ - center.getZ());
    }

    public boolean pushOutOfPath(Set<BlockPos> reservedPathColumns) {
        if (this.doesntCollideWith(reservedPathColumns)) return true;

        int movedX = 0;
        int movedZ = 0;
        for (int distance = 1; distance <= MAX_PATH_CLEARANCE_ADJUSTMENT; distance++) {
            int desiredX = (int) Math.round(this.normalX * distance);
            int desiredZ = (int) Math.round(this.normalZ * distance);

            int deltaX = desiredX - movedX;
            int deltaZ = desiredZ - movedZ;

            if (deltaX == 0 && deltaZ == 0) continue;
            this.move(deltaX, 0, deltaZ);

            movedX = desiredX;
            movedZ = desiredZ;
            if (this.doesntCollideWith(reservedPathColumns)) return true;
        }
        return false;
    }

    public boolean pushOutOfNeighbors(
        Set<BlockPos> reservedPathColumns,
        Set<BlockPos> occupied, BoundingBox allowedArea
    ) {
        if (this.doesntCollideWith(occupied)) return true;

        List<BlockPos> offsets = this.possibleOffsets();
        offsets.sort(Comparator
            .comparingDouble((BlockPos offset) -> {
                double normalDisplacement = offset.getX() * this.normalX + offset.getZ() * this.normalZ;
                double normalPenalty = Mth.square(normalDisplacement) * 4.0D;
                double inwardPenalty = normalDisplacement < 0.0D ? 2.0D : 0.0D;
                return Mth.lengthSquared(offset.getX(), offset.getZ()) + normalPenalty + inwardPenalty;
            })
            .thenComparingDouble(offset -> Mth.lengthSquared(offset.getX(), offset.getZ()))
            .thenComparingInt(BlockPos::getX)
            .thenComparingInt(BlockPos::getZ));

        for (BlockPos offset : offsets) {
            BoundingBox translated = this.boundingBox.moved(offset.getX(), 0, offset.getZ());
            if (!allowedArea.isInside(translated.minX(), translated.minY(), translated.minZ())
            || !allowedArea.isInside(translated.maxX(), translated.maxY(), translated.maxZ())
            || this.collidesHorizontally(translated, reservedPathColumns)
            || this.collidesHorizontally(translated, occupied))
                continue;

            this.move(offset.getX(), 0, offset.getZ());
            return true;
        }
        return false;
    }

    private List<BlockPos> possibleOffsets() {
        int maxOffsetX = this.boundingBox.getXSpan() / 2;
        int maxOffsetZ = this.boundingBox.getZSpan() / 2;
        List<BlockPos> offsets = new ArrayList<>(
            ((maxOffsetX * 2) + 1)
                * ((maxOffsetZ * 2) + 1) - 1
        );
        for (int offsetX = -maxOffsetX; offsetX <= maxOffsetX; offsetX++) {
            for (int offsetZ = -maxOffsetZ; offsetZ <= maxOffsetZ; offsetZ++) {
                if (offsetX != 0 || offsetZ != 0)
                    offsets.add(new BlockPos(offsetX, 0, offsetZ));
            }
        }
        return offsets;
    }

    public boolean placeOnGround(
        GreatPathGenerator generator, int proposedY,
        List<StructurePoolElement> candidates
    ) {
        OptionalInt groundY = this.findLowestGround(generator, proposedY);
        if (groundY.isEmpty()) return false;

        int foundationY = groundY.getAsInt();
        int targetBaseY = foundationY + 2 - this.baseElement.getGroundLevelDelta();
        this.move(0, targetBaseY - this.base.getBoundingBox().minY(), 0);
        this.structurePreplace(proposedY, foundationY, candidates);
        return true;
    }

    private OptionalInt findLowestGround(
        GreatPathGenerator generator, int proposedY
    ) {
        int lowestY = Integer.MAX_VALUE;
        BoundingBox box = this.base.getBoundingBox();
        for (int corner = 0; corner < 4; corner++) {
            int x = corner < 2 ? box.minX() : box.maxX();
            int z = (corner & 1) == 0 ? box.minZ() : box.maxZ();
            BlockPos cornerPosition = new BlockPos(x, proposedY, z);
            Optional<BlockPos> support = generator
                .findExtendedGround(cornerPosition).supportBelow();
            if (support.isEmpty()) return OptionalInt.empty();
            lowestY = Math.min(lowestY, support.get().getY());
        }
        return OptionalInt.of(lowestY);
    }

    // probably could use work eventually, like shifting a structure downwards
    // to fit preplacements above their own ground if its within a few blocks of
    // the main structure's height
    private void structurePreplace(
        int pathY, int groundY,
        List<StructurePoolElement> candidates
    ) {
        PoolElementStructurePiece highest = this.base;
        boolean placementRequired = groundY < pathY;
        int candidateIndex = candidates.indexOf(this.baseElement) + 1;
        while (placementRequired || highest.getBoundingBox().maxY() < pathY) {
            placementRequired = false;
            StructurePoolElement element = candidates.get(
                candidateIndex % candidates.size()
            );
            candidateIndex++;
            PoolElementStructurePiece preplacement = this.createPiece(
                element, highest.getBoundingBox().getCenter()
            );
            this.moveAbove(preplacement, highest);
            BoundingBox previousBoundingBox = highest.getBoundingBox();
            if (!this.offsetWithin(preplacement, previousBoundingBox)) {
                BoundingBox preplacementBounds = preplacement.getBoundingBox();
                PoolElementStructurePiece foundation = this.createPiece(
                    element, preplacementBounds.getCenter()
                );
                BoundingBox foundationBounds = foundation.getBoundingBox();
                BlockPos preplacementCenter = preplacementBounds.getCenter();
                BlockPos foundationCenter = foundationBounds.getCenter();
                foundation.move(
                    preplacementCenter.getX() - foundationCenter.getX(),
                    preplacementBounds.minY() - 1 - foundationBounds.maxY(),
                    preplacementCenter.getZ() - foundationCenter.getZ()
                );
                this.buildings.add(1, foundation);
            }
            this.buildings.add(1, preplacement);
            highest = preplacement;
        }
        this.moveAbove(this.top, highest);
        BoundingBox previousBoundingBox = highest.getBoundingBox();
        this.offsetWithin(this.top, previousBoundingBox);
        this.updateBoundingBox();
    }

    private boolean offsetWithin(
        PoolElementStructurePiece piece,
        BoundingBox previousBoundingBox
    ) {
        BoundingBox pieceBounds = piece.getBoundingBox();
        int minimumX = previousBoundingBox.minX() - pieceBounds.minX();
        int maximumX = previousBoundingBox.maxX() - pieceBounds.maxX();
        int minimumZ = previousBoundingBox.minZ() - pieceBounds.minZ();
        int maximumZ = previousBoundingBox.maxZ() - pieceBounds.maxZ();
        if (minimumX > maximumX || minimumZ > maximumZ) return false;

        int offsetX = this.random.nextIntBetweenInclusive(minimumX, maximumX);
        int offsetZ = this.random.nextIntBetweenInclusive(minimumZ, maximumZ);
        if (offsetX == 0 && offsetZ == 0) {
            offsetX = Mth.sign(minimumX != 0 ? minimumX : maximumX);
            if (offsetX == 0) offsetZ = Mth.sign(minimumZ != 0 ? minimumZ : maximumZ);
        }
        piece.move(offsetX, 0, offsetZ);
        return true;
    }

    private void move(int x, int y, int z) {
        for (PoolElementStructurePiece piece : this.buildings)
            piece.move(x, y, z);
        this.boundingBox.move(x, y, z);
    }

    boolean doesntCollideWith(Set<BlockPos> occupied) {
        return !collidesHorizontally(this.boundingBox, occupied);
    }

    private boolean collidesHorizontally(
        BoundingBox candidate,
        Set<BlockPos> occupied
    ) {
        for (int x = candidate.minX(); x <= candidate.maxX(); x++) {
            for (int z = candidate.minZ(); z <= candidate.maxZ(); z++) {
                if (occupied.contains(new BlockPos(x, 0, z))) return true;
            }
        }
        return false;
    }

    private PoolElementStructurePiece createPiece(
        StructurePoolElement element, BlockPos position
    ) {
        BoundingBox box = element.getBoundingBox(this.templates, position, this.rotation);
        return new GreatTrailBuildingPiece(
            this.templates, element, position, this.rotation, box
        );
    }

    private void moveAbove(
        PoolElementStructurePiece piece,
        PoolElementStructurePiece below
    ) {
        BlockPos center = piece.getBoundingBox().getCenter();
        BlockPos belowCenter = below.getBoundingBox().getCenter();
        piece.move(
            belowCenter.getX() - center.getX(),
            below.getBoundingBox().maxY() + 1 - piece.getBoundingBox().minY(),
            belowCenter.getZ() - center.getZ()
        );
    }

    private void updateBoundingBox() {
        this.boundingBox = BoundingBox.encapsulatingBoxes(
            this.buildings.stream().map(PoolElementStructurePiece::getBoundingBox).toList()
        ).orElseThrow();
    }

    record PathOrientation(double normalX, double normalZ, Rotation rotation) {
        static Optional<PathOrientation> fromPath(
            List<BlockPos> path, int pathIndex, int side
        ) {
            BlockPos before = path.get(Mth.clamp(pathIndex
                - TANGENT_SAMPLE_RADIUS, 0, path.size() - 1));
            BlockPos after = path.get(Mth.clamp(pathIndex
                + TANGENT_SAMPLE_RADIUS, 0, path.size() - 1));
            int differenceX = after.getX() - before.getX();
            int differenceZ = after.getZ() - before.getZ();
            double length = Mth.length(differenceX, differenceZ);
            if (length == 0.0D) return Optional.empty();

            double sideMultiplier = side < 0 ? 1.0D : -1.0D;
            double normalX = differenceZ / length * sideMultiplier;
            double normalZ = -differenceX / length * sideMultiplier;
            Direction facing = Direction.getNearest(-normalX, 0.0D, -normalZ);
            Rotation rotation = Arrays.stream(Rotation.values()).toList().get(
                Math.floorMod(
                    facing.get2DDataValue() - Direction.NORTH.get2DDataValue(),
                    Rotation.values().length
                )
            );
            return Optional.of(new PathOrientation(normalX, normalZ, rotation));
        }
    }

}
