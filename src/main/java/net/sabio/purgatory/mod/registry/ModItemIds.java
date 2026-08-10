package net.sabio.purgatory.mod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.sabio.purgatory.Purgatory;

public class ModItemIds {
    public static final ResourceKey<Item> SINFUL_DUST = create("sinful_dust");
    public static final ResourceKey<Item> EYE_OF_SIN = create("eye_of_sin");

    public static ResourceKey<Item> create(String name) {
        // Create the item key.
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, name));
    }
}
