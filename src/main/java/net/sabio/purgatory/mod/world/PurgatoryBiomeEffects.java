package net.sabio.purgatory.mod.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PurgatoryBiomeEffects {
    private static final int REFRESH_THRESHOLD_TICKS = 100;
    private static final int APPLY_DURATION_TICKS = 200;

    private static final float FOOTSTEP_START_CHANCE = 0.003f;
    private static final int FOOTSTEP_SEQUENCE_LENGTH = 4;
    private static final int TICKS_BETWEEN_STEPS = 14;
    private static final Random RANDOM = new Random();

    private static final Map<UUID, Integer> footstepProgress = new HashMap<>();
    private static final Map<UUID, Integer> footstepCooldown = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(PurgatoryBiomeEffects::onEndLevelTick);
    }

    private static void onEndLevelTick(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            BlockPos pos = player.blockPosition();
            Holder<Biome> biome = level.getBiome(pos);

            if (!biome.is(PurgatoryBiomes.STATIC_REACH)) {
                footstepProgress.remove(player.getUUID());
                footstepCooldown.remove(player.getUUID());
                continue;
            }

            MobEffectInstance existingDarkness = player.getEffect(MobEffects.DARKNESS);
            if (existingDarkness == null || existingDarkness.getDuration() < REFRESH_THRESHOLD_TICKS) {
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, APPLY_DURATION_TICKS, 1, false, false));
            }

            tickFakeFootsteps(level, player);
        }
    }

    private static void tickFakeFootsteps(ServerLevel level, ServerPlayer player) {
        UUID uuid = player.getUUID();
        int step = footstepProgress.getOrDefault(uuid, 0);

        if (step == 0) {
            int cooldown = footstepCooldown.getOrDefault(uuid, 0);
            if (cooldown > 0) {
                footstepCooldown.put(uuid, cooldown - 1);
                return;
            }
            if (RANDOM.nextFloat() < FOOTSTEP_START_CHANCE) {
                footstepProgress.put(uuid, 1);
            }
            return;
        }

        int cooldown = footstepCooldown.getOrDefault(uuid, 0);
        if (cooldown > 0) {
            footstepCooldown.put(uuid, cooldown - 1);
            return;
        }

        float progress = (float) step / FOOTSTEP_SEQUENCE_LENGTH;
        float volume = 0.4f + (0.5f * progress);
        double angle = RANDOM.nextDouble() * Math.PI * 2;
        double distance = 6.0 - (4.0 * progress);
        double x = player.getX() + Math.cos(angle) * distance;
        double z = player.getZ() + Math.sin(angle) * distance;

        level.playSound(null, x, player.getY(), z, SoundEvents.GRAVEL_STEP, SoundSource.HOSTILE, volume, 0.7f);

        if (step >= FOOTSTEP_SEQUENCE_LENGTH) {
            footstepProgress.put(uuid, 0);
            footstepCooldown.put(uuid, 300 + RANDOM.nextInt(600));
        } else {
            footstepProgress.put(uuid, step + 1);
            footstepCooldown.put(uuid, TICKS_BETWEEN_STEPS);
        }
    }
}
