package com.jamiedev.bygone.common.worldgen.structure.trail_ruins.path.segments;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class BridgeSegment extends SegmentState.PlacementMode {

    private static final int SECTION_PEEK = 4;
    private static final int LANTERN_SPACING = 7;
    private static final int SUPPORT_SPACING = 12;

    private final Map<BlockPos, DeckColumn> deck = new HashMap<>();

    @Override
    public BoundingBox createBoundingBox(List<BlockPos> samples, int width, int bridgingDifference) {
        BoundingBox centerLineBounds = this.fromSamples(samples);
        return this.boundingBox(
            centerLineBounds,
            this.radius(width),
            centerLineBounds.minY(),
            (centerLineBounds.maxY() + 4)
        );
    }

    @Override protected void innerPlace(
        WorldGenLevel level, BlockPos center,
        int sampleIndex, int x, int z,
        int distanceSquared, int radiusSquared, int bridgingDifference
    ) {
        BlockPos columnKey = new BlockPos(x, 0, z);
        DeckColumn current = this.deck.get(columnKey);
        if (current == null || distanceSquared < current.distanceSquared)
            this.deck.put(columnKey, new DeckColumn(
                new BlockPos(x, center.getY(), z),
                distanceSquared,
                BGBlocks.ANCIENT_SLAB.get().defaultBlockState()
                    .setValue(SlabBlock.TYPE, SlabType.TOP),
                sampleIndex
            ));
    }

    @Override public void place(
        WorldGenLevel level, BoundingBox chunkBounds,
        List<BlockPos> samples, int width, int bridgingDifference
    ) {
        BridgeConstructor bridgeConstructor = this.createConstructor(
            level, samples, width, bridgingDifference
        );

        for (DeckColumn column : bridgeConstructor.deck.values())
            this.placeIfAir(level, chunkBounds, column.position, column.state);

        this.placeSupports(level, chunkBounds, bridgeConstructor.supportSections);
        this.placeRailings(level, chunkBounds, bridgeConstructor.railingPositions);
        this.placeLanterns(level, chunkBounds, bridgeConstructor.lanternSections);
    }

    private BridgeConstructor createConstructor(
        WorldGenLevel level, List<BlockPos> samples,
        int width, int bridgingDifference
    ) {
        BoundingBox planBounds = this.createBoundingBox(
            samples, width, bridgingDifference
        );
        super.place(level, planBounds, samples, width, bridgingDifference);
        this.deck.replaceAll(this::interpolateSlab);

        Set<BlockPos> railingPositions = getRailingPositions(samples);

        List<BridgeSection> supportSections = new ArrayList<>();
        int supportMargin = SUPPORT_SPACING / 2;
        for (int i = supportMargin; i < samples.size() - supportMargin; i += SUPPORT_SPACING)
            supportSections.add(this.sectionAt(samples, width, i));

        List<BridgeSection> lanternSections = new ArrayList<>();
        int lanternMargin = LANTERN_SPACING / 2;
        for (int i = lanternMargin; i < samples.size() - lanternMargin; i += LANTERN_SPACING)
            lanternSections.add(this.sectionAt(samples, width, i));

        return new BridgeConstructor(
            this.deck, railingPositions,
            supportSections, lanternSections
        );
    }

    private Set<BlockPos> getRailingPositions(List<BlockPos> samples) {
        Set<BlockPos> railingPositions = new HashSet<>();
        int lastSampleIndex = samples.size() - 1;
        for (DeckColumn column : this.deck.values()) {
            if (column.sampleIndex == 0 || column.sampleIndex == lastSampleIndex)
                continue;

            BlockPos deckPosition = column.position;
            boolean surrounded = true;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (this.deck.containsKey(deckPosition.relative(direction).atY(0))) continue;
                surrounded = false;
                break;
            }
            if (!surrounded) railingPositions.add(deckPosition.above());
        }
        return railingPositions;
    }

    private DeckColumn interpolateSlab(BlockPos ignoredColumn, DeckColumn column) {
        boolean lowerNeighbor = false;
        boolean higherNeighbor = false;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            DeckColumn neighbor = this.deck.get(
                column.position.relative(direction).atY(0)
            );
            if (neighbor == null) continue;
            if (neighbor.position.getY() < column.position.getY()) lowerNeighbor = true;
            if (neighbor.position.getY() > column.position.getY()) higherNeighbor = true;
        }

        BlockState state = (lowerNeighbor && higherNeighbor)
            ? BGBlocks.ANCIENT_PLANKS.get().defaultBlockState() : BGBlocks.ANCIENT_SLAB.get().defaultBlockState()
            .setValue(SlabBlock.TYPE, lowerNeighbor ? SlabType.BOTTOM : SlabType.TOP);
        return new DeckColumn(
            column.position,
            column.distanceSquared,
            state, column.sampleIndex
        );
    }

    @Override public boolean canPlaceBuildings() { return false; }
    @Override public boolean rejectsSteepSlope() { return true; }

    private BridgeSection sectionAt(
        List<BlockPos> samples, int width, int index
    ) {
        BlockPos center = samples.get(index);
        BlockPos previous = samples.get(Mth.clamp(
            index - SECTION_PEEK, 0, samples.size() - 1
        ));
        BlockPos next = samples.get(Mth.clamp(
            index + SECTION_PEEK, 0, samples.size() - 1
        ));
        int differenceX = next.getX() - previous.getX();
        int differenceZ = next.getZ() - previous.getZ();
        if (differenceX == 0 && differenceZ == 0) differenceX = 1;

        double length = Mth.length(differenceX, differenceZ);
        int normalX = (int) Math.round(-differenceZ * width / length);
        int normalZ = (int) Math.round(differenceX * width / length);
        return new BridgeSection(
            this.edgeAt(center, normalX, normalZ, width),
            this.edgeAt(center, -normalX, -normalZ, width)
        );
    }

    private BlockPos edgeAt(
        BlockPos center, int normalX, int normalZ, int width
    ) {
        DeckColumn edge = this.deck.get(center.atY(0));
        int steps = Math.max(Math.abs(normalX), Math.abs(normalZ));
        if (steps == 0) return edge.position;

        int maximumDistance = width * 2 + 1;
        for (int distance = 1; distance <= maximumDistance; distance++) {
            int x = center.getX() + (int) Math.round((double) normalX * distance / steps);
            int z = center.getZ() + (int) Math.round((double) normalZ * distance / steps);
            DeckColumn column = this.deck.get(new BlockPos(x, 0, z));
            if (column == null) break;
            edge = column;
        }
        return edge.position;
    }

    private void placeRailings(
        WorldGenLevel level, BoundingBox chunkBounds,
        Set<BlockPos> positions
    ) {
        BlockState defaultRailing = BGBlocks.ANCIENT_FENCE.get().defaultBlockState();
        for (BlockPos position : positions)
            this.placeIfAir(level, chunkBounds, position, defaultRailing);

        for (BlockPos position : positions) {
            if (!chunkBounds.isInside(position)) continue;

            BlockState current = level.getBlockState(position);
            if (!current.is(BGBlocks.ANCIENT_FENCE.get())) continue;

            BlockState updated = Block.updateFromNeighbourShapes(current, level, position);
            if (updated != current) level.setBlock(position, updated, 3);
        }
    }

    private void placeLanterns(
        WorldGenLevel level, BoundingBox chunkBounds,
        List<BridgeSection> sections
    ) {
        for (BridgeSection section : sections) {
            for (BlockPos deckPosition : List.of(section.left, section.right)) {
                if (!chunkBounds.intersects(
                    deckPosition.getX(), deckPosition.getZ(),
                    deckPosition.getX(), deckPosition.getZ()
                )) continue;

                BlockPos railing = deckPosition.above();
                BlockPos post = deckPosition.above(2);
                BlockPos lantern = deckPosition.above(3);

                if (!level.getBlockState(railing).is(BGBlocks.ANCIENT_FENCE.get()) || !level.getBlockState(lantern).isAir()) continue;
                if (this.placeIfAir(level, chunkBounds, post, BGBlocks.ANCIENT_FENCE.get().defaultBlockState()))
                    this.placeIfAir(level, chunkBounds, lantern, Blocks.LANTERN.defaultBlockState());
            }
        }
    }

    private void placeSupports(
        WorldGenLevel level, BoundingBox chunkBounds,
        List<BridgeSection> sections
    ) {
        for (BridgeSection section : sections) {
            for (BlockPos deckPosition : List.of(section.left, section.right)) {
                if (!chunkBounds.intersects(
                    deckPosition.getX(), deckPosition.getZ(),
                    deckPosition.getX(), deckPosition.getZ()
                )) continue;

                BlockPos ceiling = null;
                BlockPos.MutableBlockPos target = deckPosition.mutable();
                for (int y = deckPosition.getY() + 2; y <= (level.getMaxBuildHeight() - 1); y++) {
                    target.setY(y);
                    BlockState state = level.getBlockState(target);
                    if (state.isAir()) continue;
                    if (state.getFluidState().isEmpty() && state.isFaceSturdy(level, target, Direction.DOWN))
                        ceiling = target.immutable();
                    break;
                }
                if (ceiling == null) continue;

                for (int y = deckPosition.getY() + 1; y < ceiling.getY(); y++) {
                    BlockState placementState = BGBlocks.POLISHED_BYSTONE_BRICK_WALL
                        .get().defaultBlockState();
                    if (y > deckPosition.getY() + 1 && y < ceiling.getY() - 1)
                        placementState = Blocks.CHAIN.defaultBlockState();

                    this.placeIfAir(level, chunkBounds, new BlockPos(
                        deckPosition.getX(), y, deckPosition.getZ()), placementState);
                }
            }
        }
    }

    private boolean placeIfAir(
        WorldGenLevel level, BoundingBox chunkBounds, BlockPos position, BlockState state
    ) {
        if (!chunkBounds.isInside(position) || !level.getBlockState(position).isAir())
            return false;
        level.setBlock(position, state, 3);
        return true;
    }

    private record BridgeSection(
        BlockPos left, BlockPos right
    ) {}

    private record BridgeConstructor(
        Map<BlockPos, DeckColumn> deck,
        Set<BlockPos> railingPositions,
        List<BridgeSection> supportSections,
        List<BridgeSection> lanternSections
    ) {}

    private record DeckColumn(
        BlockPos position, int distanceSquared,
        BlockState state, int sampleIndex
    ) {}
}
