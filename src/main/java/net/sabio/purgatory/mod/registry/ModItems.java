package net.sabio.purgatory.mod.registry;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.sabio.purgatory.Purgatory;

import java.util.function.Function;

public class ModItems {
    public static final Consumable EYE_CONSUMABLE_COMPONENT = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 6 * 20, 2), 1.0f))
            .build();

    public static final FoodProperties EYE_FOOD_COMPONENT = new FoodProperties.Builder()
            .alwaysEdible()
            .build();

    public static final Item SINFUL_DUST = register(
        ModItemIds.SINFUL_DUST,
        Item::new,
        new Item.Properties()
    );

    public static final Item EYE_OF_SIN = register(
        ModItemIds.EYE_OF_SIN,
        Item::new,
        new Item.Properties().food(EYE_FOOD_COMPONENT, EYE_CONSUMABLE_COMPONENT)
    );

    public static ResourceKey<Item> create(String name) {
        // Create the item key.
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, name));
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> {
                    creativeTab.accept(ModItems.SINFUL_DUST);
                    creativeTab.accept(ModItems.EYE_OF_SIN);
                });
    }
}
