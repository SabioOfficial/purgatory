package net.sabio.purgatory.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;
import net.sabio.purgatory.Purgatory;
import net.sabio.purgatory.client.render.HarasserModel;
import net.sabio.purgatory.client.render.HarasserRenderer;
import net.sabio.purgatory.client.render.ModHarasserModelLayer;
import net.sabio.purgatory.client.render.StalkerRenderer;
import net.sabio.purgatory.mod.network.EyePhasePayload;
import net.sabio.purgatory.mod.registry.ModEntityTypes;

public class PurgatoryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(EyePhasePayload.TYPE, (payload, context) -> context.client().execute(() -> EyeHudRenderer.setPhase(payload.phase())));
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> EyeHudRenderer.reset());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> EyeHudRenderer.reset());

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, "eye_hud"),
                EyeHudRenderer::render
        );

        EntityRendererRegistry.register(ModEntityTypes.STALKER, StalkerRenderer::new);
        ModelLayerRegistry.registerModelLayer(ModHarasserModelLayer.MAIN, HarasserModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.HARASSER, HarasserRenderer::new);
    }
}
