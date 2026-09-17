package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.GreatTrailSettings;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator.GroundResult;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator.PathSample;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.GreatPathGenerator.PathSegment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public record PathRasterizer(GreatPathGenerator generator) {

    public GreatTrailSettings getSettings() { return this.generator.getSettings(); }

    public record RasterizedSegment(List<PathSample> samples) {}

    public List<RasterizedSegment> rasterize(List<PathSegment> segments) {
        List<RasterizedSegment> result = new ArrayList<>(segments.size());
        for (int i = 0; i < segments.size(); i++)
            result.add(this.rasterizeSegment(segments, i));
        return result;
    }

    private RasterizedSegment rasterizeSegment(List<PathSegment> segments, int index) {
        PathSegment segment = segments.get(index);
        List<BlockPos> idealCenterLine = this.rasterizeSpline(
            ControlPoints.fromSegments(segments, index),
            this.getSettings().sampleRate()
        );
        List<PathSample> rasterized = new ArrayList<>(idealCenterLine.size());
        for (BlockPos idealPosition : idealCenterLine) {
            if (!segment.state().inlineTerrain()) {
                rasterized.add(new PathSample(idealPosition, segment.state()));
                continue;
            }
            GroundResult result = this.generator.findGround(
                idealPosition, this.getSettings().bridgingDifference(),
                idealPosition.getY(), segment.to().getY()
            );
            rasterized.add(new PathSample(result.pathPosition(), result.state()));
        }
        return new RasterizedSegment(rasterized);
    }

    private List<BlockPos> rasterizeSpline(
        ControlPoints points, int sampleRate
    ) {
        List<BlockPos> result = new ArrayList<>();
        BlockPos previous = null;
        for (int sample = 0; sample <= sampleRate; sample++) {
            float progress = (float) sample / sampleRate;
            BlockPos current = catmullRom(points, progress);
            if (previous == null) result.add(current);
            else this.appendIdeal(result, previous, current);
            previous = current;
        }
        return result;
    }

    private void appendIdeal(
        List<BlockPos> result, BlockPos from, BlockPos to
    ) {
        int differenceX = to.getX() - from.getX();
        int differenceZ = to.getZ() - from.getZ();
        int steps = Math.max(Math.abs(differenceX), Math.abs(differenceZ));
        if (steps == 0) {
            result.set(result.size() - 1, to);
            return;
        }

        for (int i = 1; i <= steps; i++) {
            double progress = (double) i / (double) steps;
            BlockPos point = new BlockPos(
                (int) Math.round(Mth.lerp(progress, from.getX(), to.getX())),
                (int) Math.round(Mth.lerp(progress, from.getY(), to.getY())),
                (int) Math.round(Mth.lerp(progress, from.getZ(), to.getZ()))
            );
            BlockPos last = result.getLast();
            if (last.getX() == point.getX() && last.getZ() == point.getZ())
                result.set(result.size() - 1, point);
            else result.add(point);
        }
    }

    // pissed off I didnt know minecraft just had this function in its math library
    private static BlockPos catmullRom(
        ControlPoints points, float progress
    ) {
        return new BlockPos(
            Math.round(Mth.catmullrom(
                progress, points.previous.getX(), points.from.getX(),
                points.to.getX(), points.next.getX()
            )),
            Math.round(Mth.catmullrom(
                progress, points.previous.getY(), points.from.getY(),
                points.to.getY(), points.next.getY()
            )),
            Math.round(Mth.catmullrom(
                progress, points.previous.getZ(), points.from.getZ(),
                points.to.getZ(), points.next.getZ()
            ))
        );
    }

    private record ControlPoints(
        BlockPos previous, BlockPos from, BlockPos to, BlockPos next
    ) {
        static ControlPoints fromSegments(List<PathSegment> segments, int index) {
            PathSegment segment = segments.get(index);
            BlockPos from = segment.from();
            BlockPos to = segment.to();
            BlockPos previous = index > 0 ? segments
                .get(index - 1).from() : from;
            BlockPos next = index < segments.size() - 1 ? segments
                .get(index + 1).to() : to;
            return new ControlPoints(previous, from, to, next);
        }
    }
}
