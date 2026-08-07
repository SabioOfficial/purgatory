package net.sabio.purgatory.mod.world;

import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.sabio.purgatory.mod.registry.ModBlocks;
import terrablender.api.SurfaceRuleManager;

public class PurgatorySurfaceRules {
    public static void register() {
        SurfaceRuleManager.addSurfaceRules(
                SurfaceRuleManager.RuleCategory.OVERWORLD,
                "purgatory",
                biomes -> {
                    SurfaceRules.RuleSource ashSurface = SurfaceRules.state(ModBlocks.STATIC_ASH.defaultBlockState());
                    SurfaceRules.ConditionSource isStaticReach = SurfaceRules.isBiome(biomes, PurgatoryBiomes.STATIC_REACH);
                    SurfaceRules.ConditionSource aboveWater = SurfaceRules.waterBlockCheck(0, 0);
                    SurfaceRules.ConditionSource speckle = SurfaceRules.noiseCondition2d(Noises.GRAVEL, -0.05, 0.05);
                    return SurfaceRules.ifTrue(isStaticReach, SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(aboveWater, SurfaceRules.ifTrue(speckle, ashSurface))));
                }
        );
    }
}