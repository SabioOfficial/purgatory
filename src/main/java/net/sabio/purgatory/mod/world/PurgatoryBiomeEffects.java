package net.sabio.purgatory.mod.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;

public class PurgatoryBiomeEffects {
    private static final int REFRESH_THRESHOLD_TICKS = 100;
    private static final int APPLY_DURATION_TICKS = 200;

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(PurgatoryBiomeEffects::onEndLevelTick);
    }

    private static void onEndLevelTick(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            BlockPos pos = player.blockPosition();
            Holder<Biome> biome = level.getBiome(pos);

            if (!biome.is(PurgatoryBiomes.STATIC_REACH)) {
                continue;
            }

            MobEffectInstance existingDarkness = player.getEffect(MobEffects.DARKNESS);
            if (existingDarkness == null || existingDarkness.getDuration() < REFRESH_THRESHOLD_TICKS) {
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, APPLY_DURATION_TICKS, 1, false, false));
            }
        }
    }
}
