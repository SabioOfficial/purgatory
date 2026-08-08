package net.sabio.purgatory;

import net.fabricmc.api.ModInitializer;
import net.sabio.purgatory.mod.registry.ModBlocks;
import net.sabio.purgatory.mod.registry.ModItems;
import net.sabio.purgatory.mod.world.PurgatoryBiomes;
import net.sabio.purgatory.mod.world.biome.PurgatoryBiomeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Purgatory implements ModInitializer {
    public static final String MOD_ID = "purgatory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();
        PurgatoryBiomes.register();
        PurgatoryBiomeSource.injectIntoOverworld();
    }
}
