package com.jamiedev.bygone.common.block.entity;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ArcaneMechanismEntity extends BlockEntity
{
    /*
     * basically just a test-run for the arcane mechanism using some stuff from the conduitblockentity.
     */
    public int tickCount;
    boolean isActive;
    private long nextAmbientSoundActivation;
    private final List<BlockPos> effectBlocks = Lists.newArrayList();
    private LivingEntity destroyTarget;

    public ArcaneMechanismEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ArcaneMechanismEntity blockEntity) {
        ++blockEntity.tickCount;
        long i = level.getGameTime();
        List<BlockPos> list = blockEntity.effectBlocks;
        if (i % 40L == 0L) {

        }

        animationTick(level, pos, list, blockEntity.destroyTarget, blockEntity.tickCount);


    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcaneMechanismEntity blockEntity) {
        ++blockEntity.tickCount;
        long i = level.getGameTime();
        List<BlockPos> list = blockEntity.effectBlocks;
        if (i % 40L == 0L) {
            /**boolean flag = updateShape(level, pos, list);
            if (flag != blockEntity.isActive) {
                SoundEvent soundevent = flag ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE;
                level.playSound((Player)null, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            blockEntity.isActive = flag;
            updateHunting(blockEntity, list);
            if (flag) {
                applyEffects(level, pos, list);
                updateDestroyTarget(level, pos, state, list, blockEntity);
            }**/
        }

        if (blockEntity.isActive()) {
            if (i % 80L == 0L) {
                level.playSound((Player)null, pos, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            if (i > blockEntity.nextAmbientSoundActivation) {
                blockEntity.nextAmbientSoundActivation = i + 60L + (long)level.getRandom().nextInt(40);
                level.playSound((Player)null, pos, SoundEvents.CONDUIT_AMBIENT_SHORT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

    }


    public boolean isActive() {
        return this.isActive;
    }

    private static void animationTick(Level level, BlockPos pos, List<BlockPos> positions, @Nullable Entity entity, int tickCount) {
        RandomSource randomsource = level.random;
        double d0 = (double)(Mth.sin((float)(tickCount + 35) * 0.1F) / 2.0F + 0.5F);
        d0 = (d0 * d0 + d0) * (double)0.3F;
        Vec3 vec3 = new Vec3((double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)1.5F + d0, (double)pos.getZ() + (double)0.5F);

        for(BlockPos blockpos : positions) {
            if (randomsource.nextInt(50) == 0) {
                BlockPos blockpos1 = blockpos.subtract(pos);
                float f = -0.5F + randomsource.nextFloat() + (float)blockpos1.getX();
                float f1 = -2.0F + randomsource.nextFloat() + (float)blockpos1.getY();
                float f2 = -0.5F + randomsource.nextFloat() + (float)blockpos1.getZ();
                level.addParticle(ParticleTypes.ASH, vec3.x, vec3.y, vec3.z, (double)f, (double)f1, (double)f2);
            }
        }

        if (entity != null) {
            Vec3 vec31 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            float f3 = (-0.5F + randomsource.nextFloat()) * (3.0F + entity.getBbWidth());
            float f4 = -1.0F + randomsource.nextFloat() * entity.getBbHeight();
            float f5 = (-0.5F + randomsource.nextFloat()) * (3.0F + entity.getBbWidth());
            Vec3 vec32 = new Vec3((double)f3, (double)f4, (double)f5);
            level.addParticle(ParticleTypes.ASH, vec31.x, vec31.y, vec31.z, vec32.x, vec32.y, vec32.z);
        }

    }
}
