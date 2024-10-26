package com.jamiedev.mod.blocks;

import com.jamiedev.mod.blocks.entity.BygoneBrushableBlockEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BygoneBrushableBlock extends BlockWithEntity implements LandingBlock {
    public static final MapCodec<BygoneBrushableBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(Registries.BLOCK.getCodec().fieldOf("turns_into").forGetter(BygoneBrushableBlock::getBaseBlock),
                Registries.SOUND_EVENT.getCodec().fieldOf("brush_sound").forGetter(BygoneBrushableBlock::getBrushingSound),
                Registries.SOUND_EVENT.getCodec().fieldOf("brush_completed_sound").forGetter(BygoneBrushableBlock::getBrushingCompleteSound),
                createSettingsCodec()).apply(instance, BygoneBrushableBlock::new);
    });
    private static final IntProperty DUSTED;
    public static final int field_42773 = 2;
    private final Block baseBlock;
    private final SoundEvent brushingSound;
    private final SoundEvent brushingCompleteSound;

    public MapCodec<BygoneBrushableBlock> getCodec() {
        return CODEC;
    }

    public BygoneBrushableBlock(Block baseBlock, SoundEvent brushingSound, SoundEvent brushingCompleteSound, AbstractBlock.Settings settings) {
        super(settings);
        this.baseBlock = baseBlock;
        this.brushingSound = brushingSound;
        this.brushingCompleteSound = brushingCompleteSound;
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(DUSTED, 0));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{DUSTED});
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, this, 2);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        world.scheduleBlockTick(pos, this, 2);
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity var6 = world.getBlockEntity(pos);
        if (var6 instanceof BrushableBlockEntity brushableBlockEntity) {
            brushableBlockEntity.scheduledTick();
        }

        if (FallingBlock.canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= world.getBottomY()) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, pos, state);
            fallingBlockEntity.setDestroyedOnLanding();
        }
    }

    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        Vec3d vec3d = fallingBlockEntity.getBoundingBox().getCenter();
        world.syncWorldEvent(2001, BlockPos.ofFloored(vec3d), Block.getRawIdFromState(fallingBlockEntity.getBlockState()));
        world.emitGameEvent(fallingBlockEntity, GameEvent.BLOCK_DESTROY, vec3d);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.down();
            if (FallingBlock.canFallThrough(world.getBlockState(blockPos))) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() - 0.05;
                double f = (double)pos.getZ() + random.nextDouble();
                world.addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, state), d, e, f, 0.0, 0.0, 0.0);
            }
        }

    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BygoneBrushableBlockEntity(pos, state);
    }

    public Block getBaseBlock() {
        return this.baseBlock;
    }

    public SoundEvent getBrushingSound() {
        return this.brushingSound;
    }

    public SoundEvent getBrushingCompleteSound() {
        return this.brushingCompleteSound;
    }

    static {
        DUSTED = Properties.DUSTED;
    }
}
