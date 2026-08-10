package net.sabio.purgatory.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.sabio.purgatory.Purgatory;
import net.sabio.purgatory.mod.entity.HarasserEntity;

public class HarasserRenderer extends MobRenderer<HarasserEntity, LivingEntityRenderState, HarasserModel<LivingEntityRenderState>> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Purgatory.MOD_ID, "textures/entity/harasser.png");

    public HarasserRenderer(EntityRendererProvider.Context context) {
        super(context, new HarasserModel<>(context.bakeLayer(ModHarasserModelLayer.MAIN)), 0.4f);
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
