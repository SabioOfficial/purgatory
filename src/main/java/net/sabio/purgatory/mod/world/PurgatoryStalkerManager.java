package net.sabio.purgatory.mod.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.levelgen.Heightmap;
import net.sabio.purgatory.mod.entity.StalkerEntity;
import net.sabio.purgatory.mod.registry.ModEntityTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PurgatoryStalkerManager {
    private static final Map<UUID, StalkerEntity> activeStalkers = new HashMap<>();
    private static final double SPAWN_DISTANCE = 20.0;

    public static void onPhaseChanged(ServerPlayer player, int newPhase) {
        UUID uuid = player.getUUID();

        if (newPhase == PurgatoryNoiseTracker.PHASE_HUNTING) {
            spawnStalkerFor(player);
        } else {
            despawnStalkerFor(uuid);
        }
    }

    public static void onPlayerRemoved(UUID uuid) {
        despawnStalkerFor(uuid);
    }

    private static void spawnStalkerFor(ServerPlayer player) {
        UUID uuid = player.getUUID();
        if (activeStalkers.containsKey(uuid)) return;

        ServerLevel level = player.level();
        BlockPos spawnPos = pickSpawnPosNear(player);

        StalkerEntity stalker = ModEntityTypes.STALKER.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (stalker == null) return;

        stalker.snapTo(spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, 0f, 0f);
        stalker.setTarget(player);
        level.addFreshEntity(stalker);

        activeStalkers.put(uuid, stalker);
    }

    private static void despawnStalkerFor(UUID uuid) {
        StalkerEntity stalker = activeStalkers.remove(uuid);
        if (stalker != null && stalker.isAlive()) {
            stalker.discard();
        }
    }

    private static BlockPos pickSpawnPosNear(ServerPlayer player) {
        double angle = player.getRandom().nextDouble() * Math.PI * 2;
        int x = (int) (player.getX() + Math.cos(angle) * SPAWN_DISTANCE);
        int z = (int) (player.getZ() + Math.sin(angle) * SPAWN_DISTANCE);
        BlockPos base = new BlockPos(x, (int) player.getY(), z);
        return player.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, base);
    }
}
