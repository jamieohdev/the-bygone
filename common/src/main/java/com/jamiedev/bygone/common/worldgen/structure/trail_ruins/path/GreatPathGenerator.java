package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path;

import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.GreatTrailSettings;
import com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments.SegmentState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GreatPathGenerator {
    private static final int REQUIRED_SPACE = 3;

    private static final int MAX_NODE_REJECTIONS = 8;
    private static final int PATH_PLACEMENT_RETRIES = 3;

    private final Structure.GenerationContext context;
    private final GreatTrailSettings settings;

    // cache lookups
    private final Map<BlockPos, HackyShittyBlockGetter> groundColumns = new HashMap<>();
    private final Map<GroundQuery, GroundResult> groundResults = new HashMap<>();

    public record GroundResult(
        BlockPos pathPosition, SegmentState state,
        Optional<BlockPos> supportBelow
    ) {}

    public GreatPathGenerator(
        Structure.GenerationContext context,
        GreatTrailSettings settings
    ) {
        this.context = context;
        this.settings = settings;
    }

    GreatTrailSettings getSettings() { return this.settings; }

    public List<BlockPos> generateNodes(BlockPos startPosition) {
        BoundingBox allowedArea = this.allowedArea();
        RandomSource random = this.context.random();
        PathResolver resolver = new PathResolver(this);

        for (int attempts = 0; attempts < PATH_PLACEMENT_RETRIES; attempts++) {
            List<BlockPos> nodes = new ArrayList<>(this.settings.nodeCount());
            nodes.add(this.groundNode(startPosition));

            while (nodes.size() < this.settings.nodeCount()) {
                Optional<BlockPos> nextNode = this.findNextNode(nodes, random, allowedArea, resolver);
                if (nextNode.isEmpty()) break;
                nodes.add(nextNode.get());
            }
            if (nodes.size() >= this.settings.nodeCount()) return nodes;
        }
        return List.of();
    }

    private Optional<BlockPos> findNextNode(
        List<BlockPos> nodes, RandomSource random,
        BoundingBox allowedArea, PathResolver resolver
    ) {
        BlockPos previous = nodes.getLast();
        for (int rejected = 0; rejected <= MAX_NODE_REJECTIONS; rejected++) {
            double rotation = this.randomNodeRotation(nodes, random);
            int distance = Mth.nextInt(
                random, settings.nodeSpacingMin(),
                this.settings.nodeSpacingMax()
            );
            BlockPos potentialNode = this.groundNode(previous.offset(
                Mth.floor(Math.cos(rotation) * distance),
                0, Mth.floor(Math.sin(rotation) * distance)
            ));

            if (Math.abs(potentialNode.getY() - previous.getY()) <= this.settings.bridgingDifference()
                && allowedArea.isInside(potentialNode) && !resolver.exceedsIntersections(nodes, potentialNode)
                && !resolver.slopeTooSteep(previous, potentialNode)) return Optional.of(potentialNode);
        }
        return Optional.empty();
    }

    private BlockPos groundNode(BlockPos position) {
        return this.findExtendedGround(position).pathPosition();
    }

    public GroundResult findExtendedGround(BlockPos position) {
        return this.findGround(
            position, OptionalInt.empty(),
            position.getY(), position.getY()
        );
    }

    public BoundingBox allowedArea() {
        ChunkPos chunk = this.context.chunkPos();
        LevelHeightAccessor heights = this.context.heightAccessor();
        int radius = this.settings.chunkRadius();
        return BoundingBox.fromCorners(
            chunk.getBlockAt(-radius * 16, heights.getMinBuildHeight(), -radius * 16),
            chunk.getBlockAt((radius + 1) * 16 - 1, heights.getMaxBuildHeight() - 1, (radius + 1) * 16 - 1)
        );
    }

    private double randomNodeRotation(List<BlockPos> nodes, RandomSource random) {
        if (nodes.size() < 2) return random.nextDouble() * Math.PI * 2.0D;

        BlockPos previous = nodes.get(nodes.size() - 2);
        BlockPos current = nodes.getLast();
        double previousRotation = Math.atan2(
            current.getZ() - previous.getZ(),
            current.getX() - previous.getX()
        );
        double maximumTurn = Math.toRadians(this.settings.maxTurnAngle());
        return previousRotation + ((random.nextDouble() * 2.0D) - 1.0D) * maximumTurn;
    }

    public GroundResult findGround(
        BlockPos position, int bridgingDifference,
        int differenceReferenceY, int segmentEndY
    ) {
        return findGround(
            position, OptionalInt.of(bridgingDifference),
            differenceReferenceY, segmentEndY
        );
    }

    public GroundResult findGround(
        BlockPos position, OptionalInt bridgingDifference,
        int differenceReferenceY, int segmentEndY
    ) {
        GroundQuery query = new GroundQuery(
            position.immutable(), bridgingDifference,
            differenceReferenceY, segmentEndY
        );
        return this.groundResults.computeIfAbsent(query,
            ignored -> groundResultRaycast(
            this.groundColumns.computeIfAbsent(
                position.atY(0), column ->
                    // patemted technology
                    new HackyShittyBlockGetter(this.context,
                        this.context.chunkGenerator().getBaseColumn(
                            position.getX(), position.getZ(),
                            this.context.heightAccessor(),
                            this.context.randomState()
                        )
                    )
            ),
            position, bridgingDifference,
            differenceReferenceY, segmentEndY
        ));
    }

    // to wrap generation contexts and access blocks using a BlockGetter without doing anything stupid
    record HackyShittyBlockGetter(
        Structure.GenerationContext context,
        NoiseColumn noiseColumn
    ) implements BlockGetter {

        @Override public @Nullable BlockEntity getBlockEntity(@NotNull BlockPos blockPos) { return null; }
        @Override public @NotNull BlockState getBlockState(BlockPos blockPos) { return noiseColumn.getBlock(blockPos.getY()); }
        @Override public @NotNull FluidState getFluidState(BlockPos blockPos) { return noiseColumn.getBlock(blockPos.getY()).getFluidState(); }

        @Override public int getHeight() { return context.heightAccessor().getHeight(); }
        @Override public int getMaxBuildHeight() { return context.heightAccessor().getMaxBuildHeight(); }
        @Override public int getMinBuildHeight() { return context.heightAccessor().getMinBuildHeight(); }
    }

    private record GroundQuery(
        BlockPos position, OptionalInt bridgingDifference,
        int differenceReferenceY, int segmentEndY
    ) {}

    public static Optional<BlockPos> findGroundPosition(
        BlockGetter level, int x, int z, int startY, int bridgingDifference
    ) {
        BlockPos idealPosition = new BlockPos(x, startY, z);
        GroundResult result = groundResultRaycast(
            level, idealPosition, OptionalInt.of(bridgingDifference), startY, startY
        );
        return result.state().inlineTerrain()
            ? Optional.of(result.pathPosition()) : Optional.empty();
    }

    static GroundResult groundResultRaycast(
        BlockGetter level, BlockPos idealPosition,
        OptionalInt bridgingDifference,
        int referenceY, int segmentEndY
    ) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - REQUIRED_SPACE - 1;
        int startY = Mth.clamp(idealPosition.getY(), minY, maxY);
        BlockPos.MutableBlockPos mutableBlockPos = idealPosition.mutable();
        mutableBlockPos.setY(startY);

        boolean searchingDown = !isValidGround(level.getBlockState(mutableBlockPos), mutableBlockPos);
        Optional<BlockPos> supportBelow = searchingDown
            ? Optional.empty() : Optional.of(mutableBlockPos.immutable());
        int direction = searchingDown ? -1 : 1;
        int endY = searchingDown ? minY : maxY;
        int searchDistance = Math.abs(endY - startY);

        for (int offset = 0; offset <= searchDistance; offset++) {
            int scanY = startY + offset * direction;
            if (bridgingDifference.isPresent() && Math.abs(scanY - referenceY) > bridgingDifference.getAsInt()) {
                SegmentState state = Math.signum(scanY - segmentEndY) > 0
                    ? SegmentState.TUNNEL : SegmentState.BRIDGE;
                return new GroundResult(idealPosition, state, supportBelow);
            }

            mutableBlockPos.setY(scanY);
            BlockState state = level.getBlockState(mutableBlockPos);
            if (!isValidGround(state, mutableBlockPos)) continue;

            if (searchingDown) supportBelow = Optional.of(mutableBlockPos.immutable());
            boolean hasSpace = hasRequiredSpace(
                level, mutableBlockPos.getX(),
                mutableBlockPos.getY() + 1,
                mutableBlockPos.getZ()
            );
            if (hasSpace) return new GroundResult(
                mutableBlockPos.immutable(),
                SegmentState.NORMAL, supportBelow
            );
            if (searchingDown) return new GroundResult(
                idealPosition, SegmentState.TUNNEL,
                supportBelow
            );
        }

        return new GroundResult(
            idealPosition, searchingDown
            ? SegmentState.BRIDGE : SegmentState.TUNNEL,
            supportBelow
        );
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidGround(BlockState state, BlockPos position) {
        return state.isFaceSturdy(EmptyBlockGetter.INSTANCE, position, Direction.UP);
    }

    static boolean hasRequiredSpace(BlockGetter level, int x, int surfaceY, int z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, surfaceY, z);
        for (int offset = 0; offset < REQUIRED_SPACE; offset++) {
            mutableBlockPos.setY(surfaceY + offset);
            if (!level.getBlockState(mutableBlockPos).isAir()) return false;
        }
        return true;
    }

    public record PathSample(BlockPos position, SegmentState state) {}
    public record PathSegment(BlockPos from, BlockPos to, SegmentState state) {}
}
