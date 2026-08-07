package net.sabio.purgatory.mod.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.sabio.purgatory.Purgatory;

public class PurgatoryBiomes {
    public static final ResourceKey<Biome> STATIC_REACH = key("static_reach");

    private static ResourceKey<Biome> key(String path) {
        return ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, path));
    }

    public static void register() {

    }
}
