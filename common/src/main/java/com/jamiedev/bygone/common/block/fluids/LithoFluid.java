package com.jamiedev.bygone.common.block.fluids;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGFluids;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class LithoFluid extends FlowingFluid {

    public LithoFluid() {}

    @Nullable
    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_HONEY;
    }

    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        BlockPos blockpos = pos.above();
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
            if (random.nextInt(100) == 0) {
                double d0 = (double)pos.getX() + random.nextDouble();
                double d1 = (double)pos.getY() + (double)1.0F;
                double d2 = (double)pos.getZ() + random.nextDouble();
                level.addParticle(DustParticleOptions2.REDSTONE, (double) pos.getX() + d0, (double) pos.getY() + d1, (double) pos.getZ() + d2, 0.0F, 0.0F, 0.0F);
                level.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                level.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }

    }

    @Override
    public Fluid getFlowing() {
        return BGFluids.LITHO_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return BGFluids.LITHO_STILL.get();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockentity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockentity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {
        return 1;
    }

    @Override
    public Item getBucket() {
        return BGItems.LITHO_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(JamiesModTag.LITHO);
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 5;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 0;
    }

    public BlockState createLegacyBlock(FluidState state) {
        return (BlockState) BGBlocks.LITHO.get().defaultBlockState()
                .setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public boolean isSame(Fluid fluid) {
        return fluid == BGFluids.LITHO_STILL.get() || fluid == BGFluids.LITHO_FLOWING.get();
    }

    protected boolean isRandomlyTicking() {
        return true;
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_AXOLOTL);
    }

    public static class Flowing extends LithoFluid {
        public Flowing() {
        }
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends LithoFluid {
        public Source() {
        }
        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
class DustParticleOptions2 extends ScalableParticleOptionsBase {
    public static final Vector3f PLASM_PARTICLE_COLOR = Vec3.fromRGB24(14151396).toVector3f();
    public static final net.minecraft.core.particles.DustParticleOptions REDSTONE;
    public static final MapCodec<DustParticleOptions> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, DustParticleOptions> STREAM_CODEC;

    static {
        REDSTONE = new net.minecraft.core.particles.DustParticleOptions(PLASM_PARTICLE_COLOR, 1.0F);
        CODEC = RecordCodecBuilder.mapCodec((p_341566_) -> p_341566_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(DustParticleOptions::getColor), SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)).apply(p_341566_, net.minecraft.core.particles.DustParticleOptions::new));
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VECTOR3F, DustParticleOptions::getColor, ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, net.minecraft.core.particles.DustParticleOptions::new);
    }

    private final Vector3f color;

    public DustParticleOptions2(Vector3f color, float scale) {
        super(scale);
        this.color = color;
    }

    public ParticleType<DustParticleOptions> getType() {
        return ParticleTypes.DUST;
    }

    public Vector3f getColor() {
        return this.color;
    }
}