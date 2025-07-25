package com.jamiedev.bygone.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class LithineOreBlock extends Block {
    public static final MapCodec<LithineOreBlock> CODEC = simpleCodec(LithineOreBlock::new);
    public static final BooleanProperty LIT;

    public MapCodec<LithineOreBlock> codec() {
        return CODEC;
    }

    public LithineOreBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(LIT, false));
    }

    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        interact(state, level, pos);
        super.attack(state, level, pos, player);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully()) {
            interact(state, level, pos);
        }

        super.stepOn(level, pos, state, entity);
    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            spawnParticles(level, pos);
        } else {
            interact(state, level, pos);
        }

        return stack.getItem() instanceof BlockItem && (new BlockPlaceContext(player, hand, stack, hitResult)).canPlace() ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION : ItemInteractionResult.SUCCESS;
    }

    private static void interact(BlockState state, Level level, BlockPos pos) {
        spawnParticles(level, pos);
        if (!(Boolean)state.getValue(LIT)) {
            level.setBlock(pos, (BlockState)state.setValue(LIT, true), 3);
        }

    }

    protected boolean isRandomlyTicking(BlockState state) {
        return (Boolean)state.getValue(LIT);
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if ((Boolean)state.getValue(LIT)) {
            level.setBlock(pos, (BlockState)state.setValue(LIT, false), 3);
        }

    }

    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        if (dropExperience) {
            this.tryDropExperience(level, pos, stack, UniformInt.of(1, 5));
        }

    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if ((Boolean)state.getValue(LIT)) {
            spawnParticles(level, pos);
        }

    }

    private static void spawnParticles(Level level, BlockPos pos) {
        double d0 = (double)0.5625F;
        RandomSource randomsource = level.random;

        for(Direction direction : Direction.values()) {
            BlockPos blockpos = pos.relative(direction);
            if (!level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? (double)0.5F + (double)0.5625F * (double)direction.getStepX() : (double)randomsource.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? (double)0.5F + (double)0.5625F * (double)direction.getStepY() : (double)randomsource.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? (double)0.5F + (double)0.5625F * (double)direction.getStepZ() : (double)randomsource.nextFloat();
                level.addParticle(DustParticleOptions1.REDSTONE, (double)pos.getX() + d1, (double)pos.getY() + d2, (double)pos.getZ() + d3, (double)0.0F, (double)0.0F, (double)0.0F);
            }
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LIT});
    }

    static {
        LIT = RedstoneTorchBlock.LIT;
    }
}

class DustParticleOptions1 extends ScalableParticleOptionsBase {
    public static final Vector3f PLASM_PARTICLE_COLOR = Vec3.fromRGB24(14151396).toVector3f();
    public static final net.minecraft.core.particles.DustParticleOptions REDSTONE;
    public static final MapCodec<net.minecraft.core.particles.DustParticleOptions> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, net.minecraft.core.particles.DustParticleOptions> STREAM_CODEC;
    private final Vector3f color;

    public DustParticleOptions1(Vector3f color, float scale) {
        super(scale);
        this.color = color;
    }

    public ParticleType<net.minecraft.core.particles.DustParticleOptions> getType() {
        return ParticleTypes.DUST;
    }

    public Vector3f getColor() {
        return this.color;
    }

    static {
        REDSTONE = new net.minecraft.core.particles.DustParticleOptions(PLASM_PARTICLE_COLOR, 1.0F);
        CODEC = RecordCodecBuilder.mapCodec((p_341566_) -> p_341566_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(DustParticleOptions::getColor), SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)).apply(p_341566_, net.minecraft.core.particles.DustParticleOptions::new));
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VECTOR3F, DustParticleOptions::getColor, ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, net.minecraft.core.particles.DustParticleOptions::new);
    }
}

