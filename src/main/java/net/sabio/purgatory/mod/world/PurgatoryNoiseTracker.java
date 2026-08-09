package net.sabio.purgatory.mod.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.sabio.purgatory.mod.network.EyePhasePayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PurgatoryNoiseTracker {
    public static final int PHASE_HIDDEN = -1;
    public static final int PHASE_UNNOTICED = 0;
    public static final int PHASE_SUSPICIOUS = 1;
    public static final int PHASE_ALERT = 2;
    public static final int PHASE_STALKING = 3;
    public static final int PHASE_HUNTING = 4;

    private static final float METER_MAX = 100.0f;
    private static final float PHASE_BAND = METER_MAX / 5.0f;

    private static final float DECAY_PER_TICK = 0.15f;
    private static final float WALK_NOISE_PER_BLOCK = 1.2f;
    private static final float SPRINT_NOISE_PER_BLOCK = 2.5f;
    private static final float JUMP_NOISE = 4.0f;
    private static final float BLOCK_BREAK_NOISE = 12.0f;

    private static final Map<UUID, Float> noiseMeter = new HashMap<>();
    private static final Map<UUID, Integer> lastSentPhase = new HashMap<>();
    private static final Map<UUID, double[]> lastPosition = new HashMap<>();
    private static final Map<UUID, Boolean> wasOnGround = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(PurgatoryNoiseTracker::onEndWorldTick);
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (player instanceof ServerPlayer serverPlayer && isInBiome(serverPlayer)) {
                addNoise(serverPlayer, BLOCK_BREAK_NOISE);
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            lastSentPhase.remove(player.getUUID());
        });
    }

    private static boolean isInBiome(ServerPlayer player) {
        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        return biome.is(PurgatoryBiomes.STATIC_REACH);
    }

    private static void onEndWorldTick(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            UUID id = player.getUUID();
            BlockPos pos = player.blockPosition();
            Holder<Biome> biome = level.getBiome(pos);

            if (!biome.is(PurgatoryBiomes.STATIC_REACH)) {
                if (lastSentPhase.containsKey(id)) {
                    ServerPlayNetworking.send(player, new EyePhasePayload(PHASE_HIDDEN));
                }
                noiseMeter.remove(id);
                lastSentPhase.remove(id);
                lastPosition.remove(id);
                wasOnGround.remove(id);
                continue;
            }

            tickMovementNoise(player);
            decayNoise(player);
            sendPhaseIfChanged(player);
        }
    }

    private static void tickMovementNoise(ServerPlayer player) {
        UUID id = player.getUUID();
        double[] last = lastPosition.get(id);
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        if (last != null) {
            double dx = x - last[0];
            double dz = z - last[2];
            double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

            if (horizontalDistance > 0.01) {
                float perBlock = player.isSprinting() ? SPRINT_NOISE_PER_BLOCK : WALK_NOISE_PER_BLOCK;
                addNoise(player, (float) (horizontalDistance * perBlock));
            }
        }

        boolean onGround = player.onGround();
        boolean previouslyOnGround = wasOnGround.getOrDefault(id, true);
        if (!previouslyOnGround && onGround) {
            addNoise(player, JUMP_NOISE);
        }
        wasOnGround.put(id, onGround);

        lastPosition.put(id, new double[]{x, y, z});
    }

    private static void decayNoise(ServerPlayer player) {
        UUID id = player.getUUID();
        float current = noiseMeter.getOrDefault(id, 0.0f);
        if (current > 0.0f) {
            noiseMeter.put(id, Math.max(0.0f, current - DECAY_PER_TICK));
        }
    }

    private static void addNoise(ServerPlayer player, float amount) {
        UUID id = player.getUUID();
        float current = noiseMeter.getOrDefault(id, 0.0f);
        noiseMeter.put(id, Math.min(METER_MAX, current + amount));
    }

    private static int phaseFor(float meterValue) {
        int phase = (int) (meterValue / PHASE_BAND);
        return Math.min(PHASE_HUNTING, phase);
    }

    private static void sendPhaseIfChanged(ServerPlayer player) {
        UUID id = player.getUUID();
        float meterValue = noiseMeter.getOrDefault(id, 0.0f);
        int phase = phaseFor(meterValue);
        int previousPhase = lastSentPhase.getOrDefault(id, -1);

        if (phase != previousPhase) {
            lastSentPhase.put(id, phase);
            ServerPlayNetworking.send(player, new EyePhasePayload(phase));
        }
    }
}
