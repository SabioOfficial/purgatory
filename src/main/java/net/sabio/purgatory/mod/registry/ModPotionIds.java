package net.sabio.purgatory.mod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;
import net.sabio.purgatory.Purgatory;

public class ModPotionIds {
    public static final ResourceKey<Potion> SIN_POTION = create("sin");
    public static final ResourceKey<Potion> FEARFUL_POTION = create("fearful");

    private static ResourceKey<Potion> create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, name);
        return ResourceKey.create(Registries.POTION, id);
    }
}
