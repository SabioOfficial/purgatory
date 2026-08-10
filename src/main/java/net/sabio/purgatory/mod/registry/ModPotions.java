package net.sabio.purgatory.mod.registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;
import java.util.Optional;

public class ModPotions {
    public static final Holder<Potion> SIN_POTION =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    ModPotionIds.SIN_POTION,
                    new Potion("sin",
                            new MobEffectInstance(MobEffects.STRENGTH, 1200, 4),
                            new MobEffectInstance(MobEffects.SPEED, 1200, 2),
                            new MobEffectInstance(MobEffects.WITHER, 400, 0),
                            new MobEffectInstance(MobEffects.DARKNESS, 1200, 0)
                    )
            );

    public static void register() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.addMix(Potions.AWKWARD, ModItems.SINFUL_DUST, ModPotions.SIN_POTION);
        });
    }
}