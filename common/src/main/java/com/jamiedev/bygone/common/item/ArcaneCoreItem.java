package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArcaneCoreItem extends Item {

    public static final int USAGE_COOLDOWN = 10;

    public ArcaneCoreItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResultHolder.consume(stack);

        float pitch = Mth.lerp(level.random.nextFloat(), 0.33F, 0.5F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), BGSoundEvents.ITEM_ARCANE_CORE_USE_EVENT, SoundSource.NEUTRAL, 1, pitch);
        player.awardStat(Stats.ITEM_USED.get(this));
        // player.getCooldowns().addCooldown(this, (int) serverLevel.tickRateManager().tickrate() * USAGE_COOLDOWN);

        BlockPos pos = serverLevel.findNearestMapStructure(JamiesModTag.BYGONE_ITEM_LOCATED, player.blockPosition(), 100, false);
        if (pos == null) return InteractionResultHolder.consume(stack);

        Vec3 center = player.position().add(0, player.getBbHeight() + 0.5F, 0);
        Vec3 forward = Vec3.directionFromRotation(0, player.getYRot()).normalize();
        Vec3 start = center.add(forward);

        Vec3 dir = Vec3.atCenterOf(pos).subtract(start).normalize();
        for (int i = 1; i <= 5; i++) {
            Vec3 particlePos = start.add(dir.scale(i * 0.5F));
            serverLevel.sendParticles((ParticleOptions) BGParticleTypes.ARCANE_SYMBOL, particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0, 0);
        }

        return InteractionResultHolder.success(stack);
    }

}
