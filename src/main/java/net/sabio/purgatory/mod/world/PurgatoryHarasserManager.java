package net.sabio.purgatory.mod.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.levelgen.Heightmap;
import net.sabio.purgatory.mod.entity.HarasserEntity;
import net.sabio.purgatory.mod.registry.ModEntityTypes;

import java.util.*;

public class PurgatoryHarasserManager {
    private static final int MIN_ACTIVE = 2;
    private static final int MAX_ACTIVE = 3;
    private static final double SPAWN_DISTANCE = 14.0;

    private static final Map<UUID, List<HarasserEntity>> activeHarassers = new HashMap<>();

    public static void onPhaseChanged(ServerPlayer player, int newPhase) {
        UUID uuid = player.getUUID();

        boolean shouldHaveHarassers = newPhase == PurgatoryNoiseTracker.PHASE_ALERT || newPhase == PurgatoryNoiseTracker.PHASE_STALKING;

        if (shouldHaveHarassers) {
            maintainHarassers(player);
        } else {
            despawnAllFor(uuid);
        }
    }

    public static void onPlayerRemoved(UUID uuid) {
        despawnAllFor(uuid);
    }

    private static void maintainHarassers(ServerPlayer player) {
        UUID uuid = player.getUUID();
        List<HarasserEntity> current = activeHarassers.computeIfAbsent(uuid, k -> new ArrayList<>());
        current.removeIf(h -> !h.isAlive());

        int target = MIN_ACTIVE + player.getRandom().nextInt(MAX_ACTIVE - MIN_ACTIVE + 1);
        while (current.size() < target) {
            HarasserEntity harasser = spawnOneFor(player);
            if (harasser == null) break;
            current.add(harasser);
        }
    }

    private static HarasserEntity spawnOneFor(ServerPlayer player) {
        ServerLevel level = player.level();
        BlockPos spawnPos = pickSpawnPosNear(player);

        HarasserEntity harasser = ModEntityTypes.HARASSER.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (harasser == null) return null;

        harasser.snapTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0f, 0f);
        level.addFreshEntity(harasser);
        return harasser;
    }

    private static BlockPos pickSpawnPosNear(ServerPlayer player) {
        double angle = player.getRandom().nextDouble() * Math.PI * 2;
        int x = (int) (player.getX() + Math.cos(angle) * SPAWN_DISTANCE);
        int z = (int) (player.getZ() + Math.sin(angle) * SPAWN_DISTANCE);
        BlockPos base = new BlockPos(x, (int) player.getY(), z);
        return player.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, base);
    }

    private static void despawnAllFor(UUID uuid) {
        List<HarasserEntity> current = activeHarassers.remove(uuid);
        if (current != null) {
            for (HarasserEntity h : current) {
                if (h.isAlive()) h.discard();
            }
        }
    }
}
