package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.core.registry.BGBlockEntities;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MegalithTotemEntity extends BlockEntity implements Clearable {

    BedBlock ref;

    private final SimpleContainer inventory = new SimpleContainer(1);
    private int fuel = 0;
    private static final int FUEL_PER_ITEM = 2400;
    private static final int MAX_FUEL = 24000;
    private static final int RANGE = 15;
    private static final int PLANT_ATTEMPTS_PER_TICK = 4;

    private final Map<BlockPos, Integer> bonemealedplants = new HashMap<>();
    public static final int BOOST_COOLDOWN_TICKS = 20;

    public MegalithTotemEntity(BlockPos pos, BlockState state) {
        super(BGBlockEntities.MEGALITH_TOTEM.get(), pos, state);
    }
    public static void tick(Level level, BlockPos pos, BlockState state, MegalithTotemEntity be) {
        if (level.isClientSide) return;

        Iterator<Map.Entry<BlockPos, Integer>> storeplants = be.bonemealedplants.entrySet().iterator();

        while (storeplants.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = storeplants.next();

            int ticksRemaining = entry.getValue();

            if (ticksRemaining <= 1) {
                storeplants.remove();
            } else {
                entry.setValue(ticksRemaining - 1);
            }
        }

        if (be.fuel > 0) {
            be.fuel--;

            if (level instanceof ServerLevel serverLevel) {
                RandomSource rand = serverLevel.random;
                final int attemptsPerTick = Math.max(1, PLANT_ATTEMPTS_PER_TICK);

                final int cooldownTicks = Math.max(40, BOOST_COOLDOWN_TICKS);

                for (int i = 0; i < attemptsPerTick; i++) {
                    int dx = rand.nextInt(RANGE * 2 + 1) - RANGE;
                    int dz = rand.nextInt(RANGE * 2 + 1) - RANGE;
                    int x = pos.getX() + dx;
                    int z = pos.getZ() + dz;
                    for (int dy = -2; dy <= 2; dy++) {
                        int y = pos.getY() + dy;
                        BlockPos plantPos = new BlockPos(x, y, z);

                        if (be.bonemealedplants.containsKey(plantPos)) continue;

                        BlockState plantState = level.getBlockState(plantPos);
                        Block block = plantState.getBlock();

                        if ((block instanceof CropBlock || block instanceof SaplingBlock)) {
                            BonemealableBlock bonemealable = (BonemealableBlock) block;
                            if (bonemealable.isValidBonemealTarget(level, plantPos, plantState)) {
                                if (bonemealable.isBonemealSuccess(level, rand, plantPos, plantState)) {
                                    bonemealable.performBonemeal(serverLevel, rand, plantPos, plantState);
                                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, plantPos.getX() + 0.5, plantPos.getY() + 0.5, plantPos.getZ() + 0.5, 8, 0.2, 0.5, 0.2, 0.1);
                                    be.bonemealedplants.put(plantPos.immutable(), cooldownTicks);
                                }
                            }
                        }
                    }

                }
            }
        } else {

            ItemStack stack = be.inventory.getItem(0);
            if (!stack.isEmpty() && isLithoplasm(stack)) {
                stack.shrink(1);
                be.fuel = Math.min(be.fuel + FUEL_PER_ITEM, MAX_FUEL);
            }
        }
    }

    @Override
    public void clearContent() {
        inventory.clearContent();
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    private static boolean isLithoplasm(ItemStack stack) {
        return stack.is(BGItems.LITHOPLASM.get());
    }
}