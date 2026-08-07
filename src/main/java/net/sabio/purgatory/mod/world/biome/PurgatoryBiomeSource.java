package net.sabio.purgatory.mod.world.biome;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.resources.Identifier;
import net.sabio.purgatory.Purgatory;
import net.sabio.purgatory.mod.world.PurgatoryBiomes;

public class PurgatoryBiomeSource {
    public static void injectIntoOverworld() {
        BiomeModifications.create(Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, "static_reach_spawns"))
                .add(ModificationPhase.ADDITIONS,
                        BiomeSelectors.includeByKey(PurgatoryBiomes.STATIC_REACH),
                        context -> {
                            var mobSpawns = context.getMobSpawnSettings();
                        });
    }
}