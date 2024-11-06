package com.jamiedev.mod.blocks;

import com.jamiedev.mod.blocks.entity.BlemishSpreadManager;
import com.jamiedev.mod.blocks.entity.BlemishSpreadable;
import com.jamiedev.mod.init.JamiesModBlocks;
import com.jamiedev.mod.init.JamiesModParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Iterator;

public class BlemishBlock extends Block implements BlemishSpreadable
{    public static final MapCodec<BlemishBlock> CODEC = createCodec(BlemishBlock::new);

    public MapCodec<BlemishBlock> getCodec() {
        return CODEC;
    }

    CactusBlock ref;
    public BlemishBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        Vec3d vec3d = new Vec3d(0.25, 0.05000000074505806, 0.25);
        entity.slowMovement(state, vec3d);
        entity.damage(world.getDamageSources().wither(), 2.0F);
    }

    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!((double)fallDistance < 4.0) && entity instanceof LivingEntity livingEntity) {
            LivingEntity.FallSounds fallSounds = livingEntity.getFallSounds();
            SoundEvent soundEvent = (double)fallDistance < 7.0 ? fallSounds.small() : fallSounds.big();
            entity.playSound(soundEvent, 1.0F, 1.0F);
        }
    }

    public static boolean canWalkOnBlemish(Entity entity) {
        if (entity.getType().isIn(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        } else {
            return entity instanceof LivingEntity && !((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).isEmpty();
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {

            if (random.nextInt(100) == 0) {
                //world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            double d = (double)pos.getX() + 0.5 + (0.5 - random.nextDouble());
            double e = (double)pos.getY() + 1.0;
            double f = (double)pos.getZ() + 0.5 + (0.5 - random.nextDouble());
            double g = (double)random.nextFloat() * 0.04;
            world.addParticle((ParticleEffect)JamiesModParticleTypes.BLEMISH, d, e, f, 0.0, g, 0.0);

    }

    @Override
    public int spread(BlemishSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
        int i = cursor.getCharge();
        if (i != 0 && random.nextInt(spreadManager.getSpreadChance()) == 0) {
            BlockPos blockPos = cursor.getPos();
            boolean bl = blockPos.isWithinDistance(catalystPos, (double)spreadManager.getMaxDistance());
            if (!bl && shouldNotDecay(world, blockPos)) {
                int j = spreadManager.getExtraBlockChance();
                if (random.nextInt(j) < i) {
                    BlockPos blockPos2 = blockPos.up();
                    BlockState blockState = this.getExtraBlockState(world, blockPos2, random, spreadManager.isWorldGen());
                    world.setBlockState(blockPos2, blockState, 3);
                    world.playSound((PlayerEntity)null, blockPos, blockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return Math.max(0, i - j);
            } else {
                return random.nextInt(spreadManager.getDecayChance()) != 0 ? i : i - (bl ? 1 : getDecay(spreadManager, blockPos, catalystPos, i));
            }
        } else {
            return i;
        }
    }

    private static int getDecay(BlemishSpreadManager spreadManager, BlockPos cursorPos, BlockPos catalystPos, int charge) {
        int i = spreadManager.getMaxDistance();
        float f = MathHelper.square((float)Math.sqrt(cursorPos.getSquaredDistance(catalystPos)) - (float)i);
        int j = MathHelper.square(24 - i);
        float g = Math.min(1.0F, f / (float)j);
        return Math.max(1, (int)((float)charge * g * 0.5F));
    }

    private BlockState getExtraBlockState(WorldAccess world, BlockPos pos, Random random, boolean allowShrieker) {
        BlockState blockState;
        blockState = JamiesModBlocks.BLEMISH_VEIN.getDefaultState();
        return blockState.contains(Properties.WATERLOGGED) && !world.getFluidState(pos).isEmpty() ? (BlockState)blockState.with(Properties.WATERLOGGED, true) : blockState;
    }

    private static boolean shouldNotDecay(WorldAccess world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.up());
        if (blockState.isAir() || blockState.isOf(Blocks.WATER) && blockState.getFluidState().isOf(Fluids.WATER)) {
            int i = 0;
            Iterator var4 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 2, 4)).iterator();

            do {
                if (!var4.hasNext()) {
                    return true;
                }

                BlockPos blockPos = (BlockPos)var4.next();

            } while(true);

        } else {
            return false;
        }
    }

    public boolean shouldConvertToSpreadable() {
        return false;
    }
}
