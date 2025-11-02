package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.util.ShockwaveHandler;
import com.jamiedev.bygone.core.registry.BGDataComponentTypes;
import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EchoGongItem extends Item {
    private static final int MAX_CHARGE = 100;
    private static final int CHARGE_PER_TICK = 1;
    private static final int COOLDOWN_TICKS = 100;
    private static final float SHOCKWAVE_RADIUS = 8.0F;
    private static final int WEAKNESS_DURATION = 200;
    private static final SimpleParticleType SHOCKWAVE_PARTICLE = ParticleTypes.ELECTRIC_SPARK;

    public EchoGongItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            BGDataComponentTypes.EchoGongData data = stack.getOrDefault(BGDataComponents.ECHO_GONG_DATA.value(), BGDataComponentTypes.EchoGongData.EMPTY);
            int charge = data.charge();

            if (charge < MAX_CHARGE && !player.getCooldowns().isOnCooldown(this)) {
                charge = Math.min(charge + CHARGE_PER_TICK, MAX_CHARGE);
                stack.set(BGDataComponents.ECHO_GONG_DATA.value(), new BGDataComponentTypes.EchoGongData(charge));

                if (charge % 20 == 0) {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            BGSoundEvents.ECHO_GONG_CHARGE.get(), SoundSource.PLAYERS,
                            0.3F, 1.0F + (charge / (float) MAX_CHARGE) * 0.5F);

                    if (level instanceof ServerLevel serverLevel) {
                        double particleY = player.getY() + 0.5;
                        for (int i = 0; i < 3; i++) {
                            double angle = level.random.nextDouble() * Math.PI * 2;
                            double distance = level.random.nextDouble() * 0.5;
                            double particleX = player.getX() + Math.cos(angle) * distance;
                            double particleZ = player.getZ() + Math.sin(angle) * distance;

                            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM,
                                    particleX, particleY, particleZ, 1, 0, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BGDataComponentTypes.EchoGongData data = itemStack.getOrDefault(BGDataComponents.ECHO_GONG_DATA.value(), BGDataComponentTypes.EchoGongData.EMPTY);
        int charge = data.charge();

        if (charge >= MAX_CHARGE) {
            if (!level.isClientSide) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(itemStack);
            }
            return InteractionResultHolder.consume(itemStack);
        } else {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("item.bygone.echo_gong.not_charged", charge, MAX_CHARGE)
                                .withStyle(ChatFormatting.GRAY), true);
            }
            return InteractionResultHolder.fail(itemStack);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            releaseShockwave(level, player, stack);
            stack.set(BGDataComponents.ECHO_GONG_DATA.value(), new BGDataComponentTypes.EchoGongData(0));

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return stack;
    }

    private void releaseShockwave(Level level, Player player, ItemStack stack) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                BGSoundEvents.ECHO_GONG_USE.get(), SoundSource.RECORDS, 2.0F, 1.0F);
        level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));

        if (level instanceof ServerLevel serverLevel) {
            ShockwaveHandler.createShockwave(serverLevel, player, SHOCKWAVE_PARTICLE, 3.0F, 20, 24);
            ShockwaveHandler.createShockwave(serverLevel, player, SHOCKWAVE_PARTICLE, 5.0F, 30, 32);
            ShockwaveHandler.createShockwave(serverLevel, player, SHOCKWAVE_PARTICLE, 8.0F, 40, 40);

            applyAreaEffects(serverLevel, player);
            breakFragileBlocks(serverLevel, player);

            serverLevel.sendParticles(SHOCKWAVE_PARTICLE,
                    player.getX(), player.getY() + 1, player.getZ(),
                    8, 0.3, 0.3, 0.3, 0.2);
        }
    }


    private void applyAreaEffects(ServerLevel level, Player player) {
        AABB area = new AABB(player.position().add(-SHOCKWAVE_RADIUS, -2, -SHOCKWAVE_RADIUS),
                player.position().add(SHOCKWAVE_RADIUS, 3, SHOCKWAVE_RADIUS));

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if (entity != player && !entity.isAlliedTo(player)) {
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, WEAKNESS_DURATION, 1));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));

                Vec3 direction = entity.position().subtract(player.position());
                double distance = direction.length();

                if (distance > 0) {
                    Vec3 knockbackVec = direction.normalize();
                    double knockbackStrength = Math.max(0.5, (SHOCKWAVE_RADIUS - distance) / SHOCKWAVE_RADIUS * 1.5);

                    double horizontalKnockback = knockbackStrength * 0.8;
                    double verticalKnockback = Math.min(0.8, knockbackStrength * 0.6);

                    entity.push(knockbackVec.x * horizontalKnockback, verticalKnockback, knockbackVec.z * horizontalKnockback);
                }
            }
        }
    }

    private void breakFragileBlocks(ServerLevel level, Player player) {
        BlockPos playerPos = player.blockPosition();
        int radius = (int) SHOCKWAVE_RADIUS;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        BlockPos pos = playerPos.offset(x, y, z);
                        BlockState state = level.getBlockState(pos);

                        if (isFragileBlock(state)) {
                            level.destroyBlock(pos, true, player);
                        }
                    }
                }
            }
        }
    }

    private boolean isFragileBlock(BlockState state) {
        return state.canBeReplaced();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        BGDataComponentTypes.EchoGongData data = stack.getOrDefault(BGDataComponents.ECHO_GONG_DATA.value(), BGDataComponentTypes.EchoGongData.EMPTY);
        int charge = data.charge();

        tooltipComponents.add(Component.translatable("item.bygone.echo_gong.desc1")
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.bygone.echo_gong.desc2")
                .withStyle(ChatFormatting.GRAY));

        if (charge >= MAX_CHARGE) {
            tooltipComponents.add(Component.translatable("item.bygone.echo_gong.ready")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            int percent = (charge * 100) / MAX_CHARGE;
            tooltipComponents.add(Component.translatable("item.bygone.echo_gong.charge", percent)
                    .withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        BGDataComponentTypes.EchoGongData data = stack.getOrDefault(BGDataComponents.ECHO_GONG_DATA.value(), BGDataComponentTypes.EchoGongData.EMPTY);
        return data.charge() >= MAX_CHARGE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

}