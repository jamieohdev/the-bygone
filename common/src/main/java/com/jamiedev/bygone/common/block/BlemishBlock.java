package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.BlemishSpreadManager;
import com.jamiedev.bygone.common.block.entity.BlemishSpreadable;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BlemishBlock extends Block implements BlemishSpreadable {
    public static final MapCodec<BlemishBlock> CODEC = simpleCodec(BlemishBlock::new);
    CactusBlock ref;

    public BlemishBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    public static boolean canWalkOnBlemish(Entity entity) {
        if (entity.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        } else {
            return entity instanceof LivingEntity && !((LivingEntity) entity).getItemBySlot(EquipmentSlot.FEET).isEmpty();
        }
    }

    private static int getDecay(BlemishSpreadManager spreadManager, BlockPos cursorPos, BlockPos catalystPos, int charge) {
        int i = spreadManager.getMaxDistance();
        float f = Mth.square((float) Math.sqrt(cursorPos.distSqr(catalystPos)) - (float) i);
        int j = Mth.square(24 - i);
        float g = Math.min(1.0F, f / (float) j);
        return Math.max(1, (int) ((float) charge * g * 0.5F));
    }

    private static boolean shouldNotDecay(LevelAccessor world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.above());
        if (blockState.isAir() || blockState.is(Blocks.WATER) && blockState.getFluidState().is(Fluids.WATER)) {
            int i = 0;
            Iterator<BlockPos> var4 = BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 2, 4)).iterator();

            do {
                if (!var4.hasNext()) {
                    return true;
                }

                BlockPos blockPos = var4.next();

            } while (true);

        } else {
            return false;
        }
    }

    @Override
    public MapCodec<BlemishBlock> codec() {
        return CODEC;
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        Vec3 vec3d = new Vec3(0.25, 0.05000000074505806, 0.25);
        entity.makeStuckInBlock(state, vec3d);
        entity.hurt(world.damageSources().wither(), 2.0F);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!((double) fallDistance < 4.0) && entity instanceof LivingEntity livingEntity) {
            LivingEntity.Fallsounds fallSounds = livingEntity.getFallSounds();
            SoundEvent soundEvent = (double) fallDistance < 7.0 ? fallSounds.small() : fallSounds.big();
            entity.playSound(soundEvent, 1.0F, 1.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, @NotNull RandomSource random) {

        if (random.nextInt(100) == 0) {
            //world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }

        double d = pos.getX() + 0.5 + (0.5 - random.nextDouble());
        double e = pos.getY() + 1.0;
        double f = pos.getZ() + 0.5 + (0.5 - random.nextDouble());
        double g = random.nextFloat() * 0.04;
        world.addParticle((ParticleOptions) BGParticleTypes.BLEMISH, d, e, f, 0.0, g, 0.0);

    }

    @Override
    public int spread(BlemishSpreadManager.Cursor cursor, LevelAccessor world, BlockPos catalystPos, @NotNull RandomSource random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
        int i = cursor.getCharge();
        if (i != 0 && random.nextInt(spreadManager.getSpreadChance()) == 0) {
            BlockPos blockPos = cursor.getPos();
            boolean bl = blockPos.closerThan(catalystPos, spreadManager.getMaxDistance());
            if (!bl && shouldNotDecay(world, blockPos)) {
                int j = spreadManager.getExtraBlockChance();
                if (random.nextInt(j) < i) {
                    BlockPos blockPos2 = blockPos.above();
                    BlockState blockState = this.getExtraBlockState(world, blockPos2, random, spreadManager.isWorldGen());
                    world.setBlock(blockPos2, blockState, 3);
                    world.playSound(null, blockPos, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return Math.max(0, i - j);
            } else {
                return random.nextInt(spreadManager.getDecayChance()) != 0 ? i : i - (bl ? 1 : getDecay(spreadManager, blockPos, catalystPos, i));
            }
        } else {
            return i;
        }
    }

    private BlockState getExtraBlockState(LevelAccessor world, BlockPos pos, @NotNull RandomSource random, boolean allowShrieker) {
        BlockState blockState;
        blockState = BGBlocks.BLEMISH_VEIN.get().defaultBlockState();
        return blockState.hasProperty(BlockStateProperties.WATERLOGGED) && !world.getFluidState(pos).isEmpty() ? blockState.setValue(BlockStateProperties.WATERLOGGED, true) : blockState;
    }

    @Override
    public boolean shouldConvertToSpreadable() {
        return false;
    }
}
