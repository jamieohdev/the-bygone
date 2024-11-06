package com.jamiedev.mod.blocks;

import com.jamiedev.mod.init.JamiesModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class AncientCaveVinesBodyBlock  extends AbstractPlantBlock implements Fertilizable, AncientCaveVines {
    public static final MapCodec<AncientCaveVinesBodyBlock> CODEC = createCodec(AncientCaveVinesBodyBlock::new);

    public MapCodec<AncientCaveVinesBodyBlock> getCodec() {
        return CODEC;
    }

    public AncientCaveVinesBodyBlock(AbstractBlock.Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(BERRIES, false));
    }

    protected AbstractPlantStemBlock getStem() {
        return (AbstractPlantStemBlock) JamiesModBlocks.CAVE_VINES;
    }

    protected BlockState copyState(BlockState from, BlockState to) {
        return (BlockState)to.with(BERRIES, (Boolean)from.get(BERRIES));
    }

    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(JamiesModBlocks.CAVE_VINES_PLANT);
    }

    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return AncientCaveVines.pickBerries(player, state, world, pos);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BERRIES});
    }

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return !(Boolean)state.get(BERRIES);
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, (BlockState)state.with(BERRIES, true), 2);
    }

}
