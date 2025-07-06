package com.jamiedev.bygone.core.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.TriConsumer;

public record Enforcement(TriConsumer<Player, ServerLevel, Restriction> consumer) {

    public static void enforceBiomeProgression(Player player, ServerLevel level) {
        Restriction.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player).ifPresent(restriction -> {

        });
    }
}