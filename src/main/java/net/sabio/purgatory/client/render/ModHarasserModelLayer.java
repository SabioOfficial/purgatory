package net.sabio.purgatory.client.render;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.sabio.purgatory.Purgatory;

public class ModHarasserModelLayer {
    public static final ModelLayerLocation MAIN =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, "harasser"), "main");
}
