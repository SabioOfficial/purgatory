package net.sabio.purgatory;

import net.minecraft.resources.Identifier;
import net.sabio.purgatory.mod.world.PurgatorySurfaceRules;
import net.sabio.purgatory.mod.world.biome.PurgatoryRegion;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class PurgatoryTerraBlender implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new PurgatoryRegion(Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, "static_reach_region"), 2));
        PurgatorySurfaceRules.register();
    }
}