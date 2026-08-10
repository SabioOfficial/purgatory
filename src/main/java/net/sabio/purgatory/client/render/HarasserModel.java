package net.sabio.purgatory.client.render;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class HarasserModel<S extends EntityRenderState> extends EntityModel<S> {
    public HarasserModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("bone",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-3.0F, -19.0F, -2.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(44, 23).addBox(1.0F, -19.0F, -2.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-5.0F, -37.0F, -2.0F, 12.0F, 18.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 30).addBox(-3.0F, -46.0F, -4.0F, 8.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 30).addBox(7.0F, -37.0F, -2.0F, 2.0F, 28.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 30).addBox(-7.0F, -37.0F, -2.0F, 2.0F, 28.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-1.0F, 24.0F, 1.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
    }
}