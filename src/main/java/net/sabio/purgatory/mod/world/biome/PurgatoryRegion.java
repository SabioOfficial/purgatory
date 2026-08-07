package net.sabio.purgatory.mod.world.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.sabio.purgatory.mod.world.PurgatoryBiomes;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class PurgatoryRegion extends Region {
    public PurgatoryRegion(Identifier id, int weight) {
        super(id, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addBiome(
                mapper,
                ParameterUtils.Temperature.COOL,
                ParameterUtils.Humidity.DRY,
                ParameterUtils.Continentalness.INLAND,
                ParameterUtils.Erosion.EROSION_2,
                ParameterUtils.Weirdness.MID_SLICE_NORMAL_DESCENDING,
                ParameterUtils.Depth.SURFACE,
                0.0F,
                PurgatoryBiomes.STATIC_REACH
        );
    }
}