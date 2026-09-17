package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.BygonePortalEntity;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class PortablePortalItem extends Item {

    public PortablePortalItem(Properties properties) {
        super(properties);
    }

    public BlockPos checkSpace(Level level, BlockPos pos) {
        if (hasSpace(level, pos)) return pos;
        //if (hasSpace(level, pos.above())) return pos.above();
        //if (hasSpace(level, pos.below())) return pos.below();
        return null;
    }

    public boolean hasSpace(Level level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    if (!level.getBlockState(checkPos).canBeReplaced()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!canBeUsed(level) && player != null) {
            player.displayClientMessage(Component.literal("Dimension Inaccessible..."), true);
            return InteractionResult.FAIL;
        }

        BlockPos clickedPos = context.getClickedPos();
        BlockPos targetPos = clickedPos.relative(context.getClickedFace(), 2);

        Optional<BlockPos> position = Optional.ofNullable(checkSpace(level, targetPos));

        if (position.isPresent() && player != null) {
            BlockPos validPos = position.get();

            player.displayClientMessage(Component.literal("Valid!"), true);
            level.playSound(player, validPos, SoundEvents.CONDUIT_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.playSound(player, validPos, SoundEvents.VAULT_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -1; y <= 1; y++) {
                        BlockPos particlePos = validPos.offset(x, 0, z);
                        level.addParticle(ParticleTypes.PORTAL,
                            particlePos.getX() + 0.5,
                            particlePos.getY() + 0.5,
                            particlePos.getZ() + 0.5,
                            (Math.random() - 0.5) * 0.5,
                            Math.random() * 0.5,
                            (Math.random() - 0.5) * 0.5);
                    }
                }
            }

            BygonePortalEntity entity = new BygonePortalEntity(BGEntityTypes.BYGONE_PORTAL.get(), level);
            entity.moveTo(validPos.getX() + 0.5, validPos.getY()+ 0.5, validPos.getZ() + 0.5);
            level.addFreshEntity(entity);

            level.addParticle(ParticleTypes.EXPLOSION,
                    validPos.getX() + 0.5,
                    validPos.getY() + 0.5,
                    validPos.getZ() + 0.5,
                    0, 0.1, 0);

            context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

            return InteractionResult.SUCCESS;
        } else if (player != null) {
            player.displayClientMessage(Component.literal("No space!!"), true);
            level.playSound(player, clickedPos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F);
            level.playSound(player, clickedPos, SoundEvents.VAULT_DEACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);

            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.SMOKE,
                        targetPos.getX() + 0.5 + (Math.random() - 0.5) * 1.5,
                        targetPos.getY() + 0.5 + Math.random(),
                        targetPos.getZ() + 0.5 + (Math.random() - 0.5) * 1.5,
                        0, 0.05, 0);
            }

            return InteractionResult.FAIL;
        }

        return super.useOn(context);
    }

    public boolean canBeUsed(Level level) {
        ResourceKey<Level> bygone = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID,"bygone"));
        ResourceKey<Level> resourcekey = level.dimension() == bygone ? Level.OVERWORLD : bygone;
        MinecraftServer server = level.getServer();
        if (server == null) return false;

        ServerLevel serverlevel = server.getLevel(resourcekey);

        if (serverlevel == null) {
            return false;
        } else {
            return serverlevel.dimension() == bygone || serverlevel.dimension() == Level.OVERWORLD;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return super.isBarVisible(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return -9856151;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return super.getBarWidth(stack);
    }
}