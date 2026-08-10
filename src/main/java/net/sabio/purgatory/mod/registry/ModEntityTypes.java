package net.sabio.purgatory.mod.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.sabio.purgatory.Purgatory;
import net.sabio.purgatory.mod.entity.HarasserEntity;
import net.sabio.purgatory.mod.entity.StalkerEntity;

public class ModEntityTypes {
    public static final EntityType<StalkerEntity> STALKER = register(
            "stalker",
            EntityType.Builder.of(StalkerEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .clientTrackingRange(10)
    );

    public static final EntityType<HarasserEntity> HARASSER = register(
            "harasser",
            EntityType.Builder.of(HarasserEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.5f)
                    .clientTrackingRange(8)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        Purgatory.LOGGER.info("Registering EntityTypes for " + Purgatory.MOD_ID);
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(STALKER, StalkerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HARASSER, HarasserEntity.createAttributes());
    }
}
