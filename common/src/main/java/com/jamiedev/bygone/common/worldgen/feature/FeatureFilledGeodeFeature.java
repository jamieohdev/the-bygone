package com.jamiedev.bygone.common.worldgen.feature;

import com.google.common.collect.Lists;
import com.jamiedev.bygone.common.worldgen.feature.config.FeatureFilledGeodeBlockAndFeatureSettings;
import com.jamiedev.bygone.common.worldgen.feature.config.FeatureFilledGeodeConfig;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;

import java.util.List;
import java.util.function.Predicate;

public class FeatureFilledGeodeFeature extends Feature<FeatureFilledGeodeConfig> {
    private static final Direction[] DIRECTIONS = Direction.values();

    public FeatureFilledGeodeFeature(Codec<FeatureFilledGeodeConfig> p_159834_) {
        super(p_159834_);
    }

    public boolean place(FeaturePlaceContext<FeatureFilledGeodeConfig> featurePlaceContext) {
        FeatureFilledGeodeConfig config = featurePlaceContext.config();
        RandomSource random = featurePlaceContext.random();
        BlockPos origin = featurePlaceContext.origin();
        WorldGenLevel level = featurePlaceContext.level();
        int minGenOffset = config.minGenOffset();
        int maxGenOffset = config.maxGenOffset();
        List<Pair<BlockPos, Integer>> outerLayerAnchorPoints = Lists.newLinkedList();
        int numberOfDistributionPoints = config.distributionPoints().sample(random);
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(level.getSeed()));
        NormalNoise normalNoise = NormalNoise.create(worldgenRandom, -4, 1.0F);
        List<BlockPos> crackPoints = Lists.newLinkedList();
        double d0 = (double) numberOfDistributionPoints / config.outerWallDistance().getMaxValue();
        GeodeLayerSettings geodeLayerSettings = config.geodeLayerSettings();
        FeatureFilledGeodeBlockAndFeatureSettings geodeBlockSettings = config.geodeBlockSettings();
        GeodeCrackSettings geodeCrackSettings = config.geodeCrackSettings();
        double anchorPointDistanceFillingThreshold = 1.0F / Math.sqrt(geodeLayerSettings.filling);
        double anchorPointDistanceInnerLayerThreshold = 1.0F / Math.sqrt(geodeLayerSettings.innerLayer + d0);
        double d3 = 1.0F / Math.sqrt(geodeLayerSettings.middleLayer + d0);
        double anchorPointDistanceOuterLayerThreshold = 1.0F / Math.sqrt(geodeLayerSettings.outerLayer + d0);
        double crackPointDistanceThreshold = 1.0F / Math.sqrt(geodeCrackSettings.baseCrackSize + random.nextDouble() / 2.0F + (numberOfDistributionPoints > 3 ? d0 : 0.0F));
        boolean shouldGenerateCrack = random.nextFloat() < geodeCrackSettings.generateCrackChance;
        int numberOfInvalidBlocks = 0;

        for (int i = 0; i < numberOfDistributionPoints; ++i) {
            int outerWallDistanceX = config.outerWallDistance().sample(random);
            int outerWallDistanceY = config.outerWallDistance().sample(random);
            int outerWallDistanceZ = config.outerWallDistance().sample(random);
            BlockPos outerWallTestPos = origin.offset(outerWallDistanceX, outerWallDistanceY, outerWallDistanceZ);
            BlockState outerWallTestState = level.getBlockState(outerWallTestPos);
            if (outerWallTestState.isAir() || outerWallTestState.is(BlockTags.GEODE_INVALID_BLOCKS)) {
                ++numberOfInvalidBlocks;
                if (numberOfInvalidBlocks > config.invalidBlocksThreshold()) {
                    return false;
                }
            }

            outerLayerAnchorPoints.add(Pair.of(outerWallTestPos, config.pointOffset().sample(random)));
        }

        if (shouldGenerateCrack) {
            // I have no idea what this does.
            int i2 = random.nextInt(4);
            int j2 = numberOfDistributionPoints * 2 + 1;
            if (i2 == 0) {
                crackPoints.add(origin.offset(j2, 7, 0));
                crackPoints.add(origin.offset(j2, 5, 0));
                crackPoints.add(origin.offset(j2, 1, 0));
            } else if (i2 == 1) {
                crackPoints.add(origin.offset(0, 7, j2));
                crackPoints.add(origin.offset(0, 5, j2));
                crackPoints.add(origin.offset(0, 1, j2));
            } else if (i2 == 2) {
                crackPoints.add(origin.offset(j2, 7, j2));
                crackPoints.add(origin.offset(j2, 5, j2));
                crackPoints.add(origin.offset(j2, 1, j2));
            } else {
                crackPoints.add(origin.offset(0, 7, 0));
                crackPoints.add(origin.offset(0, 5, 0));
                crackPoints.add(origin.offset(0, 1, 0));
            }
        }

        List<BlockPos> innerDecoratorPlacementPositions = Lists.newArrayList();
        Predicate<BlockState> predicate = isReplaceable(config.geodeBlockSettings().cannotReplace());

