package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.GreatTrailSettings;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator.PathSegment;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator.GroundResult;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments.SegmentState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.phys.Vec3;
import org.joml.Intersectiond;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public record PathResolver(GreatPathGenerator generator) {

    public GreatTrailSettings getSettings() { return this.generator.getSettings(); }

    public record ResolvedPath(
        BlockPos startPosition,
        List<PathRasterizer.RasterizedSegment> segments
    ) {
        public void addTo(StructurePiecesBuilder pieces, GreatTrailSettings settings) {
            HashMap<SegmentState, List<GreatPathPiece.PlacementPass>> placementPasses = new HashMap<>();
            for (PathRasterizer.RasterizedSegment segment : this.segments)
                GreatPathPiece.PlacementPass.addSegment(placementPasses, segment.samples());

            for (SegmentState state : SegmentState.values()) {
                List<GreatPathPiece.PlacementPass> passes = placementPasses.get(state);
                if (passes == null) continue;
                for (GreatPathPiece.PlacementPass pass : passes)
                    pieces.addPiece(new GreatPathPiece(
                        pass, settings.pathWidth(), settings.bridgingDifference()
                    ));
            }
        }
    }

    public ResolvedPath resolve(List<BlockPos> nodes) {
        List<PathSegment> segments = new ArrayList<>(nodes.size() - 1);
        for (int i = 0; i < nodes.size() - 1; i++) {
            BlockPos from = nodes.get(i);
            BlockPos to = nodes.get(i + 1);
            BlockPos idealMean = this.getMeanNode(from, to);
            GroundResult meanResult = this.generator.findGround(
                idealMean, this.getSettings().bridgingDifference(),
                from.getY(), to.getY()
            );
            SegmentState state = this.predictedState(from, to, meanResult);
            segments.add(new PathSegment(from, to, state));
        }

        return new ResolvedPath(
            nodes.getFirst(), new PathRasterizer(
                this.generator).rasterize(segments)
        );
    }

    boolean slopeTooSteep(BlockPos from, BlockPos to) {
        GroundResult meanResult = this.generator.findGround(
            this.getMeanNode(from, to),
            this.getSettings().bridgingDifference(),
            from.getY(), to.getY()
        );
        if (!this.predictedState(from, to, meanResult).rejectsSteepSlope()) return false;

        BlockPos difference = to.subtract(from);
        return Mth.square(difference.getY()) > Mth.lengthSquared(difference.getX(), difference.getZ());
    }

    private static final int MAX_INTERSECTIONS = 1;
    boolean exceedsIntersections(List<BlockPos> nodes, BlockPos candidateEnd) {
        if (nodes.size() < 2) return false;

        BlockPos candidateStart = nodes.getLast();

        int totalIntersections = 0;
        Vector3d closestCandidate = new Vector3d();
        Vector3d closestSegment = new Vector3d();
        for (int i = 0; i < nodes.size() - 1; i++) {
            BlockPos segmentStart = nodes.get(i);
            BlockPos segmentEnd = nodes.get(i + 1);

            boolean segmentIntersection = Intersectiond.findClosestPointsLineSegments(
                candidateStart.getX(), 0, candidateStart.getZ(),
                candidateEnd.getX(), 0, candidateEnd.getZ(),
                segmentStart.getX(), 0, segmentStart.getZ(),
                segmentEnd.getX(), 0, segmentEnd.getZ(),
                closestCandidate, closestSegment
            ) <= 1.0E-8;

            if (segmentIntersection) totalIntersections++;
            if (totalIntersections > MAX_INTERSECTIONS) return true;
        }
        return false;
    }

    // "early" state detection based on mean
    private SegmentState predictedState(BlockPos from, BlockPos to, GroundResult meanResult) {
        int differenceY = to.getY() - from.getY();
        if (Math.abs(differenceY) <= this.getSettings().bridgingDifference())
            return meanResult.state();
        return differenceY < 0 ? SegmentState.TUNNEL : SegmentState.BRIDGE;
    }

    private BlockPos getMeanNode(BlockPos first, BlockPos second) {
        return BlockPos.containing(Vec3.atCenterOf(first).lerp(Vec3.atCenterOf(second), 0.5D));
    }

}
