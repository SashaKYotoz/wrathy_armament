package net.sashakyotoz.wrathy_armament.client.models.technical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CycleFlameModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("wrathy_armament", "cycle_flame_model"), "main");
    private final ModelPart flame;

    public CycleFlameModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.flame = root.getChild("flame");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition flame = partdefinition.addOrReplaceChild("flame", CubeListBuilder.create().texOffs(16, 0).addBox(-16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition cube_r1 = flame.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).addBox(16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r2 = flame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 8).addBox(-16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(16, 8).addBox(16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r3 = flame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 8).addBox(16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-16.0F, -5.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        flame.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