        for (BlockPos blockInGeode : BlockPos.betweenClosed(
                origin.offset(minGenOffset, minGenOffset, minGenOffset),
                origin.offset(maxGenOffset, maxGenOffset, maxGenOffset)
        )) {
            double crackNoiseAmount = normalNoise.getValue(
                    blockInGeode.getX(),
                    blockInGeode.getY(),
                    blockInGeode.getZ()
            ) * config.noiseMultiplier();
            double distanceToAnchorPoints = 0.0F;
            double distanceToCrackPoints = 0.0F;

            for (Pair<BlockPos, Integer> outerLayerAnchorPair : outerLayerAnchorPoints) {
                distanceToAnchorPoints += Mth.invSqrt(blockInGeode.distSqr(outerLayerAnchorPair.getFirst()) + outerLayerAnchorPair.getSecond()) + crackNoiseAmount;
            }

            for (BlockPos crackPoint : crackPoints) {
                distanceToCrackPoints += Mth.invSqrt(blockInGeode.distSqr(crackPoint) + (double) geodeCrackSettings.crackPointOffset) + crackNoiseAmount;
            }

            if (!(distanceToAnchorPoints < anchorPointDistanceOuterLayerThreshold)) {
                if (shouldGenerateCrack && distanceToCrackPoints >= crackPointDistanceThreshold && distanceToAnchorPoints < anchorPointDistanceFillingThreshold) {
                    this.safeSetBlock(level, blockInGeode, Blocks.AIR.defaultBlockState(), predicate);

                    for (Direction direction : DIRECTIONS) {
                        BlockPos offsetPositionForTicking = blockInGeode.relative(direction);
                        FluidState fluidState = level.getFluidState(offsetPositionForTicking);
                        if (!fluidState.isEmpty()) {
                            level.scheduleTick(offsetPositionForTicking, fluidState.getType(), 0);
                        }
                    }
                } else if (distanceToAnchorPoints >= anchorPointDistanceFillingThreshold) {
                    this.safeSetBlock(
                            level,
                            blockInGeode,
                            geodeBlockSettings.fillingProvider().getState(random, blockInGeode),
                            predicate
                    );
                } else if (distanceToAnchorPoints >= anchorPointDistanceInnerLayerThreshold) {
                    boolean useAlternateInnerLayer = random.nextFloat() < config.useAlternateLayer0Chance();
                    if (useAlternateInnerLayer) {
                        this.safeSetBlock(
                                level,
                                blockInGeode,
                                geodeBlockSettings.alternateInnerLayerProvider().getState(random, blockInGeode),
                                predicate
                        );
                    } else {
                        this.safeSetBlock(
                                level,
                                blockInGeode,
                                geodeBlockSettings.innerLayerProvider().getState(random, blockInGeode),
                                predicate
                        );
                    }

                    if ((!config.placementsRequireLayer0Alternate() || useAlternateInnerLayer) && random.nextFloat() < config.usePotentialPlacementsChance()) {
                        innerDecoratorPlacementPositions.add(blockInGeode.immutable());
                    }
                } else if (distanceToAnchorPoints >= d3) {
                    this.safeSetBlock(
                            level,
                            blockInGeode,
                            geodeBlockSettings.middleLayerProvider().getState(random, blockInGeode),
                            predicate
                    );
                } else if (distanceToAnchorPoints >= anchorPointDistanceOuterLayerThreshold) {
                    this.safeSetBlock(
                            level,
                            blockInGeode,
                            geodeBlockSettings.outerLayerProvider().getState(random, blockInGeode),
                            predicate
                    );
                }
            }
        }

        WeightedRandomList<WeightedEntry.Wrapper<Holder<PlacedFeature>>> innerAbovePlacements = geodeBlockSettings.innerAboveFeatures();
        WeightedRandomList<WeightedEntry.Wrapper<Holder<PlacedFeature>>> innerPlacements = geodeBlockSettings.innerFeatures();
        WeightedRandomList<WeightedEntry.Wrapper<Holder<PlacedFeature>>> innerBelowPlacements = geodeBlockSettings.innerBelowFeatures();

        for (BlockPos innerDecoratorPlacementPosition : innerDecoratorPlacementPositions) {
            if (!innerAbovePlacements.isEmpty()) {
                Holder<PlacedFeature> innerAbovePlacement = innerAbovePlacements.getRandom(random).orElseThrow().data();
                innerAbovePlacement.value().place(
                        level,
                        featurePlaceContext.chunkGenerator(),
                        featurePlaceContext.random(),
                        innerDecoratorPlacementPosition.above()
                );
            }

            if (!innerPlacements.isEmpty()) {
                Holder<PlacedFeature> innerPlacement = innerPlacements.getRandom(random).orElseThrow().data();
                innerPlacement.value().place(
                        level,
                        featurePlaceContext.chunkGenerator(),
                        featurePlaceContext.random(),
                        innerDecoratorPlacementPosition
                );
            }

            if (!innerBelowPlacements.isEmpty()) {
                Holder<PlacedFeature> innerBelowPlacement = innerBelowPlacements.getRandom(random).orElseThrow().data();
                innerBelowPlacement.value().place(
                        level,
                        featurePlaceContext.chunkGenerator(),
                        featurePlaceContext.random(),
                        innerDecoratorPlacementPosition.below()
                );
            }
        }

        return true;

    }

}


