package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
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

public class ArcaneCoreItem extends Item{
    public ArcaneCoreItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel server)) return InteractionResultHolder.consume(stack);

        float pitch = Mth.lerp(level.random.nextFloat(), 0.33F, 0.5F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), BGSoundEvents.ITEM_ARCANE_CORE_USE_EVENT, SoundSource.NEUTRAL, 1, pitch);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(this, 10);

        // fun fact! the y coord of this BlockPos is ZERO. always. ahahaaaa
        BlockPos pos = server.findNearestMapStructure(JamiesModTag.BYGONE_ITEM_LOCATED, player.blockPosition(), 100, false);
        if (pos == null) return InteractionResultHolder.consume(stack);

        Vec3 center = player.getEyePosition().subtract(player.position()).scale(0.75).add(player.position());
        Vec3 dir = Vec3.atCenterOf(pos).subtract(center).with(Direction.Axis.Y, 0);
        dir = dir.normalize().scale(Math.min(dir.length(), 8));

        for (int i = 1; i <= 16; i++) {
            double x = center.x + (dir.x * i / 16D); // don't churn allocations!
            double z = center.z + (dir.z * i / 16D); // ...more than we already have bc mojang didn't make a fuckin mutable vec3
            server.sendParticles(BGParticleTypes.ARCANE_SYMBOL, x, center.y, z, 1, 0, 0, 0, 0);
        }

        return InteractionResultHolder.success(stack);
    }

}
