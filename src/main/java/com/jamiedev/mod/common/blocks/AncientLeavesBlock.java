package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.fabric.init.JamiesModParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AncientLeavesBlock extends LeavesBlock
{
    public static final MapCodec<AncientLeavesBlock> CODEC = createCodec(AncientLeavesBlock::new);

    public MapCodec<AncientLeavesBlock> getCodec() {
        return CODEC;
    }

    public AncientLeavesBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextInt(10) == 0) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (!isFaceFullSquare(blockState.getCollisionShape(world, blockPos), Direction.UP)) {
                ParticleUtil.spawnParticle(world, pos, random, (ParticleEffect) JamiesModParticleTypes.ANCIENT_LEAVES);
            }
        }
    }
}
