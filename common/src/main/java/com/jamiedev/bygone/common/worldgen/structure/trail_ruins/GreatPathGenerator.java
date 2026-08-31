package com.jamiedev.bygone.common.worldgen.structure.trail_ruins;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class GreatPathGenerator {
    private static final int REQUIRED_SPACE = 3;

    private final Structure.GenerationContext context;
    private final GreatTrailSettings settings;

    public enum SegmentState implements StringRepresentable {
        NORMAL("normal", Blocks.DIRT_PATH::defaultBlockState),
        BRIDGE("bridge", () -> BGBlocks.ANCIENT_PLANKS.get().defaultBlockState());

        static final Codec<SegmentState> CODEC = StringRepresentable.fromEnum(SegmentState::values);

        public final Supplier<BlockState> blockGetter;
        private final String identifier;
        SegmentState(String name, Supplier<BlockState> blockStateSupplier) {
            this.identifier = name;
            this.blockGetter = blockStateSupplier;
        }

        @Override
        public @NotNull String getSerializedName() {
            return identifier;
        }
    }

    public GreatPathGenerator(
        Structure.GenerationContext context,
        GreatTrailSettings settings
    ) {
        this.context = context;
        this.settings = settings;
    }

    public List<PathNode> generateNodes(BlockPos startPosition) {
        ChunkPos chunk = this.context.chunkPos();
        LevelHeightAccessor heights = this.context.heightAccessor();
        int radius = this.settings.chunkRadius();
        BoundingBox allowedArea = BoundingBox.fromCorners(
            chunk.getBlockAt(-radius * 16, heights.getMinBuildHeight(), -radius * 16),
            chunk.getBlockAt((radius + 1) * 16 - 1, heights.getMaxBuildHeight() - 1, (radius + 1) * 16 - 1)
        );
        RandomSource random = this.context.random();

        // sample nodes
        List<BlockPos> nodes = new ArrayList<>(this.settings.nodeCount());
        nodes.add(startPosition);
        double direction = random.nextDouble() * Math.PI * 2.0D;

        for (int id = 1; id < this.settings.nodeCount(); id++) {
            BlockPos previous = nodes.getLast();
            double turn = (random.nextDouble() * 2.0D - 1.0D) * this.settings.maxTurnAngle();
            direction += Math.toRadians(turn);
            int spacing = Mth.nextInt(random, this.settings.nodeSpacingMin(), this.settings.nodeSpacingMax());
            nodes.add(new BlockPos(
                previous.getX() + Mth.floor(Math.cos(direction) * spacing),
                startPosition.getY(),
                previous.getZ() + Mth.floor(Math.sin(direction) * spacing)
            ));
        }

        // get centered nodes afterwards
        List<BlockPos> centeredNodes = this.getCenteredNodes(
            nodes, allowedArea.getCenter().getX(), allowedArea.getCenter().getZ())
            .stream().filter(position -> allowedArea.isInside(
                position.getX(), position.getY(), position.getZ())).toList();

        // floor nodes
        List<PathNode> flooredNodes = new ArrayList<>(centeredNodes.size());
        for (int id = 0; id < centeredNodes.size(); id++) {
            BlockPos sampled = centeredNodes.get(id);
            // patenmted technology
            HackyShittyBlockGetter hackyShittyBlockGetterThatsWhatItsCalled =
                new HackyShittyBlockGetter(this.context,
                    this.context.chunkGenerator().getBaseColumn(
                        sampled.getX(), sampled.getZ(),
                        this.context.heightAccessor(),
                        this.context.randomState()
                ));

            Optional<BlockPos> potentialGround = findGroundPosition(
                hackyShittyBlockGetterThatsWhatItsCalled,
                sampled.getX(), sampled.getZ(), startPosition.getY()
            );
            flooredNodes.add(new PathNode(id, sampled,
                potentialGround.orElse(sampled)
            ));
        }
        if (!flooredNodes.isEmpty()) return flooredNodes;

        return List.of();
    }

    public record ResolvedPath(
        List<PathNode> nodes, List<PathSegment> segments,
        List<PathIntersection> intersections,
        int pathWidth, int scanStartY, int minBuildHeight
    ) {
        public ResolvedPath(
            List<PathNode> nodes, List<PathSegment> segments,
            int pathWidth, int scanStartY, int minBuildHeight
        ) {
            this(nodes, segments, calculateIntersections(segments),
                pathWidth, scanStartY, minBuildHeight);
        }

        static List<PathIntersection> calculateIntersections(List<PathSegment> segments) {
            Map<Long, Map<Integer, BlockPos>> visits = new HashMap<>();
            for (PathSegment segment : segments) {
                for (BlockPos point : segment.centerLine()) {
                    visits.computeIfAbsent(ChunkPos.asLong(point.getX(), point.getZ()), ignored -> new HashMap<>())
                        .putIfAbsent(segment.id, point);
                }
            }

            List<PathIntersection> result = new ArrayList<>();
            for (Map<Integer, BlockPos> positions : visits.values()) {
                if (positions.size() < 2) continue;

                Set<Integer> ids = new LinkedHashSet<>(positions.keySet());
                boolean hasNonAdjacentPair = ids.stream().anyMatch(first -> ids.stream().anyMatch(second -> Math.abs(first - second) > 1));
                if (!hasNonAdjacentPair) continue;

                BlockPos first = positions.values().iterator().next();
                boolean intersect = positions.values().stream().allMatch(point -> Math.abs(point.getY() - first.getY()) <= 1);
                if (intersect) result.add(new PathIntersection(first, List.copyOf(ids)));
            }
            return result;
        }

        public void rasterize(StructurePiecesBuilder pieces) {
            for (PathSegment segment : this.segments) pieces.addPiece(
                new GreatPathPiece(segment, this.pathWidth, this.scanStartY, this.minBuildHeight));
        }
    }

    public ResolvedPath resolvePath(List<PathNode> nodeList) {
        List<PathSegment> segments = new ArrayList<>(nodeList.size() - 1);
        for (int index = 0; index < nodeList.size() - 1; index++) {
            PathNode from = nodeList.get(index);
            PathNode to = nodeList.get(index + 1);
            int heightDifference = Math.abs(from.groundPos.getY() - to.groundPos.getY());
            PathNode lowerNode = from.groundPos.getY() <= to.groundPos.getY() ? from : to;
            boolean isLower = lowerNode.groundPos.getY() < lowerNode.sampledPos.getY();
            SegmentState state = heightDifference > this.settings.bridgingDifference()
                && isLower ? SegmentState.BRIDGE : SegmentState.NORMAL;
            segments.add(new PathSegment(index, from, to, state));
        }

        return new ResolvedPath(
            nodeList, segments, settings.pathWidth(),
            nodeList.getFirst().sampledPos.getY(),
            context.heightAccessor().getMinBuildHeight()
        );
    }

    record HackyShittyBlockGetter(
        Structure.GenerationContext context,
        NoiseColumn noiseColumn
    ) implements BlockGetter {

        @Override public @Nullable BlockEntity getBlockEntity(BlockPos blockPos) { return null; }
        @Override public BlockState getBlockState(BlockPos blockPos) { return noiseColumn.getBlock(blockPos.getY()); }
        @Override public FluidState getFluidState(BlockPos blockPos) { return noiseColumn.getBlock(blockPos.getY()).getFluidState(); }

        @Override public int getHeight() { return context.heightAccessor().getHeight(); }
        @Override public int getMaxBuildHeight() { return context.heightAccessor().getMaxBuildHeight(); }
        @Override public int getMinBuildHeight() { return context.heightAccessor().getMinBuildHeight(); }
    }

    static Optional<BlockPos> findGroundPosition(BlockGetter level, int x, int z, int startY) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - REQUIRED_SPACE - 1;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, Mth.clamp(startY, minY + 1, maxY), z);

        while (mutableBlockPos.getY() < maxY && !level.getBlockState(mutableBlockPos).isAir())
            mutableBlockPos.move(Direction.UP);

        mutableBlockPos.move(Direction.DOWN);
        while (mutableBlockPos.getY() >= minY) {
            if (hasRequiredSpace(level, mutableBlockPos.getX(), mutableBlockPos.getY() + 1, mutableBlockPos.getZ())
                && level.getBlockState(mutableBlockPos).isFaceSturdy(EmptyBlockGetter.INSTANCE, mutableBlockPos, Direction.UP)
            ) return Optional.of(mutableBlockPos.immutable());
            mutableBlockPos.move(Direction.DOWN);
        }
        return Optional.empty();
    }

    static boolean hasRequiredSpace(BlockGetter level, int x, int surfaceY, int z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, surfaceY, z);
        for (int offset = 0; offset <= REQUIRED_SPACE; offset++) {
            mutableBlockPos.setY(surfaceY + offset);
            if (!level.getBlockState(mutableBlockPos).isAir()) return false;
        }
        return true;
    }

    public record PathNode(int id, BlockPos sampledPos, BlockPos groundPos) {
        public static final Codec<PathNode> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.INT.fieldOf("Id").forGetter(PathNode::id),
                BlockPos.CODEC.fieldOf("SampledPosition").forGetter(PathNode::sampledPos),
                BlockPos.CODEC.fieldOf("GroundPosition").forGetter(PathNode::groundPos)
            ).apply(instance, PathNode::new)
        );
    }
    public record PathIntersection(BlockPos position, List<Integer> segmentIds) {}
    public record PathSegment(
        int id, PathNode from, PathNode to, SegmentState state
    ) {
        public static final Codec<PathSegment> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.INT.fieldOf("Id").forGetter(PathSegment::id),
                PathNode.CODEC.fieldOf("To").forGetter(PathSegment::to),
                PathNode.CODEC.fieldOf("From").forGetter(PathSegment::from),
                SegmentState.CODEC.fieldOf("SegmentState").forGetter(PathSegment::state)
            ).apply(instance, PathSegment::new)
        );

        public List<BlockPos> centerLine() {
            List<BlockPos> horizontal = supercoverLine(this.from.groundPos, this.to.groundPos);
            if (horizontal.size() == 1) return List.of(this.from.groundPos);

            List<BlockPos> result = new ArrayList<>(horizontal.size());
            int last = horizontal.size() - 1;
            for (int index = 0; index <= last; index++) {
                BlockPos point = horizontal.get(index);
                double progress = (double) index / (double) last;
                int y = Mth.floor(Mth.lerp(progress, this.from.groundPos.getY(), this.to.groundPos.getY()) + 0.5D);
                result.add(new BlockPos(point.getX(), y, point.getZ()));
            }
            return List.copyOf(result);
        }

        public int horizontalLength() {
            return Math.max(
                Math.abs(this.to.groundPos.getX() - this.from.groundPos.getX()),
                Math.abs(this.to.groundPos.getZ() - this.from.groundPos.getZ())
            );
        }
    }

    List<BlockPos> getCenteredNodes(List<BlockPos> positions, int centerX, int centerZ) {
        BoundingBox bounds = BoundingBox.encapsulatingPositions(positions).orElseThrow();
        BlockPos boundsCenter = bounds.getCenter();
        return positions.stream()
            .map(position -> position.offset(
                centerX - boundsCenter.getX(), 0,
                centerZ - boundsCenter.getZ())
            ).toList();
    }

    static List<BlockPos> supercoverLine(BlockPos from, BlockPos to) {
        int x = from.getX();
        int z = from.getZ();
        int targetX = to.getX();
        int targetZ = to.getZ();
        int dx = Math.abs(targetX - x);
        int dz = Math.abs(targetZ - z);
        int signX = Integer.compare(targetX, x);
        int signZ = Integer.compare(targetZ, z);
        int ix = 0;
        int iz = 0;

        List<BlockPos> result = new ArrayList<>(Math.max(dx, dz) + 1);
        result.add(new BlockPos(x, 0, z));
        while (ix < dx || iz < dz) {
            long decision = (1L + 2L * ix) * dz - (1L + 2L * iz) * dx;
            if (decision < 0) {
                x += signX;
                ix++;
            } else if (decision > 0) {
                z += signZ;
                iz++;
            } else {
                x += signX;
                ix++;
                result.add(new BlockPos(x, 0, z));
                z += signZ;
                iz++;
            }
            result.add(new BlockPos(x, 0, z));
        }
        return result;
    }
}
